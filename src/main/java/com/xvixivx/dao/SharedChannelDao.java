package com.xvixivx.dao;

import com.xvixivx.dto.GuildDTO;
import com.xvixivx.dto.SharedChannelDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SharedChannelDao {

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

    public SharedChannelDto find(long guildId, long channelId)
    {
        this.initialize();
        SharedChannelDto sharedChannel = new SharedChannelDto();

        // Send Query
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            String sql = "SELECT "
                    + "guilds.name, "
                    + "guilds.region, "
                    + "shared_channels.name, "
                    + "game_type, "
                    + "shared_channels.region, "
                    + "platform, "
                    + "receive "
                    + "FROM shared_channels "
                    + "LEFT JOIN guilds "
                    + "ON guilds.id = guild_id "
                    + "WHERE guild_id = ? "
                    + "AND channel_id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, guildId);
            pstmt.setLong(2, channelId);
            logger.debug("SharedChannelDao SQL: " + pstmt.toString());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
            {
                GuildDTO guild = new GuildDTO();
                guild.setId(guildId);
                guild.setName(rs.getString("guilds.name"));
                guild.setRegion(rs.getString("guilds.region"));

                String channelName = rs.getString("shared_channels.name");
                String gameType = rs.getString("game_type");
                String region = rs.getString("shared_channels.region");
                String platform = rs.getString("platform");
                boolean receive = rs.getBoolean("receive");

                sharedChannel = new SharedChannelDto(guild, channelId, channelName, gameType, region, platform, receive);
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

        return sharedChannel;
    }

    public List<SharedChannelDto> findTournamentChannels()
    {
        this.initialize();
        List<SharedChannelDto> sharedChannels = new ArrayList<>();
        GuildDTO guild = new GuildDTO();

        // Send Query
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            String sql = "SELECT "
                    + "guild_id, "
                    + "guilds.name, "
                    + "guilds.region, "
                    + "channel_id, "
                    + "shared_channels.name "
                    + "FROM shared_channels "
                    + "LEFT JOIN guilds "
                    + "ON guilds.id = guild_id "
                    + "WHERE game_type = ? "
                    + "AND receive = true ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "tournament");
            logger.debug("SharedChannelDao SQL: " + pstmt.toString());
            ResultSet rs = pstmt.executeQuery();
            boolean receive = true;

            while (rs.next())
            {
                guild.setId(rs.getLong("guild_id"));
                guild.setName(rs.getString("guilds.name"));
                guild.setRegion(rs.getString("guilds.region"));

                long channelId = rs.getLong("channel_id");
                String channelName = rs.getString("shared_channels.name");

                SharedChannelDto sharedChannel = new SharedChannelDto(guild, channelId, channelName, receive);
                sharedChannels.add(sharedChannel);
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

        return sharedChannels;
    }

    public List<SharedChannelDto> findPvPChannels(String region, String platform)
    {
        this.initialize();
        List<SharedChannelDto> sharedChannels = new ArrayList<>();
        GuildDTO guild = new GuildDTO();

        // Send Query
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            String sql = "SELECT "
                    + "guild_id, "
                    + "guilds.name, "
                    + "guilds.region, "
                    + "channel_id, "
                    + "shared_channels.name "
                    + "FROM shared_channels "
                    + "LEFT JOIN guilds "
                    + "ON guilds.id = guild_id "
                    + "WHERE game_type = ? "
                    + "AND shared_channels.region = ? "
                    + "AND platform = ? "
                    + "AND receive = true ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "pvp");
            pstmt.setString(2, region);
            pstmt.setString(3, platform);
            logger.debug("SharedChannelDao SQL: " + pstmt.toString());
            ResultSet rs = pstmt.executeQuery();
            boolean receive = true;

            while (rs.next())
            {
                guild.setId(rs.getLong("guild_id"));
                guild.setName(rs.getString("guilds.name"));
                guild.setRegion(rs.getString("guilds.region"));

                long channelId = rs.getLong("channel_id");
                String channelName = rs.getString("shared_channels.name");

                SharedChannelDto sharedChannel = new SharedChannelDto(guild, channelId, channelName, "pvp", region, platform, receive);
                sharedChannels.add(sharedChannel);
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

        return sharedChannels;
    }

    public int upsertPvPChannels(long guildId, long channelId, String name, String region, String platform, boolean receive)
    {
        int result = 0;

        this.initialize();
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            connection.setAutoCommit(false);
            String sql = "INSERT INTO shared_channels "
                    + "(guild_id, channel_id, name, game_type, region, platform, receive) "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY "
                    + "UPDATE "
                    + "guild_id = ?, channel_id = ?, name = ?, game_type = ?, region = ?, platform = ?, receive = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, guildId);
            pstmt.setLong(2, channelId);
            pstmt.setString(3, name);
            pstmt.setString(4, "pvp");
            pstmt.setString(5, region);
            pstmt.setString(6, platform);
            pstmt.setBoolean(7, receive);
            pstmt.setLong(8, guildId);
            pstmt.setLong(9, channelId);
            pstmt.setString(10, name);
            pstmt.setString(11, "pvp");
            pstmt.setString(12, region);
            pstmt.setString(13, platform);
            pstmt.setBoolean(14, receive);
            logger.debug("SharedChannelDao SQL: " + pstmt.toString());
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

    public int upsertTournamentChannels(long guildId, long channelId, String name, boolean receive)
    {
        int result = 0;

        this.initialize();
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            connection.setAutoCommit(false);
            String sql = "INSERT INTO shared_channels "
                    + "(guild_id, channel_id, name, game_type, region, platform, receive) "
                    + "VALUES(?, ?, ?, ?, NULL, NULL, ?) "
                    + "ON DUPLICATE KEY "
                    + "UPDATE "
                    + "guild_id = ?, channel_id = ?, name = ?, game_type = ?, region = NULL, platform = NULL, receive = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, guildId);
            pstmt.setLong(2, channelId);
            pstmt.setString(3, name);
            pstmt.setString(4, "tournament");
            pstmt.setBoolean(5, receive);
            pstmt.setLong(6, guildId);
            pstmt.setLong(7, channelId);
            pstmt.setString(8, name);
            pstmt.setString(9, "tournament");
            pstmt.setBoolean(10, receive);
            logger.debug("SharedChannelDao SQL: " + pstmt.toString());
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
            String sql = "UPDATE shared_channels "
                    + "SET receive = ? "
                    + "WHERE guild_id = ? AND channel_id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setBoolean(1, receive);
            pstmt.setLong(2, guildId);
            pstmt.setLong(3, channelId);
            logger.debug("SharedChannelDao SQL: " + pstmt.toString());
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
            String sql = "UPDATE shared_channels "
                    + "SET name = ? "
                    + "WHERE guild_id = ? AND channel_id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setLong(2, guildId);
            pstmt.setLong(3, channelId);
            logger.debug("SharedChannelDao SQL: " + pstmt.toString());
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
            String sql = "DELETE FROM shared_channels "
                    + "WHERE guild_id = ? AND channel_id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, guildId);
            pstmt.setLong(2, channelId);
            logger.debug("SharedChannelDao SQL: " + pstmt.toString());
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
