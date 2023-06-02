package me.trup10ka.jlb.web.riotapi.items;

import me.trup10ka.jlb.data.lolgame.Item;
import me.trup10ka.jlb.util.FormattedString;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class representing Riot JSON file parser for {@link Item}
 * @see me.trup10ka.jlb.data.lolgame.ItemBuild ItemBuild
 * @see <a href="https://developer.riotgames.com/docs/lol#data-dragon_data-assets">Riot API Docs</a>
 */
public final class ItemRiotJSONParser
{

    private final URL apiURL;
    /**
     * JSON file with Items
     */
    private final JSONObject allItems;


    public ItemRiotJSONParser()
    {
        URL url = null;
        try
        {
            String RIOT_API_STRING = "http://ddragon.leagueoflegends.com/cdn/13.10.1/data/en_US/item.json";
            url = new URL(RIOT_API_STRING);
        } catch (MalformedURLException e)
        {
            System.out.println("connection failed");
        }
        this.apiURL = url;
        this.allItems = getJSON();
    }

    /**
     * @return JSON file from provided URL
     */
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

    /**
     * Locates the Item in the JSON file and returns with all his attributes.
     * <br><br>
     * Compares the name of the parameter and all
     * of the available items in JSON file
     * @param nameOfItem name of the Item which you want to be created
     * @return new {@link Item} if found
     */
    public Item getItem(String nameOfItem)
    {
        for (String key : allItems.keySet())
        {
            JSONObject item = allItems.getJSONObject(key);
            String nameOfTheItem = item.getString("name");
            if (FormattedString.CSV_NAME_FORMAT.toFormat(nameOfTheItem)
                    .equals(FormattedString.CSV_NAME_FORMAT.toFormat(nameOfItem)))
            {
                String itemName = FormattedString.ITEM_DESCRIPTION.toFormat(nameOfTheItem);
                String imageName = item.getJSONObject("image").getString("sprite");
                String itemDescription = getDescriptionOfParticularItem(item);
                float x = getItemPositionsInSprite(item.getJSONObject("image"))[0];
                float y = getItemPositionsInSprite(item.getJSONObject("image"))[1];
                return new Item(itemName, imageName, itemDescription, x, y);
            }
        }
        System.err.println("Item was not found");
        return null;
    }

    public Item getItem(float[] itemPositionsInSpriteImage, String imageSprite)
    {
        for (String key : allItems.keySet())
        {
            JSONObject item = allItems.getJSONObject(key);
            JSONObject itemPositions = item.getJSONObject("image");
            if (-itemPositions.getFloat("x") == itemPositionsInSpriteImage[0]
                    && -itemPositions.getFloat("y") == itemPositionsInSpriteImage[1]
                    && itemPositions.getString("sprite").equals(imageSprite))
                return new Item(item.getString("name"),
                        imageSprite,
                        getDescriptionOfParticularItem(item),
                        itemPositionsInSpriteImage[0],
                        itemPositionsInSpriteImage[1]
                        );
        }
        System.err.println("ItemRiotJSONParser error: Did not find item with same sprite X and Y's");
        return null;
    }

    public String getDescriptionOfParticularItem(JSONObject item)
    {
        if (item != null)
            return FormattedString.ITEM_DESCRIPTION.toFormat(item.getString("description"));
        System.err.println("ItemRiotJSONParser error: Item was null");
        return null;
    }

    private float[] getItemPositionsInSprite(JSONObject item)
    {
        if (item != null)
            return new float[] {-item.getFloat("x"), -item.getFloat("y")};
        System.err.println("ItemRiotJSONParser: Item has no image properties");
        return null;
    }
}
