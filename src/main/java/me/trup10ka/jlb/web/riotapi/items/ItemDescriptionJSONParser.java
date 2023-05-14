package me.trup10ka.jlb.web.riotapi.items;

import me.trup10ka.jlb.util.FormattedString;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public final class ItemDescriptionJSONParser
{

    private final URL apiURL;

    private final JSONObject allItems;


    public ItemDescriptionJSONParser()
    {
        URL url = null;
        try
        {
            String RIOT_API_STRING = "http://ddragon.leagueoflegends.com/cdn/13.9.1/data/en_US/item.json";
            url = new URL(RIOT_API_STRING);
        } catch (MalformedURLException e)
        {
            System.out.println("connection failed");
        }
        this.apiURL = url;
        allItems = getJSON();
    }

    private JSONObject getJSON()
    {
        String json = null;
        try
        {
            json = IOUtils.toString(apiURL, StandardCharsets.UTF_8);
        } catch (IOException e)
        {
            System.err.println("Error during getting JSON document");
        }
        assert json != null;
        return new JSONObject(json).getJSONObject("data");
    }

    public String getDescriptionOfParticularItem(String nameOfItem)
    {
        for (String key : allItems.keySet())
        {
            String nameOfTheItem = allItems.getJSONObject(key).getString("name");
            if (FormattedString.CSV_NAME_FORMAT.toFormat(nameOfTheItem)
                    .equals(FormattedString.CSV_NAME_FORMAT.toFormat(nameOfItem)))
                return FormattedString.ITEM_DESCRIPTION.toFormat(allItems.getJSONObject(key).getString("description"));
        }
        return "Item was not found";
    }
}
