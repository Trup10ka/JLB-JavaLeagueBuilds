package me.trup10ka.jlb.util;

import me.trup10ka.jlb.web.riotapi.items.ItemDescriptionJSONParser;
import me.trup10ka.jlb.web.riotapi.runes.RunesDescriptionJSONParser;

public class Descriptions
{
    private static final ItemDescriptionJSONParser itemDescriptionParser;

    private static final RunesDescriptionJSONParser runeDescriptionParser;

    static
    {
        itemDescriptionParser = new ItemDescriptionJSONParser();
        runeDescriptionParser = new RunesDescriptionJSONParser();
    }

    public static String getDescriptionOfItem(String item)
    {
        return itemDescriptionParser.getDescriptionOfParticularItem(item);
    }
    public static String getDescriptionOfRune(String rune)
    {
        return runeDescriptionParser.getDescriptionForRune(rune);
    }
}
