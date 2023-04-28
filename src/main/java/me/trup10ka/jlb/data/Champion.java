package me.trup10ka.jlb.data;

import java.util.List;
/**
 * Champion is a character in game <a href="https://www.leagueoflegends.com">League of Legends</a>
 * <br><br>
 * Before the game starts there is so called "Select" stage where players choose which champion they are going to play. <br><br>
 * Each Champion has unique abilities and play styles. There are also many classes in which we can divide them for exp. "Mage", "Bruiser", etc.
 * Players champion starts with <strong>empty</strong> inventory. As you play through the game, in order to keep up with other players you
 * need to buy <strong>ITEMS</strong>.<br>
 * Items give u bonus to the basic attributes which each champion already has for exp. "Attack Damage", "Armor", "Movement Speed", etc.
 * For each champion <strong>there is only a range of items</strong> that are <strong>viable</strong> to buy on him.
 * <br><br>
 * Secondly, there are so called <strong>Runes</strong>. Runes are some kind of <strong>"bonuses"</strong> for the champion.
 * Each rune can provide you with various bonuses for exp. when you kill enemy champion, you gain small amount of health back
 * (<a href="https://leagueoflegends.fandom.com/wiki/Triumph">Triumph</a>)
 * <br><br>
 * Finally, each champion also has two {@link SummonerSpell summoner spells} which you chose again with runes.
 * Most common is {@link SummonerSpell#FLASH flash} and {@link SummonerSpell#IGNITE ignite}. You can find more {@link SummonerSpell here}
 * <br><br>
 * This class provides the summary of the best {@link ItemBuild items to build} on the chosen champion, the best {@link RunePage runes} who the most players
 * take and the most common {@link SummonerSpell Summoner Spells}
 *
 * @since 1.0.0
 * @author Lukas "Trup10ka Friedl
 * @see Rune
 * @see Item
 * @see ItemBuild
 * @see RunePage
 */
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
        return "Champion " + name + "\nItemBuild: " + itemBuild + "\nRunePage: " + runePage + "\nSummonerSpells: " + summonerSpells + "\n";
    }
}
