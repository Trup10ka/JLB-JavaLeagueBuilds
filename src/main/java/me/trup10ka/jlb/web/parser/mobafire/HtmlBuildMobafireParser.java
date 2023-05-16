package me.trup10ka.jlb.web.parser.mobafire;

import me.trup10ka.jlb.data.lolgame.ItemBuild;
import me.trup10ka.jlb.data.lolgame.RunePage;
import me.trup10ka.jlb.data.lolgame.SummonerSpell;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;
import org.jsoup.nodes.Document;

import java.util.List;

public class HtmlBuildMobafireParser implements HtmlBuildPageParser
{

    public HtmlBuildMobafireParser(String url)
    {
        setChampionToParse(url);
    }

    @Override
    public Document parse()
    {
        return null;
    }

    @Override
    public void setChampionToParse(String champion)
    {

    }

    @Override
    public ItemBuild queryItemBuild()
    {
        return null;
    }

    @Override
    public RunePage queryRunePage()
    {
        return null;
    }

    @Override
    public List<SummonerSpell> summoners()
    {
        return null;
    }
}
