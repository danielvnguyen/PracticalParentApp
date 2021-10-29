package com.example.practicalparentapp.Model;

/**
 * Keeps track of the parent's children.
 * Data includes name. Possibly coin flip variables.
 */
public class Child {
    private String name;

    public Child(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    //Edits will just set name to whatever the parameter is.
    public void editChild(String name) {
        this.name = name;
    }
}
