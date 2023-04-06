package me.trup10ka.jlb.web;

public enum PageURL {

    U_GG("https://u.gg/lol/champions"){
        @Override
        public String championURL(String champion) {
            return "https://u.gg/lol/champions/" + champion + "/build";
        }
    },
    MOBAFIRE("https://www.mobafire.com/champions/"){
        @Override
        public String championURL(String champion) {
            return null;
        }
    },
    LEAGUE_OF_GRAPHS("https://leagueofgraphs.com/champions/"){
        @Override
        public String championURL(String champion) {
            return null;
        }
    };
    public final String allChampionsURL;
    PageURL(String allChampionsURL) {
        this.allChampionsURL = allChampionsURL;
    }
    public abstract String championURL(String champion);
}
