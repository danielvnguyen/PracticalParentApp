package com.example.practicalparentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.practicalparentapp.UI.TaskEdit;
import com.example.practicalparentapp.UI.TaskHistoryAdapter;
import com.example.practicalparentapp.UI.TaskHistoryObjectClass;
import com.example.practicalparentapp.UI.TinyDB;

import java.util.ArrayList;

public class TaskHistory extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_history);
        ListView mListView = (ListView) findViewById(R.id.historytasklistview);

        String currentTask = TaskEdit.currentTask;


        TinyDB tinydb = new TinyDB(this);
        ArrayList<TaskHistoryObjectClass> List = new ArrayList<>();
        ArrayList<Object> historyObjects = tinydb.getListObject(currentTask,TaskHistoryObjectClass.class);
        for(Object objs : historyObjects){
            List.add((TaskHistoryObjectClass) objs);
        }
        ArrayList<TaskHistoryObjectClass> peopleList = new ArrayList<>();

        int size=List.size();

        String tname;
        for(int i = 0; i<size;i++) {
            TaskHistoryObjectClass o = List.get(i);
            tname=o.getTaskName();
            if (tname.equals(currentTask)) {
                Log.i(TAG, "entered the loop ");

                peopleList.add(o);
            }
        }

        Log.i(TAG, "task name is " + currentTask);

        Log.i(TAG, "size of List is " + List.size());

        Log.i(TAG, "size of peopleList is " + peopleList.size());




        TaskHistoryAdapter adapter = new TaskHistoryAdapter(this, R.layout.activity_task_history_adapter, peopleList);
        mListView.setAdapter(adapter);

    }
    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskHistory.class);
    }

}