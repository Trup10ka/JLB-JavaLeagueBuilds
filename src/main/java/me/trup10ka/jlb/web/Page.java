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

    U_GG("https://u.gg/lol/champions")
            {
                @Override
                public String championURL(String champion)
                {
                    return "https://u.gg/lol/champions/" + champion + "/build";
                }
            },
    MOBAFIRE("https://www.mobafire.com/champions/")
            {
                @Override
                public String championURL(String champion)
                {
                    return null;
                }
            },
    LEAGUE_OF_GRAPHS("https://www.leagueofgraphs.com")
            {
                @Override
                public String championURL(String champion)
                {
                    return "https://www.leagueofgraphs.com/champions/builds/" + champion;
                }
            };
    public final String ALL_CHAMPIONS_URL;

    Page(String ALL_CHAMPIONS_URL)
    {
        this.ALL_CHAMPIONS_URL = ALL_CHAMPIONS_URL;
    }

    public abstract String championURL(String champion);
}
