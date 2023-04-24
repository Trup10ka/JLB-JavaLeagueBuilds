package me.trup10ka.jlb.web;

/**
 * When you enter a wrong champion build link, connection is not canceled, but the page is blank. <br> <br>
 * This exception occurs when you enter a wrong champion build link.
 * @author Lukas "Trup10ka" Friedl
 * @see me.trup10ka.jlb.web.parser.ugg.HtmlBuildUGGParser U.GG build parser
 * @see me.trup10ka.jlb.web.parser.lographs.HtmlBuildLoGParser League of Graphs build parser
 * @see me.trup10ka.jlb.web.parser.mobafire.HtmlBuildMobafireParser Mobafire build parser
 * @since 1.0.0
 */
public class WrongChampionPathException extends RuntimeException
{
    public WrongChampionPathException(String path)
    {
        System.err.println("You have entered: " + path);
        this.printStackTrace();

    }
}
