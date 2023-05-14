package me.trup10ka.jlb.web.riotapi.runes;

import me.trup10ka.jlb.util.FormattedString;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RunesDescriptionJSONParser
{
    private final URL apiURL;
    private final JSONArray allRunes;

    public RunesDescriptionJSONParser()
    {
        URL url = null;
        try
        {
            String RIOT_API_STRING = "http://ddragon.leagueoflegends.com/cdn/13.9.1/data/en_US/runesReforged.json";
            url = new URL(RIOT_API_STRING);
        } catch (MalformedURLException e)
        {
            System.out.println("connection failed");
        }
        this.apiURL = url;
        allRunes = getJSON();
    }

    private JSONArray getJSON()
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
        return new JSONArray(json);
    }
    public String getDescriptionForRune(String compareName)
    {
        for (int i = 0; i < allRunes.length(); i++)
        {
            JSONArray slots = allRunes.getJSONObject(i).getJSONArray("slots");
            for (int j = 0; j < slots.length(); j++)
            {
                JSONArray runes = slots.getJSONObject(j).getJSONArray("runes");
                for (int k = 0; k < runes.length(); k++)
                {
                    String runeNameFromJSON = runes.getJSONObject(k).getString("name");
                    if (FormattedString.CSV_NAME_FORMAT.toFormat(compareName)
                            .equals(FormattedString.CSV_NAME_FORMAT.toFormat(runeNameFromJSON)))
                        return FormattedString.RUNE_DESCRIPTION.toFormat(runes.getJSONObject(k).getString("longDesc"));
                }
            }
        }
        System.err.println("Couldn't find desired rune");
        return null;
    }

}
