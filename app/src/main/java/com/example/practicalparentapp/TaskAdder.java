package com.example.practicalparentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.practicalparentapp.UI.ConfigureTasks;
import com.example.practicalparentapp.UI.ToDo;

public class TaskAdder extends AppCompatActivity {

    EditText enterChild;
    EditText enterTask;
    Button confirmTask;
    public ToDo thing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_adder);

        confirmTask = findViewById(R.id.confirmTask);

        enterTask = (EditText) findViewById(R.id.enterTask);
        enterChild = (EditText) findViewById(R.id.enterChild);

        confirmTask.setOnClickListener((v2) -> {
            Intent intent = ConfigureTasks.makeIntent(this);

            ConfigureTasks.task = enterTask.getText().toString();
            ConfigureTasks.name = enterChild.getText().toString();
            thing= new ToDo(enterChild.getText().toString(),enterTask.getText().toString());
            startActivity(intent);

            ConfigureTasks.toDoList.add(thing);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskAdder.class);
    }


}

