package com.example.practicalparentapp.Model;

/**
 * Keeps track of the parent's children.
 * Data includes the child's name and whether
 * they flipped a coin recently or not.
 */
public class Child {
    private String name;
    private boolean flippedLast = false;
    private byte[] childImage;

    public Child(String name, byte[] image) {

        this.name = name;
        this.childImage = image;
    }

    public Child(int id, byte[] pp) {
        this.getName();
    }

    public byte[] getChildImage() {
        return childImage;
    }

    public String getName() {
        return name;
    }

    public void editChild(String name, byte[] image) {
        this.name = name;
        this.childImage = image;
    }

    public boolean isFlippedLast() {
        return flippedLast;
    }

    public void setFlippedLast(boolean flippedLast) {
        this.flippedLast = flippedLast;
    }
}
