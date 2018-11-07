package com.xvixivx.dao;

import com.xvixivx.dto.AgentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AgentDao {

    private String url = "";
    private String userName = "";
    private String passWord = "";

    public List<AgentDto> findAgent(String agentName)
    {
        final Logger logger = LoggerFactory.getLogger(AgentDao.class);

        Connection connection = null;
        PreparedStatement pstmt = null;
        List<AgentDto> agents = new ArrayList<>();

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

        // Send Query
        try
        {
            connection = DriverManager.getConnection(url, userName, passWord);
            String sql = "SELECT "
                    + "display_name, "
                    + "role, "
                    + "weapon, "
                    + "alternate_action, "
                    + "ability, "
                    + "ultimate_ability, "
                    + "damage, "
                    + "health, "
                    + "image "
                    + "FROM agents WHERE name = ? ";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, agentName);
            logger.debug("AgentDAO SQL: " + pstmt.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                String displayName = rs.getString("display_name");
                String role = rs.getString("role");
                String weapon = rs.getString("weapon");
                String alternateAction = rs.getString("alternate_action");
                String ability = rs.getString("ability");
                String ultimateAbility = rs.getString("ultimate_ability");
                int damage = rs.getInt("damage");
                int health = rs.getInt("health");
                String image = rs.getString("image");

                AgentDto agent = new AgentDto(displayName, role, weapon, alternateAction, ability, ultimateAbility, damage, health, image);
                agents.add(agent);
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

        return agents;
    }
}
