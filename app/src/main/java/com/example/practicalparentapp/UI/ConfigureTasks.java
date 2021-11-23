package com.example.practicalparentapp.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.practicalparentapp.Model.TaskManager;
import com.example.practicalparentapp.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class displays a list of tasks to the parent.
 * Supports adding, editing, and deleting a task.
 * Also supports showing which child is next for the task.
 * Moves on to the next child when current child is done.
 */
public class ConfigureTasks extends AppCompatActivity {
    public static ArrayList<String> tasksList;
    private TaskManager taskManager;
    private ListView toDoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Your Tasks");

        setContentView(R.layout.activity_configure_tasks);

        if (getIntent().getBooleanExtra("TaskAdder", false)) {
            finish();
        }

        if (getIntent().getBooleanExtra("TaskEdit", false)) {
            finish();
        }

        updateTasks();
        setUpAddTaskBtn();
    }

    private void updateTasks() {
        taskManager = TaskManager.getInstance(this);
        tasksList = new ArrayList<>();
        clickCallBack(populateListView());
        ArrayAdapter<String> adapter = new ToDoListAdapter();
        toDoListView = findViewById(R.id.taskListView);
        toDoListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTasks();
    }

    private void clickCallBack(boolean populateListView) {
        if (!populateListView) {
            toDoListView = findViewById(R.id.taskListView);
            toDoListView.setOnItemClickListener((parent, viewClicked, position, id) -> Toast.makeText(ConfigureTasks.this, "No tasks has been created." +
                    "\nClick the Add button to create a task.", Toast.LENGTH_LONG).show());
        } else {
            toDoListView = findViewById(R.id.taskListView);
            toDoListView.setOnItemClickListener((parent, viewClicked, position, id) -> {
                Intent intent = TaskEdit.makeIntent(ConfigureTasks.this);
                intent.putExtra("position", position);
                startActivity(intent);
            });
        }
    }

    private boolean populateListView() {
        if(taskManager.size() == 0) {
            tasksList.add("");
            return false;
        } else {
            for (int i = 0; i < taskManager.size(); i++) {
                tasksList.add(taskManager.get(ConfigureTasks.this, i).toString());
            }
        }
        return true;
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

    private class ToDoListAdapter extends ArrayAdapter<String> {
        public ToDoListAdapter() {
            super(ConfigureTasks.this, R.layout.adapter_view_no_task, tasksList);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;

            if (taskManager.size() == 0) {
                if (itemView == null) {
                    itemView = getLayoutInflater().inflate(R.layout.adapter_view_no_task, parent, false);
                }
            } else {
                if (itemView == null) {
                    itemView = getLayoutInflater().inflate(R.layout.adapter_view_layout, parent, false);
                }

                String[] childTask = tasksList.get(position).split("@");

                TextView tvName = itemView.findViewById(R.id.childName);
                TextView tvTask = itemView.findViewById(R.id.taskToDo);

                tvName.setText(String.format("Child: %s", childTask[0]));
                tvTask.setText(String.format("Task: %s", childTask[1]));
            }
            return itemView;
        }
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