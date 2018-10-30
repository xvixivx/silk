package com.xvixivx.dto;

public class MatchChannelDTO {

    private GuildDTO guild = new GuildDTO();
    private long channelId;
    private String channelName;
    private String region;
    private String platform;
    private String gameType;
    private boolean receive;

    public MatchChannelDTO(){}

    public MatchChannelDTO(GuildDTO guild,
                           long channelId,
                           String channelName,
                           String region,
                           String platform,
                           String gameType,
                           boolean receive)
    {
        // If I write like this.guild = guild, All MatchChannelDTO will have same GuildDTO Instance
        this.guild.setId(guild.getId());
        this.guild.setName(guild.getName());
        this.guild.setRegion(guild.getRegion());
        this.channelId = channelId;
        this.channelName = channelName;
        this.region = region;
        this.platform = platform;
        this.gameType = gameType;
        this.receive = receive;
    }

    public GuildDTO getGuild() { return guild; }

    public long getChannelId() { return channelId; }

    public String getChannelName() { return channelName; }

    public String getRegion() { return region; }

    public String getPlatform() { return platform; }

    public String getGameType() { return gameType; }

    public boolean isReceive() { return receive; }

    public void setGuild(GuildDTO guild) { this.guild = guild; }

    public void setChannelId(long channelId) { this.channelId = channelId; }

    public void setChannelName(String channelName) { this.channelName = channelName; }

    public void setRegion(String region) { this.region = region; }

    public void setPlatform(String platform) { this.platform = platform; }

    public void setGameType(String gameType) { this.gameType = gameType; }

    public void setReceive(boolean receive) { this.receive = receive; }
}
