package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.practicalparentapp.R;

public class TimeoutTimer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_timer);
        setTitle("Timeout Timer");
    }
}