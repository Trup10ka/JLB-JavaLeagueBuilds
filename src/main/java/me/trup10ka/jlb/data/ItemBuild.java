package me.trup10ka.jlb.data;

import java.util.Map;
import java.util.Set;

public class ItemBuild
{

    private Set<Item> startItems;
    private Set<Item> coreItems;
    private Map<String, Set<Item>> endItems;
    /**
     * Boots are not null if they are not included in one of the lists.
     */
    private Item boots;

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
