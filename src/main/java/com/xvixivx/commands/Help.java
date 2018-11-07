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

    public void run(MessageReceivedEvent event)
    {
        Message message = event.getMessage();
        String[] contents = message.getContentRaw().split(" ");

        String botName = "silk";
        // Check Channel Type

        Guild guild = event.getGuild();
        // Check prefix
        if (!(Content.isRightPrefix(guild.getId(), contents[0]) || botName.equalsIgnoreCase(contents[0])))
        {
            return;
        }

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
            builder.setDescription("This server's prefix is `-s`");
            builder.addField("-s invite", "make the invitation link", false);
            builder.addField("-s agent", "mcvs agent info", false);
            builder.addField("-s bot", "bot info ", false);
            builder.addField("-s guild", "guild info ", false);
            builder.addField("-s share channel pvp", "share pvp code channel", false);
            builder.addField("-s share channel tournament", "share tournament code channel", false);
            builder.addField("-s match", "send match info to the other guilds", false);
            builder.addField("-s match set help", "help of a matchmaking channel settings", false);
            builder.addField("-s match guilds", "guild list", false);
            channel.sendMessage(builder.build()).queue();
        }
    }
}
