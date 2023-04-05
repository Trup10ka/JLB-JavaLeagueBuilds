package me.trup10ka.jwj.data;

import java.util.Set;

public class RunePage {
    private final Rune mainRune;

    private final Set<Rune> secondaryMainRunes;

    private final Set<Rune> secondaryRunes;

    private RunePage(Rune mainRune, Set<Rune> secondaryMainRunes, Set<Rune> secondaryRunes) {
        this.mainRune = mainRune;
        this.secondaryMainRunes = secondaryMainRunes;
        this.secondaryRunes = secondaryRunes;
    }
    public static class Builder {
        private Rune mainRune;

        private Set<Rune> secondaryMainRunes;

        private Set<Rune> secondaryRunes;

        public Builder mainRune(Rune mainRune) {
            this.mainRune = mainRune;
            return this;
        }
        public Builder secondaryMainRunes(Set<Rune> secondaryMainRunes) {
            this.secondaryMainRunes = secondaryMainRunes;
            return this;
        }
        public Builder secondaryRunes(Set<Rune> secondaryRunes) {
            this.secondaryRunes = secondaryRunes;
            return this;
        }
        public RunePage build() {
            return new RunePage(mainRune, secondaryMainRunes, secondaryRunes);
        }
    }
}