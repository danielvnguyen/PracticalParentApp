package com.example.practicalparentapp.Model;

import android.annotation.SuppressLint;
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

/**
 * This class handles how the task history list
 * looks in each individual task in the Tasks activity
 */
public class TaskHistoryAdapter extends ArrayAdapter<TaskHistoryObjectClass> {

    private final Context mContext;
    private final int mResource;

    public TaskHistoryAdapter(Context context, int resource, ArrayList<TaskHistoryObjectClass> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, View convertAView, ViewGroup parent) {
        String ChildName = getItem(position).getChildName();
        String TaskName = getItem(position).getTaskName();
        String date = getItem(position).getDate();
        byte[] image = getItem(position).getChildImage();

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