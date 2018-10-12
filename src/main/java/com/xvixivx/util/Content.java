package com.xvixivx.util;

import org.jetbrains.annotations.NotNull;

public class Content {

    public static boolean isRightPrefix(String content)
    {
        boolean result = false;
        // Check prefix
        if (content.equalsIgnoreCase("-s"))
        {
            result = true;
        }
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
            //TODO: create the logger
            System.out.println("content is too long");
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
                //TODO: create the logger
                System.out.println("content is too long");
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
            System.out.println("Denied special character");
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
                System.out.println("Denied special character");
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
