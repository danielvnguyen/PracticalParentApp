package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import com.example.practicalparentapp.Model.ToDoListAdapter;
import com.example.practicalparentapp.R;
import com.example.practicalparentapp.TaskAdder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class displays a list of tasks to the parent.
 * Supports adding, editing, and deleting a task.
 * Also supports showing which child is next for the task.
 * Moves on to the next child when current child is done.
 */
public class ConfigureTasks extends AppCompatActivity {

    public static String name;
    public static String task;
    public static int numOfTasks;

    public static ArrayList<ToDo> toDoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Your Tasks");
        setContentView(R.layout.activity_configure_tasks);
        ListView toDoListView = (ListView) findViewById(R.id.taskListView);
        setUpAddTaskBtn();


//        if (toDoList.size()!=0) {
//            toDoList.add;
//        }
        ArrayList<ToDo> list = new ArrayList<>();
        ToDoListAdapter adapter = new ToDoListAdapter(this,R.layout.adapter_view_layout, toDoList);
        toDoListView.setAdapter(adapter);

//        ToDo child = new ToDo (name,task);

//        while (i!=0;) {
//
//        }

//        toDoList.add(child);
    }


    private void setUpAddTaskBtn() {
        Button btn = findViewById(R.id.addTaskBtn);
        btn.setOnClickListener((v2) -> {
            Intent intent2 = TaskAdder.makeIntent(this);
            startActivity(intent2);
        });
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, ConfigureTasks.class);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}