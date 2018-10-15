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
                    + "region "
                    + "FROM guilds WHERE id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, guildId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String region = rs.getString("region");

                guild = new GuildDTO(name, region);
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

    public int upsertAll(long id, String name, String region)
    {
        int result = 0;

        this.initialize();
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            connection.setAutoCommit(false);
            String sql = "INSERT INTO guilds "
                    + "(id, name, region) "
                    + "VALUES(?, ?, ?) "
                    + "ON DUPLICATE KEY "
                    + "UPDATE "
                    + "id = ?, name = ?, region = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, region);
            pstmt.setLong(4, id);
            pstmt.setString(5, name);
            pstmt.setString(6, region);
            logger.debug(pstmt.toString());

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

    public int delete(long id)
    {
        int result = 0;

        this.initialize();
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            connection.setAutoCommit(false);
            String sql = "DELETE FROM guilds "
                    + "WHERE id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, id);

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
