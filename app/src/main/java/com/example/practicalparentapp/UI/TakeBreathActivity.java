package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.practicalparentapp.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;

/**
 * This class handles the 'Take Breath' activity
 * in the application. Guides the parent and/or
 * child through a relaxing breathing process.
 */
public class TakeBreathActivity<ImageView extends View> extends AppCompatActivity {

    /**
     * This class is a state, handling the
     * different states of the TakeBreathActivity.
     */
    @SuppressWarnings("InnerClassMayBeStatic")
    private abstract class State {

        void handleEnter() {}
        void handleExit() {}
    }

    public final State inhaleState = new InhaleState();
    public final State exhaleState = new ExhaleState();
    public final State askMoreState = new AskMoreState();
    private State currentState = new IdleState();

    private ImageView beginButton;
    private ImageView inButton;
    private ImageView outButton;
    private ImageView goodJobButton;
    private ImageView up_arrow;
    private ImageView down_arrow;

    private Integer numOfBreaths;
    private TextView helpText;
    private EditText inputNumBreaths;
    private Integer breathsToDo;
    private long secondsHeld;
    private long secondsDuration;
    private SharedPreferences.Editor editor;

    private MediaPlayer inhale_sound;
    private MediaPlayer exhale_sound;

    public void setState(State newState) {
        if (currentState != newState) {
            currentState.handleExit();
            currentState = newState;
            currentState.handleEnter();
        }
    }

    // ***********************************************************
    // Plain old Android Code (Non-State code)
    // ***********************************************************

    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        setTitle(R.string.take_a_breath);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        
        SharedPreferences prefs = getSharedPreferences("myPref", MODE_PRIVATE);
        editor = prefs.edit();

        inputNumBreaths = findViewById(R.id.input_num_ET);
        breathsToDo = prefs.getInt("breathsToDo", 0);
        inputNumBreaths.setText(breathsToDo + "");

        up_arrow = findViewById(R.id.inhale_animation);
        down_arrow = findViewById(R.id.exhale_animation);

        beginButton = findViewById(R.id.begin_btn);
        inButton = findViewById(R.id.in_btn);
        outButton = findViewById(R.id.out_btn);
        goodJobButton = findViewById(R.id.good_job_Btn);

        helpText = findViewById(R.id.input_breaths_TV);
        secondsHeld = 0;
        numOfBreaths = 0;

        Button yesButton = findViewById(R.id.yesBtn);
        yesButton.setVisibility(View.INVISIBLE);
        Button noButton = findViewById(R.id.noBtn);
        noButton.setVisibility(View.INVISIBLE);

