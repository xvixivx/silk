package com.xvixivx.commands;

import com.xvixivx.dto.AgentDto;
import com.xvixivx.dao.AgentDao;
import com.xvixivx.util.Content;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;

public class Agent extends ListenerAdapter {

    public void run(MessageReceivedEvent event)
    {
        Message message = event.getMessage();
        String[] contents = message.getContentRaw().split(" ");

        // Check Channel Type
        if (!event.isFromType(ChannelType.TEXT))
        {
            return;
        }
        Guild guild = event.getGuild();

        TextChannel channel = event.getTextChannel();

        // Check permission
        if (!guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
        {
            return;
        }

        if (contents[1].equalsIgnoreCase("agent"))
        {
            EmbedBuilder builder = new EmbedBuilder();

            if (contents.length != 3)
            {
                commandInfo(channel, builder);
                return;
            }

            AgentDao agentDao = new AgentDao();
            String agentName;

            if (Content.isRightSize(contents[2], 10) && Content.isMatchTheRegex(contents[2]))
            {
                agentName = contents[2];
            }
            else
            {
                commandInfo(channel, builder);
                return;
            }

            List<AgentDto> agents = agentDao.findAgent(agentName);

            if (agents.size() == 0)
            {
                builder.setColor(Color.RED);
                builder.addField("**Error**", agentName + " is not found", false);
            }
            else
            {
                builder.setTitle("**Agent Info**");
                builder.setColor(Color.CYAN);
                for (AgentDto agent : agents)
                {
                    builder.addField("NAME", agent.getDisplayName(), false);
                    builder.addField("ROLE", agent.getRole(), false);
                    builder.addField("WEAPON", agent.getWeapon(), false);
                    builder.addField("ALTERNATE ACTION", agent.getAlternateAction(), false);
                    builder.addField("ABILITY", agent.getAbility(), false);
                    builder.addField("ULTIMATE ABILITY", agent.getUltimateAbility(), false);
                    builder.addField("DAMAGE", String.valueOf(agent.getDamage()), false);
                    builder.addField("HEALTH", String.valueOf(agent.getHealth()), false);
                    String discordAttachmentURL = "https://cdn.discordapp.com/attachments/";
                    builder.setImage(discordAttachmentURL + agent.getImage());
                }
            }
            channel.sendMessage(builder.build()).queue();
        }
    }

    private void commandInfo(TextChannel channel, EmbedBuilder builder)
    {
        builder.setTitle("**Command Information**");
        builder.setColor(Color.RED);
        builder.addField("usage", "-s agent (agent-name)", false);
        builder.addField("example", "-s agent ghost", false);
        channel.sendMessage(builder.build()).queue();
        builder.clear();
    }
}
