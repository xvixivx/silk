package com.xvixivx.dao;

import com.xvixivx.dto.GuildDTO;
import com.xvixivx.dto.MatchChannelDTO;
import com.xvixivx.dto.MatchDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MatchChannelDAO {

    private Connection connection = null;
    private PreparedStatement pstmt = null;

    private String url = "";
    private String userName = "";
    private String passWord = "";

    final Logger logger = LoggerFactory.getLogger(GuildDAO.class);

    private void initialize()
    {
        // Load Driver
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        // Get Properties
        try
        {
            Properties properties = new Properties();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            url = properties.getProperty("mySqlUrl");
            userName = properties.getProperty("mySqlUserName");
            passWord = properties.getProperty("mySqlPassword");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public MatchChannelDTO find(long guildId, long channelId)
    {
        this.initialize();
        MatchChannelDTO matchChannel = new MatchChannelDTO();

        // Send Query
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            String sql = "SELECT "
                    + "guilds.name, "
                    + "match_channels.name, "
                    + "match_channels.region, "
                    + "platform, "
                    + "game_type, "
                    + "receive "
                    + "FROM guilds "
                    + "LEFT JOIN match_channels "
                    + "ON guilds.id = guild_id "
                    + "WHERE guild_id = ? AND channel_id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, guildId);
            pstmt.setLong(2, channelId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
            {
                String guildName = rs.getString("guilds.name");
                String channelName = rs.getString("match_channels.name");
                String region = rs.getString("region");
                String platform = rs.getString("platform");
                String gameType = rs.getString("game_type");
                boolean receive = rs.getBoolean("receive");

                matchChannel = new MatchChannelDTO(guildId, guildName, channelId, channelName, region, platform, gameType, receive);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return matchChannel;
    }

    public List<MatchChannelDTO> findChannels(String region, String platform, String gameType)
    {
        this.initialize();
        List<MatchChannelDTO> matchChannels = new ArrayList<>();

        // Send Query
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            String sql = "SELECT "
                    + "guild_id, "
                    + "guilds.name, "
                    + "channel_id, "
                    + "match_channels.name "
                    + "FROM guilds "
                    + "LEFT JOIN match_channels "
                    + "ON guilds.id = guild_id "
                    + "WHERE (match_channels.region = ? OR match_channels.region IS NULL) "
                    + "AND (platform = ? OR platform IS NULL) "
                    + "AND (game_type = ? OR game_type IS NULL) "
                    + "AND receive = true ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, region);
            pstmt.setString(2, platform);
            pstmt.setString(3, gameType);
            ResultSet rs = pstmt.executeQuery();
            boolean receive = true;

            while (rs.next())
            {
                long guildId = rs.getLong("guild_id");
                String guildName = rs.getString("guilds.name");
                long channelId = rs.getLong("channel_id");
                String channelName = rs.getString("match_channels.name");

                MatchChannelDTO matchChannel = new MatchChannelDTO(guildId, guildName, channelId, channelName, region, platform, gameType, receive);
                matchChannels.add(matchChannel);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return matchChannels;
    }

    public int upsertAll(long guildId, long channelId, String name, String region, String platform, String gameType, boolean receive)
    {
        int result = 0;

        this.initialize();
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            connection.setAutoCommit(false);
            String sql = "INSERT INTO match_channels "
                    + "(guild_id, channel_id, name, region, platform, game_type, receive) "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY "
                    + "UPDATE "
                    + "guild_id = ?, channel_id = ?, name = ?, region = ?, platform = ?, game_type = ?, receive = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, guildId);
            pstmt.setLong(2, channelId);
            pstmt.setString(3, name);
            pstmt.setString(4, region);
            pstmt.setString(5, platform);
            pstmt.setString(6, gameType);
            pstmt.setBoolean(7, receive);
            pstmt.setLong(8, guildId);
            pstmt.setLong(9, channelId);
            pstmt.setString(10, name);
            pstmt.setString(11, region);
            pstmt.setString(12, platform);
            pstmt.setString(13, gameType);
            pstmt.setBoolean(14, receive);
            logger.debug("MatchChannelDAO SQL: " + pstmt.toString());

            result = pstmt.executeUpdate();
            connection.commit();
        }
        catch (SQLException e)
        {
            try
            {
                connection.rollback();
            }
            catch (SQLException e2)
            {
                e2.printStackTrace();
            }
        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public int updateReceive(long guildId, long channelId, boolean receive)
    {
        int result = 0;

        this.initialize();
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            connection.setAutoCommit(false);
            String sql = "UPDATE match_channels "
                    + "SET receive = ? "
                    + "WHERE guild_id = ? AND channel_id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setBoolean(1, receive);
            pstmt.setLong(2, guildId);
            pstmt.setLong(3, channelId);

            result = pstmt.executeUpdate();
            connection.commit();
        }
        catch (SQLException e)
        {
            try
            {
                connection.rollback();
            }
            catch (SQLException e2)
            {
                e2.printStackTrace();
            }
        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public int updateChannelName(long guildId, long channelId, String name)
    {
        int result = 0;

        this.initialize();
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            connection.setAutoCommit(false);
            String sql = "UPDATE match_channels "
                    + "SET name = ? "
                    + "WHERE guild_id = ? AND channel_id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setLong(2, guildId);
            pstmt.setLong(3, channelId);

            result = pstmt.executeUpdate();
            connection.commit();
        }
        catch (SQLException e)
        {
            try
            {
                connection.rollback();
            }
            catch (SQLException e2)
            {
                e2.printStackTrace();
            }
        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public int delete(long guildId, long channelId)
    {
        int result = 0;

        this.initialize();
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            connection.setAutoCommit(false);
            String sql = "DELETE FROM match_channels "
                    + "WHERE guild_id = ? AND channel_id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, guildId);
            pstmt.setLong(2, channelId);

            result = pstmt.executeUpdate();
            connection.commit();
        }
        catch (SQLException e)
        {
            try
            {
                connection.rollback();
            }
            catch (SQLException e2)
            {
                e2.printStackTrace();
            }
        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}