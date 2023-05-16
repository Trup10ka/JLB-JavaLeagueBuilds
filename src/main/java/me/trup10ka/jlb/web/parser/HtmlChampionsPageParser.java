package me.trup10ka.jlb.web.parser;

import me.trup10ka.jlb.data.lolgame.Champion;
import me.trup10ka.jlb.web.parser.ugg.HtmlChampionsUGGParser;
import me.trup10ka.jlb.web.parser.mobafire.HtmlChampionsMobafireParser;
import me.trup10ka.jlb.web.parser.lographs.HtmlChampionsLoGParser;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * Interface for parsing data from champions page.
 * <br> <br>
 * Each website should provide you with a list of champions for which they have champion builds.
 * <br> <br>
 * @author Lukas "Trup10ka" Friedl
 * @see HtmlChampionsUGGParser
 * @see HtmlChampionsMobafireParser
 * @see HtmlChampionsLoGParser
 * @since 1.0.0
 */
public interface HtmlChampionsPageParser
{
    /**
     * @return Html document from which data are queried
     */
    Document parse();

    /**
     * @return All champions that have a build registered on chosen website
     */
    ArrayList<Champion> champions();
}
