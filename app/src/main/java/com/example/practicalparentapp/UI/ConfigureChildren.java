package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.practicalparentapp.R;

import java.util.Objects;

public class ConfigureChildren extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_children);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Configure Children");
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

    //when back button is pressed, finish the activity
    @Override
    public void onBackPressed() {
        finish();
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, ConfigureChildren.class);
    }
}