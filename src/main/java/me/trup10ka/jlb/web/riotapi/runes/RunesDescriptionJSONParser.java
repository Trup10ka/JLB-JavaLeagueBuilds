package me.trup10ka.jlb.web.riotapi.runes;

import me.trup10ka.jlb.util.FormattedString;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Class for parsing JSON Riot file with {@link me.trup10ka.jlb.data.lolgame.Rune Runes}
 * @see me.trup10ka.jlb.data.lolgame.RunePage RunePage
 * @see <a href="https://developer.riotgames.com/docs/lol">RIOT API Docs</a>
 */
public class RunesDescriptionJSONParser
{
    private final URL apiURL;
    /**
     * JSON file with Items
     */
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

    /**
     * @return Requested JSON file from API
     */
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

    /**
     *
     * @param compareName rune which you want the description of
     * @return a {@link me.trup10ka.jlb.data.lolgame.Rune Rune} description
     */
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
        System.err.println("RunesDescriptionJSONParser miss input: Couldn't find desired rune. Looked for: " + compareName);
        return null;
    }
}
