package commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class StatusHandler {
    JDA jda;

    public StatusHandler(JDA jda) {
        this.jda = jda;

    }

    public void updateSlashCommands() {
        System.out.println("Adding slash commands");
        jda.updateCommands().addCommands(
                Commands.slash("schedule", "Shows upcoming streams and events for a given organization")
                        .addOption(OptionType.STRING, "organization",
                                "Holodex Organization Name (e.g Hololive, Nijisanji, Phase Connect, PRISM, Production Kawaii)",
                                true))
                .addCommands(
                        Commands.slash("schedule-channel", "Shows upcoming streams and events for a given channel ID")
                                .addOption(OptionType.STRING, "channel-id",
                                        "YouTube Channel ID (e.g UCp6993wxpyDPHUpavwDFqgg)",
                                        true))
                .addCommands(
                        Commands.slash("configure", "Sets channel to be updated with live and upcoming streams for the organization provided")
                                .addOptions(new OptionData(OptionType.STRING, "type", "Individual Channel or Organization", true)
                                        .addChoice("Channel", "channel")
                                        .addChoice("Organization", "org"))
                                .addOption(OptionType.STRING, "id", "The name of the organization or the channel ID", true))
                .addCommands(
                        Commands.slash("remove-config", "Removes the configuration for the channel this command is run in")
                                .addOption(OptionType.STRING, "term", "The channel ID or Organization")).queue();





    }

    public void purgeSlashCommands() {
        System.out.println("Purging all slash commands");
        jda.retrieveCommands().queue(commands -> {
            for (int i = 0; i < commands.size(); i++) {
                commands.get(i).delete().queue();
            }
        });
    }


}
