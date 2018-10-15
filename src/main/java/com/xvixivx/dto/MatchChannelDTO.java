package com.xvixivx.dto;

public class MatchChannelDTO {

    private long guildId;
    private String guildName;
    private long channelId;
    private String channelName;
    private String region;
    private String platform;
    private String gameType;
    private boolean receive;

    public MatchChannelDTO(){}

    public MatchChannelDTO(long guildId,
                           String guildName,
                           long channelId,
                           String channelName,
                           String region,
                           String platform,
                           String gameType,
                           boolean receive)
    {
        this.guildId = guildId;
        this.guildName = guildName;
        this.channelId = channelId;
        this.channelName = channelName;
        this.region = region;
        this.platform = platform;
        this.gameType = gameType;
        this.receive = receive;
    }

    public long getGuildId() { return guildId; }

    public String getGuildName() { return guildName; }

    public long getChannelId() { return channelId; }

    public String getChannelName() { return channelName; }

    public String getRegion() { return region; }

    public String getPlatform() { return platform; }

    public String getGameType() { return gameType; }

    public boolean isReceive() { return receive; }

    public void setGuildId(long guildId) { this.guildId = guildId; }

    public void setGuildName(String guildName) { this.guildName = guildName; }

    public void setChannelId(long channelId) { this.channelId = channelId; }

    public void setChannelName(String channelName) { this.channelName = channelName; }

    public void setRegion(String region) { this.region = region; }

    public void setPlatform(String platform) { this.platform = platform; }

    public void setGameType(String gameType) { this.gameType = gameType; }

    public void setReceive(boolean receive) { this.receive = receive; }
}
