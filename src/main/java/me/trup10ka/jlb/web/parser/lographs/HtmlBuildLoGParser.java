package me.trup10ka.jlb.web.parser.lographs;

import me.trup10ka.jlb.data.lolgame.*;
import me.trup10ka.jlb.util.Descriptions;
import me.trup10ka.jlb.web.Page;
import me.trup10ka.jlb.web.WrongChampionPathException;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;
import me.trup10ka.jlb.web.riotapi.items.ItemRiotJSONParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public final class HtmlBuildLoGParser implements HtmlBuildPageParser
{

    private Document document;
    private Connection connection;

    private ItemRiotJSONParser riotJSONParser;
    private final Elements COLUMNS_WITH_ITEMS;
    private final Elements ALL_BOXES_WITH_CONTENT;

    private final int numberOfKeystoneRunes;

    private final int numberOfSideRunes;

    private final int numberOfSecondaryRunes;

    public HtmlBuildLoGParser(String url)
    {
        setChampionToParse(url);
        this.COLUMNS_WITH_ITEMS = document.select("div.box.box-padding-10.overviewBox").select("div.columns");
        this.ALL_BOXES_WITH_CONTENT = document.select("div.box.box-padding-10.overviewBox");
        this.riotJSONParser = new ItemRiotJSONParser();

        this.numberOfKeystoneRunes = calculateNumberOfKeystoneRunes();
        this.numberOfSideRunes = calculateNumberSideRunes();
        this.numberOfSecondaryRunes = calculateNumberOfSecondaryRunes();
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
    public void setChampionToParse(String champion)
    {
        try
        {
            this.connection = Jsoup.connect(Page.LEAGUE_OF_GRAPHS.championURL(champion));
            this.document = parse();
        } catch (RuntimeException ignored)
        {
        }
        if (validate(document))
        {
            throw new WrongChampionPathException(champion);
        }
    }

    @Override
    public ItemBuild queryItemBuild()
    {
        Set<Item> startingItems = queryStartingItems();
        Set<Item> coreItems = queryCoreItems();
        Map<String, Set<Item>> endItems = queryEndItems();
        Item boots = queryBoots();

        return new ItemBuild.Builder()
                .startItems(startingItems)
                .coreItems(coreItems)
                .endItems(endItems)
                .boots(boots)
                .build();
    }

    @Override
    public RunePage queryRunePage()
    {
        Rune keystone = queryKeystone();
        Set<Rune> secondaryMainRunes = querySecondaryMainRunes();
        Set<Rune> secondaryRunes = querySecondRunes();
        ArrayList<Attribute> attributes = queryAttributes();

        return new RunePage.Builder()
                .mainRune(keystone)
                .secondaryMainRunes(secondaryMainRunes)
                .secondaryRunes(secondaryRunes)
                .attributes(attributes)
                .build();
    }

    @Override
    public List<SummonerSpell> summoners()
    {
        return querySummoners();
    }

    private Set<Item> queryStartingItems()
    {
        return querySpecifiedItems("starting build");
    }

    private Set<Item> queryCoreItems()
    {
        return querySpecifiedItems("core build");
    }

    private Map<String, Set<Item>> queryEndItems()
    {
        return querySpecifiedItemsFromMap("end build");
    }

    private Item queryBoots()
    {
        return querySpecifiedItem("boots");
    }

    private Rune queryKeystone()
    {
        return queryKeystoneRune();
    }

    private Set<Rune> querySecondaryMainRunes()
    {
        return queryRunes("side");
    }
    private Set<Rune> querySecondRunes()
    {
        return queryRunes("secondary");
    }

    private ArrayList<Attribute> queryAttributes()
    {
        ArrayList<Attribute> attributes = new ArrayList<>();
        for (Element element : ALL_BOXES_WITH_CONTENT)
        {
            if (isNotNeededCategory(element, "runes"))
                continue;
            for (int i = numberOfKeystoneRunes + numberOfSecondaryRunes + numberOfSideRunes; i < element.select("div.img-align-block > div").size(); i++)
            {
                Element attribute = element.select("div.img-align-block > div").get(i);
                if (isNotActive(attribute))
                    continue;
                attributes.add(new Attribute(attribute.select("img.requireTooltip").attr("alt")));
            }
        }
        return attributes;
    }
    private List<SummonerSpell> querySummoners()
    {
        ArrayList<SummonerSpell> summoners = new ArrayList<>();
        for (Element element : ALL_BOXES_WITH_CONTENT)
        {
            if (isNotNeededCategory(element, "summoner spells"))
                continue;
            for (Element summoner : element.select("img.requireTooltip"))
                summoners.add(SummonerSpell.valueOf(summoner.attr("alt").toUpperCase()));
        }
        return summoners;
    }

    @SuppressWarnings("SameParameterValue")
    private Item querySpecifiedItem(String queryTarget)
    {
        Item item = null;
        for (Element element : COLUMNS_WITH_ITEMS)
        {
            if (isNotNeededCategory(element, queryTarget))
                continue;
            Elements startingItem = element.select("img.requireTooltip");
            if (!startingItem.attr("tooltip-class").equals("itemTooltip") ||
                    !startingItem.attr("height").equals("48"))
                continue;
            item = riotJSONParser.getItem(startingItem.first().attr("alt"));
        }
        return item;
    }

    private Set<Item> querySpecifiedItems(String queryTarget)
    {
        Set<Item> items = new TreeSet<>();
        for (Element element : COLUMNS_WITH_ITEMS)
        {
            if (isNotNeededCategory(element, queryTarget))
                continue;
            for (Element startingItem : element.select("img.requireTooltip"))
            {
                if (!startingItem.attr("tooltip-class").equals("itemTooltip") ||
                        !startingItem.attr("height").equals("48"))
                    continue;
                Item item = riotJSONParser.getItem(startingItem.attr("alt"));
                if (item == null)
                    continue;
                items.add(item);
            }
        }
        return items;
    }

    private Rune queryKeystoneRune()
    {
        for (Element element : ALL_BOXES_WITH_CONTENT)
        {
            if (isNotNeededCategory(element, "runes"))
                continue;
            for (Element rune : element.select("div.img-align-block > div"))
            {
                if (isNotActive(rune))
                    continue;
                String runeName = rune.select("img.requireTooltip").attr("alt");
                return new Rune(runeName, Descriptions.getDescriptionOfRune(runeName),0);
            }
        }
        return null;
    }

    private Set<Rune> queryRunes(String descriptionOfRunes)
    {
        Set<Rune> runes = new TreeSet<>();
        for (Element element : ALL_BOXES_WITH_CONTENT)
        {
            if (isNotNeededCategory(element, "runes"))
                continue;
            int[] indicesOfBoundaries = defineBoundaries(descriptionOfRunes);

            for (int i = indicesOfBoundaries[0]; i < indicesOfBoundaries[1]; i++)
            {
                Element rune = element.select("div.img-align-block > div").get(i);
                if (isNotActive(rune))
                    continue;
                String runeName = rune.select("img.requireTooltip").attr("alt");
                runes.add(new Rune(runeName, Descriptions.getDescriptionOfRune(runeName),i));
            }
        }
        return runes;
    }


    @SuppressWarnings("SameParameterValue")
    private Map<String, Set<Item>> querySpecifiedItemsFromMap(String endBuild)
    {
        Map<String, Set<Item>> mapOfItems = new HashMap<>();
        Set<Item> setOfEndItems = querySpecifiedItems(endBuild);
        mapOfItems.put("only one", setOfEndItems);
        return mapOfItems;
    }

    private int[] defineBoundaries(String descriptionOfRunes)
    {
        int startingIndexRune = switch (descriptionOfRunes)
        {
            case "side" -> numberOfKeystoneRunes;
            case "secondary" -> numberOfKeystoneRunes + numberOfSideRunes;
            default -> throw new IllegalArgumentException();
        };
        int endingIndexRune = switch (descriptionOfRunes)
        {
            case "side" -> numberOfKeystoneRunes + numberOfSideRunes;
            case "secondary" -> numberOfKeystoneRunes + numberOfSideRunes + numberOfSecondaryRunes;
            default -> throw new IllegalArgumentException();
        };
        return new int[] {startingIndexRune, endingIndexRune};
    }
    private boolean isNotActive(Element element)
    {
        return element.attr("style").equals("opacity: 0.2;");
    }
    private boolean isNotNeededCategory(Element element, String identifier)
    {
        return !element.select("h3.box-title").text().equalsIgnoreCase(identifier);
    }
    private int calculateNumberOfKeystoneRunes()
    {
        for (Element element : ALL_BOXES_WITH_CONTENT)
        {
            if (isNotNeededCategory(element, "runes"))
                continue;
            Elements boxWithRunes = element.select("tbody");
            return boxWithRunes.get(0).select("tr").get(1).select("td").size();
        }
        return 0;
    }
    private int calculateNumberSideRunes()
    {
        return calculateNumberOfRunes(0, 5);
    }
    private int calculateNumberOfSecondaryRunes()
    {
        return calculateNumberOfRunes(1, 4);
    }
    private int calculateNumberOfRunes(int indexOfSideOrSecondary, int indexOfLastCountedRow)
    {
        int sum = 0;
        for (Element element : ALL_BOXES_WITH_CONTENT)
        {
            if (isNotNeededCategory(element, "runes"))
                continue;
            int forI = indexOfSideOrSecondary == 0 ? 2 : 1;
            for (int i = forI; i < indexOfLastCountedRow; i++)
            {
                Elements boxWithRunes = element.select("tbody");
                sum += boxWithRunes.get(indexOfSideOrSecondary).select("tr").get(i).select("td").size();
            }
        }
        return sum;
    }
}
