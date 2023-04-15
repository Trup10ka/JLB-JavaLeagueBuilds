package me.trup10ka.jlb.data;

import java.util.List;

public class Champion
{

    private final String name;

    private ItemBuild itemBuild;

    private RunePage runePage;

    private List<SummonerSpell> summonerSpells;

    public Champion(String name, ItemBuild itemBuild, RunePage runePage, List<SummonerSpell> summonerSpells)
    {
        this.name = name;
        this.itemBuild = itemBuild;
        this.runePage = runePage;
        this.summonerSpells = summonerSpells;
    }

    public Champion(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public ItemBuild getItemBuild()
    {
        return itemBuild;
    }

    public RunePage getRunePage()
    {
        return runePage;
    }

    public List<SummonerSpell> getSummonerSpell()
    {
        return summonerSpells;
    }

    @Override
    public String toString()
    {
        return "Champion " + name + "\nItemBuild: " + itemBuild + "\nRunePage: " + runePage + "\nSummonerSpells: " + summonerSpells;
    }
}
