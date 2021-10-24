package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.practicalparentapp.R;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpConfigChildrenBtn();
        //setUpCoinFlipBtn();
        //setUpTimeoutTimerBtn();
    }

    private void setUpConfigChildrenBtn() {
        Button btn = findViewById(R.id.configureChildrenBtn);
        btn.setOnClickListener((v)-> {
            Intent intent = ConfigureChildren.makeIntent(this);
            startActivity(intent);
        });
    }
}