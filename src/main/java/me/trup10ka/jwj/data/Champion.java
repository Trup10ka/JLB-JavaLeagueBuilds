package me.trup10ka.jwj.data;

public class Champion {

    private String name;

    public Champion(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Champion " + name;
    }
}
