package com.example.practicalparentapp.Model;

/**
 * Keeps track of the parent's children.
 * Data includes name. Possibly coin flip variables.
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
