package me.trup10ka.jlb.web.parser.ugg;

import me.trup10ka.jlb.data.*;
import me.trup10ka.jlb.web.PageURL;
import me.trup10ka.jlb.web.WrongChampionPathException;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlBuildUGGParser implements HtmlBuildPageParser {

    private Document document;
    private Connection connection;

    public HtmlBuildUGGParser(String champion) {
        setChampionToParse(champion);
    }
    @Override
    public void setChampionToParse(String champion) {
        try {
            this.connection = Jsoup.connect(PageURL.U_GG.championURL(champion));
            this.document = parse();
        } catch (RuntimeException ignored) {}
        if (validate()) {
            throw new WrongChampionPathException(champion);
        }
    }

    private boolean validate() {
        return document.select("div.page-not-found").size() != 0;
    }
    @Override
    public Document parse() {
        try {
            return connection.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemBuild itemBuild() {
        Set<Item> setOfStartingItems =  startingItems();
        Set<Item> setOfCoreItems = coreItems();
        Map<String, Set<Item>> mapOfEndItems = endItems();
        return new ItemBuild.Builder()
                .startItems(setOfStartingItems)
                .coreItems(setOfCoreItems)
                .endItems(mapOfEndItems)
                .build();
    }

    @Override
    public RunePage runePage() {
        Rune nameOfKeyRune = nameOfKeyRune();
        Set<Rune> namesOfMainSecondaryRunes = secondaryRunes();
        Set<Rune> setOfSeconderMainRunes = addElementsFromRange(1, 3, namesOfMainSecondaryRunes);
        Set<Rune> setOfSecondaryRunes = addElementsFromRange(4, 5 , namesOfMainSecondaryRunes);
        ArrayList<Attribute> attributes = attributes();
        return new RunePage.Builder()
                .mainRune(nameOfKeyRune)
                .secondaryMainRunes(setOfSeconderMainRunes)
                .secondaryRunes(setOfSecondaryRunes)
                .attributes(attributes)
                .build();
    }

    @Override
    public List<SummonerSpell> summoners() {
        return querySummoners();
    }
    // TODO: Optimize this with exclusion of mobile version of website
    private Set<Item> startingItems() {
        return queryItemPosition("div.starting-items");
    }
    private Set<Item> coreItems() {
        return queryItemPosition("div.core-items");
    }
    private Map<String, Set<Item>> endItems() {
        Map<String, Set<Item>> mapOfEndItems = new TreeMap<>();
        for (int i = 1; i < 4; i++) {
            Set<Item> endItems = queryItemPosition("div.item-options-" + i);
            mapOfEndItems.put(i + 3 + ". option", endItems);
        }
        return mapOfEndItems;
    }
    private Rune nameOfKeyRune() {
        return new Rune(document.select("div.perk.keystone.perk-active")
              .select("img")
              .attr("alt")
              .replaceAll("The Keystone ", ""), 0);
    }
    private TreeSet<Rune> secondaryRunes() {
        TreeSet<Rune> namesOfMainSecondaryRunes = new TreeSet<>();
        int runePosition = 1;
        for (Element element : document.select("div.perk.perk-active")) {
            String nameOfRune = element
                    .select("img")
                    .attr("alt")
                    .replaceAll("The Rune ", "");
            if (isRunePresentAndNotAKeyStone(nameOfRune, namesOfMainSecondaryRunes))
                continue;
            Rune rune = new Rune(nameOfRune, runePosition);
            runePosition++;
            namesOfMainSecondaryRunes.add(rune);
        }
        return namesOfMainSecondaryRunes;
    }
    private ArrayList<Attribute> attributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        for (Element element : document.select("div.shard-active")) {
            String shard = element.select("img")
                    .attr("alt")
                    .replaceAll("The ", "")
                    .replaceAll("Scaling ", "")
                    .replaceAll("Bonus ", "")
                    .replaceAll(" Shard", "");
            if (shard.equals("CDR"))
                shard = "ability_haste";
            if (attributes.size() < 3)
                attributes.add(new Attribute(shard));
        }
        return attributes;
    }
    private List<SummonerSpell> querySummoners() {
        ArrayList<SummonerSpell> result = new ArrayList<>();
        for (Element element : document.select("div.summoner-spells").select("img")) {
            String summonerString = element.attr("alt").replaceAll("Summoner Spell ", "");
            SummonerSpell summoner = SummonerSpell.valueOf(summonerString.toUpperCase());
            result.add(summoner);
        }
        return result;
    }
    private Set<Item> queryItemPosition(String itemCategory) {
        Set<Item> coreItems = new TreeSet<>();

        int[] usedPositionValues = null;
        String usedImagePath = null;

        for (Element element : document.select(itemCategory).select("div.item-img")) {
            String startingItem = element.select("div > div").select("div > div")
                    .attr("style");
            String imagePath = imagePath(startingItem);
            int[] positionValues = positionOfImage(startingItem);

            if (usedPositionValues == null) {
                usedPositionValues = new int[2];
                setValuesForUsedVariables(usedPositionValues, positionValues);
                usedImagePath = imagePath;
                coreItems.add(new Item(imagePath, positionValues[0], positionValues[1]));
                continue;
            }

            if (isItemAlreadyRegistered(usedImagePath, imagePath , usedPositionValues, positionValues))
                break;
            coreItems.add(new Item(imagePath, positionValues[0], positionValues[1]));
        }
        return coreItems;
    }
    private TreeSet<Rune> addElementsFromRange(int first, int last, Set<Rune> runes) {
        final TreeSet<Rune> splitSet = new TreeSet<>();
        ArrayList<Rune> tmp = new ArrayList<>(runes);
        for (int i = first; i <= last; i++)
            splitSet.add(tmp.get(i - 1));
        return splitSet;
    }
    private boolean isRunePresentAndNotAKeyStone(String rune, Set<Rune> runes) {
        if (rune.contains("The Keystone"))
            return true;
        for (Rune r : runes)
            if (r.name().equals(rune))
                return true;
        return false;

    }

    private String imagePath(String image) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(image);

        String url = matcher.find() ? matcher.group(0) : "";

        return url.substring(url.lastIndexOf("/") + 1, url.length() - 1).replaceAll("webp","png");
    }
    private int[] positionOfImage(String image) {
        Pattern pattern = Pattern.compile("(?<=background-position:).*?(?=;)");
        Matcher matcher = pattern.matcher(image);

        String position = matcher.find()? matcher.group(0) : "";
        String positionParseAble = position.replaceAll("px", "");
        String[] result = positionParseAble.split(" ");

        int positionX = Integer.parseInt(result[0]);
        int positionY = Integer.parseInt(result[1]);
        return new int [] {positionX, positionY};
    }
    private boolean isItemAlreadyRegistered(String usedImagePath,
                                            String imagePath,
                                            int[] positionValues,
                                            int[] usedPositionValues) {
        return usedPositionValues[0] == positionValues[0] && usedPositionValues[1] == positionValues[1] && usedImagePath.equals(imagePath);
    }
    private void setValuesForUsedVariables(int[] usedPositionValues, int[] positionValues) {
        usedPositionValues[0] = positionValues[0];
        usedPositionValues[1] = positionValues[1];
    }
}
