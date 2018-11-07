package com.xvixivx.util;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

public class Content {

    final static Logger logger = LoggerFactory.getLogger(Content.class);

    public static boolean isRightPrefix(String guildId, String content)
    {
        boolean result = false;
        // Check prefix
        Jedis jedis = new Jedis("localhost", 6379);
        String prefix = jedis.get(guildId);

        if (prefix == null)
        {
            prefix = "-s";
        }
        if (content.equalsIgnoreCase(prefix))
        {
            result = true;
        }
        logger.debug("Prefix is " + prefix);
        return result;
    }

    public static boolean hasCommand(String[] contents)
    {
        boolean result = false;

        if (contents.length >= 2)
        {
            result = true;
        }
        return result;
    }

    public static boolean isRightSize(String content, int size)
    {
        boolean result = false;

        if (content.length() <= size)
        {
            result = true;
        }
        else
        {
            logger.debug("Content is too long");
        }

        return result;
    }

    public static boolean isRightSize(String[] contents, int size)
    {
        boolean result = false;

        for (int i = 1; i < contents.length; i++) {

            if (contents[i].length() <= size)
            {
                result = true;
            }
            else
            {
                logger.debug("Content is too long");
            }
        }

        return result;
    }

    public static boolean isMatchTheRegex(String content)
    {
        boolean result = false;

        if (content.matches("^[a-zA-Z0-9]+$"))
        {
            result = true;
        }
        else
        {
            logger.debug("Denied special character");
        }

        return result;
    }

    public static boolean isMatchTheRegex(String[] contents)
    {
        boolean result = false;

        for (int i = 1; i < contents.length; i++)
        {


            if (contents[i].matches("^[a-zA-Z0-9]+$"))
            {
                result = true;
            }
            else
            {
                logger.debug("Denied special character");
            }
        }

        return result;
    }

    public static boolean isMatch(String content, @NotNull String[] comparisons)
    {
        boolean result = false;

        for (String comparison : comparisons)
        {
            if (comparison.equalsIgnoreCase(content))
            {
                result = true;
            }
        }
        return  result;
    }
}
