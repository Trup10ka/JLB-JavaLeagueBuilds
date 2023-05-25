package me.trup10ka.jlb.web.parser.mobafire;

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
import java.util.Collections;
import java.util.Comparator;

public class HtmlChampionsMobafireParser implements HtmlChampionsPageParser
{

    private final Document document;
    private final Connection connection;
    private ArrayList<Champion> champions;

    public HtmlChampionsMobafireParser()
    {
        this.connection = Jsoup.connect(Page.MOBAFIRE.ALL_CHAMPIONS_URL);
        this.document = parse();
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

    @Override
    public ArrayList<Champion> champions()
    {
        if (this.champions != null && this.champions.size() > 0)
            return champions;
        if (this.document == null)
        {
            ChampionsScene.getInstance().executeErrorLabel(new NullPointerException("You couldn't parse the desired page - U.GG"));
            return null;
        }
        ArrayList<Champion> champions = new ArrayList<>(190);
        for (Element element : document.select("a.champ-list__item.visible")
                .select("div.champ-list__item__name"))
        {
            String name = element.select("b").text();
            champions.add(new Champion(name, FormattedString.MOBAFIRE_CHAMPION_HYPERLINK_FORMAT.toFormat(name)));
        }
        champions.sort(Comparator.comparing(Champion::getName));
        return champions;
    }
}
