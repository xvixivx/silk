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

public class Help extends ListenerAdapter {

    public void run(MessageReceivedEvent event, String prefix)
    {
        Message message = event.getMessage();
        String[] contents = message.getContentRaw().split(" ");

        String botName = "silk";
        // Check Channel Type

        Guild guild = event.getGuild();

        TextChannel channel = event.getTextChannel();

        // Check permission
        if (!guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
        {
            return;
        }

        if (contents[1].equalsIgnoreCase("help"))
        {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("**Command List**");
            builder.setColor(Color.CYAN);
            builder.setDescription("This server's prefix is `" + prefix + "`");
            builder.addField(prefix + " invite", "make the invitation link", false);
            builder.addField(prefix + " agent", "mcvs agent info", false);
            builder.addField(prefix + " bot", "bot info ", false);
            builder.addField(prefix + " guild", "guild info ", false);
            builder.addField(prefix + " share channel pvp", "share pvp code channel", false);
            builder.addField(prefix + " share channel tournament", "share tournament code channel", false);
            builder.addField(prefix + " match", "send match info to the other guilds", false);
            builder.addField(prefix + " match set help", "help of a matchmaking channel settings", false);
            builder.addField(prefix + " match guilds", "guild list", false);
            channel.sendMessage(builder.build()).queue();
        }
    }
}
