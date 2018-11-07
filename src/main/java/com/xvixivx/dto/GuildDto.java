package com.xvixivx.dto;

public class GuildDto {

    private long id;
    private String name;
    private String region;
//    private String matchChannelId;
//    private boolean receiveMatch;

    public GuildDto()
    {
    }

    public GuildDto(long id,
                    String name,
                    String region)
    {
        this.id = id;
        this.name = name;
        this.region = region;
    }

    public long getId() { return id; }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public void setId(long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setRegion(String region) {
        this.region = region;
    }
}
