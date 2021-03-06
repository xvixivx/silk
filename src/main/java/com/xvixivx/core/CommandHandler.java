package com.xvixivx.core;

import com.xvixivx.commands.*;
import com.xvixivx.dao.GuildDao;
import com.xvixivx.dao.MatchChannelDao;
import com.xvixivx.dao.SharedChannelDao;
import com.xvixivx.util.Content;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdateNameEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateRegionEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class CommandHandler extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(CommandHandler.class);

    private Jedis jedis;

    private String defaultPrefix;

    // Commands
    private Invite invite;
    private AppInfo appInfo;
    private GuildInfo guildInfo;
    private Prefix prefix;
    private Help help;
    private Agent agent;
    private Match match;
    private ShareChannels shareChannels;
    private ShareMessages shareMessages;

    CommandHandler()
    {
        // TODO: host name is "redis" for production environment. Get URL from properties.
        this.jedis = new Jedis("localhost", 6379);
        Properties properties = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.defaultPrefix = properties.getProperty("prefix");
        this.invite = new Invite();
        this.appInfo = new AppInfo();
        this.guildInfo = new GuildInfo();
        this.prefix = new Prefix();
        this.help = new Help();
        this.agent = new Agent();
        this.match = new Match();
        this.shareChannels = new ShareChannels();
        this.shareMessages = new ShareMessages();
    }

    @Override
    public void onReady(ReadyEvent event)
    {
        List<Guild> guilds = event.getJDA().getGuilds();
        for (Guild target : guilds)
        {
            int result = new GuildDao().upsertAll(target.getIdLong(), target.getName(), target.getRegionRaw());
            if (result == 0)
            {
                logger.error("Upsert Error at onReady(ReadyEvent event)");
            }
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event)
    {
        Guild guild = event.getGuild();
        int result = new GuildDao().upsertAll(guild.getIdLong(), guild.getName(), guild.getRegionRaw());
        if (result == 0)
        {
            logger.error("Upsert Error at onGuildJoin(GuildJoinEvent event)");
        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event)
    {
        Guild guild = event.getGuild();
        int result = new GuildDao().delete(guild.getIdLong());
        if (result == 0)
        {
            logger.error("Delete Error at onGuildLeave(GuildLeaveEvent event)");
        }
    }

    @Override
    public void onGuildUpdateName(GuildUpdateNameEvent event)
    {
        Guild guild = event.getGuild();
        int result = new GuildDao().upsertAll(guild.getIdLong(), guild.getName(), guild.getRegionRaw());
        if (result == 0)
        {
            logger.error("Upsert Error at onGuildUpdateName(GuildUpdateNameEvent event)");
        }
    }

    @Override
    public void onGuildUpdateRegion(GuildUpdateRegionEvent event)
    {
        Guild guild = event.getGuild();
        int result = new GuildDao().upsertAll(guild.getIdLong(), guild.getName(), guild.getRegionRaw());
        if (result == 0)
        {
            logger.error("Upsert Error at onGuildUpdateName(GuildUpdateNameEvent event)");
        }
    }

    @Override
    public void onTextChannelDelete(TextChannelDeleteEvent event)
    {
        Guild guild = event.getGuild();
        int result = new MatchChannelDao().delete(guild.getIdLong(), event.getChannel().getIdLong());
        if (result == 0)
        {
            logger.error("Delete Error at onTextChannelDelete(TextChannelDeleteEvent event)");
            logger.error("DAO: MatchChannelDAO");
        }
        result = new SharedChannelDao().delete(guild.getIdLong(), event.getChannel().getIdLong());
        if (result == 0)
        {
            logger.error("Delete Error at onTextChannelDelete(TextChannelDeleteEvent event)");
            logger.error("DAO: SharedChannelDao");
        }
    }

    @Override
    public void onTextChannelUpdateName(TextChannelUpdateNameEvent event)
    {
        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel();
        int result = new MatchChannelDao().updateChannelName(guild.getIdLong(), channel.getIdLong(), channel.getName());
        if (result == 0)
        {
            logger.error("Update Error at onTextChannelUpdateName(TextChannelUpdateNameEvent event)");
            logger.error("DAO: MatchChannelDAO");
        }
        result = new SharedChannelDao().updateChannelName(guild.getIdLong(), channel.getIdLong(), channel.getName());
        if (result == 0)
        {
            logger.error("Update Error at onTextChannelUpdateName(TextChannelUpdateNameEvent event)");
            logger.error("DAO: SharedChannelDao");
        }
    }

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

        String guildId = event.getGuild().getId();

        String prefixStr = jedis.get(guildId);

        if (prefixStr == null)
        {
            prefixStr = defaultPrefix;
        }

        if (prefixCheck(contents[0], prefixStr) && Content.hasCommand(contents))
        {
            agent.run(event, prefixStr);
            appInfo.run(event);
            guildInfo.run(event);
            prefix.run(event);
            help.run(event, prefixStr);
            invite.run(event);
            match.run(event, prefixStr);
            shareChannels.run(event, prefixStr);
        }

        shareMessages.run(event, prefixStr);
    }

    private boolean prefixCheck(String content, String prefix)
    {
        boolean result = false;

        if (content.equalsIgnoreCase(prefix))
        {
            result = true;
        }

        return result;
    }
}
