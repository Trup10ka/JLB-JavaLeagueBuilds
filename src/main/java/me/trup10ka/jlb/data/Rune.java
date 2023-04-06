package me.trup10ka.jlb.data;

public record Rune(String name, int position) implements Comparable<Rune>{
    @Override
    public int compareTo(Rune rune) {
        return this.position - rune.position;
    }
}