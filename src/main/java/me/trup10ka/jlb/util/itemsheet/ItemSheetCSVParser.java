package me.trup10ka.jlb.util.itemsheet;

import me.trup10ka.jlb.data.Item;

import java.io.*;

public class ItemSheetCSVParser implements ItemSheetParser
{
    private final BufferedReader reader;
    public ItemSheetCSVParser()
    {
        BufferedReader bufferedReader = null;
        try
        {
            bufferedReader = new BufferedReader(new FileReader(ItemSheetPath.CSV.path));
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
            System.out.println(ioException.getMessage());
        }
        this.reader = bufferedReader;
    }
    @Override
    public Item getItemFromName(String name)
    {
        try
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] itemParameters = line.split(";");
                if (itemParameters[0].equals("name") || itemParameters[1].equals("0"))
                    continue;
                if (itemParameters[0].equals(name))
                    return new Item(name, itemParameters[1], Float.parseFloat(itemParameters[2]), Float.parseFloat(itemParameters[3]));
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

}
