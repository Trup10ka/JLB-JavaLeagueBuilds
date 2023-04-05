package me.trup10ka.jwj.web.parser;

import me.trup10ka.jwj.data.Champion;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class UGGChampionsPageParser implements HtmlChampionsPageParser {

    private Document parsedHtml;
    private Connection connection;
    private ArrayList<Champion> champions;
    private final String UGG_URL = "https://u.gg";

    public UGGChampionsPageParser() {
        this.connection = Jsoup.connect(UGG_URL + "/lol/champions/");
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
