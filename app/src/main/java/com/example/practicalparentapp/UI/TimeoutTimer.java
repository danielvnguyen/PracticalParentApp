package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practicalparentapp.R;

import java.util.Locale;

public class TimeoutTimer extends AppCompatActivity {

    private long START_TIME_IN_MILLIS;
    private TextView mTextViewCountDown, timeText;
    private Button mButtonStartPause, mButtonReset, mButtonSave;
    private EditText inputTime;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_timer);
        setTitle("Timeout Timer");

        // function for the Dropdown Menu for selecting the number of minutes to timeout
        timeoutDropdown();

        updateCountDownText();

        setupSpinner();
    }

    private void setupSpinner() {
        Spinner mySpinner = findViewById(R.id.spinner);
        mySpinner.setAdapter(new SpinnerAdapter(this, R.layout.spinner_dropdown,
                getResources().getStringArray(R.array.timeout_minutes)));

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        setInputVisiblityToTrue();
                        START_TIME_IN_MILLIS = 60000;
                        mTimeLeftInMillis= START_TIME_IN_MILLIS;
                        updateCountDownText();
                        break;
                    case 1:
                        setInputVisiblityToTrue();
                        START_TIME_IN_MILLIS = 120000;
                        mTimeLeftInMillis= START_TIME_IN_MILLIS;
                        updateCountDownText();
                        break;
                    case 2:
                        setInputVisiblityToTrue();
                        START_TIME_IN_MILLIS = 180000;
                        mTimeLeftInMillis= START_TIME_IN_MILLIS;
                        updateCountDownText();
                        break;
                    case 3:
                        setInputVisiblityToTrue();
                        START_TIME_IN_MILLIS = 300000;
                        mTimeLeftInMillis= START_TIME_IN_MILLIS;
                        updateCountDownText();
                        break;
                    case 4:
                        setInputVisiblityToTrue();
                        START_TIME_IN_MILLIS = 600000;
                        mTimeLeftInMillis= START_TIME_IN_MILLIS;
                        updateCountDownText();
                        break;
                    case 5:
                        mTextViewCountDown.setVisibility(View.INVISIBLE);
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        timeText = findViewById(R.id.textTime);
                        timeText.setVisibility(View.VISIBLE);
                        inputTime = findViewById(R.id.editTextNumber);
                        inputTime.setVisibility(View.VISIBLE);
                        mButtonSave = findViewById(R.id.button_save);
                        mButtonSave.setVisibility(View.VISIBLE);
                        mButtonSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int minutes = Integer.parseInt(inputTime.getText().toString());
                                setInputVisiblityToTrue();
                                START_TIME_IN_MILLIS = minutes*60*1000;
                                mTimeLeftInMillis= START_TIME_IN_MILLIS;
                                updateCountDownText();
                               }
                        });
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setInputVisiblityToTrue() {
        mTextViewCountDown.setVisibility(View.VISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        timeText = findViewById(R.id.textTime);
        timeText.setVisibility(View.INVISIBLE);
        inputTime = findViewById(R.id.editTextNumber);
        inputTime.setVisibility(View.GONE);
        mButtonSave = findViewById(R.id.button_save);
        mButtonSave.setVisibility(View.INVISIBLE);
    }

    private void timeoutDropdown() {
        mTextViewCountDown=findViewById(R.id.text_view_countdown);

        mButtonStartPause=findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                }
                else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeoutTimer.class);
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis=millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                mTimerRunning=false;
                mButtonStartPause.setText("start");
                mButtonStartPause.setVisibility(View.INVISIBLE);
            }
        } .start();

        // The lines below happen when the pause button has been clicked
        mTimerRunning = true;
        mButtonStartPause.setText("pause");
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning=false;
        mButtonStartPause.setText("Resume");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        mTimeLeftInMillis=START_TIME_IN_MILLIS;
        mCountDownTimer.cancel();
        updateCountDownText();
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }
}