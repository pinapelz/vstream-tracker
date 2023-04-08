package vtuber;

import com.pina.Holodex;
import com.pina.HolodexException;
import com.pina.datatypes.SimpleVideo;
import com.pina.query.VideoQueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class ScheduleHandler {
    Holodex holodex;

    public ScheduleHandler(String apikey) {
        System.out.println("ScheduleHandler initialized");
        holodex = new Holodex(apikey);

    }

    public List<SimpleVideo> getSchedule(String org, int limit) {
        System.out.println("Getting schedule for " + org);
        List<SimpleVideo> upcomingAndLiveVideos = new ArrayList<>();
        try {
            List<SimpleVideo> upcomingVideos = holodex.getLiveAndUpcomingVideos(new VideoQueryBuilder().setOrg(org).setLimit(limit).setStatus("upcoming"));
            List<SimpleVideo> liveVideos = holodex.getLiveAndUpcomingVideos(new VideoQueryBuilder().setStatus("live").setOrg(org).setLimit(limit));
            upcomingAndLiveVideos.addAll(liveVideos);
            upcomingAndLiveVideos.addAll(upcomingVideos);
        } catch (HolodexException e) {
            System.out.println("Error getting schedule for " + org);
            System.out.println(e.getMessage());
        }
        return upcomingAndLiveVideos;
    }

    public List<SimpleVideo> getSchedule(String org) {
        System.out.println("Getting schedule for " + org);
        List<SimpleVideo> upcomingAndLiveVideos = new ArrayList<>();
        try {
            List<SimpleVideo> upcomingVideos = holodex.getLiveAndUpcomingVideos(new VideoQueryBuilder().setOrg(org).setStatus("upcoming"));
            List<SimpleVideo> liveVideos = holodex.getLiveAndUpcomingVideos(new VideoQueryBuilder().setStatus("live").setOrg(org));
            upcomingAndLiveVideos.addAll(liveVideos);
            upcomingAndLiveVideos.addAll(upcomingVideos);
        } catch (HolodexException e) {
            System.out.println("Error getting schedule for " + org);
            System.out.println(e.getMessage());
        }
        return upcomingAndLiveVideos;
    }

    public List<SimpleVideo> getScheduleChannelId(String channelId) {
        System.out.println("Getting schedule for " + channelId);
        List<SimpleVideo> upcomingAndLiveVideos = new ArrayList<>();
        try {
            List<SimpleVideo> upcomingVideos = holodex.getLiveAndUpcomingVideos(new VideoQueryBuilder().setChannelId(channelId).setStatus("upcoming"));
            List<SimpleVideo> liveVideos = holodex.getLiveAndUpcomingVideos(new VideoQueryBuilder().setStatus("live").setChannelId(channelId));
            upcomingAndLiveVideos.addAll(liveVideos);
            upcomingAndLiveVideos.addAll(upcomingVideos);
        } catch (HolodexException e) {
            System.out.println("Error getting schedule for " + channelId);
            System.out.println(e.getMessage());
        }
        return upcomingAndLiveVideos;

    }
}
