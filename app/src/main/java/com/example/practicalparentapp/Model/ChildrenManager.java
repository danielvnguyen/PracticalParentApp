package com.example.practicalparentapp.Model;
import java.util.List;
import java.util.ArrayList;


/**
 * This class is responsible for allowing the parent
 * to configure their children. Data includes their names.
 * Includes adding, removing, and editing children.
 */
public class ChildrenManager {

    private List<String> children = new ArrayList<>();

    private static ChildrenManager instance;

    public static ChildrenManager getInstance() {
        if (instance == null) {
            instance = new ChildrenManager();
        }
        return instance;
    }

    private ChildrenManager() {
        // nothing to ensure this is a singleton
    }

    public void addChild (String name) {
        String childName = name;
        children.add(childName);
    }

    public void removeChild (String name) {
        String toBeRemoved=name;
        children.remove(toBeRemoved);
    }



}
