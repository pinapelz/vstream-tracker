package commands;

import builders.ScheduleMessageBuilder;
import fileutils.UpcomingChannelsManager;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import vtuber.ScheduleHandler;

import java.util.ArrayList;

public class CommandManager extends ListenerAdapter {
    private ScheduleHandler scheduleHandler;
    private ScheduleMessageBuilder scb;
    private UpcomingChannelsManager ucm;
    private long adminRole;
    public CommandManager(String holodexAPIKey, long adminRole) {
        scheduleHandler = new ScheduleHandler(holodexAPIKey);
        scb = new ScheduleMessageBuilder();
        ucm = new UpcomingChannelsManager();
        this.adminRole = adminRole;
        System.out.println("CommandManager initialized");
    }
    @Override
    public void onSlashCommand(SlashCommandEvent e) {
        String command = e.getName();
        MessageEmbed scheduleMessage;
        switch (command) {
            case "remove-config":
                if (!hasPermission(e)){
                    e.reply("Sorry, you don't have permission to run this command").queue();
                    return;
                }
                String searchTerm = e.getOption("term").getAsString();
                long currentDiscChannelID = e.getChannel().getIdLong();
                ucm.removeEntry(searchTerm, currentDiscChannelID);
                e.reply("Successfully removed " + searchTerm + " from this channel. Please restart the bot for this to take effect").queue();
                break;
            case "configure":
                if (!hasPermission(e)){
                    e.reply("Sorry, you don't have permission to run this command").queue();
                    return;
                }
                System.out.println("RUNNING?");
                String type  = e.getOption("type").getAsString();
                String id = e.getOption("id").getAsString();
                long discordChannelId = e.getChannel().getIdLong();
                if (checkValidConfig(type, id) == false){
                    e.reply("Sorry, I couldn't find any information on that channel. Please check the ID you provided").queue();
                    return;
                }
                ucm.addNewEntry(type, id, Long.toString(discordChannelId));
                e.reply("Successfully added " + type + " " + id + " feed to this channel. Please restart the bot for this to take effect").queue();
                break;
            case "schedule-channel":
                String channelId = e.getOption("channel-id").getAsString();
                if (scheduleHandler.channelExists(channelId) == false) {
                    e.reply("Sorry, I couldn't find any information on that channel. Please check the ID you provided").queue();
                    return;
                }
                scheduleMessage = scb.buildLiveAndUpcomingMessage(scheduleHandler.getScheduleChannelId(channelId, 10));
                e.deferReply().queue();
                e.getHook().sendMessageEmbeds(scheduleMessage).queue();
                break;
            case "schedule":
                String organization = e.getOption("organization").getAsString();
                organization = organization.replaceAll(" ", "%20");
                if (scheduleHandler.organizationExists(organization) == false) {
                    e.reply("Sorry, I couldn't find any information on that organization. Please ensure it matches Holodex's spelling").queue();
                    return;
                }
                scheduleMessage = scb.buildLiveAndUpcomingMessage(scheduleHandler.getSchedule(organization, 10));
                e.deferReply().queue();
                e.getHook().sendMessageEmbeds(scheduleMessage).queue();
                break;
            default:
                e.reply("Unknown command received").queue();
                break;
        }
    }

    public ArrayList<MessageEmbed> updateUpcomingChannel(String name, String type){
        ArrayList<MessageEmbed> messageEmbeds = new ArrayList<>();
        switch (type) {
            case "org":
                messageEmbeds = scb.getUpcomingLiveListMessages(scheduleHandler.getSchedule(name));
                break;
            case "channel":
                messageEmbeds = scb.getUpcomingLiveListMessages(scheduleHandler.getScheduleChannelId(name));
                break;
            default:
                System.out.println("Unknown type");
                break;
        }
        return messageEmbeds;
    }

    public boolean checkValidConfig(String type, String id){
        switch (type) {
            case "org":
                if (scheduleHandler.organizationExists(id) == false) {
                    return false;
                }
                break;
            case "channel":
                if (scheduleHandler.channelExists(id) == false) {
                    return false;
                }
                break;
            default:
                System.out.println("Unknown type");
                throw new IllegalArgumentException("Unknown type");
        }
        return true;
    }

    public boolean hasPermission(SlashCommandEvent e){
        if (e.getMember().isOwner() || e.getMember().getRoles().contains(e.getGuild().getRoleById(adminRole))){
            return true;
        }
        return false;
    }

}
