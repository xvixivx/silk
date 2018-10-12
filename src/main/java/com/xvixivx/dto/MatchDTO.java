package com.xvixivx.dto;

import com.xvixivx.util.Content;

public class MatchDTO {

    private String region;
    private String platform;
    private String gameType;
    private String roomId;
    private String note;
    private boolean ready;

    public MatchDTO()
    {
        this.note = "";
        this.ready = true;
    }

    public String getRegion() { return region; }

    public String getPlatform() { return platform; }

    public String getGameType() { return gameType; }

    public String getRoomId() { return roomId; }

    public String getNote() { return note; }

    public boolean isReady() { return ready; }

    public void setRegion(String region)
    {
        String regions[] = {"as", "eu", "na", "sa"};

        if (Content.isMatch(region, regions))
        {
            this.region = region;
        }
        else
        {
            this.ready = false;
        }
    }

    public void setPlatform(String platform)
    {
        String platforms[] = {"pc", "mobile"};

        if (Content.isMatch(platform, platforms))
        {
            this.platform = platform;
        }
        else
        {
            this.ready = false;
        }
    }

    public void setGameType(String gameType)
    {
        String gameTypes[] = {"group", "tournament"};
        if (Content.isMatch(gameType, gameTypes))
        {
            this.gameType = gameType;
        }
        else
        {
            this.ready = false;
        }
    }

    public void setRoomId(String roomId)
    {
        if (Content.isRightSize(roomId, 8) && Content.isMatchTheRegex(roomId))
        {
            this.roomId = roomId;
        }
        else
        {
            this.ready = false;
        }
    }

    public void setNote(String note)
    {
        if (Content.isRightSize(note, 100))
        {
            this.note = note;
        }
        else
        {
            this.ready = false;
        }
    }

    public void setReady(boolean ready) { this.ready = ready; }
}
