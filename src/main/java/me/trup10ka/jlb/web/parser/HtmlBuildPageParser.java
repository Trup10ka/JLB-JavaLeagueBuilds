package me.trup10ka.jlb.web.parser;

import me.trup10ka.jlb.data.ItemBuild;
import me.trup10ka.jlb.data.RunePage;
import me.trup10ka.jlb.data.SummonerSpell;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Interface responsible for parsing champions build data from web pages.
 * <br> <br>
 * Each build page has to provide you with champion's runes, item build and his summoner spells
 *
 * @author Lukas "Trup10ka" Friedl
 * @see me.trup10ka.jlb.web.parser.ugg.HtmlBuildUGGParser U.GG build parser
 * @see me.trup10ka.jlb.web.parser.lographs.HtmlBuildLoGParser League of Graphs build parser
 * @see me.trup10ka.jlb.web.parser.mobafire.HtmlBuildMobafireParser Mobafire build parser
 * @since 1.0.0.
 */
public interface HtmlBuildPageParser
{
    /**
     * @return Html document from which the build is going to be parsed
     */
    Document parse();

    /**
     * Sets connection with the page containing chosen champion data
     * @param champion champion whose build is going to be parsed
     */
    void setChampionToParse(String champion);
    /**
     * @return {@link me.trup10ka.jlb.data.ItemBuild Items} which are currently the best option to buy on chosen {@link me.trup10ka.jlb.data.Champion champion}
     */
    ItemBuild queryItemBuild();

    /**
     * @return {@link me.trup10ka.jlb.data.RunePage Runes} which are currently the best option to take on chosen {@link me.trup10ka.jlb.data.Champion champion}
     */
    RunePage queryRunePage();

    /**
     * @return {@link me.trup10ka.jlb.data.SummonerSpell Summoners} which are currently the best option to take on chosen {@link me.trup10ka.jlb.data.Champion champion}
     */
    List<SummonerSpell> summoners();
}