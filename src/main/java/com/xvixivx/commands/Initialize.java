package com.xvixivx.commands;

import com.xvixivx.dao.GuildDAO;
import com.xvixivx.dao.MatchChannelDAO;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdateNameEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateRegionEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Initialize extends ListenerAdapter {

    final Logger logger = LoggerFactory.getLogger(Initialize.class);

    @Override
    public void onReady(ReadyEvent event) {
        List<Guild> guilds = event.getJDA().getGuilds();
        for (Guild target : guilds)
        {
            int result = new GuildDAO().upsertAll(target.getIdLong(), target.getName(), target.getRegionRaw());
            if (result == 0)
            {
                logger.error("Upsert Error at onReady(ReadyEvent event)");
            }
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        int result = new GuildDAO().upsertAll(guild.getIdLong(), guild.getName(), guild.getRegionRaw());
        if (result == 0)
        {
            logger.error("Upsert Error at onGuildJoin(GuildJoinEvent event)");
        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        Guild guild = event.getGuild();
        int result = new GuildDAO().delete(guild.getIdLong());
        if (result == 0)
        {
            logger.error("Delete Error at onGuildLeave(GuildLeaveEvent event)");
        }
    }

    @Override
    public void onGuildUpdateName(GuildUpdateNameEvent event) {
        Guild guild = event.getGuild();
        int result = new GuildDAO().upsertAll(guild.getIdLong(), guild.getName(), guild.getRegionRaw());
        if (result == 0)
        {
            logger.error("Upsert Error at onGuildUpdateName(GuildUpdateNameEvent event)");
        }
    }

    @Override
    public void onGuildUpdateRegion(GuildUpdateRegionEvent event) {
        Guild guild = event.getGuild();
        int result = new GuildDAO().upsertAll(guild.getIdLong(), guild.getName(), guild.getRegionRaw());
        if (result == 0)
        {
            logger.error("Upsert Error at onGuildUpdateName(GuildUpdateNameEvent event)");
        }
    }

    @Override
    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        Guild guild = event.getGuild();
        int result = new MatchChannelDAO().delete(guild.getIdLong(), event.getChannel().getIdLong());
        if (result == 0)
        {
            logger.error("Delete Error at onTextChannelDelete(TextChannelDeleteEvent event)");
            logger.error("DAO: MatchChannelDAO");
        }
    }

    @Override
    public void onTextChannelUpdateName(TextChannelUpdateNameEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel();
        int result = new MatchChannelDAO().updateChannelName(guild.getIdLong(), channel.getIdLong(), channel.getName());
        if (result == 0)
        {
            logger.error("Update Error at onTextChannelUpdateName(TextChannelUpdateNameEvent event)");
            logger.error("DAO: MatchChannelDAO");
        }
    }
}
