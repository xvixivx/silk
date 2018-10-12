package com.xvixivx.commands;

import com.xvixivx.dao.GuildDAO;
import com.xvixivx.dto.GuildDTO;
import com.xvixivx.dto.MatchDTO;
import com.xvixivx.util.Content;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Match extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;
        // We don't want to respond to other bot accounts, including ourself
        Message message = event.getMessage();
        String[] contents = message.getContentRaw().split(" ", 7);
        // getContentRaw() is an atomic getter
        // getContentDisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)

        // Check Channel Type
        if (!event.isFromType(ChannelType.TEXT))
        {
            return;
        }
        // Check prefix
        if (!Content.isRightPrefix(contents[0]))
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

        // -s match
        if (contents[1].equalsIgnoreCase("match"))
        {
            EmbedBuilder builder = new EmbedBuilder();
            GuildDAO guildDAO = new GuildDAO();
            GuildDTO guildData = guildDAO.find(guild.getId());
            String textChannelId = guildData.getMatchChannelId();
            Channel textChannel = null;

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
                if (!event.getMember().isOwner())
                {
                    builder.setTitle("**Error**");
                    builder.setColor(Color.RED);
                    builder.setDescription("Permission Required");
                }
                boolean receiveMatch = true;
                int result = guildDAO.upsertAll(guild.getIdLong(), guild.getName(), guild.getRegionRaw(), channel.getIdLong(), receiveMatch);
                if (result != 0)
                {
                    builder.setTitle("**Set Channel**");
                    builder.setColor(Color.CYAN);
                    builder.setDescription("Success!");
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
                if (!event.getMember().isOwner())
                {
                    builder.setTitle("**Error**");
                    builder.setColor(Color.RED);
                    builder.setDescription("Permission Required");
                }
                boolean receiveMatch = true;
                int result = 0;

                guildData = guildDAO.find(guild.getId());

                if (Objects.nonNull(guildData))
                {
                    result = guildDAO.updateReceiveMatch(guild.getIdLong(), receiveMatch);
                }

                if (result != 0)
                {
                    builder.setTitle("**Success!**");
                    builder.setColor(Color.CYAN);
                    builder.setDescription("Match Receive Setting is On");
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

            // -s match off
            if (contents[2].equalsIgnoreCase("off"))
            {
                if (!event.getMember().isOwner())
                {
                    builder.setTitle("**Error**");
                    builder.setColor(Color.RED);
                    builder.setDescription("Permission Required");
                }
                boolean receiveMatch = false;
                int result = 0;

                guildData = guildDAO.find(guild.getId());

                if (Objects.nonNull(guildData))
                {
                    result = guildDAO.updateReceiveMatch(guild.getIdLong(), receiveMatch);
                }

                if (result != 0)
                {
                    builder.setTitle("**Success!**");
                    builder.setColor(Color.CYAN);
                    builder.setDescription("Match Receive Setting is Off");
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

            if (!Objects.isNull(textChannelId)) {
                textChannel = guild.getTextChannelById(textChannelId);
            }
            if (Objects.isNull(textChannelId) || Objects.isNull(textChannel))
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

            MatchDTO match = new MatchDTO();
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

            for (Guild target : guilds) {
//                System.out.println(target.getName());
                guildData = guildDAO.find(target.getId());
                textChannelId = guildData.getMatchChannelId();
                if (Objects.isNull(guildData) || Objects.isNull(textChannelId)) {
                    continue;
                }

                TextChannel targetChannel = target.getTextChannelById(textChannelId);

                if (Objects.isNull(targetChannel)) {
                    continue;
                }
                if (!guildData.isReceiveMatch() && !guild.getId().equals(target.getId()))
                {
                    continue;
                }
                builder.setTitle("**Match**");
                builder.setColor(Color.CYAN);
                builder.addField("Server", match.getRegion(), false);
                builder.addField("Platform", match.getPlatform(), false);
                builder.addField("Game Type", match.getGameType(), false);
                builder.addField("Room ID", match.getRoomId(), false);
                builder.addField("Note", match.getNote(), false);
                builder.setFooter("Created by " + event.getMember().getEffectiveName(), event.getAuthor().getEffectiveAvatarUrl());

                targetChannel.sendMessage(builder.build()).queue();

                if (guild.getId().equals(target.getId()) && !channel.getId().equals(targetChannel.getId()))
                {
                    channel.sendMessage(builder.build()).queue();
                }

                builder.clear();
            }
        }
    }

    private void commandInfo(TextChannel channel, EmbedBuilder builder)
    {
        builder.setTitle("**Command Information**");
        builder.setColor(Color.RED);
        builder.addField("usage", "-s match (region) (platform) (game-type) (room-id)", false);
        builder.addField("example", "-s match eu mobile tournament 8E3RC", false);
        builder.addField("region", "as = asia, eu = european union, na = north america, sa = south america", false);
        builder.addField("platform", "pc or mobile", false);
        builder.addField("game-type", "group or tournament", false);
        channel.sendMessage(builder.build()).queue();
        builder.clear();
        return;
    }
}
