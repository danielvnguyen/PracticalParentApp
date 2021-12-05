package com.example.practicalparentapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.practicalparentapp.Model.Child;
import com.example.practicalparentapp.Model.ChildrenManager;
import com.example.practicalparentapp.Model.TaskManager;
import com.example.practicalparentapp.R;
import com.example.practicalparentapp.TaskHistory;
import com.example.practicalparentapp.UI.TinyDB;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class handles
 * editing the tasks when press on
 * by the parent
 */
public class TaskEdit extends AppCompatActivity {

    public static String currentTask;
    String currentChild;
    private static final String TAG = "MyAct";
    ArrayList<TaskHistoryObjectClass> taskList= new ArrayList<>();
    private TaskManager taskManager;
    private ChildrenManager childrenManager;
    private int pos;


    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskEdit.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        setTitle("Editing Task");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        TinyDB tinydb = new TinyDB(this);
        taskManager = TaskManager.getInstance(this);
        childrenManager = ChildrenManager.getInstance(this);
        editFields();

        Button history = findViewById(R.id.historyBtn);
        history.setOnClickListener((v -> {
            Intent intent = TaskHistory.makeIntent(this);
            currentTask = taskManager.get(TaskEdit.this, pos).getTaskName();
            startActivity(intent);
        }));



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    private void editFields() {
        TextView childName = findViewById(R.id.childName);
        EditText editedChildTask = findViewById(R.id.editChildTask);
        Button confirm = findViewById(R.id.confirmTask);
        Button cancel = findViewById(R.id.cancel);
        Button remove = findViewById(R.id.removeTask);

        ImageView childImage = findViewById(R.id.childImg);
        confirm.setText("Confirm Turn");

        pos = getIntent().getIntExtra("position", -1);

        Child childWithTurn = new Child(taskManager.get(TaskEdit.this, pos).getChild().getName(),
                taskManager.get(TaskEdit.this, pos).getChild().getChildImageInBytes());
        childImage.setImageBitmap(childWithTurn.getChildImage());

        childName.setText("It is currently " + childWithTurn.getName() + "'s turn to:");
        editedChildTask.setText(taskManager.get(TaskEdit.this, pos).getTaskName());

        editedChildTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirm.setText("Save Changes");
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        confirm.setOnClickListener(view -> {
            int childPosition = childrenManager.getChildIndex(childWithTurn);
            if (childPosition == childrenManager.size() - 1) {
                childPosition = 0;
            } else if (childPosition == -1) {
                childPosition = 0;
            } else {
                childPosition += 1;
            }


            Calendar calendar = Calendar.getInstance();
            String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

            TinyDB tinydb = new TinyDB(this);
            ArrayList<Object> historyObjects = tinydb.getListObject(currentTask, TaskHistoryObjectClass.class);

            for(Object objs : historyObjects){
                taskList.add((TaskHistoryObjectClass) objs);
            }

            currentChild = childWithTurn.getName();
            currentTask = taskManager.get(TaskEdit.this, pos).getTaskName();
            Log.i(TAG, "current child is  " + currentChild);
            Log.i(TAG, "current task is  " + currentTask);
            TaskHistoryObjectClass history = new TaskHistoryObjectClass(currentTask,currentChild, currentDate,
                    childWithTurn.getChildImageInBytes());
            taskList.add(history);


            ArrayList<Object> playerObjects = new ArrayList<Object>();

            for(TaskHistoryObjectClass a : taskList){
                playerObjects.add((Object)a);
            }
            Log.i(TAG, "size of taskList " + taskList.size());
            Log.i(TAG, "size of playerObjects " + playerObjects.size());

            tinydb.putListObject(currentTask, playerObjects);

            ArrayList<String> taskList = new ArrayList<>();
            taskList = tinydb.getListString("tasks");
            taskList.add(currentTask);
            tinydb.putListString("tasks",taskList);



            if (confirm.getText().toString().equals("Save Changes")) {
                if (!childrenManager.doesChildExist(childWithTurn)) {
                    Toast.makeText(TaskEdit.this, "You already deleted " + childWithTurn.getName()
                            + " from the list of children.", Toast.LENGTH_LONG).show();
                    taskManager.get(TaskEdit.this, pos).setChild(childrenManager.getChild(childPosition));
                } else {
                    Toast.makeText(TaskEdit.this, "Your changes have now been saved", Toast.LENGTH_SHORT).show();
                }
                taskManager.get(TaskEdit.this, pos).setTaskName(editedChildTask.getText().toString());
                finish();
                Intent intent = ConfigureTasks.makeIntent(TaskEdit.this);
                intent.putExtra("TaskEdit", true);
                startActivity(intent);
            } else {
                if (!childrenManager.doesChildExist(childWithTurn)) {
                    Toast.makeText(TaskEdit.this, "You already deleted " + childWithTurn.getName()
                            + " from the list of children.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TaskEdit.this, "It's now " + childrenManager.getChild(childPosition).getName()
                            + "'s turn." , Toast.LENGTH_SHORT).show();
                    taskManager.get(TaskEdit.this, pos).setTaskName(editedChildTask.getText().toString());
                }
                taskManager.get(TaskEdit.this, pos).setChild(childrenManager.getChild(childPosition));
                finish();
                Intent intent = ConfigureTasks.makeIntent(TaskEdit.this);
                intent.putExtra("TaskEdit", true);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(view -> {
            Toast.makeText(TaskEdit.this, "Your changes have now been discarded", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = ConfigureTasks.makeIntent(TaskEdit.this);
            intent.putExtra("TaskEdit", true);
            startActivity(intent);
        });

        remove.setOnClickListener(view -> {
            taskManager.remove(TaskEdit.this, pos);
            Toast.makeText(TaskEdit.this, "Your task has now been deleted", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = ConfigureTasks.makeIntent(TaskEdit.this);
            intent.putExtra("TaskEdit", true);
            startActivity(intent);
        });
    }
}

