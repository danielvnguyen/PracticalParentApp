package com.example.practicalparentapp.Model;

/**
 * Keeps track of the parent's children.
 * Data includes the child's name and whether
 * they flipped a coin recently or not.
 */
public class Child {
    private String name;
    private boolean flippedLast = false;

    public Child(String name) {
        this.name = name;
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
