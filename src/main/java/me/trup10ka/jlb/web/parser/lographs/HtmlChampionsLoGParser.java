package me.trup10ka.jlb.web.parser.lographs;

import me.trup10ka.jlb.controllers.ChampionsScene;
import me.trup10ka.jlb.data.Champion;
import me.trup10ka.jlb.util.FormattedString;
import me.trup10ka.jlb.web.Page;
import me.trup10ka.jlb.web.parser.HtmlChampionsPageParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class HtmlChampionsLoGParser implements HtmlChampionsPageParser
{
    private final Document document;
    private final Connection connection;
    private ArrayList<Champion> champions;

    public HtmlChampionsLoGParser()
    {
        this.connection = Jsoup.connect(Page.LEAGUE_OF_GRAPHS.ALL_CHAMPIONS_URL);
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
            ChampionsScene.getInstance().executeErrorLabel(new NullPointerException("You couldn't parse the desired page - League of Graphs"));
            return null;
        }
        champions = new ArrayList<>(190);
        for (Element element : document.select("div#championListBox").select("div.championName"))
        {
            String formattedName = FormattedString.HYPERLINK_FORMAT.toFormat(element.text());
            formattedName = solveSpecialCaseChampionNames(formattedName);
            champions.add(
                    new Champion(element.text(), formattedName)
            );
        }
        return champions;
    }
    private String solveSpecialCaseChampionNames(String previousName)
    {
        String name = previousName;
        if (previousName.contains("&"))
            name = previousName.split("&")[0];
        else if (previousName.contains("glasc"))
            name = previousName.split("glasc")[0];
        else if (previousName.equals("wukong"))
            name = "monkeyking";
        return name;
    }
}
