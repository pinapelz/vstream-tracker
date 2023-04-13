package fileutils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class UpcomingChannelsManager {
    final String CONFIG_FILE_PATH = "settings//upcomingChannels.txt";
    public void addNewEntry(String type, String information, String discordChannelID){
        File f = new File(CONFIG_FILE_PATH);
        try {
            System.out.printf("Written data to upcomingChannels.txt");
            FileWriter fw = new FileWriter(f, true);
            fw.write(type + ":" + information + ":" + discordChannelID+"\n");
            fw.close();
        }
        catch(IOException e){
            System.out.println("Unable to open upcomingChannels.txt for writing");
        }
    }

    public void removeEntry(String term, long discordChannelID){
        // remove the line containing the term
        File f  = new File(CONFIG_FILE_PATH);
        try{
            List<String> lines = Files.readAllLines(Paths.get(CONFIG_FILE_PATH));
            FileWriter fw = new FileWriter(f, false);
            for(String line : lines){
                System.out.println(line);
                if(line.contains(term) && line.contains(Long.toString(discordChannelID))){
                    continue;
                }
                fw.write(line+"\n");
            }
            fw.close();
        }
        catch(IOException e){
            System.out.println("Unable to open upcomingChannels.txt for writing");
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Out of bounds Exception, is the upcomingChannels.txt formatted correctly?");
        }

    }
}
