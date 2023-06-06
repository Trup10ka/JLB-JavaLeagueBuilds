package me.trup10ka.jlb.web.parser.mobafire;

import me.trup10ka.jlb.data.lolgame.*;
import me.trup10ka.jlb.util.Descriptions;
import me.trup10ka.jlb.util.FormattedString;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;
import me.trup10ka.jlb.web.riotapi.items.ItemRiotJSONParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class HtmlBuildMobafireParser implements HtmlBuildPageParser
{

    private Connection connection;

    private Document document;

    private ItemRiotJSONParser riotJSONParser;

    public HtmlBuildMobafireParser(String url)
    {
        setChampionToParse(url);
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
    public void setChampionToParse(String urlOfBuild)
    {
        this.connection = Jsoup.connect(urlOfBuild);
        this.document = parse();
        this.riotJSONParser = new ItemRiotJSONParser();
    }

    @Override
    public ItemBuild queryItemBuild()
    {
        Set<Item> items = queryRecommendedItems();
        return new ItemBuild.Builder().startItems(null)
                .coreItems(items)
                .endItems(null)
                .build();
    }

    @Override
    public RunePage queryRunePage()
    {
        Rune keystone = queryKeyStone();
        Set<Rune> sideRunes = querySecondaryRunes(2, 5);
        Set<Rune> secondaryRunes = querySecondaryRunes(6, 8);
        ArrayList<Attribute> attributes = queryAttributes();
        return new RunePage.Builder()
                .mainRune(keystone)
                .secondaryMainRunes(sideRunes)
                .secondaryRunes(secondaryRunes)
                .attributes(attributes)
                .build();
    }

    private ArrayList<Attribute> queryAttributes()
    {
        Element element = document.select("div.new-runes__bonuses").get(0)
                .getElementsByTag("p").get(0);
        String attributes = element.text();
        return createAttributes(attributes);
    }

    @Override
    public List<SummonerSpell> summoners()
    {
        List<SummonerSpell> summonerSpells = new ArrayList<>();
        for (Element element : document.select("div.view-guide__spells"))
            for (Element itemElement : element.select("h4"))
            {
                if (summonerSpells.size() == 2)
                    return summonerSpells;
                summonerSpells.add(getSummonerSpell(itemElement));
            }
        return summonerSpells;
    }

    private Set<Item> queryRecommendedItems()
    {
        Set<Item> items = new TreeSet<>();
        for (Element element : document.select("div.view-guide__build__core"))
            for (Element itemElement : element.select("span.ajax-tooltip"))
                items.add(riotJSONParser.getItem(itemElement.text()));
        return items;
    }

    private SummonerSpell getSummonerSpell(Element itemElement)
    {
        return SummonerSpell.valueOf(
                FormattedString.MOBAFIRE_SUMMONERS_SPECIAL_CASE.toFormat(itemElement.text()).toUpperCase());
    }

    private Rune queryKeyStone()
    {
        Element element = document.select("div.view-guide__build__runes")
                .select("div.new-runes__item").get(1);
        String nameOfTheKeystone = element.text();
        String descriptionOfKeystone = Descriptions.getDescriptionOfRune(nameOfTheKeystone);
        return new Rune(nameOfTheKeystone, descriptionOfKeystone, 0);
    }

    private Set<Rune> querySecondaryRunes(int origin, int bound)
    {
        Set<Rune> runes = new TreeSet<>();
        Elements elements = document.select("div.view-guide__build__runes")
                .select("div.new-runes__item");
        for (int i = origin; i < bound; i++)
        {
            if (isRuneClass(i))
                continue;
            Element runeElement = elements.get(i);
            String runeName = runeElement.text();
            String runeDescription = Descriptions.getDescriptionOfRune(runeName);
            runes.add(new Rune(runeName, runeDescription, i));
        }
        return runes;
    }

    private ArrayList<Attribute> createAttributes(String attributesString)
    {
        String[] separateAttributesString = attributesString.split("[+]");
        ArrayList<Attribute> attributes = new ArrayList<>();
        for (String nameOfAttribute : separateAttributesString)
        {
            if (nameOfAttribute.isBlank())
                continue;
            attributes.add(new Attribute(nameOfAttribute));
        }

        return attributes;
    }

    private boolean isRuneClass(int runeIndex)
    {
        return runeIndex == 0 || runeIndex == 5;
    }
}
