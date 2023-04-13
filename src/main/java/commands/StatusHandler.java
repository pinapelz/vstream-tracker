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
        jda.upsertCommand(new CommandData("schedule", "Shows upcoming streams and events for a given organization")
                .addOption(OptionType.STRING, "organization",
                        "Holodex Organization Name (e.g Hololive, Nijisanji, Phase Connect, PRISM, Production Kawaii)",
                        true))
                .queue();

    }


}
