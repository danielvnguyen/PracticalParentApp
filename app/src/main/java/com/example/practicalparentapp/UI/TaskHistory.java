package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.practicalparentapp.Model.Child;
import com.example.practicalparentapp.Model.ChildrenManager;
import com.example.practicalparentapp.R;
import com.example.practicalparentapp.Model.TaskHistoryAdapter;
import com.example.practicalparentapp.Model.TaskHistoryObjectClass;
import com.example.practicalparentapp.Model.TinyDB;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class handles the task history activity screen.
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class TaskHistory extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Task History");
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
        ChildrenManager cm = ChildrenManager.getInstance(this);
        int size=List.size();
        Child child;
        ArrayList<Child> childList;
        childList = cm.getChildList();
        ArrayList<String> stringList = new ArrayList<>();

        int childListSize = cm.size();
        for (int x = 0;x <childListSize;x++) {
            child = childList.get(x);
            stringList.add(child.getName());
        }

        Child aChild;
        String tname;
        for(int i = 0; i<size;i++) {
            TaskHistoryObjectClass o = List.get(i);
            tname=o.getTaskName();
            for (int j = 0; j<childList.size();j++) {
                aChild=childList.get(j);
                if (tname.equals(currentTask) && o.getChildName().equals(aChild.getName())) {
                    Log.i(TAG, "entered the loop ");
                    peopleList.add(o);

                }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}