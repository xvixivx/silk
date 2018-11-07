package com.xvixivx.commands;

import com.xvixivx.util.Content;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class Invite extends ListenerAdapter {

    public void run(MessageReceivedEvent event)
    {

        Message message = event.getMessage();
        String[] contents = message.getContentRaw().split(" ");

        Guild guild = event.getGuild();
        TextChannel channel = event.getTextChannel();

        // Check permission
        if (!guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
        {
            return;
        }

        if (contents[1].equalsIgnoreCase("invite")) {
            EmbedBuilder builder = new EmbedBuilder();

            if (contents.length != 2) {
                builder.setTitle("**Error**");
                builder.setColor(Color.RED);
                builder.addField("usage", "-s invite", false);
                channel.sendMessage(builder.build()).queue();
                builder.clear();
                return;
            }

            builder.setTitle("Get SILK on your server!");
            builder.setColor(Color.CYAN);
            builder.setDescription("[Click to invite SILK to your server]"
                    + "(https://discordapp.com/api/oauth2/authorize?"
                    + "client_id=493037301429960706"
                    + "&permissions=339213393"
                    + "&scope=bot)");
            channel.sendMessage(builder.build()).queue();
            builder.clear();
            return;
        }
    }
}
