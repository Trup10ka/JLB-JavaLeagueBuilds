package me.trup10ka.jlb.util;

import me.trup10ka.jlb.web.riotapi.runes.RunesDescriptionJSONParser;

public final class Descriptions
{

    private static final RunesDescriptionJSONParser runeDescriptionParser;

    static
    {
        runeDescriptionParser = new RunesDescriptionJSONParser();
    }


    public static String getDescriptionOfRune(String rune)
    {
        return runeDescriptionParser.getDescriptionForRune(rune);
    }
}
