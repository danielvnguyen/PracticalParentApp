package com.example.practicalparentapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practicalparentapp.Model.TaskManager;
import com.example.practicalparentapp.R;

public class TaskEdit extends AppCompatActivity {

    private TaskManager taskManager;

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskEdit.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        taskManager = TaskManager.getInstance();
        editFields();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editFields() {
        EditText editedChildName = findViewById(R.id.editChildName);
        EditText editedChildTask = findViewById(R.id.editChildTask);
        Button confirm = findViewById(R.id.confirmTask);
        Button cancel = findViewById(R.id.cancel);
        Button remove = findViewById(R.id.removeTask);

//        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();

        int pos = getIntent().getIntExtra("position", -1);
//        if (pos == 0) {
//            if (prefs.getInt("posChild", -1) == -1)
//                editor.putInt("posChild", taskManager.size()-1);
//            editor.apply();
//        }

        editedChildName.setText(taskManager.get(pos).getChild().getName());
        editedChildTask.setText(taskManager.get(pos).getTaskName());

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int index = prefs.getInt("posChild", -1);

                Toast.makeText(TaskEdit.this, "Your changes have now been saved", Toast.LENGTH_SHORT).show();
                taskManager.get(pos).getChild().editChild(editedChildName.getText().toString());
                taskManager.get(pos).setTaskName(editedChildTask.getText().toString());
                Intent intent = ConfigureTasks.makeIntent(TaskEdit.this);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TaskEdit.this, "Your changes have now been discarded", Toast.LENGTH_SHORT).show();
                Intent intent = ConfigureTasks.makeIntent(TaskEdit.this);
                startActivity(intent);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskManager.remove(pos);
                Toast.makeText(TaskEdit.this, "Your task has now been deleted", Toast.LENGTH_SHORT).show();
                Intent intent = ConfigureTasks.makeIntent(TaskEdit.this);
                startActivity(intent);
            }
        });
    }


}