package com.example.practicalparentapp.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.widget.ImageView;

public class TaskHistoryObjectClass{
    private byte[] childImage;
    private String taskName;
    private String childName;
    private String date;

    public TaskHistoryObjectClass(String taskName, String childName, String date, byte[] image) {
        this.taskName = taskName;
        this.childName = childName;
        this.date = date;
        this.childImage = image;
    }

    public byte[] getChildImage() { return childImage;}

    public void setImage(byte[] image) { this.childImage = image;}

    public Bitmap getChildImageBitmap() {
        return BitmapFactory.decodeByteArray(childImage, 0, childImage.length);
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
