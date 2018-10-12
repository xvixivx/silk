package com.xvixivx.dao;

import com.xvixivx.dto.AgentDTO;
import com.xvixivx.dto.GuildDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class GuildDAO {

    private Connection connection = null;
    private PreparedStatement pstmt = null;

    private String url = "";
    private String userName = "";
    private String passWord = "";

    Logger logger = LoggerFactory.getLogger(GuildDAO.class);

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

    public GuildDTO find(String guildId)
    {
        this.initialize();
        GuildDTO guild = new GuildDTO();

        // Send Query
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            String sql = "SELECT "
                    + "name, "
                    + "region, "
                    + "match_channel_id, "
                    + "receive_match "
                    + "FROM guilds WHERE id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, guildId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String region = rs.getString("region");
                String matchChannelId = rs.getString("match_channel_id");
                boolean receiveMatch = rs.getBoolean("receive_match");

                guild = new GuildDTO(name, region, matchChannelId, receiveMatch);
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

        return guild;
    }

    public int upsertAll(long id, String name, String region, long matchChannelId, boolean receiveMatch)
    {
        int result = 0;

        this.initialize();
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            connection.setAutoCommit(false);
            String sql = "INSERT INTO guilds "
                    + "(id, name, region, match_channel_id, receive_match) "
                    + "VALUES(?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY "
                    + "UPDATE "
                    + "id = ?, name = ?, region = ?, match_channel_id = ?, receive_match = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, region);
            pstmt.setLong(4, matchChannelId);
            pstmt.setBoolean(5, receiveMatch);
            pstmt.setLong(6, id);
            pstmt.setString(7, name);
            pstmt.setString(8, region);
            pstmt.setLong(9, matchChannelId);
            pstmt.setBoolean(10, receiveMatch);
//            System.out.println(pstmt.toString());

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

    public int updateReceiveMatch(long id, boolean receiveMatch)
    {
        int result = 0;

        this.initialize();
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            connection.setAutoCommit(false);
            String sql = "UPDATE guilds "
                    + "SET receive_match = ? "
                    + "WHERE id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setBoolean(1, receiveMatch);
            pstmt.setLong(2, id);

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
