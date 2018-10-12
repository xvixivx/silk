package com.xvixivx.commands;

import com.xvixivx.util.Content;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class Invite extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        // We don't want to respond to other bot accounts, including ourself
        Message message = event.getMessage();
        String[] contents = message.getContentRaw().split(" ");
        // getContentRaw() is an atomic getter
        // getContentDisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)

        MessageChannel channel = event.getChannel();

        // Check Channel Type
        if (!event.isFromType(ChannelType.TEXT)) {
            return;
        }
        // Check prefix
        if (!Content.isRightPrefix(contents[0]))
        {
            return;
        }

        if (!Content.hasCommand(contents))
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
