package com.example.practicalparentapp.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * This class handles each component of the task history
 * list in the Task activity.
 */
public class TaskHistoryObjectClass{
    private final byte[] childImage;
    private final String taskName;
    private final String childName;
    private final String date;

    public TaskHistoryObjectClass(String taskName, String childName, String date, byte[] image) {
        this.taskName = taskName;
        this.childName = childName;
        this.date = date;
        this.childImage = image;
    }

    public byte[] getChildImage() { return childImage;}

    public Bitmap getChildImageBitmap() {
        return BitmapFactory.decodeByteArray(childImage, 0, childImage.length);
    }

    public String getDate() { return date;}

    public String getTaskName() {
        return taskName;
    }

    public String getChildName() {
        return childName;
    }
}
