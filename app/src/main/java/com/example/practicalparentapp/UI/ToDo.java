package com.example.practicalparentapp.UI;

public class ToDo {

    private String task;
    private String name;

public ToDo (String name, String task) {
    this.name=name;
    this.task=task;

}

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
