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
import android.media.RingtoneManager;
import android.net.Uri;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practicalparentapp.R;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class TimeoutTimer extends AppCompatActivity {
    private boolean isSelected;
    private SharedPreferences prefs;
    private static final String HAG= "internalsOfSpinner";
    private static final String TAG= "TimeoutTimer";
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
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_timer);
        Log.d(TAG, "it started onCreate"+ mTimeLeftInMillis);

        setTitle("Timeout Timer");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (!getIntent().getBooleanExtra("clickedNotification", false)) {
            notificationManager = NotificationManagerCompat.from(this);
            alarmSound = MediaPlayer.create(this, R.raw.sunny);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }

        // If the intent called is from clicking the notification
        Log.d("D_MSG", "I was clicked. 3");
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


        // function for the Dropdown Menu for selecting the number of minutes to timeout
        timeoutDropdown();

        updateCountDownText();
        Log.d(TAG, "it started onCreate + after updateCountDown" + mTimeLeftInMillis);
        setupSpinner();
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

    private void setupSpinner() {
        Spinner mySpinner = findViewById(R.id.spinner);
        mySpinner.setAdapter(new SpinnerAdapter(this, R.layout.spinner_dropdown,
                getResources().getStringArray(R.array.timeout_minutes)));

        SharedPreferences prefs_start = getSharedPreferences("prefs", MODE_PRIVATE);
        mTimeLeftInMillis = prefs_start.getLong("millisLeft", START_TIME_IN_MILLIS);

        SharedPreferences.Editor editor = prefs_start.edit();



            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            int j=i;
                            if (prefs_start.getBoolean("isSelected",false)) {
                                j=prefs_start.getInt("spinnerSelection",-6);
                                Log.d(TAG, "this is the J:" + j);

                                editor.putBoolean("isSelected",false);
                                editor.apply();
                            }
                    switch (j) {
                        case 0:
                            if (mTimerRunning && prefs_start.getInt("getSelection",-1)!=0) {
                                pauseTimer();
                                resetTimer();
                                updateButtons();
                                Log.d(HAG, "firstIfStatement");

                            }
                            if ( prefs_start.getInt("getSelection",-2)!=0) {
                                editor.putInt("spinnerSelection",j);
                                editor.putInt("getSelection",0);
                                editor.apply();
                                START_TIME_IN_MILLIS = 60000;
                                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                                updateButtons();
                                updateCountDownText();
                                Log.d(HAG, "secondIfStatement");
                                break;
                            }
                            else if (prefs_start.getInt("getSelection",-1)==0){

                                updateButtons();
                                updateCountDownText();
                                Log.d(HAG, "thirdIfStatement");
                                break;
                            }

                        case 1:
                            if (mTimerRunning && prefs_start.getInt("getSelection",-1)!=1) {
                                pauseTimer();
                                resetTimer();
                                updateButtons();

                            }
                            if (prefs_start.getInt("getSelection",-2)!=1) {
                                editor.putInt("spinnerSelection",j);
                                editor.putInt("getSelection",1);
                                editor.apply();
                                START_TIME_IN_MILLIS = 120000;
                                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                                updateButtons();
                                updateCountDownText();
                                break;
                            }
                            else if (prefs_start.getInt("getSelection",-1)==1){
                                updateButtons();
                                updateCountDownText();
                                break;
                            }

                        case 2:
                            if (mTimerRunning && prefs_start.getInt("getSelection",-1)!=2) {
                                pauseTimer();
                                resetTimer();
                                updateButtons();

                            }
                            if (prefs_start.getInt("getSelection",-2)!=2) {
                                editor.putInt("spinnerSelection",j);
                                editor.putInt("getSelection",2);
                                editor.apply();
                                START_TIME_IN_MILLIS = 180000;
                                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                                updateButtons();
                                updateCountDownText();
                                break;
                            }
                            else if (prefs_start.getInt("getSelection",-1)==2) {
                                updateButtons();
                                updateCountDownText();
                                break;
                            }

                        case 3:
                            if (mTimerRunning && prefs_start.getInt("getSelection",-1)!=3) {
                                pauseTimer();
                                resetTimer();
                                updateButtons();

                            }
                            if (prefs_start.getInt("getSelection",-2)!=3) {
                                editor.putInt("spinnerSelection",j);
                                editor.putInt("getSelection",3);
                                editor.apply();
                                START_TIME_IN_MILLIS = 300000;
                                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                                updateButtons();
                                updateCountDownText();
                                break;
                            }
                            else if (prefs_start.getInt("getSelection",-1)==3) {
                                updateButtons();
                                updateCountDownText();
                                break;
                            }

                        case 4:
                            if (mTimerRunning && prefs_start.getInt("getSelection",-1)!=4) {
                                pauseTimer();
                                resetTimer();
                                updateButtons();

                            }
                            if (prefs_start.getInt("getSelection",-2)!=4) {
                                editor.putInt("spinnerSelection",j);
                                editor.putInt("getSelection",4);
                                editor.apply();
                                START_TIME_IN_MILLIS = 600000;
                                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                                updateButtons();
                                updateCountDownText();
                                break;
                            }
                            else if (prefs_start.getInt("getSelection",-1)==4){
                                updateButtons();
                                updateCountDownText();
                                break;
                        }

                        case 5:
                            if (mTimerRunning && prefs_start.getInt("getSelection",-1)!=5) {
                                pauseTimer();
                                resetTimer();
                                updateButtons();

                            }
                            if (prefs_start.getInt("getSelection",-2)!=5) {
                                editor.putInt("spinnerSelection",j);
                                editor.putInt("getSelection",5);
                                editor.apply();

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
                                    }
                                });
                                break;
                            }
                            else if (prefs_start.getInt("getSelection",-1)==5) {
                                updateButtons();
                                updateCountDownText();
                                break;
                            }


                        default:
                            break;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

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
        // changed this NOW
//        mTimerRunning = false;

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
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis=millisUntilFinished;
                if (convertTimeToSecond(mTimeLeftInMillis) == 0) {
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                    updateCountDownText();
//                    setupAlarmVibrate();
                }
//                else {
                updateCountDownText();
//                }
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
        mTimerRunning=false;
        mButtonStartPause.setText("Resume");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        SharedPreferences prefs_start = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs_start.edit();
        editor.putInt("getSelection",-3);
        editor.apply();
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

        String timeLeftFormatted;
        timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);

    }


    @Override
    protected void onStop() {
        super.onStop();
        alarmSound.stop();
        vibrator.cancel();



        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putBoolean("status",true);
        editor.putBoolean("isSelected",true);
//        editor.putInt("getSelection",);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        Log.d("MSG", "" + prefs.getLong("millisLeft", START_TIME_IN_MILLIS));
        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        Log.d(TAG, "it stopped");

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs_start = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs_start.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs_start.getBoolean("timerRunning", false);
        Log.d(TAG, "it started onStart");
        updateCountDownText();
        updateButtons();
        Log.d("MSG", "" + prefs_start.getLong("millisLeft", START_TIME_IN_MILLIS));
        if (mTimerRunning) {
            mEndTime = prefs_start.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
//                mTimeLeftInMillis = 0;
//                mTimerRunning = false;
//                updateCountDownText();
//                updateButtons();
            } else {
                startTimer();

            }

        }
        updateButtons();
    }



}