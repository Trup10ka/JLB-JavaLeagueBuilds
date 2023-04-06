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

public class HtmlBuildUGGParser implements HtmlBuildPageParser {

    private Document document;
    private Connection connection;

    public HtmlBuildUGGParser(String champion) {
        this.connection = Jsoup.connect(PageURL.U_GG.championURL(champion));
        this.document = parse();
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


        return new ItemBuild.Builder().build();
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
        List<SummonerSpell> summonerSpells = querySummoners();
        return summonerSpells;
    }

    private Set<Item> startingItems() {
        Set<Item> startingItems = new HashSet<>();
        for (Element element : document.select("div.starting-items").select("div.item-img")) {
            var startingItem = element.select("div").select("div")
                    .attr("background-position");
        }
        return startingItems;
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
                    .replaceAll(" Shard", "");
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
}
