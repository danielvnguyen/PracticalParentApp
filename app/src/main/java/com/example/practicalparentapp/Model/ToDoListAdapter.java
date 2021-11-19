package com.example.practicalparentapp.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.practicalparentapp.R;
import com.example.practicalparentapp.UI.ToDo;

import java.util.ArrayList;
import java.util.List;

public class ToDoListAdapter extends ArrayAdapter<ToDo> {
    private Context mContext;
    int mResource;


    public ToDoListAdapter(Context context, int resource, ArrayList<ToDo> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource=resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String task = getItem(position).getTask();

        LayoutInflater inflator = LayoutInflater.from(mContext);
        convertView = inflator.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.childName);
        TextView tvTask = (TextView) convertView.findViewById(R.id.taskToDo);


        tvName.setText(name);
        tvTask.setText(task);

        return convertView;
    }



}
