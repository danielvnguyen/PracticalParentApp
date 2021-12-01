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

        //Create the Person objects
//        TaskHistoryObjectClass john = new TaskHistoryObjectClass ("clean","john");
//        TaskHistoryObjectClass  steve = new TaskHistoryObjectClass ("shave","steve");
//        TaskHistoryObjectClass  stacy = new TaskHistoryObjectClass ("clean","stacy");
//        TaskHistoryObjectClass  ashley = new TaskHistoryObjectClass ("clean","ashley");

        TinyDB tinydb = new TinyDB(this);
        ArrayList<TaskHistoryObjectClass> List = new ArrayList<>();
        ArrayList<Object> historyObjects = tinydb.getListObject(currentTask,TaskHistoryObjectClass.class);
        for(Object objs : historyObjects){
            List.add((TaskHistoryObjectClass) objs);
        }
        ArrayList<TaskHistoryObjectClass> peopleList = new ArrayList<>();
//        ArrayList<String> names = new ArrayList<>();
        int size=List.size();

        for(int i = 0; i<size;i++) {
            TaskHistoryObjectClass o = List.get(i);
//            if (o.getTaskName()==currentTask) {
                peopleList.add(o);
//                names.add(o.getChildName());
//            }
        }

        Log.i(TAG, "size of peopleList is " + peopleList.size());

        //Add the Person objects to an ArrayList
//        ArrayList<TaskHistoryObjectClass> peopleList = new ArrayList<>();
//        int sizeofnames=names.size();
//        for (x = 0;x<sizeofnames;x++) {


//        peopleList.add(john);
//        peopleList.add(steve);
//        peopleList.add(stacy);
//        peopleList.add(ashley);


        TaskHistoryAdapter adapter = new TaskHistoryAdapter(this, R.layout.activity_task_history_adapter, peopleList);
        mListView.setAdapter(adapter);

    }
    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskHistory.class);
    }

}