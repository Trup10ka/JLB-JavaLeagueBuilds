package me.trup10ka.jlb.data.lolgame;

import java.util.ArrayList;
import java.util.Set;

/**
 * A Rune page consists of many runes in group.
 * <br> <br>
 * RunePage is divided into parts:
 * <ul>
 *     <li> {@link RunePage#keyStoneRune Keystone rune}</li>
 *     <li> {@link RunePage#sideMainRunes Main runes}</li>
 *     <li> {@link RunePage#secondaryRunes Secondary runes}</li>
 *     <li> {@link RunePage#attributes Attributes}</li>
 * </ul>

 * @see Rune
 * @see Champion
 * @author Lukas "Trup10ka" Friedl
 * @since 1.0.0
 */
public class RunePage
{
    /**
     * Keystone rune is the primary rune which plays the most important role during the game.<br>
     * Keystones:
     * <ul>
     *     <li><strong>Precision</strong></li>
     *     <ul>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Press_the_Attack">Press the Attack</a></li>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Lethal_Tempo">Lethal Tempo</a></li>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Fleet_Footwork">Fleet Footwork</a></li>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Conqueror">Conqueror</a></li>
     *     </ul>
     *     <li><strong>Domination</strong></li>
     *     <ul>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Electrocute">Electrocute</a></li>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Predator">Predator</a></li>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Dark_Harvest">Dark Harvest</a></li>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Hail_of_Blades">Hail of Blades</a></li>
     *     </ul>
     *     <li><strong>Sorcery</strong></li>
     *     <ul>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Summon_Aery">Summon Aery</a></li>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Arcane_Comet">Arcane Comet</a></li>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Phase_Rush">Phase Rush</a></li>
     *     </ul>
     *     <li><strong>Resolve</strong></li>
     *     <ul>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Grasp_of_the_Undying_(Rune)">Grasp of the Undying</a></li>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Aftershock">Aftershock</a></li>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Guardian">Guardian</a></li>
     *     </ul>
     *     <li><strong>Inspiration</strong></li>
     *     <ul>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Glacial_Augment">Glacial Augment</a></li>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/Unsealed_Spellbook">Unsealed Spellbook</a></li>
     *         <li><a href="https://leagueoflegends.fandom.com/wiki/First_Strike">First Strike</a></li>
     *     </ul>
     * </ul>
     */
    private final Rune keyStoneRune;
    /**
     * <p>
     *     Main runes are formed with 3 smaller runes that are relative to the keystones rune class (Precision, Domination, Sorcery, Resolve, Inspiration).
     *     <br> <br>
     *     You can find the list of the smaller runes <a href="https://leagueoflegends.fandom.com/wiki/Rune_(League_of_Legends)#Rune_paths">here</a>
     *     <br> <br>
     *     For example, if you choose Fleet Footwork, you have to choose runes from the slot 1,2 and 3 from the Precision class.
     * </p>
     */
    private final Set<Rune> sideMainRunes;
    /**
     * <p>
     *     Secondary runes are formed with 2 smaller runes that must not be relative to the keystone rune class.
     *     You can choose from whatever class you want.
     *     <br> <br>
     *     You can find the list of the smaller runes <a href="https://leagueoflegends.fandom.com/wiki/Rune_(League_of_Legends)#Rune_paths">here</a>
     * </p>
     */
    private final Set<Rune> secondaryRunes;
    /**
     * Attributes are formed with 3 attribute shards. Each shard provides a small bonus for the base stats of champion.
     * <br> <br>
     * You can find more about attribute shards <a href="https://leagueoflegends.fandom.com/wiki/Rune_(League_of_Legends)#Shards">here</a>
     */
    private final ArrayList<Attribute> attributes;

    private RunePage(Rune keyStoneRune, Set<Rune> sideMainRunes, Set<Rune> secondaryRunes, ArrayList<Attribute> attributes)
    {
        this.keyStoneRune = keyStoneRune;
        this.sideMainRunes = sideMainRunes;
        this.secondaryRunes = secondaryRunes;
        this.attributes = attributes;
    }

    public static class Builder
    {
        private Rune mainRune;

        private Set<Rune> secondaryMainRunes;

        private Set<Rune> secondaryRunes;

        private ArrayList<Attribute> attributes;

        public Builder mainRune(Rune mainRune)
        {
            this.mainRune = mainRune;
            return this;
        }

        public Builder secondaryMainRunes(Set<Rune> secondaryMainRunes)
        {
            this.secondaryMainRunes = secondaryMainRunes;
            return this;
        }

        public Builder secondaryRunes(Set<Rune> secondaryRunes)
        {
            this.secondaryRunes = secondaryRunes;
            return this;
        }

        public Builder attributes(ArrayList<Attribute> attributes)
        {
            this.attributes = attributes;
            return this;
        }

        public RunePage build()
        {
            return new RunePage(mainRune, secondaryMainRunes, secondaryRunes, attributes);
        }
    }

    public Rune getKeyStoneRune()
    {
        return keyStoneRune;
    }

    public Set<Rune> getSideMainRunes()
    {
        return sideMainRunes;
    }

    public Set<Rune> getSecondaryRunes()
    {
        return secondaryRunes;
    }

    public ArrayList<Attribute> getAttributes()
    {
        return attributes;
    }

    @Override
    public String toString()
    {
        return "Main rune: " + keyStoneRune + "\nSecondary main runes: " + sideMainRunes + "\nSecondary runes: " + secondaryRunes + "\nAttributes: " + attributes;
    }
}