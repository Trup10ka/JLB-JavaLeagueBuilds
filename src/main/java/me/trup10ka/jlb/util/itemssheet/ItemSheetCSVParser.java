package me.trup10ka.jlb.util.itemssheet;

import me.trup10ka.jlb.data.lolgame.Item;
import me.trup10ka.jlb.util.FormattedString;
import me.trup10ka.jlb.util.Descriptions;

import static me.trup10ka.jlb.util.itemssheet.ItemSheetPath.CSV;

import java.io.*;

public final class ItemSheetCSVParser
{
    private static BufferedReader createBufferedReader()
    {
        BufferedReader bufferedReader = null;
        try
        {
            bufferedReader = new BufferedReader(new FileReader(CSV.path));
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
            System.out.println(ioException.getMessage());
        }
        return bufferedReader;
    }
    public static Item getItemFromName(String previousName)
    {
        BufferedReader reader = createBufferedReader();
        String name = generateFormattedString(previousName);
        if (name == null)
            return null;
        try
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] itemParameters = line.split(";");
                if (itemParameters[0].equals("name") || itemParameters[1].equals("0"))
                    continue;
                if (itemParameters[0].equals(name))
                {
                    reader.close();
                    return new Item(name, itemParameters[1], Descriptions.getDescriptionOfItem(name), Float.parseFloat(itemParameters[2]), Float.parseFloat(itemParameters[3]));
                }
            }
            System.err.println("I have not found the item, provided name: " + name);
            reader.close();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
        return null;
    }
    public static String generateNameFromGivenPositionsAndSprite(int[] positions, String imageSprite)
    {
        BufferedReader reader = createBufferedReader();

        if (positions == null || imageSprite == null)
            return null;
        try
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] itemParameters = line.split(";");
                if (itemParameters[0].equals("name") || itemParameters[1].equals("0"))
                    continue;
                if (itemParameters[1].equals(imageSprite)
                        && (Integer.parseInt(itemParameters[2]) == positions[0]
                        && Integer.parseInt(itemParameters[3]) == positions[1]))
                {
                    reader.close();
                    return itemParameters[0];
                }
            }
            System.err.println("Did not find desired item");
            reader.close();
        }
        catch (IOException ioException)
        {
            System.err.println("Error during reading a file: " + ioException.getMessage());
        }
        return null;
    }

    private static String generateFormattedString(String nameOfTheItem)
    {
        String itemNameFormatted = FormattedString.CSV_NAME_FORMAT.toFormat(nameOfTheItem);
        if (itemNameFormatted.equals("stealth_ward") || itemNameFormatted.contains("oracle_lens") || itemNameFormatted.equals("farsight_alteration"))
            return null;
        return itemNameFormatted;
    }
}
