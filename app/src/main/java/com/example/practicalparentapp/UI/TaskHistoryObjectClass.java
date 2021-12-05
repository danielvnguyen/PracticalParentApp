package com.example.practicalparentapp.UI;

import android.media.Image;
import android.widget.ImageView;

public class TaskHistoryObjectClass{

    private String taskName;
    private String childName;
    private String date;

    public TaskHistoryObjectClass(String taskName, String childName, String date) {
        this.taskName = taskName;
        this.childName = childName;
        this.date = date;

    }


    public String getDate() { return date;}

    public void setDate(String date) { this.date = date;}

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
