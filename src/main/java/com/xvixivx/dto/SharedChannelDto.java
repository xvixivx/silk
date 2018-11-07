package com.xvixivx.dto;

public class SharedChannelDto {

    private GuildDto guild = new GuildDto();
    private long channelId;
    private String channelName;
    private String gameType;
    private String region;
    private String platform;
    private boolean receive;

    public SharedChannelDto(){}

    public SharedChannelDto(GuildDto guild,
                            long channelId,
                            String channelName,
                            boolean receive)
    {
        this.guild.setId(guild.getId());
        this.guild.setName(guild.getName());
        this.guild.setRegion(guild.getRegion());
        this.channelId = channelId;
        this.channelName = channelName;
        this.receive = receive;
    }

    public SharedChannelDto(GuildDto guild,
                            long channelId,
                            String channelName,
                            String gameType,
                            String region,
                            String platform,
                            boolean receive)
    {
        this.guild.setId(guild.getId());
        this.guild.setName(guild.getName());
        this.guild.setRegion(guild.getRegion());
        this.channelId = channelId;
        this.channelName = channelName;
        this.gameType = gameType;
        this.region = region;
        this.platform = platform;
        this.receive = receive;
    }

    public GuildDto getGuild() { return guild; }

    public long getChannelId() { return channelId; }

    public String getChannelName() { return channelName; }

    public String getGameType() { return gameType; }

    public String getRegion() { return region; }

    public String getPlatform() { return platform; }

    public boolean isReceive() { return receive; }

    public void setGuild(GuildDto guild) { this.guild = guild; }

    public void setChannelId(long channelId) { this.channelId = channelId; }

    public void setChannelName(String channelName) { this.channelName = channelName; }

    public void setGameType(String gameType) { this.gameType = gameType; }

    public void setRegion(String region) { this.region = region; }

    public void setPlatform(String platform) { this.platform = platform; }

    public void setReceive(boolean receive) { this.receive = receive; }
}
