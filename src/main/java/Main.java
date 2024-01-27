import commands.CommandManager;
import commands.StatusHandler;
import common.OrgChannelTuple;
import fileutils.FileDataProcessor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class Main extends ListenerAdapter{
    private int REFRESH_INTERVAL = 15;
    private JDA jda;
    private StatusHandler statusHandler;
    private JDABuilder jdaBuilder;
    private FileDataProcessor fileDataProcessor;
    private CommandManager commandManager;
    private long adminRoleId;

    public void initializeBot(){
        fileDataProcessor = new FileDataProcessor();
        adminRoleId = Long.parseLong(fileDataProcessor.getField("adminRole"));
        commandManager = new CommandManager(fileDataProcessor.getField("HOLODEXAPIKEY"), adminRoleId);
        jdaBuilder = JDABuilder.createDefault(fileDataProcessor.getField("DISCORDTOKEN"));
        jdaBuilder.addEventListeners(commandManager);
        jdaBuilder.addEventListeners(this);
        try {
            jda = jdaBuilder.build();
        }
        catch (Exception e) {
            System.out.println("Unable to login with the provided token. Please check your token and try again.");
            throw new RuntimeException(e);
        }

    }

    public void initializeAutoRefresh(){
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ses.scheduleAtFixedRate(() -> {
            try {
                System.out.println("Refreshing upcoming channels");
                List<OrgChannelTuple> refreshChannels = fileDataProcessor.getRefreshChannels();
                List<Long> usedChannels = fileDataProcessor.getUsedChannels();
                for (Long channelId : usedChannels) {
                    System.out.println("Purging channel " + channelId);
                    jda.getTextChannelById(channelId).purgeMessages(
                            jda.getTextChannelById(channelId).getIterableHistory().complete());
                }
                if (refreshChannels.size() == 0) {
                    System.out.println("No channels to refresh");
                    return;
                }
                for (OrgChannelTuple orgChannelTuple : refreshChannels) {
                    System.out.println("Refreshing " + orgChannelTuple.getType() + " " + orgChannelTuple.getName());
                    List<MessageEmbed> messageEmbeds = commandManager.updateUpcomingChannel(orgChannelTuple.getName(), orgChannelTuple.getType());
                    if (messageEmbeds.size() == 0) {
                        continue;
                    }
                    for (MessageEmbed messageEmbed : messageEmbeds) {
                        jda.getTextChannelById(orgChannelTuple.getDiscordChannelId()).sendMessageEmbeds(messageEmbed).queue();
                    }
                }
            }
            catch(NullPointerException ex){
                System.out.println(ex);
                System.out.println("Channel is empty. Skipping refresh there");
            }
            catch (Exception e) {
                System.out.println("Error occurred while refreshing upcoming channels");
                e.printStackTrace();
            }
        }, 0, REFRESH_INTERVAL, TimeUnit.MINUTES);
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        JDA jda = e.getJDA();
        Message message = e.getMessage();
        String msg = message.getContentDisplay();

    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Logged in as " + event.getJDA().getSelfUser().getAsTag());
        statusHandler = new StatusHandler(jda);
        //statusHandler.purgeSlashCommands();
        statusHandler.updateSlashCommands();
        initializeAutoRefresh();
        System.out.println("Bot is ready!");
    }
    public static void main(String args[]) {
        Main main = new Main();
        main.initializeBot();
    }
}

