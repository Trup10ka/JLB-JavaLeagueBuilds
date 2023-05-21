package me.trup10ka.jlb.web.riotapi.items;

import me.trup10ka.jlb.data.lolgame.Item;
import me.trup10ka.jlb.util.FormattedString;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public final class ItemRiotJSONParser
{

    private final URL apiURL;

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
