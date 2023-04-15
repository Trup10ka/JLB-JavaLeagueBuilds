package me.trup10ka.jlb.data;

import java.util.ArrayList;
import java.util.Set;

public class RunePage
{
    private final Rune mainRune;

    private final Set<Rune> secondaryMainRunes;

    private final Set<Rune> secondaryRunes;

    private final ArrayList<Attribute> attributes;

    private RunePage(Rune mainRune, Set<Rune> secondaryMainRunes, Set<Rune> secondaryRunes, ArrayList<Attribute> attributes)
    {
        this.mainRune = mainRune;
        this.secondaryMainRunes = secondaryMainRunes;
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

    public Rune getMainRune()
    {
        return mainRune;
    }

    public Set<Rune> getSecondaryMainRunes()
    {
        return secondaryMainRunes;
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
        return "Main rune: " + mainRune + "\nSecondary main runes: " + secondaryMainRunes + "\nSecondary runes: " + secondaryRunes + "\nAttributes: " + attributes;
    }
}