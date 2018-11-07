package com.xvixivx.commands;

import com.xvixivx.dto.AgentDTO;
import com.xvixivx.dao.AgentDAO;
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

        if (contents[1].equalsIgnoreCase("agent"))
        {
            EmbedBuilder builder = new EmbedBuilder();

            if (contents.length != 3)
            {
                commandInfo(channel, builder);
                return;
            }

            AgentDAO agentDAO = new AgentDAO();
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

            List<AgentDTO> agents = agentDAO.findAgent(agentName);

            if (agents.size() == 0)
            {
                builder.setColor(Color.RED);
                builder.addField("**Error**", agentName + " is not found", false);
            }
            else
            {
                builder.setTitle("**Agent Info**");
                builder.setColor(Color.CYAN);
                for (AgentDTO agent : agents)
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
