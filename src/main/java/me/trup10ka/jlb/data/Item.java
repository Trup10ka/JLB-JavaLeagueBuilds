package me.trup10ka.jlb.data;

public record Item(String name, String imagePath, float x, float y) implements Comparable<Item>{
    public Item {
    }
    public Item(String name, String imagePath) {
        this(name, imagePath, 0, 0);
    }
    public Item(String imagePath, float x, float y) {
        this("", imagePath, x, y);
    }

    /**
     *
     * @param item the object to be compared - <strong>IS IGNORED</strong>.
     * @return always positive, because we want to sort items as they were put into the collection (TreeSet case).
     */
    @Override
    public int compareTo(Item item) {
        return 1;
    }
}
