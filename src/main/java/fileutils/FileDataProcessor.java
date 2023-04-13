package fileutils;

import common.OrgChannelTuple;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileDataProcessor {

    public static String getField(String parameter){
        try {
            Object obj = new JSONParser().parse(new FileReader("settings//config.json"));
            JSONObject jo = (JSONObject) obj;
            return (String) jo.get(parameter);
        }
        catch(FileNotFoundException e){
            System.out.println("Credential file could not be found. Please create it at settings//config.json");
        }
        catch(ParseException ex){
            System.out.println("Ensure that your credential file is valid JSON");
        }
        catch(IOException ex){
            System.out.println("An error occurred while reading the credential file");
        }
        return "";

    }

    public List<OrgChannelTuple> getRefreshChannels(){
        List<OrgChannelTuple> orgChannelTuples = new ArrayList<>();
        try{
            File channelFile = new File("settings//upcomingChannels.txt");
            if(channelFile.createNewFile()){
                System.out.println("upcomingChannels.txt created. Please fill it out with the organizations you want to track (refer to README)");
            }
            for (String line : Files.readAllLines(Paths.get("settings//upcomingChannels.txt"))) {
                String type = line.split(":")[0];
                String name = line.split(":")[1];
                String channelIdStr = line.split(":")[2];
                long channelId = Long.parseLong(channelIdStr);
                orgChannelTuples.add(new OrgChannelTuple(type, name, channelId));
            }
        } catch (IOException e) {
            System.out.println("Unable to create upcomingChannels.txt file for updating Discord Channels");
        }
        return orgChannelTuples;
    }

}
