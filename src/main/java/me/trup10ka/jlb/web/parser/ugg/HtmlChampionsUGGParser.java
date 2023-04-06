package me.trup10ka.jlb.web.parser.ugg;

import me.trup10ka.jlb.data.Champion;
import me.trup10ka.jlb.web.PageURL;
import me.trup10ka.jlb.web.parser.HtmlChampionsPageParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class HtmlChampionsUGGParser implements HtmlChampionsPageParser {

    private Document parsedHtml;
    private Connection connection;
    private ArrayList<Champion> champions;

    public HtmlChampionsUGGParser() {
        this.connection = Jsoup.connect(PageURL.U_GG.allChampionsURL);
        this.parsedHtml = parse();
    }
    @Override
    public Document parse() {
        Document parsedHtml = null;
        try {
            parsedHtml = connection.get();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return parsedHtml;
    }

    @Override
    public ArrayList<Champion> champions() {
        if (this.champions != null && this.champions.size() > 0)
            return champions;
        this.champions = new ArrayList<>(180);
        for (Element element : parsedHtml.select("a.champion-link"))
            champions.add(new Champion(element.select("div.champion-name").text()));
        return champions;
    }

}
