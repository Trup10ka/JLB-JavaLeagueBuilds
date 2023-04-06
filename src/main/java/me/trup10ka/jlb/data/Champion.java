package me.trup10ka.jlb.data;

public class Champion {

    private String name;

    private ItemBuild itemBuild;

    private RunePage runePage;

    private SummonerSpell summonerSpell;

    public Champion(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public ItemBuild getItemBuild() {
        return itemBuild;
    }

    public RunePage getRunePage() {
        return runePage;
    }

    @Override
    public String toString() {
        return "Champion " + name;
    }
}