        setUpBeginButton();
        setUpGoodJobButton();
        setUpSounds();
    }

    private void setUpSounds() {
        inhale_sound = new MediaPlayer();
        inhale_sound = MediaPlayer.create(this, R.raw.inflate_sound);
        exhale_sound = new MediaPlayer();
        exhale_sound = MediaPlayer.create(this, R.raw.exhale_sound);
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putInt("breathsToDo", breathsToDo);
        editor.apply();
    }

    private void setUpGoodJobButton() {
        goodJobButton.setOnClickListener((v) -> setState(askMoreState));
    }

    private void setUpBeginButton() {
        beginButton.setOnClickListener((v) -> {
            if (validateInput(inputNumBreaths)) {
                setState(inhaleState);
                breathsToDo = Integer.parseInt(inputNumBreaths.getText().toString());
                beginButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void resetSeconds() {
        this.secondsDuration = 0;
        this.secondsHeld = 0;
    }

    private boolean validateInput(EditText numBreaths) {
        String inputString = numBreaths.getText().toString();
        if (inputString.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "You didn't enter a number!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int inputNum = Integer.parseInt(inputString);

        if (inputNum < 1 || inputNum > 10) {
            Toast.makeText(getApplicationContext(),
                    "Your number is not between 1-10!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TakeBreathActivity.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ************************************************************
    // State Pattern states/classes
    // ************************************************************

    private class InhaleState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = () -> setState(exhaleState);

        @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
        @Override
        void handleEnter() {

            Button yesButton = findViewById(R.id.yesBtn);
            yesButton.setVisibility(View.INVISIBLE);
            Button noButton = findViewById(R.id.noBtn);
            noButton.setVisibility(View.INVISIBLE);

            inButton.setVisibility(View.VISIBLE);
            helpText.setText(R.string.inhale_txt);
            inputNumBreaths.setVisibility(View.INVISIBLE);
            resetSeconds();

            inButton.setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    setUpSounds();
                    inhale_sound.start();
                    YoYo.with(Techniques.SlideInUp).duration(10000).repeat(0).playOn(up_arrow);
                    up_arrow.setVisibility(View.VISIBLE);
                    secondsHeld = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 10000);

                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    inhale_sound.stop();
                    up_arrow.setVisibility(View.INVISIBLE);
                    secondsDuration = System.currentTimeMillis() - secondsHeld;

                    if (secondsDuration >= 3000 && secondsDuration < 10000) {
                        setState(exhaleState);
                    }
                    else {
                        resetSeconds();
                        timerHandler.removeCallbacks(timerRunnable);
                    }
                }
                return true;
            });
        }

        @Override
        void handleExit() {
            timerHandler.removeCallbacks(timerRunnable);
            inButton.setVisibility(View.INVISIBLE);
            up_arrow.setVisibility(View.INVISIBLE);
        }
    }

    private class ExhaleState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = () -> {
            numOfBreaths++;
            if (numOfBreaths < breathsToDo) {
                setState(inhaleState);
            }
            else {
                setState(askMoreState);
            }
        };

        @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
        @Override
        void handleEnter() {
            outButton.setVisibility(View.VISIBLE);
            helpText.setText(R.string.exhale_txt);
            resetSeconds();

            outButton.setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    setUpSounds();
                    down_arrow.setVisibility(View.VISIBLE);
                    exhale_sound.start();
                    YoYo.with(Techniques.SlideInDown).duration(10000).repeat(0).playOn(down_arrow);
                    secondsHeld = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 10000);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    exhale_sound.stop();
                    down_arrow.setVisibility(View.INVISIBLE);
                    secondsDuration = System.currentTimeMillis() - secondsHeld;

                    if (secondsDuration >= 3000 && secondsDuration < 10000) {
                        numOfBreaths++;

                        if (numOfBreaths.equals(breathsToDo)) {
                            goodJobButton.setVisibility(View.VISIBLE);
                            helpText.setVisibility(View.INVISIBLE);
                        }
                        else {
                            setState(inhaleState);
                        }
                    }
                    else {
                        resetSeconds();
                        timerHandler.removeCallbacks(timerRunnable);
                    }
                }

                return true;
            });
        }

        @Override
        void handleExit() {
            timerHandler.removeCallbacks(timerRunnable);
            outButton.setVisibility(View.INVISIBLE);
            down_arrow.setVisibility(View.INVISIBLE);
        }
    }

    private class AskMoreState extends State {
        @SuppressLint("SetTextI18n")
        void handleEnter() {
            goodJobButton.setVisibility(View.INVISIBLE);
            helpText.setVisibility(View.VISIBLE);
            helpText.setText(R.string.ask_more_txt);
            resetSeconds();

            Button yesButton = findViewById(R.id.yesBtn);
            yesButton.setVisibility(View.VISIBLE);
            yesButton.setOnClickListener((v) -> setState(inhaleState));

            Button noButton = findViewById(R.id.noBtn);
            noButton.setVisibility(View.VISIBLE);
            noButton.setOnClickListener((v) -> finish());
        }

        void handleExit() {
            numOfBreaths = 0;
        }
    }

    private class IdleState extends State {
    }
}