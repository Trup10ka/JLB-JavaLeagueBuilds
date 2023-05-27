package me.trup10ka.jlb.web.parser.mobafire;

import me.trup10ka.jlb.data.Rating;
import me.trup10ka.jlb.data.lolgame.CommunityBuild;
import me.trup10ka.jlb.web.Page;
import me.trup10ka.jlb.web.WrongChampionPathException;
import me.trup10ka.jlb.web.parser.HtmlAllBuildsPageParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HtmlAllBuildsMobafireParser implements HtmlAllBuildsPageParser
{

    private Connection connection;

    private Document document;
    public HtmlAllBuildsMobafireParser(String path)
    {
        setChampionToParse(path);
    }

    public void setChampionToParse(String champion)
    {
        try
        {
            this.connection = Jsoup.connect(Page.MOBAFIRE.championURL(champion));
            this.document = parse();
        } catch (RuntimeException ignored)
        {}
        if (validate(document))
            throw new WrongChampionPathException(champion);

    }

    @Override
    public Document parse()
    {
        try
        {
            return connection.get();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Override
    public ArrayList<CommunityBuild> allCommunityBuilds()
    {
        ArrayList<CommunityBuild> allPossibleCurrentPatchBuilds = new ArrayList<>();
        for (Element element : document.select("div.mf-listings").get(0).children())
        {
            if (element.is("div.mf-listings__divider"))
            {
                if (element.select("div.mf-listings__divider").text().contains("Other Patches"))
                    break;
                continue;
            }
            Elements ratings = element.select("div.mf-listings__item__rating__info");
            Elements creator = element.select("div.mf-listings__item__info").select("div.mf-listings__item__info__main");
            String nameOfTheBuild = creator.select("h3").text();
            URL buildUrl = null;
            try
            {
                buildUrl = new URL(Page.MOBAFIRE.RAW_WEB_URL + element.attr("href"));
            } catch (MalformedURLException e)
            {
                System.err.println("HtmlAllBuildsMobafireParser error: Could not create URL from given text");
            }
            short ratingUp = Short.parseShort(ratings.select("div").get(1).text());
            short ratingDown = Short.parseShort(ratings.select("div").get(2).text());
            Rating rating = new Rating(ratingUp, ratingDown);

            allPossibleCurrentPatchBuilds.add(new CommunityBuild(nameOfTheBuild, buildUrl, rating));
        }

        return allPossibleCurrentPatchBuilds;
    }
}
