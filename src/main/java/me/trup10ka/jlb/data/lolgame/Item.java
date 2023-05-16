package me.trup10ka.jlb.data.lolgame;

/**
 * Items are very important part of League of legends
 * <p>
 * At the start of the game, champion has <strong>empty inventory</strong> and he has to buy so called <strong>starting items</strong>.
 * As the game goes, player buys more advanced and more expensive items for golds he has earned in the <a href="https://leagueoflegends.fandom.com/wiki/Summoner%27s_Rift">Rift</a>
 * for slaying wildlife or minions.
 * </p>
 * <p>
 * Players buy items for a very basic reason, to get stronger. Items provide you with bonus attributes to your basic stats.
 * For example, let's say that you have <strong>70 attack damage</strong> power. If you buy a <strong>sword</strong> you gain
 * additional <strong>10 attack damage</strong>, that means when you attack someone, instead of 70 you will deal 80 points of damage
 * to the enemy. You can build more complex items which provides you with greater bonuses (exp. 70 AD, 20% critical strike chance, 15% attack speed).
 * </p>
 * <p>
 * As are champions divided into different categories (Marksman, Bruiser, Tank, etc.), with items it is the same. Each item is only viable
 * for a specific category of champions.
 * </p>
 * @param name name of the item
 * @param imageName some pages have maps of multiple images in one big image; defines big image name (path)
 * @param x if item is in map of images, his x position
 * @param y if item is in map of images, his Y position
 * @author Lukas "Trup10ka" Friedl
 * @see <a href="https://leagueoflegends.fandom.com/wiki/Item_(League_of_Legends)">Items</a>
 * @see ItemBuild
 * @since 1.0.0
 */
public record Item(String name, String imageName, String description, float x, float y) implements Comparable<Item>
{

    public Item(String imagePath, float x, float y)
    {
        this("", imagePath,"", x, y);
    }

    /**
     * @param item the object to be compared - <strong>IS IGNORED</strong>.
     * @return always positive, because we want to sort items as they were put into the collection (TreeSet case).
     */
    @Override
    public int compareTo(Item item)
    {
        return 1;
    }
}
