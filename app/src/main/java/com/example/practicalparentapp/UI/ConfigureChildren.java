package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.practicalparentapp.R;
import com.example.practicalparentapp.Model.Child;
import com.example.practicalparentapp.Model.ChildrenManager;
import com.example.practicalparentapp.Model.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class displays the list of children to the parent.
 */
public class ConfigureChildren extends AppCompatActivity {

    private ArrayList<Child> childList;
    private ChildrenManager childrenManager;
    private ArrayAdapter<Child> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_children);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Your Children");

        childrenManager = ChildrenManager.getInstance(this);
        setUpList();
        setUpAddButton();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        // If the back button is pressed triggered cancel warning
        if (itemID == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ConfigureChildren.class);
    }

    protected void onStart() {
        super.onStart();
        //update list with any new children
        listAdapter.notifyDataSetChanged();
    }

    private void setUpAddButton() {
        Button btn = findViewById(R.id.add_child);
        btn.setOnClickListener(view -> {
            Intent intent = NewChildActivity.makeIntent(this);
            startActivity(intent);
        });
    }

    //need to use adapter class here
    private void setUpList() {
        //create list view with properties
        ListView childListLV = findViewById(R.id.list_of_children);
        listAdapter = new RecyclerViewAdapter(this, R.layout.adapter, childrenManager.getChildList());
        childListLV.setAdapter(listAdapter);
        childList = childrenManager.getChildList();
    }
}