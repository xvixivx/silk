package com.xvixivx.dto;

import com.xvixivx.util.Content;

import java.util.HashMap;
import java.util.Map;

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

        HashMap<String, String> map = new HashMap<>();
        map.put("p", platforms[0]);
        map.put("m", platforms[1]);

        for (Map.Entry<String, String> entry : map.entrySet())
        {
            if (entry.getKey().equalsIgnoreCase(platform))
            {
                platform = entry.getValue();
            }
        }

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

        HashMap<String, String> map = new HashMap<>();
        map.put("g", gameTypes[0]);
        map.put("t", gameTypes[1]);

        for (Map.Entry<String, String> entry : map.entrySet())
        {
            if (entry.getKey().equalsIgnoreCase(gameType))
            {
                gameType = entry.getValue();
            }
        }

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
