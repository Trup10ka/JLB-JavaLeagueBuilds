package me.trup10ka.jlb.data.lolgame;

/**
 * <p>
 *    Summoner spells are spells which player chooses at the before the game starts (when player is choosing his champion).
 *    Summoner spells remain until the game ends and cannot be changed (<a href="https://leagueoflegends.fandom.com/wiki/Unsealed_Spellbook">Unsealed Spellbook</a>
 *    keystone allows you to change them during game).
 * </p>
 * <p>
 *     These spells have a long cooldown and this cool down cannot be reduced by {@link Item items} which provides cooldown reduction.
 *     Only a <a href="https://leagueoflegends.fandom.com/wiki/Cosmic_Insight">rune</a> and
 *     <a href="https://leagueoflegends.fandom.com/wiki/Ionian_Boots_of_Lucidity">boots</a> can reduce this cooldown.
 * </p>
 *
 * @since 1.0.0
 * @author Lukas "Trup10ka" Friedl
 * @see Champion
 * @see <a href="https://leagueoflegends.fandom.com/wiki/Summoner_spell#Standard_summoner_spells">Summoner Spells</a>
 */
@SuppressWarnings("unused")
public enum SummonerSpell
{
    /**
     * <a href="https://leagueoflegends.fandom.com/wiki/Heal">Heal</a>
     */
    HEAL,

    /**
     * <a href="https://leagueoflegends.fandom.com/wiki/Ghost">Ghost</a>
     */
    GHOST,

    /**
     * <a href="https://leagueoflegends.fandom.com/wiki/Exhaust">Exhaust</a>
     */
    EXHAUST,

    /**
     * <a href="https://leagueoflegends.fandom.com/wiki/Ignite">Ignite</a>
     */
    IGNITE,

    /**
     * <a href="https://leagueoflegends.fandom.com/wiki/Barrier">Barrier</a>
     */
    BARRIER,

    /**
     * <a href="https://leagueoflegends.fandom.com/wiki/Mark">Mark</a>
     */
    MARK,

    /**
     * <a href="https://leagueoflegends.fandom.com/wiki/Cleanse">Cleanse</a>
     */
    CLEANSE,

    /**
     * <a href="https://leagueoflegends.fandom.com/wiki/Teleport">Teleport</a>
     */
    TELEPORT,

    /**
     * <a href="https://leagueoflegends.fandom.com/wiki/Clarity">Clarity</a>
     */
    CLARITY,

    /**
     * <a href="https://leagueoflegends.fandom.com/wiki/Flash">Flash</a>
     */
    FLASH,

    /**
     * <a href="https://leagueoflegends.fandom.com/wiki/Smite">Smite</a>
     */
    SMITE
}
