package com.example.practicalparentapp.UI;

public class TaskHistoryObjectClass{
    private String taskName;
    private String childName;

    public TaskHistoryObjectClass(String taskName, String childName) {
        this.taskName = taskName;
        this.childName = childName;

    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }


}
