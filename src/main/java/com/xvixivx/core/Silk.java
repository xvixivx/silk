package com.xvixivx.core;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.io.InputStream;
import java.util.Properties;

public class Silk {
    public static void main(String[] arguments) throws Exception
    {
        try
        {
            JDABuilder builder = new JDABuilder(AccountType.BOT);
            // Get Bot Token
            Properties properties = new Properties();
            InputStream inputStream = Silk.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            String token = properties.getProperty("botToken");
            // Set Bot Token
            builder.setToken(token);
            // Add Event Listener
            builder.addEventListener(new CommandHandler());
            // Set Game
            builder.setGame(Game.of(Game.GameType.DEFAULT,"-s help"));
            // Build
            JDA jda = builder.build();
            // Blocking guarantees that JDA will be completely loaded.
            jda.awaitReady();
        }
        catch (LoginException e)
        {
            //If anything goes wrong in terms of authentication, this is the exception that will represent it
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            // Due to the fact that awaitReady is a blocking method, one which waits until JDA is fully loaded,
            //  the waiting can be interrupted. This is the exception that would fire in that situation.
            // As a note: in this extremely simplified example this will never occur. In fact, this will never occur unless
            //  you use awaitReady in a thread that has the possibility of being interrupted (async thread usage and interrupts)
            e.printStackTrace();
        }
    }
}
