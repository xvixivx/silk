package com.xvixivx.commands;

import com.xvixivx.util.Content;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import redis.clients.jedis.Jedis;

import java.awt.*;

public class Prefix extends ListenerAdapter {

    public void run(MessageReceivedEvent event)
    {
        Message message = event.getMessage();
        String[] contents = message.getContentRaw().split(" ");

        Guild guild = event.getGuild();

        TextChannel channel = event.getTextChannel();
        EmbedBuilder builder = new EmbedBuilder();

        if (!contents[1].equalsIgnoreCase("prefix"))
        {
            return;
        }

        Jedis jedis = new Jedis("localhost", 6379);
        String prefix;

        if (contents.length == 2)
        {
            prefix = jedis.get(guild.getId());
            if (prefix == null)
            {
                builder.setTitle("Error");
                builder.setColor(Color.RED);
                builder.setDescription("Your server's prefix is not set yet");
                channel.sendMessage(builder.build()).queue();
                builder.clear();
                return;
            }
            builder.setTitle("Prefix");
            builder.setDescription("Your server's prefix is " + prefix);
            builder.setColor(Color.CYAN);
            channel.sendMessage(builder.build()).queue();
            builder.clear();
            return;
        }

        if (contents[2].equalsIgnoreCase("set"))
        {
            if (!event.getMember().isOwner() && !event.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                builder.setTitle("**Error**");
                builder.setColor(Color.RED);
                builder.setDescription("Permission Required");
                channel.sendMessage(builder.build()).queue();
                builder.clear();
                return;
            }

            if (contents.length != 4)
            {
                builder.setTitle("Error");
                builder.setColor(Color.RED);
                builder.setDescription("Usage: -s prefix set (prefix)");
                channel.sendMessage(builder.build()).queue();
                builder.clear();
                return;
            }

            prefix = contents[3];
            if (!Content.isRightSize(prefix, 5))
            {
                builder.setTitle("Error");
                builder.setColor(Color.RED);
                builder.setDescription("Max character length of the prefix is 5");
                channel.sendMessage(builder.build()).queue();
                builder.clear();
                return;
            }
            String statusCode = jedis.set(guild.getId(), prefix);
            System.out.println("Status Code: " + statusCode);

            if (statusCode.equals("NG"))
            {
                builder.setTitle("Error!");
                builder.setColor(Color.RED);
                builder.setDescription("Something happend");
                channel.sendMessage(builder.build()).queue();
                builder.clear();
                return;
            }

            builder.setTitle("Success!");
            builder.setColor(Color.CYAN);
            builder.setDescription("Your server's prefix is " + prefix);
            channel.sendMessage(builder.build()).queue();
            builder.clear();
        }
    }
}
