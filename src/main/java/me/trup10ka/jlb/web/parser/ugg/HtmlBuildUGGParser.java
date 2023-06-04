package me.trup10ka.jlb.web.parser.ugg;

import javafx.application.Platform;
import me.trup10ka.jlb.app.JavaLeagueBuilds;
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

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *     Parser for the {@link Page#U_GG U.GG} page
 * </p>
 * @author Lukas "Trup10ka" Friedl
 * @see HtmlBuildPageParser
 * @since 1.0.0
 */
public class HtmlBuildUGGParser implements HtmlBuildPageParser
{

    private Document document;

    private Connection connection;

    private ItemRiotJSONParser riotJSONParser;

    public HtmlBuildUGGParser(String champion)
    {
        setChampionToParse(champion);
    }

    @Override
    public void setChampionToParse(String champion)
    {
        try
        {
            this.connection = Jsoup.connect(Page.U_GG.championURL(champion));
            this.document = parse();
            this.riotJSONParser = new ItemRiotJSONParser();
        }
        catch (RuntimeException ignored)
        {}
        if (validate(document))
        {
            throw new WrongChampionPathException(champion);
        }
    }


    @Override
    public Document parse()
    {
        try
        {
            return connection.get();
        }
        catch (IOException e)
        {
            Platform.runLater(() -> JavaLeagueBuilds.getInstance().switchToChampions());
        }
        return null;
    }

    @Override
    public ItemBuild queryItemBuild()
    {
        Set<Item> setOfStartingItems = queryStartingItems();
        Set<Item> setOfCoreItems = queryCoreItems();
        Map<String, Set<Item>> mapOfEndItems = queryEndItems();
        return new ItemBuild.Builder()
                .startItems(setOfStartingItems)
                .coreItems(setOfCoreItems)
                .endItems(mapOfEndItems)
                .build();
    }

    @Override
    public RunePage queryRunePage()
    {
        Rune nameOfKeyRune = nameOfKeyRune();
        Set<Rune> namesOfMainSecondaryRunes = secondaryRunes();
        Set<Rune> setOfSeconderMainRunes = addElementsFromRange(1, 3, namesOfMainSecondaryRunes);
        Set<Rune> setOfSecondaryRunes = addElementsFromRange(4, 5, namesOfMainSecondaryRunes);
        ArrayList<Attribute> attributes = attributes();
        return new RunePage.Builder()
                .mainRune(nameOfKeyRune)
                .secondaryMainRunes(setOfSeconderMainRunes)
                .secondaryRunes(setOfSecondaryRunes)
                .attributes(attributes)
                .build();
    }

    @Override
    public List<SummonerSpell> summoners()
    {
        return querySummoners();
    }

    // TODO: Optimize this with exclusion of mobile version of website
    private Set<Item> queryStartingItems()
    {
        return queryItemPosition("div.starting-items");
    }

    private Set<Item> queryCoreItems()
    {
        return queryItemPosition("div.core-items");
    }

    private Map<String, Set<Item>> queryEndItems()
    {
        Map<String, Set<Item>> mapOfEndItems = new TreeMap<>();
        for (int i = 1; i < 4; i++)
        {
            Set<Item> endItems = queryItemPosition("div.item-options-" + i);
            mapOfEndItems.put(i + 3 + ". option", endItems);
        }
        return mapOfEndItems;
    }

    /**
     * Extract the Keystone rune from build page
     * @return {@link RunePage#keyStoneRune Keystone rune}
     */
    private Rune nameOfKeyRune()
    {
        String nameOfTheRune = document.select("div.perk.keystone.perk-active")
                .select("img")
                .attr("alt")
                .replaceAll("The Keystone ", "");
        return new Rune(nameOfTheRune, Descriptions.getDescriptionOfRune(nameOfTheRune),0);
    }

    /**
     * Extract secondary runes from build page
     * @return {@link RunePage#secondaryRunes Secondary runes}
     */
    private TreeSet<Rune> secondaryRunes()
    {
        TreeSet<Rune> namesOfMainSecondaryRunes = new TreeSet<>();
        int runePosition = 1;
        for (Element element : document.select("div.perk.perk-active"))
        {
            String nameOfRune = element
                    .select("img")
                    .attr("alt")
                    .replaceAll("The Rune ", "");
            if (isRunePresentAndNotAKeyStone(nameOfRune, namesOfMainSecondaryRunes))
                continue;
            Rune rune = new Rune(nameOfRune, Descriptions.getDescriptionOfRune(nameOfRune),runePosition);
            runePosition++;
            namesOfMainSecondaryRunes.add(rune);
        }
        return namesOfMainSecondaryRunes;
    }

    /**
     * Extracts attributes shards from the build page
     * @return {@link RunePage#attributes Attributes}
     */
    private ArrayList<Attribute> attributes()
    {
        ArrayList<Attribute> attributes = new ArrayList<>();
        for (Element element : document.select("div.shard-active"))
        {
            String shard = element.select("img").attr("alt");
            if (attributes.size() < 3)
                attributes.add(new Attribute(shard));
        }
        return attributes;
    }

    /**
     * Extracts summoner spells from the build page
     * @return {@link SummonerSpell Summoner spells}
     */
    private List<SummonerSpell> querySummoners()
    {
        ArrayList<SummonerSpell> result = new ArrayList<>();
        for (Element element : document.select("div.summoner-spells").select("img"))
        {
            String summonerString = element.attr("alt").replaceAll("Summoner Spell ", "");
            SummonerSpell summoner = SummonerSpell.valueOf(summonerString.toUpperCase());
            result.add(summoner);
        }
        return result;
    }

    /**
     * Because U.GG uses map of images with {@link Item items}, each item image has background image of the map, and position x and y
     * of the item in the map
     * @param itemCategory category of items (starting, core or other items)
     * @return set of items where items instead of image path have the x and y positions and reference to one of the map of items
     */
    private Set<Item> queryItemPosition(String itemCategory)
    {
        Set<Item> coreItems = new TreeSet<>();
        float[] usedPositionValues = null;
        String usedImagePath = null;

        for (Element element : document.select(itemCategory).select("div.item-img"))
        {
            String startingItem = element.select("div > div").select("div > div")
                    .attr("style");
            String imagePath = imagePath(startingItem);
            float[] positionValues = positionOfImage(startingItem);
            if (usedPositionValues == null)
            {
                usedPositionValues = new float[2];
                setValuesForUsedVariables(usedPositionValues, positionValues);
                usedImagePath = imagePath;
                coreItems.add(riotJSONParser.getItem(positionValues, imagePath));
                continue;
            }

            if (isItemAlreadyRegistered(usedImagePath, imagePath, usedPositionValues, positionValues))
                break;
            coreItems.add(riotJSONParser.getItem(positionValues, imagePath));
        }
        return coreItems;
    }

    /**
     * Returns a new Set containing all the elements from the previous collection from defined range
     * @param first position of the first element that will be added to new Set
     * @param last position of the last element that will be added to new Set
     * @param previousSet previous collection from which elements will be extracted
     * @return new Set containing all elements from the previous collection within defined range
     */
    private TreeSet<Rune> addElementsFromRange(int first, int last, Set<Rune> previousSet)
    {
        final TreeSet<Rune> splitSet = new TreeSet<>();
        ArrayList<Rune> tmp = new ArrayList<>(previousSet);
        for (int i = first; i <= last; i++)
            splitSet.add(tmp.get(i - 1));
        return splitSet;
    }

    /**
     * Check whether rune is already present and not a {@link RunePage#keyStoneRune Keystone} rune
     * @param rune rune that is being checked
     * @param runes list of already saved runes
     * @return true when rune is already present; false when it is not
     */
    private boolean isRunePresentAndNotAKeyStone(String rune, Set<Rune> runes)
    {
        if (rune.contains("The Keystone"))
            return true;
        for (Rune r : runes)
            if (r.name().equals(rune))
                return true;
        return false;

    }

    /**
     * Extracts the name of the file from URL
     * @param image map image URL
     * @return just the name of the map of images
     */
    private String imagePath(String image)
    {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(image);
        String url = matcher.find() ? matcher.group(0) : "";

        return url.substring(url.lastIndexOf("/") + 1, url.length() - 1).replaceAll("webp", "png");
    }

    /**
     * Extracts background position of the image in the map of images
     * @param image item image URL
     * @return array of background positions x and y
     */
    private float[] positionOfImage(String image)
    {
        Pattern pattern = Pattern.compile("(?<=background-position:).*?(?=;)");
        Matcher matcher = pattern.matcher(image);

        String position = matcher.find() ? matcher.group(0) : "";
        String positionParseAble = position.replaceAll("px", "");
        String[] result = positionParseAble.split(" ");

        float positionX = Float.parseFloat(result[0]);
        float positionY = Float.parseFloat(result[1]);
        return new float[]{positionX, positionY};
    }
    /**
     * Checks whether the specified item is already saved
     * @param usedImagePath reference to the map file
     * @param imagePath reference to the new map file
     * @param positionValues position values
     * @param usedPositionValues new position values
     * @return true if all conditions are met; item already exists in saved items
     * @see #queryItemPosition(String) How items are queried
     */
    private boolean isItemAlreadyRegistered(String usedImagePath,
                                            String imagePath,
                                            float[] positionValues,
                                            float[] usedPositionValues)
    {
        return usedPositionValues[0] == positionValues[0] && usedPositionValues[1] == positionValues[1] && usedImagePath.equals(imagePath);
    }

    /**
     * Refreshes the old used values with the new position values
     * @param usedPositionValues old used position values
     * @param positionValues new position values
     */
    private void setValuesForUsedVariables(float[] usedPositionValues, float[] positionValues)
    {
        usedPositionValues[0] = positionValues[0];
        usedPositionValues[1] = positionValues[1];
    }
}
