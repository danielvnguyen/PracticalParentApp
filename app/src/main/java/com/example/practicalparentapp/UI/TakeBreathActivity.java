package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import com.example.practicalparentapp.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.Objects;

/**
 * This class handles the 'Take Breath' activity
 * in the application. Guides the parent and/or
 * child through a relaxing breathing process.
 */
public class TakeBreathActivity extends AppCompatActivity {

    // ************************************************************
    // State Pattern states/classes
    // ************************************************************

    /**
     * This class is a state, handling the
     * different states of the TakeBreathActivity.
     */
    private abstract static class State {

        void handleEnter() {}
        void handleExit() {}
        void handleClickOn() {}
        void handleClickOff() {}
    }

    public final State inhaleState = new InhaleState();
    public final State exhaleState = new ExhaleState();
    private State currentState = new IdleState();

    public void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    private static class InhaleState extends State {

    }

    private static class ExhaleState extends State {
        private int numOfBreaths = 0;

    }

    private static class IdleState extends State {
        //Does nothing
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

        setUpBeginButton();
        //setUpButtons();
    }

    private void setUpBeginButton() {
        setState(inhaleState);
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
}