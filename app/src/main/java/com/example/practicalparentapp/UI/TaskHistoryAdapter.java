package com.example.practicalparentapp.UI;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import com.example.practicalparentapp.R;
import com.example.practicalparentapp.UI.TaskHistoryObjectClass;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;



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


        //Create the person object with the information
        TaskHistoryObjectClass taskHistory = new TaskHistoryObjectClass(TaskName,ChildName);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertAView = inflater.inflate(mResource,parent,false);

        TextView tvTaskName = (TextView) convertAView.findViewById(R.id.task);
        TextView tvChildName = (TextView) convertAView.findViewById(R.id.name);

        tvTaskName.setText(TaskName);
        tvChildName.setText(ChildName);



        return convertAView;
    }
}