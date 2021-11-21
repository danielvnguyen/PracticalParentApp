package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import com.example.practicalparentapp.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Objects;

/**
 * This class handles displaying the help screen information
 */
public class HelpScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Help Screen");
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CoinFlip.class);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}