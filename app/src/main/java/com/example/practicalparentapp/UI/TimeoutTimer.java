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
import com.example.practicalparentapp.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

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
    private long mEndTime;

    private ArrayList<PieEntry> pieEntries;
    private PieChart chart;
    private ImageView chartBackground;
    private boolean isNewTimer;
    private long initialTime;

    private TextView displayRate;
    private double currentRate;
    private boolean isTicking;
    private long lastMillisLeft;

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

        currentRate = 1;
        isTicking = true;

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
            chart.setVisibility(View.VISIBLE);
            isNewTimer = true;
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
            chart.setVisibility(View.VISIBLE);
            isNewTimer = true;
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
            chart.setVisibility(View.VISIBLE);
            isNewTimer = true;
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
            chart.setVisibility(View.VISIBLE);
            isNewTimer = true;
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
            chart.setVisibility(View.INVISIBLE);
            chartBackground.setVisibility(View.INVISIBLE);

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

                builder.setSingleChoiceItems(rates, originalSelection, (dialog, which) -> updatedSelection[0] = which);

                builder.setPositiveButton("OK", (dialog, which) -> {
                    // user clicked OK
                    switch(updatedSelection[0]) {
                        case 0:
                            currentRate = 0.25;
                            if ((mTimeLeftInMillis / 1000) % 2 == 1) {
                                isTicking = true;
                            }
                            mTimeLeftInMillis /= 0.25;
                            mCountDownTimer.cancel();
                            isNewTimer = false;
                            startTimer();

                            displayRate.setText("Time @25%");
                            break;
                        case 1:
                            currentRate = 0.5;
                            if ((mTimeLeftInMillis / 1000) % 2 == 1) {
                                isTicking = true;
                            }
                            mTimeLeftInMillis /= 0.5;
                            mCountDownTimer.cancel();
                            isNewTimer = false;
                            startTimer();

                            displayRate.setText("Time @50%");
                            break;
                        case 2:
                            currentRate = 0.75;
                            if ((mTimeLeftInMillis / 1000) % 2 == 1) {
                                isTicking = true;
                            }
                            mTimeLeftInMillis /= 0.75;
                            mCountDownTimer.cancel();
                            isNewTimer = false;
                            startTimer();

                            displayRate.setText("Time @75%");
                            break;
                        case 3:
                            currentRate = 1;

                            displayRate.setText("Time @100%");
                            break;
                        case 4:
                            currentRate = 2;
                            if ((mTimeLeftInMillis / 1000) % 2 == 1) {
                                isTicking = true;
                            }
                            lastMillisLeft = mTimeLeftInMillis;
                            mTimeLeftInMillis /= 2;
                            mCountDownTimer.cancel();
                            isNewTimer = false;
                            startTimer();

                            displayRate.setText("Time @200%");
                            break;
                        case 5:
                            currentRate = 3;
                            if ((mTimeLeftInMillis / 1000) % 2 == 1) {
                                isTicking = true;
                            }
                            mTimeLeftInMillis /= 3;
                            mCountDownTimer.cancel();
                            isNewTimer = false;
                            startTimer();

                            displayRate.setText("Time @300%");
                            break;
                        case 6:
                            currentRate = 4;
                            if ((mTimeLeftInMillis / 1000) % 2 == 1) {
                                isTicking = true;
                            }
                            mTimeLeftInMillis /= 4;
                            mCountDownTimer.cancel();
                            isNewTimer = false;
                            startTimer();

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
            pieEntries.subList((int) (timeLeft * currentRate), initialSize).clear();

            chart.invalidate();

            chart.animateY(1400, Easing.EaseInOutQuad);
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

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, (long) (1000 / currentRate)) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                decreasePieChart();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mTimeLeftInMillis = 0;
                updateCountDownText();
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.INVISIBLE);

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
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("cancel", true);
        mCountDownTimer.cancel();
        editor.apply();
        mTimerRunning = false;
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonStartPause.setText(R.string.resume);
        mButtonReset.setVisibility(View.VISIBLE);
        isNewTimer = false;
    }


//    private void updateCountDownText() {
//        int minutes, seconds;
//        if (isTicking) {
//            if (currentRate >= 1) {
//                minutes = (int) (((mTimeLeftInMillis / 1000) / 60) * currentRate); // 30
//                seconds = (int) (((mTimeLeftInMillis / 1000) % 60) * currentRate); // 30
//            } else {
//
//                minutes = (int) ((mTimeLeftInMillis / 1000) / 60); // 30
//                seconds = (int) ((mTimeLeftInMillis / 1000) % 60); // 30
//            }
//            isTicking = false;
//        } else {
//            if (currentRate > 1) {
//                minutes = (int) (((mTimeLeftInMillis / 1000) / 60) * currentRate);
//                seconds = (int) (((mTimeLeftInMillis / 1000) % 60) * currentRate) - 1;
//                if (seconds <= 0) {
//                    seconds += 1;
//                    currentRate = 1;
//                }
//                isTicking = true;
//            } else if (currentRate == 1) {
//                minutes = (int) (((mTimeLeftInMillis / 1000) / 60) * currentRate);
//                seconds = (int) (((mTimeLeftInMillis / 1000) % 60) * currentRate);
//            } else {
//                minutes = (int) ((mTimeLeftInMillis / 1000) / 60);
//                seconds = (int) ((mTimeLeftInMillis / 1000) % 60) - 1;
//                if (seconds <= 0) {
//                    seconds += 1;
//                    currentRate = 1;
//                }
//                isTicking = true;
//            }
//        }
//
//        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
//        mTextViewCountDown.setText(timeLeftFormatted);
//    }

    @SuppressLint("SetTextI18n")
    private void updateCountDownText() {
        int minutes;
        int seconds;

        if (isTicking) {
            minutes = (int) ((mTimeLeftInMillis / 1000) / 60);
            seconds = (int) (lastMillisLeft / 1000);
            isTicking = false;
        }
        else{
            if (currentRate != 1) {
                minutes = (int) ((mTimeLeftInMillis / 1000) / 60);
                seconds = (int) (lastMillisLeft / 1000) - 1;
                lastMillisLeft -= 1;

                if (seconds <= 0) {
                    seconds += 1;
                    currentRate = 1;
                    displayRate.setText("Time @100%");
                }
                isTicking = true;
            }
            else {
                minutes = (int) (((mTimeLeftInMillis / 1000) / 60) * currentRate);
                seconds = (int) (((mTimeLeftInMillis / 1000) % 60) * currentRate);
            }
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

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("newTimer", isNewTimer);
        editor.putLong("initialTime", initialTime);

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
        isNewTimer = prefs.getBoolean("newTimer", true);
        initialTime = prefs.getLong("initialTime", START_TIME_IN_MILLIS);

        updateCountDownText();
        lastSelector=prefs.getLong("lastSelector",-1);

        //If timer was not running (paused, allow user to 'start')
        if (lastSelector != -1 && mTimeLeftInMillis != START_TIME_IN_MILLIS && !mTimerRunning) {
            mButtonStartPause.setVisibility(View.VISIBLE);
            mButtonStartPause.setText(R.string.start);
            mButtonReset.setVisibility(View.INVISIBLE);
        }
        isCustom = prefs.getBoolean("isCustom",false);

        //If timer was running
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