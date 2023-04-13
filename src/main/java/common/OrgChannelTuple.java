package common;

public class OrgChannelTuple {
    private String name;
    private long discordChannelId;
    private String type;

    public OrgChannelTuple(String type, String name, long discordChannelId) {
        this.name = name;
        this.type = type;
        this.discordChannelId = discordChannelId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDiscordChannelId() {
        return discordChannelId;
    }

    public void setDiscordChannelId(long discordChannelId) {
        this.discordChannelId = discordChannelId;
    }
}
