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

import javax.security.auth.login.LoginException;
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

    public void initializeBot(){
        fileDataProcessor = new FileDataProcessor();
        commandManager = new CommandManager(fileDataProcessor.readCredential("holodexAPIKey"));
        jdaBuilder = JDABuilder.createDefault(fileDataProcessor.readCredential("discordToken"));
        jdaBuilder.addEventListeners(commandManager);
        try {
            jda = jdaBuilder.build();
            statusHandler = new StatusHandler(jda);
            statusHandler.updateSlashCommands();
            System.out.println("Bot is ready!");
            initializeAutoRefresh();
        }
        catch (LoginException e) {
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
                    jda.getTextChannelById(orgChannelTuple.getDiscordChannelId()).purgeMessages(
                            jda.getTextChannelById(orgChannelTuple.getDiscordChannelId()).getIterableHistory().complete());
                    for (MessageEmbed messageEmbed : messageEmbeds) {
                        jda.getTextChannelById(orgChannelTuple.getDiscordChannelId()).sendMessageEmbeds(messageEmbed).queue();
                    }
                }
            }
            catch(NullPointerException ex){
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
    public static void main(String args[]) {
        Main main = new Main();
        main.initializeBot();
    }
}

