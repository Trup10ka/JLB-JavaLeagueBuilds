package me.trup10ka.jlb.web.parser;

import me.trup10ka.jlb.data.lolgame.ItemBuild;
import me.trup10ka.jlb.data.lolgame.RunePage;
import me.trup10ka.jlb.data.lolgame.SummonerSpell;
import me.trup10ka.jlb.data.lolgame.Champion;
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
     * @return {@link ItemBuild Items} which are currently the best option to buy on chosen {@link Champion champion}
     */
    ItemBuild queryItemBuild();

    /**
     * @return {@link RunePage Runes} which are currently the best option to take on chosen {@link Champion champion}
     */
    RunePage queryRunePage();

    /**
     * @return {@link SummonerSpell Summoners} which are currently the best option to take on chosen {@link Champion champion}
     */
    List<SummonerSpell> summoners();
    /**
     * Validates whether correct URL to champion was provided
     * @return true if the document contains div with class "page not found"; means wrong path to champion was entered
     */
    default boolean validate(Document document)
    {
        return document.select("div.page-not-found").size() != 0;
    }
}