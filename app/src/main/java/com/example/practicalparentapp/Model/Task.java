package com.example.practicalparentapp.Model;

import androidx.annotation.NonNull;

/**
 * This class handles the functionality
 * of the Task.
 */
public class Task {

    private String task_name;
    private Child child;

    public Task(String task_name, Child child) {
        this.task_name = task_name;
        this.child = child;
    }

    public String getTaskName() {
        return task_name;
    }

    public void setTaskName(String task_names) {
        this.task_name = task_names;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    @NonNull
    @Override
    public String toString() {
        String str = "";
        str += child.getName();
        str += "@";
        str += task_name;
        return str;
    }
}
