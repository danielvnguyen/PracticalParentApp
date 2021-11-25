package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import com.example.practicalparentapp.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
    private abstract class State {

        void handleEnter() {}
        void handleExit() {}
        void handleClickOn() {}
        void handleClickOff() {}
    }

    public final State inhaleState = new InhaleState();
    public final State exhaleState = new ExhaleState();
    private State currentState = new IdleState();
    private ImageView generalButton;
    private Integer numOfBreaths;
    private TextView helpText;
    private EditText inputNumBreaths;

    public void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    // ***********************************************************
    // Plain old Android Code (Non-State code)
    // ***********************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        setTitle(R.string.take_a_breath);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        generalButton = findViewById(R.id.begin_btn);
        helpText = findViewById(R.id.input_breaths_TV);
        inputNumBreaths = findViewById(R.id.input_num_ET);

        setUpBeginButton();
    }

    private void setUpBeginButton() {
        generalButton.setOnClickListener((v) -> {
            if (validateInput(inputNumBreaths)) {
                setState(inhaleState);
                numOfBreaths = Integer.parseInt(inputNumBreaths.getText().toString());
            }
        });
    }

    private boolean validateInput(EditText numBreaths) {
        String inputString = numBreaths.getText().toString();
        if (inputString.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "You didn't enter a number!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int inputNum = Integer.parseInt(inputString);

        if (inputNum < 0 || inputNum > 10) {
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

        @SuppressLint("SetTextI18n")
        @Override
        void handleEnter() {
            generalButton.setImageResource(R.drawable.in_button);
            helpText.setText("Breath in while holding the 'In' button");
            inputNumBreaths.setVisibility(View.INVISIBLE);
        }
    }

    private class ExhaleState extends State {
        private int numOfBreaths = 0;

        @SuppressLint("SetTextI18n")
        @Override
        void handleEnter() {
            generalButton.setImageResource(R.drawable.out_button);
            helpText.setText("Now breath out while holding the 'Out' button");
        }

    }

    private class IdleState extends State {
        //Does nothing
    }
}