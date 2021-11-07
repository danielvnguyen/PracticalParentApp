package com.example.practicalparentapp.UI;

import static java.lang.Math.log10;
import static java.lang.Math.pow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.practicalparentapp.R;

import java.util.Locale;

public class TimeoutTimer extends AppCompatActivity {

    private long START_TIME_IN_MILLIS;
    private TextView mTextViewCountDown, timeText;
    private Button mButtonStartPause, mButtonReset, mButtonSave;
    private EditText inputTime;

    private CountDownTimer mCountDownTimer;
    public static MediaPlayer alarmSound;
    private static Vibrator vibrator;
    private NotificationManagerCompat notificationManager;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_timer);

        setTitle("Timeout Timer");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        notificationManager = NotificationManagerCompat.from(this);
        alarmSound = MediaPlayer.create(this, R.raw.sunny);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        // function for the Dropdown Menu for selecting the number of minutes to timeout
        timeoutDropdown();

        updateCountDownText();

        setupSpinner();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        alarmSound.stop();
        vibrator.cancel();
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
                        inputTime.setOnKeyListener(new View.OnKeyListener() {
                            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                // To respond to when the user clicks the 'Enter' key
                                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                    int minutes = Integer.parseInt(inputTime.getText().toString());
                                    setInputVisiblityToTrue();
                                    START_TIME_IN_MILLIS = minutes*60*1000;
                                    mTimeLeftInMillis= START_TIME_IN_MILLIS;
                                    updateCountDownText();

                                    // To automatically hide the virtual keyboard once the user clicks the 'Enter' button
                                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                                return false;
                            }
                        });
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

    public static Vibrator getVibrator(){
        return vibrator;
    }

    public static MediaPlayer getAlarm(){
        return alarmSound;
    }

    private void timeoutDropdown() {
        mTextViewCountDown=findViewById(R.id.text_view_countdown);

        mButtonStartPause=findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        mTimerRunning = false;

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
                if (convertTimeToSecond(mTimeLeftInMillis) == 0) {
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                    updateCountDownText();
                    setupAlarmVibrate();
                } else {
                    updateCountDownText();
                }
            }

            @Override
            public void onFinish() {
                mTimerRunning=false;
                mButtonReset.setVisibility(View.INVISIBLE);
                mButtonStartPause.setText("Start");
                mTimeLeftInMillis=START_TIME_IN_MILLIS;
            }
        } .start();

        // The lines below happen when the pause button has been clicked
        mTimerRunning = true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private int convertTimeToSecond(long time) {
        double temp_time = pow(10, (int)log10(time));
        return (int)((int)(time / temp_time) * temp_time) / 1000;
    }

    private void setupAlarmVibrate() {
        // Playing the alarm sound when the timer reaches 00:00
        alarmSound.start();

        // Setting up the notification
        Intent activityIntent = new Intent(this, TimeoutTimer.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, App.ChannelID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Timer is Up!")
                .setContentText("Your set timer is now up. Click OK to stop the timer.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setColor(Color.BLUE)
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher, "OK", actionIntent)
                .build();

        notificationManager.notify(1, notification);

        // Setting up the Vibration functionality when the timer reaches 00:00
        vibrator.vibrate(VibrationEffect.createOneShot(START_TIME_IN_MILLIS*1000, VibrationEffect.DEFAULT_AMPLITUDE));
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
        mTimerRunning=false;
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }
}