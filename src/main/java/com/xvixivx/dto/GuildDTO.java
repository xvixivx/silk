package com.xvixivx.dto;

public class GuildDTO {

    private String name;
    private String region;
    private String matchChannelId;
    private boolean receiveMatch;

    public GuildDTO()
    {
    }

    public GuildDTO(String name,
                    String region,
                    String matchChannelId,
                    boolean receiveMatch)
    {
        this.name = name;
        this.region = region;
        this.matchChannelId = matchChannelId;
        this.receiveMatch = receiveMatch;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getMatchChannelId() {
        return matchChannelId;
    }

    public boolean isReceiveMatch() { return receiveMatch; }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setMatchChannelId(String matchChannelId) {
        this.matchChannelId = matchChannelId;
    }

    public void setReceiveMatch(boolean receiveMatch) { this.receiveMatch = receiveMatch; }
}
