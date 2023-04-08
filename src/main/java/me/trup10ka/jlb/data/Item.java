package me.trup10ka.jlb.data;

public record Item(String name, String imagePath, float x, float y) {
    public Item {
    }
    public Item(String name, String imagePath) {
        this(name, imagePath, 0, 0);
    }
    public Item(String imagePath, float x, float y) {
        this("", imagePath, x, y);
    }
}
