package com.example.practicalparentapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.practicalparentapp.Model.Child;
import com.example.practicalparentapp.Model.ChildrenManager;
import com.example.practicalparentapp.Model.TaskManager;
import com.example.practicalparentapp.R;
import com.example.practicalparentapp.Model.Task;

import java.util.Objects;

/**
 * This class handles adding tasks
 * to the ConfigureTasks activity.
 */
public class TaskAdder extends AppCompatActivity {

    EditText enterChild;
    EditText enterTask;
    Button confirmTask;
    private TaskManager taskManager;
    private ChildrenManager childrenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_adder);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        taskManager = TaskManager.getInstance(this);
        childrenManager = ChildrenManager.getInstance(this);
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
            Child child = childrenManager.getChildByName(enterChild.getText().toString());

            if (child == null) {
                Toast.makeText(TaskAdder.this,  enterChild.getText().toString()
                        + " has not been configured as a child.\n" + "Please, configure " +
                        enterChild.getText().toString() + " as a child and try again.", Toast.LENGTH_LONG).show();
            } else {
                Task task = new Task(task_name, child);
                taskManager.add(TaskAdder.this, task);
                Intent intent = ConfigureTasks.makeIntent(this);
                intent.putExtra("TaskAdder", true);
                startActivity(intent);
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskAdder.class);
    }
}

