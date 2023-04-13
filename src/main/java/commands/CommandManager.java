package commands;

import builders.ScheduleMessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import vtuber.ScheduleHandler;

import java.util.ArrayList;

public class CommandManager extends ListenerAdapter {
    ScheduleHandler scheduleHandler;
    ScheduleMessageBuilder scb;
    public CommandManager(String holodexAPIKey) {
        scheduleHandler = new ScheduleHandler(holodexAPIKey);
        scb = new ScheduleMessageBuilder();
        System.out.println("CommandManager initialized");
    }
    @Override
    public void onSlashCommand(SlashCommandEvent e) {
        String command = e.getName();
        MessageEmbed scheduleMessage;
        switch (command) {
            case "schedule-channel":
                String channelId = e.getOption("channel-id").getAsString();
                if (scheduleHandler.channelExists(channelId) == false) {
                    e.reply("Sorry, I couldn't find any information on that channel. Please ensure it matches Holodex's spelling").queue();
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

}
