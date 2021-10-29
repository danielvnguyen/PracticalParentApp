package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import com.example.practicalparentapp.Model.Child;
import com.example.practicalparentapp.Model.ChildrenManager;
import com.example.practicalparentapp.Model.RecyclerViewAdapter;
import com.example.practicalparentapp.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

/**
 * This class manages the parent's children.
 * Supports adding, deleting, and editing.
 */
public class NewChildActivity extends AppCompatActivity {

    private ChildrenManager childrenManager;
    private boolean isEditingChild = false;
    private Integer editChildIndex;
    private Child editedChild;
    private EditText childNameInput;
    private Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_child);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        childrenManager = ChildrenManager.getInstance(this);

        setInterface();
        setUpSaveBtn();
        setUpDeleteBtn();
    }

    private void setInterface() {
        childNameInput = findViewById(R.id.child_name_input);
        deleteBtn = findViewById(R.id.delete_btn);

        //check if child being edited
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isEditingChild = (Boolean) extras.get(RecyclerViewAdapter.STRING_EXTRA);
            editChildIndex = (Integer) extras.get(RecyclerViewAdapter.POSITION_EXTRA);
        }

        if (isEditingChild) {
            setTitle("Configuring a child");
            deleteBtn.setVisibility(View.VISIBLE);
            editedChild = childrenManager.getChildList().get(editChildIndex);
            childNameInput.setText(editedChild.getName());
        }
        else {
            setTitle("Adding a new child");
        }
    }

    private void setUpSaveBtn() {
        Button btn = findViewById(R.id.save_btn);
        btn.setOnClickListener(view -> {
            if (isEditingChild) {
                editedChild.editChild(childNameInput.getText().toString());
                ChildrenManager.saveChildList(this, childrenManager.getChildList());
            }
            else {
                Child newChild = new Child(childNameInput.getText().toString());
                childrenManager.addChildToList(this, newChild);
            }
            Toast.makeText(getApplicationContext(),"Child has been saved!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setUpDeleteBtn() {
        deleteBtn.setOnClickListener(view -> {
            childrenManager.removeChild(this, editChildIndex);
            ChildrenManager.saveChildList(this, childrenManager.getChildList());
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        // If the back button is pressed triggered cancel warning
        if (itemID == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, NewChildActivity.class);
    }
}