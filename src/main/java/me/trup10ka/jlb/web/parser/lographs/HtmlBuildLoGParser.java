package me.trup10ka.jlb.web.parser.lographs;

import me.trup10ka.jlb.data.ItemBuild;
import me.trup10ka.jlb.data.RunePage;
import me.trup10ka.jlb.data.SummonerSpell;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;
import org.jsoup.nodes.Document;

import java.util.List;

public class HtmlBuildLoGParser implements HtmlBuildPageParser
{


    public HtmlBuildLoGParser(String url)
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
