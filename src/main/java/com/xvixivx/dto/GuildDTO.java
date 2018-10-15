package com.xvixivx.dto;

public class GuildDTO {

    private String name;
    private String region;
//    private String matchChannelId;
//    private boolean receiveMatch;

    public GuildDTO()
    {
    }

    public GuildDTO(String name,
                    String region)
    {
        this.name = name;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
