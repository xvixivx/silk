package com.xvixivx.commands;

import com.xvixivx.util.Content;
import net.dv8tion.jda.bot.JDABot;
import net.dv8tion.jda.bot.entities.ApplicationInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class AppInfo extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        // We don't want to respond to other bot accounts, including ourself
        Message message = event.getMessage();
        String[] contents = message.getContentRaw().split(" ");
        // getContentRaw() is an atomic getter
        // getContentDisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)

        // Check Channel Type
        if (!event.isFromType(ChannelType.TEXT))
        {
            return;
        }
        Guild guild = event.getGuild();
        // Check prefix
        if (!Content.isRightPrefix(guild.getId(), contents[0]))
        {
            return;
        }

        TextChannel channel = event.getTextChannel();
        JDABot bot = event.getJDA().asBot();
        ApplicationInfo appInfo = bot.getApplicationInfo().complete();

        if (!Content.hasCommand(contents))
        {
            return;
        }
        // Check permission
        if (!guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
        {
            return;
        }

        if (contents[1].equalsIgnoreCase("bot")) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("**Bot Information**");
            builder.setColor(Color.CYAN);
            builder.addField("Owner", appInfo.getOwner().getName(), false);
//            builder.addField("Public", appInfo.isBotPublic() ? "Yes" : "No", false);
//            builder.addField("Require Code Grand", appInfo.doesBotRequireCodeGrant() ? "Yes" : "No", false);
//            builder.addField("Invite Link", appInfo.getInviteUrl(Permission.MESSAGE_WRITE), false);
            builder.addField("Description", appInfo.getDescription(), false);
            channel.sendMessage(builder.build()).queue();
        }
    }
}
