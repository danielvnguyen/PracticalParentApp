package com.example.practicalparentapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.practicalparentapp.Model.Child;
import com.example.practicalparentapp.Model.TaskManager;
import com.example.practicalparentapp.R;
import com.example.practicalparentapp.Model.Task;

public class TaskAdder extends AppCompatActivity {

    EditText enterChild;
    EditText enterTask;
    Button confirmTask;
    private TaskManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_adder);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        taskManager = TaskManager.getInstance();
        setTask();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTask() {
        confirmTask = findViewById(R.id.confirmTask);
        enterTask = findViewById(R.id.editChildName);
        enterChild = findViewById(R.id.editChildTask);

        confirmTask.setOnClickListener((v2) -> {
            String task_name = enterTask.getText().toString();
            Child child = new Child(enterChild.getText().toString());
            Task task = new Task(task_name, child);
            taskManager.add(task);

            Intent intent = ConfigureTasks.makeIntent(this);
            startActivity(intent);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskAdder.class);
    }


}

