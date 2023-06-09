package me.trup10ka.jlb.web.parser.ugg;

import me.trup10ka.jlb.controllers.ChampionsScene;
import me.trup10ka.jlb.data.lolgame.Champion;
import me.trup10ka.jlb.util.FormattedString;
import me.trup10ka.jlb.web.Page;
import me.trup10ka.jlb.web.parser.HtmlChampionsPageParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Parser for champions page for {@link Page#U_GG U.GG} page
 * <br> <br>
 * @author Lukas "Trup10ka" Friedl
 * @see HtmlChampionsPageParser
 * @since 1.0.0
 */
public final class HtmlChampionsUGGParser implements HtmlChampionsPageParser
{

    private final Document parsedHtml;
    private final Connection connection;
    private ArrayList<Champion> champions;

    public HtmlChampionsUGGParser()
    {
        this.connection = Jsoup.connect(Page.U_GG.ALL_CHAMPIONS_URL);
        this.parsedHtml = parse();
    }

    @Override
    public Document parse()
    {
        Document parsedHtml = null;
        try
        {
            parsedHtml = connection.get();
        } catch (IOException | RuntimeException e)
        {
            ChampionsScene.getInstance().executeErrorLabel(e);
        }
        return parsedHtml;
    }

    /**
     * Queries the champions page for all possible champion builds
     * @return ArrayList of all registered champions on the U.GG page
     */
    @Override
    public ArrayList<Champion> champions()
    {
        if (this.champions != null && this.champions.size() > 0)
            return champions;
        if (this.parsedHtml == null)
        {
            ChampionsScene.getInstance().executeErrorLabel(new NullPointerException("You couldn't parse the desired page - U.GG"));
            return null;
        }
        this.champions = new ArrayList<>(190);
        for (Element element : parsedHtml.select("a.champion-link"))
        {
            String path = FormattedString.U_GG_CHAMPION_HYPERLINK_FORMAT.toFormat(element.select("div.champion-name").text());
            champions.add(new Champion(element.select("div.champion-name").text(), path));
        }
        return champions;
    }
}
