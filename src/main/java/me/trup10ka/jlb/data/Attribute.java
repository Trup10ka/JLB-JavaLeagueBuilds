package me.trup10ka.jlb.data;

/**
 * Data class (record) which represents something called <strong>attribute</strong>
 * <br> <br>
 * Each rune page also has a place for <strong>three</strong> attributes which can be:
 * <ul>
 *     <li>Armor</li>
 *     <li>Magic Resistance</li>
 *     <li>Cool-down reduction</li>
 *     <li>Adaptive Force</li>
 *     <li>Bonus Health</li>
 *     <li>Attack Speed</li>
 * </ul>
 * These are minor boosts, which means they do not affect late game that much. They are meant to help the player in
 * <a href="https://leaguefeed.net/when-does-the-laning-phase-end/"> Laning phase</a>.
 * @param propertyName name of the attribute listed above
 * @since 1.0.0
 * @author Lukas "Trup10ka" Friedl
 * @see RunePage
 * @see Champion
 */
public record Attribute(String propertyName)
{
}
