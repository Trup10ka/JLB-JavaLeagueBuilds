package me.trup10ka.jlb.util;

import me.trup10ka.jlb.web.riotapi.items.ItemDescriptionJSONParser;

public class ItemDescription
{
    private static final ItemDescriptionJSONParser parser;

    static
    {
        parser = new ItemDescriptionJSONParser();
    }

    public static String getDescriptionOfItem(String item)
    {
        return parser.returnDescriptionOfParticularItem(item);
    }
}
