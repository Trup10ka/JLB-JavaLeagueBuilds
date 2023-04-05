package me.trup10ka.jwj.data;

public class Item {
    private final String name;
    private final String imagePath;

    public Item(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }
    public String getName() {
        return name;
    }
    public String getImagePath() {
        return imagePath;
    }
}
