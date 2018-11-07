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

    public void run(MessageReceivedEvent event)
    {
        Message message = event.getMessage();
        String[] contents = message.getContentRaw().split(" ");

        Guild guild = event.getGuild();

        TextChannel channel = event.getTextChannel();
        JDABot bot = event.getJDA().asBot();
        ApplicationInfo appInfo = bot.getApplicationInfo().complete();

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
