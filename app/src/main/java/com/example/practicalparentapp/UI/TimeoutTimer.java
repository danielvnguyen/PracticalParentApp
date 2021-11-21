package com.example.practicalparentapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.practicalparentapp.Model.NotificationClass;
import com.example.practicalparentapp.Model.NotificationReceiver;
import com.example.practicalparentapp.R;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

/**
 * This class handles the time out timer activity.
 * Supports a drop down menu with a list of default
 * timer options, as well as a custom time option.
 * Can also pause and reset the timer.
 */
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
    private boolean isCustom;

    private long lastSelector;
    private long mTimeLeftInMillis;
    private long mEndTime;


    @Override
    protected void onUserLeaveHint() {
        finish();
        super.onUserLeaveHint();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_timer);

        setTitle("Timeout Timer");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (!getIntent().getBooleanExtra("clickedNotification", false)) {
            notificationManager = NotificationManagerCompat.from(this);
            alarmSound = MediaPlayer.create(this, R.raw.sunny);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }

        if (getIntent().getBooleanExtra("clickedNotification", false)) {
            Log.d("D_MSG", "I was clicked. 90");
            vibrator.cancel();
            alarmSound.stop();
            try {
                alarmSound.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            getIntent().putExtra("clickedNotification", false);
            finish();
        }

        mButtonStartPause=findViewById(R.id.button_start_pause);
        mButtonReset=findViewById(R.id.button_reset);
        Button mButton_one = findViewById(R.id.button_one);
        Button mButton_two = findViewById(R.id.button_two);
        Button mButton_three = findViewById(R.id.button_three);
        Button mButton_five = findViewById(R.id.button_five);
        Button mButton_ten = findViewById(R.id.button_ten);
        Button mButton_custom = findViewById(R.id.button_custom);

        mTextViewCountDown=findViewById(R.id.text_view_countdown);

        mButtonStartPause.setOnClickListener(v -> {
            if (mTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        mButtonReset.setOnClickListener(v -> resetTimer());

        mButton_one.setOnClickListener(view -> {
            if (mTimerRunning) {
                pauseTimer();
                resetTimer();
            }
            START_TIME_IN_MILLIS = 60000;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            changeLayout();
            updateCountDownText();
            lastSelector=1;
            isCustom=false;
        });

        mButton_two.setOnClickListener(view -> {
            if (mTimerRunning) {
                pauseTimer();
                resetTimer();
            }
            START_TIME_IN_MILLIS = 120000;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            changeLayout();
            updateCountDownText();
            lastSelector=2;
            isCustom=false;

        });

        mButton_three.setOnClickListener(view -> {
            if (mTimerRunning) {
                pauseTimer();
                resetTimer();
            }
            START_TIME_IN_MILLIS = 180000;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            changeLayout();
            updateCountDownText();
            lastSelector=3;
            isCustom=false;

        });

        mButton_five.setOnClickListener(view -> {
            if (mTimerRunning) {
                pauseTimer();
                resetTimer();
            }
            START_TIME_IN_MILLIS = 300000;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            changeLayout();
            updateCountDownText();
            lastSelector=5;
            isCustom=false;

        });

        mButton_ten.setOnClickListener(view -> {
            if (mTimerRunning) {
                pauseTimer();
                resetTimer();
            }
            START_TIME_IN_MILLIS = 600000;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            changeLayout();
            updateCountDownText();
            lastSelector=10;
            isCustom=false;
        });

        mButton_custom.setOnClickListener(view -> {
            if (mTimerRunning) {
                pauseTimer();
                resetTimer();
            }
            mTextViewCountDown.setVisibility(View.INVISIBLE);
            mButtonStartPause.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            timeText = findViewById(R.id.textTime);
            timeText.setVisibility(View.VISIBLE);
            inputTime = findViewById(R.id.editTextNumber);
            inputTime.setVisibility(View.VISIBLE);
            inputTime.setOnKeyListener((v, keyCode, event) -> {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    int minutes = Integer.parseInt(inputTime.getText().toString());
                    setInputVisibilityToTrue();
                    START_TIME_IN_MILLIS = (long) minutes * 60 * 1000;
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                    updateCountDownText();

                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    lastSelector= START_TIME_IN_MILLIS;
                }
                return false;
            });
            mButtonSave = findViewById(R.id.button_save);
            mButtonSave.setVisibility(View.VISIBLE);
            mButtonSave.setOnClickListener(v -> {
                int minutes = Integer.parseInt(inputTime.getText().toString());
                setInputVisibilityToTrue();
                START_TIME_IN_MILLIS = (long) minutes * 60 * 1000;
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                updateCountDownText();
                lastSelector= START_TIME_IN_MILLIS;
            });

            isCustom=true;
        });

    }

    private void changeLayout() {
        mTextViewCountDown.setVisibility(View.VISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonStartPause.setText(R.string.start);
        mButtonSave = findViewById(R.id.button_save);
        mButtonSave.setVisibility(View.INVISIBLE);
        inputTime = findViewById(R.id.editTextNumber);
        inputTime.setVisibility(View.INVISIBLE);
        timeText = findViewById(R.id.textTime);
        timeText.setVisibility(View.INVISIBLE);
    }

    private void setInputVisibilityToTrue() {
        mTextViewCountDown.setVisibility(View.VISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonReset.setVisibility(View.INVISIBLE);
        timeText = findViewById(R.id.textTime);
        timeText.setVisibility(View.INVISIBLE);
        inputTime = findViewById(R.id.editTextNumber);
        inputTime.setVisibility(View.GONE);
        mButtonSave = findViewById(R.id.button_save);
        mButtonSave.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetTimer() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("cancel", true);
        mCountDownTimer.cancel();
        editor.apply();
        if (lastSelector!=-1) {
            if (lastSelector==1 && !isCustom) {
                mTimeLeftInMillis=60000;
            }
            if (lastSelector==2 && !isCustom) {
                mTimeLeftInMillis=120000;
            }
            if (lastSelector==3 && !isCustom) {
                mTimeLeftInMillis=180000;
            }
            if (lastSelector==5 && !isCustom) {
                mTimeLeftInMillis=300000;
            }
            if (lastSelector==10 && !isCustom) {
                mTimeLeftInMillis=600000;
            }
            if (isCustom) {
                Log.d("TAG: isCustomYay ", " is custom" + lastSelector);
                mTimeLeftInMillis=lastSelector;
            }
        } else {
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
        }

        mTimerRunning = false;
        updateCountDownText();
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonStartPause.setText(R.string.start);
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    public static Vibrator getVibrator(){
        return vibrator;
    }

    public static MediaPlayer getAlarm(){
        return alarmSound;
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeoutTimer.class);
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning=false;
                mTimeLeftInMillis = 0;
                updateCountDownText();
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.INVISIBLE);

                Log.d("OK", "onFinished");
                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                if (!prefs.getBoolean("cancel", false)) {
                    setupAlarmVibrate();
                }
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("cancel", false);
                editor.apply();
            }
        } .start();

        mTimerRunning = true;
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonStartPause.setText(R.string.pause);
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void setupAlarmVibrate() {
        alarmSound.start();

        Intent intent = new Intent(this, TimeoutTimer.class);
        intent.putExtra("clickedNotification", true);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent contentIntent = PendingIntent.getActivity(this,
                UUID.randomUUID().hashCode(), intent, 0);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                UUID.randomUUID().hashCode(), broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, NotificationClass.ChannelID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("Timer is Up!")
                .setContentText("Your set timer is now up. Click OK to stop the timer.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setColor(Color.parseColor("#FF58ADF1"))
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher, "OK", actionIntent)
                .build();

        notificationManager.notify(1, notification);

        vibrator.vibrate(VibrationEffect.createOneShot(40000, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private void pauseTimer() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("cancel", true);
        mCountDownTimer.cancel();
        editor.apply();
        mTimerRunning = false;
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonStartPause.setText(R.string.resume);
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putBoolean("isCustom",isCustom);
        editor.putLong("endTime", mEndTime);
        editor.putLong("lastSelector",lastSelector);

        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        lastSelector=prefs.getLong("lastSelector",-1);
        if (lastSelector != -1 && mTimeLeftInMillis != START_TIME_IN_MILLIS && !mTimerRunning) {
            mButtonStartPause.setVisibility(View.VISIBLE);
            mButtonStartPause.setText(R.string.start);
            mButtonReset.setVisibility(View.INVISIBLE);
        }
        isCustom=prefs.getBoolean("isCustom",false);

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
            } else {
                startTimer();
            }
        }
    }
}