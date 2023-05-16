package me.trup10ka.jlb.data.lolgame;

import java.util.Map;
import java.util.Set;

/**
 * This class represents all the buildable items which player can buy in League of Legends
 * <br> <br>
 * As it is called, ItemBuild consists of the following item categories:
 * <ul>
 *     <li>{@link ItemBuild#startItems Starting items}</li>
 *     <li>{@link ItemBuild#coreItems Core items}</li>
 *     <li>{@link ItemBuild#endItems Other (end) items}</li>
 * </ul>
 * The {@link Champion} has ItemBuild object as a parameter.
 * <br>
 * @author Lukas "Trup10ka" Friedl
 * @see Champion
 * @see Item
 * @see <a href="https://leagueoflegends.fandom.com/wiki/Item_(League_of_Legends)">League Items</a>
 * @since 1.0.0
 */
public class ItemBuild
{
    /**
     * Starting items are items which player buys at the <strong>start of the game</strong> and eventually sells them for better equipment. <br>
     * There are <strong>usually only two items</strong> but can be more
     */
    private final Set<Item> startItems;
    /**
     * Core items are items which player should <strong>buy every game</strong> and keep them for the rest of the match
     * <br><br>
     * They are considered to be the <strong>best choice for the champion</strong>.
     * <br>
     * Some of these items are also called <strong>Mythic items</strong>
     */
    private final Set<Item> coreItems;
    /**
     * End items are items <strong>recommended</strong> to the player to buy, meaning they are <strong>strong in wide range</strong> of situations.
     * <br><br>
     * However, it is just a recommendation, so that means these items may not be the strongest choice for every situation.
     * <br>
     * That means you may follow the <strong>recommended</strong> items, or you can buy other items by your own choice which you think are appropriate
     * <br> <br>
     * Ending items are <strong>Map</strong> because there are so called <strong>options</strong>. <br>
     * Possible string keys options are:
     * <ul>
     *     <li>"Fourth"</li>
     *     <li>"Fifth"</li>
     *     <li>"Sixth"</
     * </ul>
     */
    private final Map<String, Set<Item>> endItems;
    /**
     * Boots are not null if they are not included in one of the lists.
     */
    private final Item boots;

    private ItemBuild(Set<Item> startItems, Set<Item> coreItems, Map<String, Set<Item>> endItems, Item boots)
    {
        this.startItems = startItems;
        this.coreItems = coreItems;
        this.endItems = endItems;
        this.boots = boots;
    }

    public static class Builder
    {
        private Set<Item> startItems;
        private Set<Item> coreItems;
        private Map<String, Set<Item>> endItems;
        private Item boots;

        public Builder startItems(Set<Item> startItems)
        {
            this.startItems = startItems;
            return this;
        }

        public Builder coreItems(Set<Item> coreItems)
        {
            this.coreItems = coreItems;
            return this;
        }

        public Builder endItems(Map<String, Set<Item>> endItems)
        {
            this.endItems = endItems;
            return this;
        }

        public Builder boots(Item boots)
        {
            this.boots = boots;
            return this;
        }

        public ItemBuild build()
        {
            return new ItemBuild(startItems, coreItems, endItems, boots);
        }
    }

    public Set<Item> getStartItems()
    {
        return startItems;
    }

    public Set<Item> getCoreItems()
    {
        return coreItems;
    }

    public Map<String, Set<Item>> getEndItems()
    {
        return endItems;
    }

    public Item getBoots()
    {
        return boots;
    }

    @Override
    public String toString()
    {
        return "ItemBuild = StartItems: " + startItems + ", CoreItems: " + coreItems + ", EndItems: " + endItems + ", Boots: " + boots;
    }
}
