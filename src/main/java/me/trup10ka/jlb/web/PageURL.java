package me.trup10ka.jlb.web;

public enum PageURL {

    U_GG{
        @Override
        public String championURL(String champion) {
            return "https://u.gg/lol/champions/" + champion + "/build";
        }
    },
    MOBAFIRE {
        @Override
        public String championURL(String champion) {
            return null;
        }
    },
    LEAGUE_OF_GRAPHS {
        @Override
        public String championURL(String champion) {
            return null;
        }
    };
    public abstract String championURL(String champion);
}
