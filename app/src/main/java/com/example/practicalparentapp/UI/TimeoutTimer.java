package com.example.practicalparentapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.practicalparentapp.Model.NotificationClass;
import com.example.practicalparentapp.Model.NotificationReceiver;
import com.example.practicalparentapp.Model.Task;
import com.example.practicalparentapp.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Timer;

/**
 * This class handles the time out timer activity.
 * Supports a drop down menu with a list of default
 * timer options, as well as a custom time option.
 * Can also pause and reset the timer.
 * Also supports visual timer and speed/slow down rates.
 */
public class TimeoutTimer extends AppCompatActivity {

    public static final int [] COLOR = {
        Color.rgb(64, 224, 208)
    };

    private long START_TIME_IN_MILLIS;
    private TextView mTextViewCountDown;
    private TextView timeText;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private Button mButtonSave;
    private EditText inputTime;

    private CountDownTimer mCountDownTimer;
    public static MediaPlayer alarmSound;
    private static Vibrator vibrator;
    private NotificationManagerCompat notificationManager;

    private boolean mTimerRunning;
    private boolean isCustom;

    private long lastSelector;
    private long mTimeLeftInMillis;
    private long screenTimeLeftInMillis;
    private long clickedTimeInMillis;
    private long endTimeScreenTime;
    private long mEndTime;

    private ArrayList<PieEntry> pieEntries;
    private PieChart chart;
    private ImageView chartBackground;
    private boolean isNewTimer;
    private long initialTime;

    private TextView displayRate;
    private float currentRate;
    private boolean ticked;
    private boolean temp_state;


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
        chart = findViewById(R.id.chart);
        chart.getDescription().setEnabled(false);
        chart.setDrawEntryLabels(false);
        chart.getLegend().setEnabled(false);
        chart.setDrawMarkers(false);
        chart.setDrawCenterText(false);
        chart.setDrawHoleEnabled(false);
        chart.setRotationEnabled(false);
        chart.setTouchEnabled(false);
        chart.setNoDataText("");
        chartBackground = findViewById(R.id.chartBackground);
        isNewTimer = true;

        displayRate = findViewById(R.id.rate_Textview);


        if (!getIntent().getBooleanExtra("clickedNotification", false)) {
            notificationManager = NotificationManagerCompat.from(this);
            alarmSound = MediaPlayer.create(this, R.raw.sunny);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }

