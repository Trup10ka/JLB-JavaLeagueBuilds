package me.trup10ka.jlb.web;

/**
 * Enum class for all possible pages from which app can parse data.
 * <br> <br>
 * Each constant can provide link to all champions page URL and champion build page URL.
 *
 * @author Lukas "Trup10ka" Friedl
 * @since 1.0.0
 */
public enum Page
{

    U_GG("https://u.gg/lol/champions", "https://u.gg", false)
            {
                @Override
                public String championURL(String champion)
                {
                    return "https://u.gg/lol/champions/" + champion + "/build";
                }
            },
    MOBAFIRE("https://www.mobafire.com/league-of-legends/champions","https://www.mobafire.com", true)
            {
                @Override
                public String championURL(String champion)
                {
                    return "https://www.mobafire.com/league-of-legends/champion/" + champion;
                }
            },
    LEAGUE_OF_GRAPHS("https://www.leagueofgraphs.com", false)
            {
                @Override
                public String championURL(String champion)
                {
                    return "https://www.leagueofgraphs.com/champions/builds/" + champion;
                }
            };
    /**
     * URL for page, where all champions, which page provides, are.
     */
    public final String ALL_CHAMPIONS_URL;

    /**
     * URL for home screen of the page
     */
    public final String RAW_WEB_URL;
    /**
     * Stores a value as boolean if this page builds are created by players or by statistics
     */
    public final boolean IS_COMMUNITY_BUILD;

    Page(String ALL_CHAMPIONS_URL, boolean IS_COMMUNITY_BUILD)
    {
        this.ALL_CHAMPIONS_URL = ALL_CHAMPIONS_URL;
        this.RAW_WEB_URL = ALL_CHAMPIONS_URL;
        this.IS_COMMUNITY_BUILD = IS_COMMUNITY_BUILD;
    }
    Page(String ALL_CHAMPIONS_URL, String RAW_WEB_URL, boolean IS_COMMUNITY_BUILD)
    {
        this.ALL_CHAMPIONS_URL = ALL_CHAMPIONS_URL;
        this.RAW_WEB_URL = RAW_WEB_URL;
        this.IS_COMMUNITY_BUILD = IS_COMMUNITY_BUILD;
    }

    public abstract String championURL(String champion);
}
