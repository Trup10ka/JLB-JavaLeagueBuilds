package me.trup10ka.jlb.web;

public class WrongChampionPathException extends RuntimeException {
    public WrongChampionPathException(String path) {
        System.err.println("You have entered: " + path);
        this.printStackTrace();

    }
}
