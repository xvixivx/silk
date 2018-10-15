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

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        // We don't want to respond to other bot accounts, including ourself
        Message message = event.getMessage();
        String[] contents = message.getContentRaw().split(" ");
        // getContentRaw() is an atomic getter
        // getContentDisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)

        String botName = "silk";
        // Check Channel Type
        if (!event.isFromType(ChannelType.TEXT))
        {
            return;
        }
        // Check prefix
        if (!(Content.isRightPrefix(contents[0]) || botName.equalsIgnoreCase(contents[0])))
        {
            return;
        }

        Guild guild = event.getGuild();
        TextChannel channel = event.getTextChannel();

        if (!Content.hasCommand(contents))
        {
            return;
        }
        // Check permission
        if (!guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE))
        {
            return;
        }

        if (contents[1].equalsIgnoreCase("help"))
        {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("**Command List**");
            builder.setColor(Color.CYAN);
            builder.setDescription("This server's prefix is `-s`");
            builder.addField("agent", "mcvs agent info", false);
            builder.addField("bot", "bot info ", false);
            builder.addField("guild", "guild info ", false);
            builder.addField("match", "send match info to the other guilds", false);
            builder.addField("match set help", "help of a matchmaking channel settings", false);
            builder.addField("invite", "make the invitation link", false);
            channel.sendMessage(builder.build()).queue();
        }
    }
}
