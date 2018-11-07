package com.xvixivx.commands;

import com.xvixivx.util.Content;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GuildInfo extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
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

        if (!Content.hasCommand(contents))
        {
            return;
        }
        // Check permission
        if (!guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
        {
            return;
        }

        if (contents[1].equalsIgnoreCase("guild"))
        {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("**Guild Information**");
            builder.setColor(Color.decode("#FB3364"));
            builder.setThumbnail(guild.getIconUrl());
            builder.addField("Guild Name", guild.getName(), true);
            builder.addField("Region", guild.getRegion().getName(), true);
            builder.addField("Owner", guild.getOwner().getEffectiveName(), true);
            builder.addField("Creation Time", guild.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")), true);
            builder.addField("Text Channels", String.valueOf(guild.getTextChannels().size()), true);
            builder.addField("Voice Channels", String.valueOf(guild.getVoiceChannels().size()), true);
            builder.addField("Member", String.valueOf(guild.getMembers().size()), false);
            builder.addField("Online Members", String.valueOf(getSpecificMemberSize(guild, OnlineStatus.ONLINE)), true);
            builder.addField("Do Not Disturb Members", String.valueOf(getSpecificMemberSize(guild, OnlineStatus.DO_NOT_DISTURB)), true);
            builder.addField("Idle Members", String.valueOf(getSpecificMemberSize(guild, OnlineStatus.IDLE)), true);
            builder.addField("Offline Members", String.valueOf(getSpecificMemberSize(guild, OnlineStatus.OFFLINE)), true);
            builder.setFooter("Requested by " + event.getMember().getEffectiveName(), event.getAuthor().getEffectiveAvatarUrl());

            channel.sendMessage(builder.build()).queue();
        }
    }

    private long getSpecificMemberSize(Guild guild, OnlineStatus onlineStatus)
    {
        long memberSize = guild.getMembers().stream().filter(member -> member.getOnlineStatus() == onlineStatus).count();
        return memberSize;
    }

//    private String getSpecificMembers(Guild guild, OnlineStatus onlineStatus)
//    {
//        String output = "";
//
//        List<Member> members = guild.getMembers();
//        for (Member member : members)
//        {
//            if(member.getOnlineStatus() == onlineStatus)
//            {
//                output += member.getEffectiveName() + "\n";
//            }
//        }
//        return output;
//    }

//    private String textChannels(Guild guild)
//    {
//        String output = "";
//
//        List<TextChannel> channels = guild.getTextChannels();
//        for (TextChannel textChannel : channels)
//        {
//            output += textChannel.getName() + "\n";
//        }
//        return output;
//    }
//
//    private String voiceChannels(Guild guild)
//    {
//        String output = "";
//
//        List<VoiceChannel> channels = guild.getVoiceChannels();
//        for (VoiceChannel voiceChannel : channels)
//        {
//            output += voiceChannel.getName() + "\n";
//        }
//        return output;
//    }
//
//    private String members(Guild guild)
//    {
//        String output = "";
//
//        List<Member> members = guild.getMembers();
//        for (Member member : members)
//        {
//            output += member.getEffectiveName() + "\n";
//        }
//        return output;
//    }
}
