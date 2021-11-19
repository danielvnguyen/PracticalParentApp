package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import com.example.practicalparentapp.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.Objects;

/**
 * This class displays a list of tasks to the parent.
 * Supports adding, editing, and deleting a task.
 * Also supports showing which child is next for the task.
 * Moves on to the next child when current child is done.
 */
public class ConfigureTasks extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Your Tasks");
        setContentView(R.layout.activity_configure_tasks);

        setUpAddTaskBtn();
    }



    private void setUpAddTaskBtn() {
        Button btn = findViewById(R.id.addTask);
        btn.setOnClickListener((v) -> {
            Intent intent = AddTask.makeIntent(this);
            startActivity(intent);
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