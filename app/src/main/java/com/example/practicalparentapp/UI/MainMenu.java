package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.practicalparentapp.R;

/**
 * This class handles the main menu screen.
 * Supports button functionality for all of the
 * necessary activities.
 */
public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpConfigChildrenBtn();
        setUpCoinFlipBtn();
        setUpTimeoutTimerBtn();
        setUpHelpScreenBtn();
        setUpConfigureTasksBtn();
        setUpTakeBreathBtn();
    }

    private void setUpTakeBreathBtn() {
        Button breathBtn = findViewById(R.id.take_breath_btn);
        breathBtn.setOnClickListener((v) -> {
            Intent intent = TakeBreathActivity.makeIntent(this);
            startActivity(intent);
        });
    }

    private void setUpConfigureTasksBtn() {
        Button btn = findViewById(R.id.configureTaskBtn);
        btn.setOnClickListener((v2) -> {
            Intent intent2 = ConfigureTasks.makeIntent(this);
            startActivity(intent2);
        });
    }

    private void setUpHelpScreenBtn() {
        Button helpBtn = findViewById(R.id.help_screen_btn);
        helpBtn.setOnClickListener((v) -> {
            Intent intent = HelpScreen.makeIntent(this);
            startActivity(intent);
        });
    }

    private void setUpTimeoutTimerBtn() {
        Button timeOutBtn = findViewById(R.id.timeoutBtn);
        timeOutBtn.setOnClickListener((v) -> {
            Intent intent = TimeoutTimer.makeIntent(this);
            startActivity(intent);
        } );
    }

    public void setUpCoinFlipBtn() {
        Button flip_btn = findViewById(R.id.flipCoinBtn);
        flip_btn.setOnClickListener((v) -> {
            Intent intent = CoinFlip.makeIntent(this);
            startActivity(intent);
        } );
    }

    private void setUpConfigChildrenBtn() {
        Button btn = findViewById(R.id.configureChildrenBtn);
        btn.setOnClickListener((v2) -> {
            Intent intent2 = ConfigureChildren.makeIntent(this);
            startActivity(intent2);
        });
    }
}