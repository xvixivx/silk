package com.xvixivx.dto;

public class MatchChannelDto {

    private GuildDto guild;
    private long channelId;
    private String channelName;
    private String region;
    private String platform;
    private String gameType;
    private boolean receive;

    public MatchChannelDto(){}

    public MatchChannelDto(GuildDto guild,
                           long channelId,
                           String channelName,
                           String region,
                           String platform,
                           String gameType,
                           boolean receive)
    {
        this.guild = new GuildDto(guild.getId(), guild.getName(), guild.getRegion());
        this.channelId = channelId;
        this.channelName = channelName;
        this.region = region;
        this.platform = platform;
        this.gameType = gameType;
        this.receive = receive;
    }

    public GuildDto getGuild() { return guild; }

    public long getChannelId() { return channelId; }

    public String getChannelName() { return channelName; }

    public String getRegion() { return region; }

    public String getPlatform() { return platform; }

    public String getGameType() { return gameType; }

    public boolean isReceive() { return receive; }

    public void setGuild(GuildDto guild) { this.guild = guild; }

    public void setChannelId(long channelId) { this.channelId = channelId; }

    public void setChannelName(String channelName) { this.channelName = channelName; }

    public void setRegion(String region) { this.region = region; }

    public void setPlatform(String platform) { this.platform = platform; }

    public void setGameType(String gameType) { this.gameType = gameType; }

    public void setReceive(boolean receive) { this.receive = receive; }
}
