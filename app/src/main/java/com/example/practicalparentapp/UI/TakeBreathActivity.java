package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import com.example.practicalparentapp.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;

/**
 * This class handles the 'Take Breath' activity
 * in the application. Guides the parent and/or
 * child through a relaxing breathing process.
 */
public class TakeBreathActivity extends AppCompatActivity {

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

    private ImageView generalButton;
    private Integer numOfBreaths;
    private TextView helpText;
    private EditText inputNumBreaths;
    private int breathsToDo;
    private long secondsHeld;
    private long secondsDuration;
    private SharedPreferences.Editor editor;

    public void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    // ***********************************************************
    // Plain old Android Code (Non-State code)
    // ***********************************************************

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        setTitle(R.string.take_a_breath);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Initialize
        SharedPreferences prefs = getSharedPreferences("myPref", MODE_PRIVATE);
        editor = prefs.edit();

        inputNumBreaths = findViewById(R.id.input_num_ET);
        breathsToDo = prefs.getInt("breathsToDo", 0);
        inputNumBreaths.setText(breathsToDo + "");

        generalButton = findViewById(R.id.begin_btn);
        helpText = findViewById(R.id.input_breaths_TV);
        secondsHeld = 0;
        numOfBreaths = 0;

        Button yesButton = findViewById(R.id.yesBtn);
        yesButton.setVisibility(View.INVISIBLE);
        Button noButton = findViewById(R.id.noBtn);
        noButton.setVisibility(View.INVISIBLE);

        setUpBeginButton();
        setUpProceedButton();
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putInt("breathsToDo", breathsToDo);
        editor.apply();
    }

    private void setUpProceedButton() {
        Button proceedBtn = findViewById(R.id.proceedBtn);
        proceedBtn.setOnClickListener((v) -> setState(askMoreState));
    }

    private void setUpBeginButton() {
        generalButton.setOnClickListener((v) -> {
            if (validateInput(inputNumBreaths)) {
                setState(inhaleState);
                breathsToDo = Integer.parseInt(inputNumBreaths.getText().toString());
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

        @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
        @Override
        void handleEnter() {
            Button yesButton = findViewById(R.id.yesBtn);
            yesButton.setVisibility(View.INVISIBLE);
            Button noButton = findViewById(R.id.noBtn);
            noButton.setVisibility(View.INVISIBLE);

            generalButton.setVisibility(View.VISIBLE);
            generalButton.setImageResource(R.drawable.in_button);
            helpText.setText("Breath in while holding the 'In' button");
            inputNumBreaths.setVisibility(View.INVISIBLE);
            resetSeconds();

            generalButton.setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    secondsHeld = System.currentTimeMillis();
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    secondsDuration = System.currentTimeMillis() - secondsHeld;
                    //Need to automatically do this at 10 seconds.
                    if (secondsDuration >= 3000) {
                        System.out.println("Button held for 3 seconds at least");
                        setState(exhaleState);
                    }
                    else {
                        resetSeconds();
                    }
                }
                return true;
            });
        }
    }

    private class ExhaleState extends State {

        @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
        @Override
        void handleEnter() {
            generalButton.setImageResource(R.drawable.out_button);
            helpText.setText("Now breathe out while holding the 'Out' button");
            resetSeconds();

            generalButton.setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    secondsHeld = System.currentTimeMillis();
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    secondsDuration = System.currentTimeMillis() - secondsHeld;
                    //Need to automatically do this at 10 seconds.
                    if (secondsDuration >= 3000) {
                        System.out.println("Button held for 3 seconds at least");
                        numOfBreaths++;

                        //Check if user has completed all breaths
                        if (numOfBreaths == breathsToDo) {
                            helpText.setVisibility(View.INVISIBLE);
                            generalButton.setImageResource(R.drawable.good_job_button);
                            Button proceedBtn = findViewById(R.id.proceedBtn);
                            proceedBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            setState(inhaleState);
                        }
                    }
                    else {
                        resetSeconds();
                    }
                }

                return true;
            });
        }
    }

    private class AskMoreState extends State {
        @SuppressLint("SetTextI18n")
        void handleEnter() {
            Button proceedBtn = findViewById(R.id.proceedBtn);
            proceedBtn.setVisibility(View.INVISIBLE);
            generalButton.setVisibility(View.INVISIBLE);
            helpText.setVisibility(View.VISIBLE);
            helpText.setText("Would you like to take more breaths?");
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
        //Does nothing
    }
}