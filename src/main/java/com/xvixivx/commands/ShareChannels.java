package com.xvixivx.commands;

import com.xvixivx.dao.GuildDao;
import com.xvixivx.dao.SharedChannelDao;
import com.xvixivx.dto.GuildDto;
import com.xvixivx.util.Content;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;

public class ShareChannels extends ListenerAdapter {

    enum GameType {DEFAULT, PVP, TOURNAMENT}

    private final Logger logger = LoggerFactory.getLogger(ShareChannels.class);

    public void run(MessageReceivedEvent event) {

        Message message = event.getMessage();
        String[] contents = message.getContentRaw().split(" ", 7);

        Guild guild = event.getGuild();

        TextChannel channel = event.getTextChannel();

        // Check permission
        if (!guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
        {
            return;
        }

        // -s share channel
        if (contents[1].equalsIgnoreCase("share") && contents[2].equalsIgnoreCase("channel"))
        {
            EmbedBuilder builder = new EmbedBuilder();
            GuildDao guildDao = new GuildDao();
            SharedChannelDao sharedChannelDao = new SharedChannelDao();

            // Minimum Arguments set of an example is "-s share channel tournament"
            int minimumArgumentsNumberForSet = 4;

            if (contents.length < minimumArgumentsNumberForSet)
            {
                commandInfo(channel, builder, GameType.DEFAULT);
                return;
            }

            if (!contents[3].equalsIgnoreCase("tournament")
                    && !contents[3].equalsIgnoreCase("pvp")
                    && !contents[3].equalsIgnoreCase("on")
                    && !contents[3].equalsIgnoreCase("off")
                    && !contents[3].equalsIgnoreCase("guilds"))
            {
                commandInfo(channel, builder, GameType.DEFAULT);
                return;
            }

            // -s share channel tournament
            if (contents[3].equalsIgnoreCase("tournament"))
            {
                if (!event.getMember().isOwner() && !event.getMember().hasPermission(Permission.ADMINISTRATOR))
                {
                    builder.setTitle("**Error**");
                    builder.setColor(Color.RED);
                    builder.setDescription("Permission Required\n" + "Only server owner or administrator can change the channel settings");
                    channel.sendMessage(builder.build()).queue();
                    builder.clear();
                    return;
                }
                int maxArguments = 4;
                if (contents.length > maxArguments)
                {
                    builder.setTitle("**Error**");
                    builder.setColor(Color.RED);
                    builder.setDescription("usage: -s share channel tournament");
                    builder.addField("Too many Arguments", "Tournament channel doesn't require region or platform", false);
                    channel.sendMessage(builder.build()).queue();
                    builder.clear();
                    return;
                }

                boolean receive = true;

                int result = sharedChannelDao.upsertTournamentChannels(guild.getIdLong(), channel.getIdLong(), channel.getName(), receive);
                if (result != 0)
                {
                    builder.setTitle("**Set Channel**");
                    builder.setColor(Color.CYAN);
                    builder.setDescription("Success!");
                    builder.addField("GameType", "Tournament", false);
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

            // -s share channel pvp
            if (contents[3].equalsIgnoreCase("pvp"))
            {
                if (!event.getMember().isOwner() && !event.getMember().hasPermission(Permission.ADMINISTRATOR))
                {
                    builder.setTitle("**Error**");
                    builder.setColor(Color.RED);
                    builder.setDescription("Permission Required\n" + "Only server owner or administrator can change the channel settings");
                    channel.sendMessage(builder.build()).queue();
                    builder.clear();
                    return;
                }
                int NumberOfArguments = 6;
                if (contents.length != NumberOfArguments)
                {
                    commandInfo(channel, builder, GameType.PVP);
                    return;
                }

                boolean receive = true;

                String region = contents[4].toUpperCase();
                String platform = contents[5].toLowerCase();

                if (!checkRegion(region) || !checkPlatform(platform))
                {
                    commandInfo(channel, builder, GameType.PVP);
                    return;
                }
                int result = sharedChannelDao.upsertPvPChannels(guild.getIdLong(), channel.getIdLong(), channel.getName(), region, platform, receive);
                if (result != 0)
                {
                    builder.setTitle("**Set Channel**");
                    builder.setColor(Color.CYAN);
                    builder.setDescription("Success!");
                    builder.addField("GameType", "PvP", false);
                    builder.addField("Region", region, false);
                    builder.addField("Platform", platform, false);
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

            // -s share channel on
            if (contents[3].equalsIgnoreCase("on"))
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
                boolean receive = true;

                int result = sharedChannelDao.updateReceive(guild.getIdLong(), channel.getIdLong(), receive);

                if (result != 0)
                {
                    builder.setTitle("**Success!**");
                    builder.setColor(Color.CYAN);
                    builder.setDescription("Receive Setting is On");
                    channel.sendMessage(builder.build()).queue();
                }
                else
                {
                    builder.setTitle("**Error**");
                    builder.setColor(Color.RED);
                    builder.setDescription("This channel has not been set yet\n"
                            + "Please type `-s share channel`");
                    channel.sendMessage(builder.build()).queue();
                }
                builder.clear();
                return;
            }

            // -s share channel off
            if (contents[3].equalsIgnoreCase("off"))
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
                boolean receive = false;

                int result = sharedChannelDao.updateReceive(guild.getIdLong(), channel.getIdLong(), receive);

                if (result != 0)
                {
                    builder.setTitle("**Success!**");
                    builder.setColor(Color.CYAN);
                    builder.setDescription("Receive Setting is Off");
                    channel.sendMessage(builder.build()).queue();
                }
                else
                {
                    builder.setTitle("**Error**");
                    builder.setColor(Color.RED);
                    builder.setDescription("This channel has not been set yet\n");
                    channel.sendMessage(builder.build()).queue();
                }
                builder.clear();
                return;
            }

            // -s share channel guilds
            if (contents[3].equalsIgnoreCase("guilds"))
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
        }
    }

    private void commandInfo(TextChannel channel, EmbedBuilder builder, GameType gameType)
    {
        builder.setTitle("**Command Information**");
        builder.setColor(Color.RED);
        if (gameType.equals(GameType.PVP) || gameType.equals(GameType.DEFAULT))
        {
            builder.addField("Share PvP Code Channel", "usage: -s share channel pvp (region) (platform)\n"
                    + "example: -s share channel pvp eu mobile", false);
            builder.addField("(region)", "as = asia, eu = european union, na = north america, sa = south america", false);
            builder.addField("(platform)", "pc or mobile", false);
        }
        if (gameType.equals(GameType.TOURNAMENT) || gameType.equals(GameType.DEFAULT))
        {
            builder.addField("Share Tournament Code Channel", "usage: -s share channel tournament", false);
        }
        channel.sendMessage(builder.build()).queue();
        builder.clear();
    }

    private boolean checkRegion(String region)
    {
        String regions[] = {"as", "eu", "na", "sa"};

        return Content.isMatch(region, regions);
    }

    private boolean checkPlatform(String platform)
    {
        String platforms[] = {"pc", "mobile"};

        return Content.isMatch(platform, platforms);
    }
}
