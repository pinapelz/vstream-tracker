package fileutils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UpcomingChannelsManager {
    public void addNewEntry(String type, String information, String discordChannelID){
        File f = new File("settings//upcomingChannels.txt");
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
}
