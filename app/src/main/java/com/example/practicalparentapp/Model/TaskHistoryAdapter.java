package com.example.practicalparentapp.Model;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import java.util.ArrayList;

import com.example.practicalparentapp.R;
import com.example.practicalparentapp.Model.TaskHistoryObjectClass;


public class TaskHistoryAdapter extends ArrayAdapter<TaskHistoryObjectClass> {



    private Context mContext;
    private int mResource;
    private int lastPosition = -1;



    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public TaskHistoryAdapter(Context context, int resource, ArrayList<TaskHistoryObjectClass> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertAView, ViewGroup parent) {
        //get the persons information
        String ChildName = getItem(position).getChildName();
        String TaskName = getItem(position).getTaskName();
        String date = getItem(position).getDate();
        byte[] image = getItem(position).getChildImage();


        //Create the person object with the information
        TaskHistoryObjectClass taskHistory = new TaskHistoryObjectClass(TaskName,ChildName,date,image);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertAView = inflater.inflate(mResource,parent,false);

        TextView tvTaskName = (TextView) convertAView.findViewById(R.id.task);
        TextView tvChildName = (TextView) convertAView.findViewById(R.id.name);
        TextView tvDate = (TextView) convertAView.findViewById(R.id.date);
        ImageView tvImage = (ImageView) convertAView.findViewById(R.id.image);


        tvImage.setImageBitmap(taskHistory.getChildImageBitmap());
        tvTaskName.setText(TaskName);
        tvChildName.setText(ChildName);
        tvDate.setText(date);




        return convertAView;
    }
}