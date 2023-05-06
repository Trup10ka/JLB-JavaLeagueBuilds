package me.trup10ka.jlb.util.itemssheet;

public enum ItemSheetPath
{
    CSV("src/main/resources/ItemSheet.csv");

    public final String path;

    ItemSheetPath(String path)
    {
        this.path = path;
    }
}
