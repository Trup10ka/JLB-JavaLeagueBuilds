package me.trup10ka.jlb.web.parser;

import me.trup10ka.jlb.data.ItemBuild;
import me.trup10ka.jlb.data.RunePage;
import me.trup10ka.jlb.data.SummonerSpell;
import org.jsoup.nodes.Document;

import java.util.List;

public interface HtmlBuildPageParser {
    Document parse();

    void setChampionToParse(String champion);

    ItemBuild itemBuild();

    RunePage runePage();

    List<SummonerSpell> summoners();
}