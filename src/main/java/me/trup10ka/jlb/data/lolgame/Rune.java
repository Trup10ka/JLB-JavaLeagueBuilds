package me.trup10ka.jlb.data.lolgame;

/**
 * <p>
 *     Runes can be defined very easily, especially the <a href="https://leagueoflegends.fandom.com/wiki/Rune_(League_of_Legends)#Rune_paths">Keystone rune</a>.
 *     When you enter the "Select" stage of the game (or "pick" stage), when you are picking a champion, you also choose a {@link RunePage} for him.
 * </p>
 * <p>
 *     We could say that Rune defines a certain play style, provides you with a bonus or just helps you out in different situations.
 * </p>
 * <p>
 *     Let's say that you've entered a fight with enemy and each attack you deal to him gives you a small amount of of bonus for your next attack
 *     (increasing your attack speed). After 6 attack (6 stacks), you receive a bigger amount of bonus to attack speed and also you gain increased range of the attack.
 *     This Rune is a keystone rune and its called <a href="https://leagueoflegends.fandom.com/wiki/Lethal_Tempo">Lethal Tempo</a>. Since it is a keystone rune,
 *     it is a strong rune and could be also called a <strong>main rune</strong>. This rune gives you a very specific bonus so as you could tell, only certain amount
 *     of champions are doing good with this rune. For example, imagine a mage, who is using his abilities only (since it is a mage) uses this rune, it just doesn't work well.
 * </p>
 * <p>
 *     So there is the Keystone rune, and also many other runes; "secondary" main runes, secondary runes and attribute shards. Keystone is a very powerful rune, from which
 *     main runes are provided (there are rune <a href="https://leagueoflegends.fandom.com/wiki/Rune_(League_of_Legends)#Rune_paths">classes<a/>)
 * </p>
 * <p>
 *     Conclusion: runes provides you with various bonuses and again, each champion has statistically "best" runes to take to be useful the most.
 * </p>
 *
 * @param name name of the rune
 * @param position rune position in RunePage (basically defines the order of the runes in RunePage)
 * @author Lukas "Trup10ka" Friedl
 * @since 1.0.0
 * @see RunePage
 * @see Champion
 */
public record Rune(String name, String description, int position) implements Comparable<Rune>
{
    @Override
    public int compareTo(Rune rune)
    {
        return this.position - rune.position;
    }
}