package com.example.practicalparentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.practicalparentapp.Model.Child;
import com.example.practicalparentapp.Model.ChildrenManager;
import com.example.practicalparentapp.UI.TaskEdit;
import com.example.practicalparentapp.UI.TaskHistoryAdapter;
import com.example.practicalparentapp.UI.TaskHistoryObjectClass;
import com.example.practicalparentapp.UI.TinyDB;

import java.util.ArrayList;
import java.util.Arrays;

public class TaskHistory extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_history);
        ListView mListView = (ListView) findViewById(R.id.historytasklistview);

        // all below handles outputting child task history
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
        ArrayList<Child> childList = new ArrayList<>();
        childList = cm.getChildList();
        ArrayList<String> stringList = new ArrayList<>();

        int childListsize = cm.size();
        for (int x = 0;x <childListsize;x++) {
            child = childList.get(x);
            stringList.add(child.getName());
        }

        int stringListSize = stringList.size();
        // This handles checking if child in history got removed from child manager, then adds
        // the children that are still existing
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
        int peopleListsize = peopleList.size();




//
//        TaskHistoryObjectClass zeChild;
//        String theChild;
//        for (int i = 0; i<peopleList.size();i++) {
//            zeChild = peopleList.get(i);
//
//            for (int j = 0; j<childList.size();j++) {
//                aChild=childList.get(j);
//                if (zeChild.getChildName().equals(aChild.getName())) {
//
//
//                    peopleList.remove(i);
//
//                }
//            }
//        }
//        String theChild;
//        Child aChild;
//        for (int i=0;i<peopleListsize;i++) {
//            TaskHistoryObjectClass chld=peopleList.get(i);
//            theChild = chld.getChildName();
//            aChild = cm.getChildByName(theChild);
//            if (aChild == null) {
//                peopleList.remove(i);
//            }
//        }



//        peopleList.removeAll(Arrays.asList(stringList));


//        for (int o=0; o<peopleListsize;o++) {
//            TaskHistoryObjectClass obj = peopleList.get(o);
//            boolean childExist = false;
//            for (int i=0;i<stringListSize;i++) {
//                String someChild= stringList.get(i);
//                if (obj.getChildName()==someChild) {
//                    childExist = true;
//                    break;
//                }
//
//            }
////            if (!childExist) {
////                peopleList.remove(o);
////            }
//
//        }

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