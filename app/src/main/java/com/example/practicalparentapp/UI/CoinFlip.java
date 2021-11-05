package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practicalparentapp.Model.ChildrenManager;
import com.example.practicalparentapp.R;

import java.util.Objects;
import java.util.Random;

public class CoinFlip extends AppCompatActivity {
    private MediaPlayer coinSound;
    private ChildrenManager childrenManager;
    private TextView enterPosTV;
    private EditText enterPosOne;
    private EditText enterPosTwo;
    private boolean hasConfiguredChildren = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        setTitle("Flip Coin");

        //get the input names stuff. invisible if isChildListEmpty() is true.
        enterPosTV = findViewById(R.id.enter_posTV);
        enterPosOne = findViewById(R.id.enter_pos1);
        enterPosTwo = findViewById(R.id.enter_pos2);
        TextView childChoiceTV = findViewById(R.id.current_child);
        Button headsBtn = findViewById(R.id.heads_btn);
        Button tailsBtn = findViewById(R.id.tails_btn);
        Button confirmBtn = findViewById(R.id.confirm_btn);
        TextView askChild = findViewById(R.id.current_child);
        childrenManager = ChildrenManager.getInstance(this);
        //if no configured children, just a coin flip button.
        if (childrenManager.isChildListEmpty()) {
            enterPosTV.setVisibility(View.INVISIBLE);
            enterPosOne.setVisibility(View.INVISIBLE);
            enterPosTwo.setVisibility(View.INVISIBLE);
            childChoiceTV.setVisibility(View.INVISIBLE);
            headsBtn.setVisibility(View.INVISIBLE);
            tailsBtn.setVisibility(View.INVISIBLE);
            confirmBtn.setVisibility(View.INVISIBLE);
            askChild.setVisibility(View.INVISIBLE);
            hasConfiguredChildren = false;
        }
        //otherwise, ask parents children before flipping
        //do this in when flip button is clicked

        setUpSounds();
        //setUpConfirmBtn();
        setUpFlipBtn();
    }

    private boolean validateInput(EditText childOne, EditText childTwo) {
        String nameOne = childOne.getText().toString();
        String nameTwo = childTwo.getText().toString();

        //check for empty inputs
        if (nameOne.isEmpty() || nameTwo.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You didn't finish entering the positions!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int nameOneToInt = Integer.parseInt(nameOne);
        int nameTwoToInt = Integer.parseInt(nameTwo);

        //check for invalid inputs / out of bound inputs
        if (!childrenManager.isChildExist(nameOneToInt) || !childrenManager.isChildExist(nameTwoToInt)) {
            Toast.makeText(getApplicationContext(), "You input a position that doesn't correlate to a child!", Toast.LENGTH_SHORT).show();
            return false;
        }

        //check if parent input same child/position
        if (nameOneToInt == nameTwoToInt) {
            Toast.makeText(getApplicationContext(), "You need to enter 2 different positions!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /*
    To do:
    - Once coin flip activity is started, check if there's configured children (don't show kid names if not) (CHECK)
    - Obtain the names of the 2 kids they put. Probably easier if you just ask which position maybe? (CHECK)
    - Make sure they're valid positions, then flip the coin when the parent presses 'Flip the coin' (CHECK)
    - Make sure button works if there's no configured children. (CHECK)
    - Need to let first kid choose heads/tails, then switch to other kid for next flip.
            - Add a 'confirm' button that gets rid of the position inputs when clicked.
            - That way, don't have to worry about parent changing the positions.
            - Lock the flip the coin button until this button is clicked.

    History of flips:
    - I suppose, figure out a button that starts another activity or something?
    - Could probably just make the ListView scrollable in the CoinFlip window.
    - No GSON saving. Should be there if you leave and come back to the coin flip activity tho?
    - History includes: time + date, name of kid who chose, result of flip, and a check/x if they won/lost.
    - Do we need a history for no configured children flips? Probably not.
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        // If the back button is pressed triggered cancel warning
        if (itemID == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //when back button is pressed, finish the activity
    @Override
    public void onBackPressed() {
        finish();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CoinFlip.class);
    }

    private static final Random random = new Random();
    private ImageView coin;

    private void setUpFlipBtn() {
        ImageView coinsImage = findViewById(R.id.heads);
        Button flip_btn = findViewById(R.id.flip_button);
        /*
        Should add constraints here to make sure parent chose children properly.
        Need to get the result of the flip, name of child, date, time, etc.
        Get result from flipTheCoin(). Date/time create when coin flipped as well?
         */
        flip_btn.setOnClickListener((v) -> {

            //check for errors
            if (!hasConfiguredChildren || validateInput(enterPosOne, enterPosTwo)) {
                CoinFl(coinsImage);
                flipTheCoin();
                coinSound.start();

                //add information to flip history
            }
            else {
                //do nothing. wait until parent enters valid input
            }
        });
    }

    public void CoinFl(ImageView coin) {
        this.coin = coin;
    }

    private void flipTheCoin()
    {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);
        fadeOut.setFillAfter(true);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                coin.setImageResource(random.nextFloat()>0.5f?R.drawable.tails:R.drawable.heads);

                int side = random.nextInt(2);

                if (side == 1) {
                    coin.setImageResource(R.drawable.heads);
                    Toast.makeText(getApplicationContext(), "Heads", Toast.LENGTH_SHORT).show();
                }
                else {
                    coin.setImageResource(R.drawable.tails);
                    Toast.makeText(getApplicationContext(), "Tails", Toast.LENGTH_SHORT).show();
                }
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator());
                fadeIn.setDuration(3000);
                fadeIn.setFillAfter(true);

                coin.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        coin.startAnimation(fadeOut);
    }

    private void setUpSounds() {
        coinSound = MediaPlayer.create(getApplicationContext(), R.raw.coin_flip_sound);
    }
}






