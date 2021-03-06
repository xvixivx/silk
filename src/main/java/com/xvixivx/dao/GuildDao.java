package com.xvixivx.dao;

import com.xvixivx.dto.GuildDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GuildDao {

    private final Logger logger = LoggerFactory.getLogger(GuildDao.class);

    private Connection connection = null;
    private PreparedStatement pstmt = null;

    private String url = "";
    private String userName = "";
    private String passWord = "";

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

    public GuildDto find(String guildId)
    {
        this.initialize();
        GuildDto guild = new GuildDto();

        // Send Query
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            String sql = "SELECT "
                    + "id, "
                    + "name, "
                    + "region "
                    + "FROM guilds WHERE id = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, guildId);
            logger.debug("GuildDAO SQL: " + pstmt.toString());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String region = rs.getString("region");

                guild = new GuildDto(id, name, region);
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

    public List<GuildDto> findAll()
    {
        this.initialize();
        List<GuildDto> guilds = new ArrayList<>();

        // Send Query
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            String sql = "SELECT "
                    + "id, "
                    + "name, "
                    + "region "
                    + "FROM guilds ";
            pstmt = connection.prepareStatement(sql);
            logger.debug("GuildDAO SQL: " + pstmt.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String region = rs.getString("region");

                GuildDto guild = new GuildDto(id, name, region);
                guilds.add(guild);

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

        return guilds;
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
            logger.debug("GuildDAO SQL: " + pstmt.toString());

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
            logger.debug("GuildDAO SQL: " + pstmt.toString());
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
