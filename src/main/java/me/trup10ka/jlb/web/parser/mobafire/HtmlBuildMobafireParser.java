package me.trup10ka.jlb.web.parser.mobafire;

import me.trup10ka.jlb.data.ItemBuild;
import me.trup10ka.jlb.data.RunePage;
import me.trup10ka.jlb.data.SummonerSpell;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;
import org.jsoup.nodes.Document;

import java.util.List;

public class HtmlBuildMobafireParser implements HtmlBuildPageParser {

    public HtmlBuildMobafireParser(String url) {
        setChampionToParse(url);
    }

    @Override
    public Document parse() {
        return null;
    }

    @Override
    public void setChampionToParse(String champion) {

    }

    @Override
    public ItemBuild itemBuild() {
        return null;
    }

    @Override
    public RunePage runePage() {
        return null;
    }

    @Override
    public List<SummonerSpell> summoners() {
        return null;
    }
}
