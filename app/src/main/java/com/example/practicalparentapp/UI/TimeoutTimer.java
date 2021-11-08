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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.practicalparentapp.R;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class TimeoutTimer extends AppCompatActivity {

    private long START_TIME_IN_MILLIS = 60000;
    private TextView mTextViewCountDown, timeText;
    private Button mButtonStartPause, mButtonReset, mButtonSave;
    private Button mButton_one, mButton_two, mButton_three, mButton_five, mButton_ten, mButton_custom;
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_timer);

        setTitle("Timeout Timer");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (!getIntent().getBooleanExtra("clickedNotification", false)) {
            notificationManager = NotificationManagerCompat.from(this);
            alarmSound = MediaPlayer.create(this, R.raw.sunny);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }

        // If the intent called is from clicking the notification
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
        mButton_one=findViewById(R.id.button_one);
        mButton_two=findViewById(R.id.button_two);
        mButton_three=findViewById(R.id.button_three);
        mButton_five=findViewById(R.id.button_five);
        mButton_ten=findViewById(R.id.button_ten);
        mButton_custom=findViewById(R.id.button_custom);

        mTextViewCountDown=findViewById(R.id.text_view_countdown);



        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        mButton_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                    resetTimer();
                }
                START_TIME_IN_MILLIS = 60000;
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                changeLayout();
                updateButtons();
                updateCountDownText();
                lastSelector=1;
                isCustom=false;

            }
        });

        mButton_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                    resetTimer();
                }
                START_TIME_IN_MILLIS = 120000;
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                changeLayout();
                updateButtons();
                updateCountDownText();
                lastSelector=2;
                isCustom=false;

            }
        });

        mButton_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                    resetTimer();
                }
                START_TIME_IN_MILLIS = 180000;
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                changeLayout();
                updateButtons();
                updateCountDownText();
                lastSelector=3;
                isCustom=false;

            }
        });

        mButton_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                    resetTimer();
                }
                START_TIME_IN_MILLIS = 300000;
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                changeLayout();
                updateButtons();
                updateCountDownText();
                lastSelector=5;
                isCustom=false;

            }
        });

        mButton_ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                    resetTimer();
                }
                START_TIME_IN_MILLIS = 600000;
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                changeLayout();
                updateButtons();
                updateCountDownText();
                lastSelector=10;
                isCustom=false;
            }
        });

        mButton_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                    resetTimer();
                }
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
                            START_TIME_IN_MILLIS = minutes * 60 * 1000;
                            mTimeLeftInMillis = START_TIME_IN_MILLIS;
                            updateCountDownText();

                            // To automatically hide the virtual keyboard once the user clicks the 'Enter' button
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            lastSelector= START_TIME_IN_MILLIS;
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
                        START_TIME_IN_MILLIS = minutes * 60 * 1000;
                        mTimeLeftInMillis = START_TIME_IN_MILLIS;
                        updateCountDownText();
                        lastSelector= START_TIME_IN_MILLIS;
                    }
                });

                isCustom=true;
            }
        });

    }

    private void changeLayout() {
        mTextViewCountDown.setVisibility(View.VISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonSave = findViewById(R.id.button_save);
        mButtonSave.setVisibility(View.INVISIBLE);
        inputTime = findViewById(R.id.editTextNumber);
        inputTime.setVisibility(View.INVISIBLE);
        timeText = findViewById(R.id.textTime);
        timeText.setVisibility(View.INVISIBLE);
    }

    private void setInputVisiblityToTrue() {
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
        switch(item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void resetTimer() {
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
        }

        if (lastSelector==0) {
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
        }
        updateCountDownText();
        updateButtons();

    }

    private void updateButtons() {
        if (mTimerRunning) {
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mButtonStartPause.setText("Start");

            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
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
                updateButtons();
            }
        } .start();

        // The lines below happen when the pause button has been clicked
        mTimerRunning = true;
        updateButtons();
    }

    private int convertTimeToSecond(long time) {
        double temp_time = pow(10, (int)log10(time));
        return (int)((int)(time / temp_time) * temp_time) / 1000;
    }

    private void setupAlarmVibrate() {
        // Playing the alarm sound when the timer reaches 00:00
        alarmSound.start();

        // Setting up the notification
        Intent intent = new Intent(this, TimeoutTimer.class);
        intent.putExtra("clickedNotification", true);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                UUID.randomUUID().hashCode(), intent, 0);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                UUID.randomUUID().hashCode(), broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, App.ChannelID)
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

        // Setting up the Vibration functionality when the timer reaches 00:00
        vibrator.vibrate(VibrationEffect.createOneShot(START_TIME_IN_MILLIS*1000, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
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

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateButtons();
        lastSelector=prefs.getLong("lastSelector",-1);
        isCustom=prefs.getBoolean("isCustom",false);

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
    }
}