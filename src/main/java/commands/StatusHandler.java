package commands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class StatusHandler {
    JDA jda;
    public StatusHandler(JDA jda) {
        this.jda = jda;

    }
    public void updateSlashCommands(){
        System.out.println("Adding slash commands");
        jda.upsertCommand(new CommandData("schedule", "Shows upcoming streams and events for a given organization")
                .addOption(OptionType.STRING, "organization",
                        "Holodex Organization Name (e.g Hololive, Nijisanji, Phase Connect, PRISM, Production Kawaii)",
                        true))
                .queue();
        jda.upsertCommand(new CommandData("schedule-channel", "Shows upcoming streams and events for a given channel ID")
                .addOption(OptionType.STRING, "channel-id",
                        "YouTube Channel ID (e.g UCp6993wxpyDPHUpavwDFqgg)",
                        true))
                .queue();
        jda.upsertCommand(new CommandData("configure-stream-channel", "Sets channel to be updated with live and upcoming streams for the channel ID provided")
                .addOption(OptionType.STRING,"channel-id", "YouTube Channel ID (e.g UCp6993wxpyDPHUpavwDFqgg)", true))
                .queue();
        jda.upsertCommand(new CommandData("configure-org-channel", "Sets channel to be updated with live and upcoming streams for the organization provided")
                .addOption(OptionType.STRING,"organization", "Holodex Organization Name (e.g Hololive, Nijisanji, Phase Connect, PRISM, Production Kawaii)", true))
                .queue();


    }

    public void purgeSlashCommands(){
        System.out.println("Purging all slash commands");
        jda.retrieveCommands().queue(commands -> {
            for (int i = 0; i < commands.size(); i++) {
                commands.get(i).delete().queue();
            }
        });
    }


}
