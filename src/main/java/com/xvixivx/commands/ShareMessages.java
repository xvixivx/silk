package com.xvixivx.commands;

import com.xvixivx.dao.SharedChannelDao;
import com.xvixivx.dto.SharedChannelDto;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class ShareMessages extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(Match.class);

    public void run(MessageReceivedEvent event)
    {
        Message message = event.getMessage();
        String contents = message.getContentRaw();

        if (contents.startsWith("-s share channel") || contents.startsWith("-s match"))
        {
            return;
        }

        Guild guild = event.getGuild();
        TextChannel channel = event.getTextChannel();

        EmbedBuilder builder = new EmbedBuilder();

        SharedChannelDao sharedChannelDao = new SharedChannelDao();
        SharedChannelDto sharedChannel;

        sharedChannel = sharedChannelDao.find(guild.getIdLong(), channel.getIdLong());

        if (sharedChannel.getChannelId() == 0)
        {
            return;
        }

        if (sharedChannel.getGameType().equals("tournament"))
        {
            List<SharedChannelDto> sharedChannels;
            sharedChannels = sharedChannelDao.findTournamentChannels();

            if (sharedChannels.size() == 0)
            {
                logger.debug("No shared channels");
                return;
            }

            send(sharedChannels, event, builder, contents);
        }

        if (sharedChannel.getGameType().equals("pvp"))
        {

            List<SharedChannelDto> sharedChannels;
            sharedChannels = sharedChannelDao.findPvPChannels(sharedChannel.getRegion(), sharedChannel.getPlatform());

            if (sharedChannels.size() == 0)
            {
                logger.debug("No shared channels");
                return;
            }

            send(sharedChannels, event, builder, contents);
        }
    }

    private boolean send(List<SharedChannelDto> sharedChannels, MessageReceivedEvent event, EmbedBuilder builder, String contents)
    {
        Guild guild = event.getGuild();
        TextChannel channel = event.getTextChannel();
        List<Guild> guilds = guild.getJDA().getGuilds();
        User author = event.getAuthor();

        for (SharedChannelDto target : sharedChannels)
        {
            Guild targetGuild = event.getJDA().getGuildById(target.getGuild().getId());
            String guildName = target.getGuild().getName();

            if (!guilds.contains(targetGuild))
            {
                logger.debug("Guild is not exists");
                logger.debug("Guild: " + guildName);
                continue;
            }
            TextChannel targetChannel = targetGuild.getTextChannelById(target.getChannelId());
            if (Objects.isNull(targetChannel))
            {
                logger.debug("Channel is not exists");
                logger.debug("Guild: " + guildName);
                logger.debug("Channel: " + target.getChannelName());
                continue;
            }
            // Check Permission
            if (!targetGuild.getSelfMember().hasPermission(targetChannel, Permission.MESSAGE_WRITE))
            {
                logger.debug("Permission.MESSAGE_WRITE Required");
                logger.debug("Guild: " + guildName);
                logger.debug("Channel: " + target.getChannelName());
                continue;
            }
            // Check Permission
            if (!targetGuild.getSelfMember().hasPermission(targetChannel, Permission.MESSAGE_EMBED_LINKS))
            {
                logger.debug("Permission.MESSAGE_EMBED_LINKS Required");
                logger.debug("Guild: " + guildName);
                logger.debug("Channel: " + target.getChannelName());
                continue;
            }

            if (guild.getIdLong() == targetGuild.getIdLong() && channel.getIdLong() == targetChannel.getIdLong())
            {
                continue;
            }

            builder.setColor(new Color(43, 47, 53));
            builder.setAuthor(author.getName(), null, author.getEffectiveAvatarUrl());
            builder.setDescription(contents);
            builder.setFooter("via " + guild.getName(), null);

            targetChannel.sendMessage(builder.build()).queue();

            builder.clear();
        }
        return true;
    }
}
