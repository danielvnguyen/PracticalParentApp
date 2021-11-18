package com.example.practicalparentapp.Model;

/**
 * Keeps track of the parent's children.
 * Data includes the child's name and whether
 * they flipped a coin recently or not.
 */
public class Child {
    private String name;
    private boolean flippedLast = false;
    private String imgUrl;

    public Child(String name) {

        this.name = name;
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Child(int id, byte[] pp) {
        this.getName();
    }

    public String getName() {
        return name;
    }

    public void editChild(String name) {
        this.name = name;
    }

    public boolean isFlippedLast() {
        return flippedLast;
    }

    public void setFlippedLast(boolean flippedLast) {
        this.flippedLast = flippedLast;
    }
}
