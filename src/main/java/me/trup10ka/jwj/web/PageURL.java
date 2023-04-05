package me.trup10ka.jwj.web;

public enum PageURL {

    U_GG("https://u.gg"),
    MOBAFIRE("https://www.mobafire.com"),
    LEAGUE_OF_GRAPHS("https://www.leagueofgraphs.com"),;
    public final String url;
    PageURL(String url) {
        this.url = url;
    }
}
