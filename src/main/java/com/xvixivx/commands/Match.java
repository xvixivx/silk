package com.xvixivx.commands;

import com.xvixivx.dao.GuildDao;
import com.xvixivx.dao.MatchChannelDao;
import com.xvixivx.dto.GuildDto;
import com.xvixivx.dto.MatchChannelDto;
import com.xvixivx.dto.MatchDto;
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

public class Match extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(Match.class);

    public void run(MessageReceivedEvent event)
    {
        Message message = event.getMessage();
        String[] contents = message.getContentRaw().split(" ", 7);

        Guild guild = event.getGuild();

        TextChannel channel = event.getTextChannel();

        // Check permission
        if (!guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
        {
            return;
        }

        // -s match
        if (contents[1].equalsIgnoreCase("match"))
        {
            EmbedBuilder builder = new EmbedBuilder();
            GuildDao guildDao = new GuildDao();
            MatchChannelDao matchChannelDAO = new MatchChannelDao();
            List<MatchChannelDto> matchChannels;

            // Minimum Arguments set of an example for the set is "-s match set"
            int minimumArgumentsNumberForSet = 3;

            if (contents.length < minimumArgumentsNumberForSet)
            {
                commandInfo(channel, builder);
                return;
            }

            // -s match set
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
                int maxArguments = 6;
                if (contents.length > maxArguments)
                {
                    builder.setTitle("**Error**");
                    builder.setColor(Color.RED);
                    builder.setDescription("Too many Arguments");
                    channel.sendMessage(builder.build()).queue();
                    builder.clear();
                    return;
                }
                // -s match set help <= command length is 4
                if (contents.length == 4 && contents[3].equalsIgnoreCase("help"))
                {
                    builder.setTitle("**Command Info**");
                    builder.setColor(Color.RED);
                    builder.setDescription("Filters");
                    builder.addField("Set All Attribute", "`-s match set (region :optional) (platform :optional) (game-type :optional)`\n"
                            + "**Example**: `-s match set eu mobile tournament`", false);
                    builder.addField("Set Region And Platform", "`-s match set (region) (platform)`\n"
                            + "**Example**: `-s match set as pc`", false);
                    builder.addField("Set Platform And Game Type", "`-s match set (platform) (game-type)`\n"
                            + "**Example**: `-s match set na group`", false);
                    builder.addField("No Filter", "`-s match set`", false);
                    channel.sendMessage(builder.build()).queue();
                    builder.clear();
                    return;
                }

                boolean receive = true;
                MatchDto match = new MatchDto();
                if (contents.length > 3)
                {
                    for (int i = 3; i < contents.length; i++)
                    {
                        match.setRegion(contents[i]);
                        match.setPlatform(contents[i]);
                        match.setGameType(contents[i]);
                    }
                }
                int result = matchChannelDAO.upsertAll(guild.getIdLong(), channel.getIdLong(), channel.getName(), match.getRegion(), match.getPlatform(), match.getGameType(), receive);
                if (result != 0)
                {
                    builder.setTitle("**Set Channel**");
                    builder.setColor(Color.CYAN);
                    builder.setDescription("Success!");
                    builder.addField("Region", Objects.toString(match.getRegion(), ""), false);
                    builder.addField("Platform", Objects.toString(match.getPlatform(), ""), false);
                    builder.addField("GameType", Objects.toString(match.getGameType(), ""), false);
                    channel.sendMessage(builder.build()).queue();
                }
                else
                {
                    builder.setTitle("**Error**");
                    builder.setColor(Color.RED);
                    builder.setDescription("Something happened...");
                    channel.sendMessage(builder.build()).queue();
                }
                builder.clear();
                return;
            }

            // -s match on
            if (contents[2].equalsIgnoreCase("on"))
            {
                boolean receive = true;
                toggleReceiveStatus(event, builder, matchChannelDAO, receive);
                return;
            }

            // -s match off
            if (contents[2].equalsIgnoreCase("off"))
            {
                boolean receive = false;
                toggleReceiveStatus(event, builder, matchChannelDAO, receive);
                return;
            }

            // -s match guilds
            if (contents[2].equalsIgnoreCase("guilds"))
            {
                List<GuildDto> guilds = guildDao.findAll();

                if (guilds.size() == 0)
                {
                    builder.setTitle("Error");
                    builder.setColor(Color.RED);
                    builder.setDescription("Connection Error");
                    channel.sendMessage(builder.build()).queue();
                    builder.clear();
                }

                builder.setTitle("Guilds");
                builder.setColor(Color.CYAN);
                for (GuildDto target : guilds)
                {
                    builder.appendDescription(target.getName() + " (" + target.getRegion() + ")\n");
                }
                channel.sendMessage(builder.build()).queue();
                builder.clear();
                return;
            }

            matchChannels = matchChannelDAO.find(guild.getIdLong());

            // If Database doesn't have an at least one active receive channel in the current guild, show error message.
            if (matchChannels.size() == 0)
            {
                builder.setTitle("**Error**");
                builder.setColor(Color.RED);
                builder.setDescription("Please enter the command below on the channel that will accept the match info");
                builder.addField("Command", "-s match set", false);
                channel.sendMessage(builder.build()).queue();
                builder.clear();
                return;
            }

            // Minimum Arguments set of an example for the match is "-s match as mobile group 6EECB"
            int minimumArgumentsNumber = 6;
            int maximumArgumentsNumber = 7;

            if (contents.length < minimumArgumentsNumber || contents.length > maximumArgumentsNumber)
            {
                commandInfo(channel, builder);
                return;
            }

            MatchDto match = new MatchDto();
            match.setRegion(contents[2]);
            match.setPlatform(contents[3]);
            match.setGameType(contents[4]);
            match.setRoomId(contents[5]);

            if (contents.length == maximumArgumentsNumber)
            {
                match.setNote(contents[6]);
            }

            if (!match.isReady())
            {
                commandInfo(channel, builder);
                return;
            }

            List<Guild> guilds = guild.getJDA().getGuilds();

            matchChannels = matchChannelDAO.findChannels(match.getRegion(), match.getPlatform(), match.getGameType());

            if (matchChannels.size() == 0)
            {
                matchInfo(event, channel, builder, match);
            }

            boolean matchChannelTableHasThisChannel = false;

            for (MatchChannelDto target : matchChannels)
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

                matchInfo(event, targetChannel, builder, match);

                if (guild.getIdLong() == targetGuild.getIdLong() && channel.getIdLong() == targetChannel.getIdLong())
                {
                    matchChannelTableHasThisChannel = true;
                }
            }
            if (!matchChannelTableHasThisChannel)
            {
                matchInfo(event, channel, builder, match);
            }

            logger.debug("Match Info: "
                    + "(Region: " + match.getRegion() + ") "
                    + "(Platform: " + match.getPlatform() + ") "
                    + "(Game Type: " + match.getGameType() + ") "
                    + "(Room Id: " + match.getRoomId().toUpperCase() + ") "
                    + "(Note: " + match.getNote() + ") "
                    + "(From: " + guild.getName() + ") "
                    + "(Created by: " + event.getMember().getEffectiveName() + ")");
        }
    }

    private void toggleReceiveStatus(MessageReceivedEvent event, EmbedBuilder builder, MatchChannelDao matchChannelDAO, boolean receive)
    {
        Guild guild = event.getGuild();
        TextChannel channel = event.getTextChannel();
        String description;

        if (!event.getMember().isOwner() && !event.getMember().hasPermission(Permission.ADMINISTRATOR))
        {
            builder.setTitle("**Error**");
            builder.setColor(Color.RED);
            builder.setDescription("Permission Required");
            channel.sendMessage(builder.build()).queue();
            builder.clear();
            return;
        }

        int result = matchChannelDAO.updateReceive(guild.getIdLong(), channel.getIdLong(), receive);

        if (result != 0)
        {
            builder.setTitle("**Success!**");
            builder.setColor(Color.CYAN);
            description = "Match Receive Setting is ";
            if (receive)
            {
                description += "On";
            }
            else
            {
                description += "Off";
            }
            builder.setDescription(description);
            channel.sendMessage(builder.build()).queue();
        }
        else
        {
            builder.setTitle("**Error**");
            builder.setColor(Color.RED);
            description = "This channel has not been set yet\n";
            if (receive)
            {
                description += "Please type `-s match set (region) (platform) (game-type)`";
            }
            builder.setDescription(description);
            channel.sendMessage(builder.build()).queue();
        }
        builder.clear();
    }

    private void matchInfo(MessageReceivedEvent event, TextChannel channel, EmbedBuilder builder, MatchDto match)
    {
        Guild guild = event.getGuild();

        builder.setTitle("**Match**");
        builder.setColor(Color.CYAN);
        builder.addField("Server", match.getRegion(), false);
        builder.addField("Platform", match.getPlatform(), false);
        builder.addField("Game Type", match.getGameType(), false);
        builder.addField("Room ID", match.getRoomId().toUpperCase(), false);
        builder.addField("Note", match.getNote(), false);
        builder.addField("From", guild.getName(), false);
        if (match.getGameType().equalsIgnoreCase("tournament"))
        {
            String earthGlobeAmericas = "\uD83C\uDF0E";
            builder.addField("It's a tournament !", earthGlobeAmericas + " You can play the tournament beyond the server region", false);
        }
        builder.setFooter("Created by " + event.getMember().getEffectiveName(),
                event.getAuthor().getEffectiveAvatarUrl());

        channel.sendMessage(builder.build()).queue();

        builder.clear();
    }

    private void commandInfo(TextChannel channel, EmbedBuilder builder)
    {
        builder.setTitle("**Command Information**");
        builder.setColor(Color.RED);
        builder.addField("usage", "-s match (region) (platform) (game-type) (room-id) (note: optional)", false);
        builder.addField("example", "-s match eu mobile tournament 8E3RCCCE\n" + "-s match eu m t 8E3RCCCE", false);
        builder.addField("region", "as = asia, eu = european union, na = north america, sa = south america", false);
        builder.addField("platform", "pc or mobile (p or m)", false);
        builder.addField("game-type", "group or tournament (g or t)", false);
        channel.sendMessage(builder.build()).queue();
        builder.clear();
    }
}
