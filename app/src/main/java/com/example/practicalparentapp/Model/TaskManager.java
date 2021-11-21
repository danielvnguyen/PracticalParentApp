package com.example.practicalparentapp.Model;

import java.util.ArrayList;

public class TaskManager {
    private ArrayList<Task> tasks = new ArrayList<>();

    private static TaskManager instance;

    private TaskManager() {};

    public static TaskManager getInstance(){
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public void remove(int index) {
        tasks.remove(index);
    }

    public int size() {
        return tasks.size();
    }
}
