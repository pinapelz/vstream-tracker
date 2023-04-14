package builders;

import com.pina.datatypes.SimpleVideo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import java.text.NumberFormat;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleMessageBuilder {
    private int COLOR = 8805674;
    private NumberFormat numberFormat = NumberFormat.getInstance();
    public MessageEmbed buildLiveAndUpcomingMessage(List<SimpleVideo> videos){
        if (videos.size() == 0){
            return new EmbedBuilder()
                    .setTitle("No streams upcoming or live. Check back later!")
                    .setDescription("If you think this is a mistake, please check that you've spelled the organization as listed on Holodex")
                    .setColor(new Color(COLOR))
                    .setTimestamp(OffsetDateTime.now())
                    .build();
        }
        String pfp = videos.get(0).channel.photo;
        EmbedBuilder messageBuilder = new EmbedBuilder()
                .setThumbnail(pfp)
                .setTitle("Upcoming and Live Streams")
                .setDescription("The schedule you asked for")
                .setColor(new Color(COLOR))
                .setTimestamp(OffsetDateTime.now());

        for (SimpleVideo video : videos){
            String gmtStartTime = video.start_scheduled;
            long unixTime = OffsetDateTime.parse(gmtStartTime).toEpochSecond();
            String unixTimeStr = "<t:" + unixTime + ":R> ⏰";
            if (video.status.equals("live")){
                unixTimeStr = "LIVE \uD83D\uDD34";
            }
            String titleText = video.channel.english_name + " - " + unixTimeStr;
            try {
                if (video.channel.english_name.equals(null)) {
                    titleText = video.channel.name + " - " + unixTimeStr;
                }
            } catch (NullPointerException e){
                titleText = video.channel.name + " - " + unixTimeStr;
            }
            String videoURL = "https://www.youtube.com/watch?v=" + video.id;
            messageBuilder.addField(titleText, "["+video.title+"]"+"("+videoURL+")", false);
        }
        return messageBuilder.build();

    }

    public ArrayList<MessageEmbed> getUpcomingLiveListMessages(List<SimpleVideo> simpleVideos){
        ArrayList<MessageEmbed> messageEmbeds = new ArrayList<>();
        for (SimpleVideo video : simpleVideos){
            String channel_name  = video.channel.english_name;
            try {
                if (video.channel.english_name.equals(null)) {
                    channel_name = video.channel.name;
                }
            } catch (NullPointerException e){
                channel_name = video.channel.name;
            }
            String title = channel_name + " is streaming soon!   ⏰";
            String fieldTitle = "Scheduled Start Time";
            String viewerText = "Not Live Yet";
            if (video.status.equals("live")){
                title = channel_name + " is live!   \uD83D\uDD34";
                fieldTitle = "Live Since";
                viewerText = "Live Viewers";
            }
            String gmtStartTime = video.start_scheduled;
            long unixTime = OffsetDateTime.parse(gmtStartTime).toEpochSecond();
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle(title)
                    .setDescription("["+video.title+"]"+"(https://www.youtube.com/watch?v="+video.id+")")
                    .addField(fieldTitle, "<t:" + unixTime + ":R>", true)
                    .setThumbnail(video.channel.photo)
                    .setImage("https://img.youtube.com/vi/"+video.id+"/maxresdefault.jpg")
                    .setTimestamp(OffsetDateTime.now());
            if (video.status.equals("live")){ //uhhhhhh boilerplate but oh well
                embedBuilder.addField(viewerText, numberFormat.format(video.live_viewers), true);
            }
            messageEmbeds.add(embedBuilder.build());
        }
        return messageEmbeds;

    }
}