        if (getIntent().getBooleanExtra("clickedNotification", false)) {
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

        currentRate = 1;
        ticked = false;
        temp_state = false;

        mButtonStartPause = findViewById(R.id.button_start_pause);
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
            chart.setVisibility(View.VISIBLE);
            isNewTimer = true;
            currentRate = 1;
            if (mTimerRunning) {
                pauseTimer();
                resetTimer();
            }
            START_TIME_IN_MILLIS = 60000;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            screenTimeLeftInMillis = mTimeLeftInMillis;
            changeLayout();
            updateCountDownText();
            lastSelector=1;
            isCustom=false;
        });

        mButton_two.setOnClickListener(view -> {
            chart.setVisibility(View.VISIBLE);
            isNewTimer = true;
            currentRate = 1;
            if (mTimerRunning) {
                pauseTimer();
                resetTimer();
            }
            START_TIME_IN_MILLIS = 120000;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            screenTimeLeftInMillis = mTimeLeftInMillis;
            changeLayout();
            updateCountDownText();
            lastSelector=2;
            isCustom=false;

        });

        mButton_three.setOnClickListener(view -> {
            chart.setVisibility(View.VISIBLE);
            isNewTimer = true;
            currentRate = 1;
            if (mTimerRunning) {
                pauseTimer();
                resetTimer();
            }
            START_TIME_IN_MILLIS = 180000;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            screenTimeLeftInMillis = mTimeLeftInMillis;
            changeLayout();
            updateCountDownText();
            lastSelector=3;
            isCustom=false;
        });

        mButton_five.setOnClickListener(view -> {
            chart.setVisibility(View.VISIBLE);
            isNewTimer = true;
            currentRate = 1;
            if (mTimerRunning) {
                pauseTimer();
                resetTimer();
            }
            START_TIME_IN_MILLIS = 300000;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            screenTimeLeftInMillis = mTimeLeftInMillis;
            changeLayout();
            updateCountDownText();
            lastSelector=5;
            isCustom=false;
        });

        mButton_ten.setOnClickListener(view -> {
            chart.setVisibility(View.VISIBLE);
            isNewTimer = true;
            currentRate = 1;
            if (mTimerRunning) {
                pauseTimer();
                resetTimer();
            }
            START_TIME_IN_MILLIS = 600000;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            screenTimeLeftInMillis = mTimeLeftInMillis;
            changeLayout();
            updateCountDownText();
            lastSelector=10;
            isCustom=false;
        });

        mButton_custom.setOnClickListener(view -> {
            chart.setVisibility(View.INVISIBLE);
            chartBackground.setVisibility(View.INVISIBLE);
            currentRate = 1;
            if (mTimerRunning) {
                pauseTimer();
                resetTimer();
                chartBackground.setVisibility(View.INVISIBLE);
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
                    screenTimeLeftInMillis = mTimeLeftInMillis;
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
                if (!inputTime.getText().toString().equals("")) {
                    int minutes = Integer.parseInt(inputTime.getText().toString());
                    setInputVisibilityToTrue();
                    START_TIME_IN_MILLIS = (long) minutes * 60 * 1000;
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                    screenTimeLeftInMillis = mTimeLeftInMillis;
                    updateCountDownText();
                    lastSelector= START_TIME_IN_MILLIS;
                    chart.setVisibility(View.VISIBLE);
                }
            });

            isCustom = true;
        });
    }

    // ************************************************************
    // Setting up the Tool Bar
    // ************************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeout_timer, menu);
        return true;
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
                
            case R.id.action_settings:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select a rate below:");

                String[] rates = {"25%", "50%", "75%", "100%", "200%", "300%", "400%"};
                int originalSelection = 3;
                final int[] updatedSelection = new int[1];
                updatedSelection[0] = 3;

                builder.setSingleChoiceItems(rates, originalSelection, (dialog, which) -> updatedSelection[0] = which);

                builder.setPositiveButton("OK", (dialog, which) -> {
                    // user clicked OK
                    switch(updatedSelection[0]) {
                        case 0:
                            if (mCountDownTimer != null)
                                mCountDownTimer.cancel();
                            mCountDownTimer = null;
                            isNewTimer = false;
                            if (mTimerRunning) {
                                currentRate = 0.25F;
                                mTimeLeftInMillis = (long) (screenTimeLeftInMillis / 0.25);
                                startTimer();
                            }

                            displayRate.setText("Time @25%");
                            break;
                        case 1:
                            if (mCountDownTimer != null)
                                mCountDownTimer.cancel();
                            mCountDownTimer = null;
                            isNewTimer = false;
                            if (mTimerRunning) {
                                currentRate = 0.5F;
                                mTimeLeftInMillis = (long) (screenTimeLeftInMillis / 0.5);
                                startTimer();
                            }

                            displayRate.setText("Time @50%");
                            break;
                        case 2:
                            if (mCountDownTimer != null)
                                mCountDownTimer.cancel();
                            mCountDownTimer = null;
                            isNewTimer = false;
                            if (mTimerRunning) {
                                currentRate = 0.75F;
                                mTimeLeftInMillis = (long) (screenTimeLeftInMillis / 0.75);
                                startTimer();
                            }

                            displayRate.setText("Time @75%");
                            break;
                        case 3:
                            if (mCountDownTimer != null)
                                mCountDownTimer.cancel();
                            mCountDownTimer = null;
                            isNewTimer = false;
                            if (mTimerRunning) {
                                currentRate = 1;
                                mTimeLeftInMillis = screenTimeLeftInMillis;
                                startTimer();
                            }

                            displayRate.setText("Time @100%");
                            break;
                        case 4:
                            if (mCountDownTimer != null)
                                mCountDownTimer.cancel();
                            mCountDownTimer = null;
                            isNewTimer = false;
                            if (mTimerRunning) {
                                currentRate = 2;
                                mTimeLeftInMillis = screenTimeLeftInMillis / 2;
                                startTimer();
                            }

                            displayRate.setText("Time @200%");
                            break;
                        case 5:
                            if (mCountDownTimer != null)
                                mCountDownTimer.cancel();
                            mCountDownTimer = null;
                            isNewTimer = false;
                            if (mTimerRunning) {
                                currentRate = 3;
                                mTimeLeftInMillis = screenTimeLeftInMillis / 3;
                                startTimer();
                            }

                            displayRate.setText("Time @300%");
                            break;
                        case 6:
                            if (mCountDownTimer != null)
                                mCountDownTimer.cancel();
                            mCountDownTimer = null;
                            isNewTimer = false;
                            if (mTimerRunning) {
                                currentRate = 4;
                                mTimeLeftInMillis = screenTimeLeftInMillis / 4;
                                startTimer();
                            }

                            displayRate.setText("Time @400%");
                            break;
                        default:
                            break;
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // ************************************************************
    // Visual Timer Code
    // ************************************************************

    private void clearPieChart() {
        if (chart.getData() != null) {
            chart.getData().clearValues();
        }
        chart.clear();
        chartBackground.setVisibility(View.INVISIBLE);
    }

    //Call every time the timer ticks.
    private void decreasePieChart() {
        int pieSize = pieEntries.size();
        if (pieSize > 0) {
            PieEntry toRemove = pieEntries.get(pieSize-1);
            pieEntries.remove(toRemove);
        }
        if (pieSize == 0) {
            screenTimeLeftInMillis = 0;
            updateCountDownText();
            setupAlarmVibrate();
        }
        chart.invalidate(); //refreshes chart
    }

    //This function sets up the initial pie chart
    private void createPieChart() {
        if (isNewTimer) {
            Log.d("MSG_D", "hey there");
            chartBackground.setVisibility(View.VISIBLE);
            pieEntries = new ArrayList<>();

            for (int i = 0; i < mTimeLeftInMillis; i+=1000) {
                pieEntries.add(new PieEntry(1));
            }

            initialTime = mTimeLeftInMillis;

            PieDataSet dataSet = new PieDataSet(pieEntries, "Visual Timer");
            dataSet.setColors(COLOR);
            dataSet.setDrawValues(false);
            PieData data = new PieData(dataSet);

            chart.setData(data);
            chart.invalidate();

            chart.animateY(1400, Easing.EaseInOutQuad);
        }
    }

    //This function retrieves the data for an ongoing pie chart
    private void obtainPieChart() {
        if (!isNewTimer) {
            chartBackground.setVisibility(View.VISIBLE);
            //Create new array list for the pie entries
            pieEntries = new ArrayList<>();

            //Fill pie chart to original size
            for (int i = 0; i < initialTime; i+=1000) {
                pieEntries.add(new PieEntry(1));
            }

            //Create the data for the pie chart
            PieDataSet dataSet = new PieDataSet(pieEntries, "Visual Timer");
            dataSet.setColors(COLOR);
            dataSet.setDrawValues(false);
            PieData data = new PieData(dataSet);

            //Set the data to the pie chart
            chart.setData(data);

            //Remove amount of time that has passed already
            int timeLeft = (int) ((mTimeLeftInMillis / 1000)); //time left
            int initialSize = (int) (initialTime / 1000); //total time

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (!temp_state) {
                        mButtonStartPause.performClick();
                        mButtonStartPause.performClick();
                    }
                    temp_state = true;
                }
            };

            handler.postDelayed(runnable, 10);


            pieEntries.subList((int) (timeLeft * currentRate), initialSize).clear();

            chart.invalidate();
        }
    }

    // ************************************************************
    // Timeout Timer Code
    // ************************************************************

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

    @SuppressLint("SetTextI18n")
    private void resetTimer() {
        clearPieChart();
        isNewTimer = true;
        currentRate = 1;
        ticked = false;
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = null;
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
                mTimeLeftInMillis=lastSelector;
            }
            screenTimeLeftInMillis = mTimeLeftInMillis;
        } else {
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            screenTimeLeftInMillis = mTimeLeftInMillis;
        }

        mTimerRunning = false;
        updateCountDownText();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = null;
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonStartPause.setText(R.string.start);
        mButtonReset.setVisibility(View.INVISIBLE);
        displayRate.setText("Time @100%");

        createPieChart();
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
        endTimeScreenTime = System.currentTimeMillis() + screenTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, (long) (1000 / currentRate)) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                decreasePieChart();
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateCountDownText();
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        } .start();

        mTimerRunning = true;
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonStartPause.setText(R.string.pause);
        mButtonReset.setVisibility(View.VISIBLE);

        obtainPieChart();
        createPieChart();
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
        ticked = false;
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
        mCountDownTimer = null;
        mTimerRunning = false;
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonStartPause.setText(R.string.resume);
        mButtonReset.setVisibility(View.VISIBLE);
        isNewTimer = false;
    }


    private void updateCountDownText() {
        int minutes, seconds;

        if (mTimerRunning) {
            screenTimeLeftInMillis -= 1000;
        }
        minutes = (int) ((screenTimeLeftInMillis / 1000) / 60);
        seconds = (int) ((screenTimeLeftInMillis / 1000) % 60);
        if (seconds <= 0) {
            seconds = 0;
            if (minutes == 0)
                ticked = true;
        }

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mTimerRunning) {
            isNewTimer = false;
        }
        ticked = false;

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("newTimer", isNewTimer);
        editor.putLong("initialTime", initialTime);

        editor.putFloat("rate", currentRate);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putLong("screenTime", screenTimeLeftInMillis);
        editor.putLong("endScreenTime", endTimeScreenTime);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putBoolean("isCustom",isCustom);
        editor.putLong("endTime", mEndTime);
        editor.putLong("lastSelector",lastSelector);
        editor.putLong("startTime", START_TIME_IN_MILLIS);
        editor.putLong("clickedTime", System.currentTimeMillis());

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = null;
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        clickedTimeInMillis = prefs.getLong("clickedTime", 0);
        START_TIME_IN_MILLIS = prefs.getLong("startTime", 0);
        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        isNewTimer = prefs.getBoolean("newTimer", true);
        Log.d("TIMER STATe", "" + isNewTimer);
        initialTime = prefs.getLong("initialTime", START_TIME_IN_MILLIS);
        currentRate = prefs.getFloat("rate", 0);
        screenTimeLeftInMillis = prefs.getLong("screenTime", START_TIME_IN_MILLIS);

        updateCountDownText();
        lastSelector = prefs.getLong("lastSelector", -1);

        //If timer was not running (paused, allow user to 'start')
        if (lastSelector != -1 && mTimeLeftInMillis != START_TIME_IN_MILLIS && !mTimerRunning) {
            mButtonStartPause.setVisibility(View.VISIBLE);
            mButtonStartPause.setText(R.string.start);
            mButtonReset.setVisibility(View.INVISIBLE);
        }
        if (screenTimeLeftInMillis <= 0) {
            mButtonStartPause.setVisibility(View.INVISIBLE);
        }
        isCustom = prefs.getBoolean("isCustom", false);
        ticked = false;

        //If timer was running
        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            endTimeScreenTime = prefs.getLong("endScreenTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (currentRate == 1) {
               screenTimeLeftInMillis = endTimeScreenTime - System.currentTimeMillis();}
            else
               screenTimeLeftInMillis = (long) (screenTimeLeftInMillis - (System.currentTimeMillis() - clickedTimeInMillis) * currentRate);

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                screenTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
            } else {
                if (mCountDownTimer != null)
                    mCountDownTimer.cancel();
                mCountDownTimer = null;
                startTimer();
            }
        }
    }
}