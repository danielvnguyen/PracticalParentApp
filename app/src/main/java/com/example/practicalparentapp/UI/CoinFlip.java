package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.practicalparentapp.Model.Child;
import com.example.practicalparentapp.Model.ChildrenManager;
import com.example.practicalparentapp.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * This class handles the coin flip activity.
 * Supports history of flips, animations, sounds,
 * and children management.
 */
public class CoinFlip extends AppCompatActivity {
    private MediaPlayer coinSound;
    private ChildrenManager childrenManager;
    private TextView enterPosTV;
    private EditText enterPosOne;
    private EditText enterPosTwo;
    private Child childOne;
    private Child childTwo;
    private Child currentChild;
    private String resultOfFlip;
    private String childChoice;
    private boolean hasConfiguredChildren = true;
    private ArrayList<String> listItems;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Flip Coin");

        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listItems);
        ListView flipHistory = findViewById(R.id.flipHistory);
        flipHistory.setAdapter(adapter);
        childrenManager = ChildrenManager.getInstance(this);

        //get the input names stuff. invisible if isChildListEmpty() is true.
        enterPosTV = findViewById(R.id.enter_posTV);
        enterPosOne = findViewById(R.id.enter_pos1);
        enterPosTwo = findViewById(R.id.enter_pos2);
        Button headsBtn = findViewById(R.id.heads_btn);
        Button tailsBtn = findViewById(R.id.tails_btn);
        Button confirmBtn = findViewById(R.id.confirm_btn);
        TextView askChildChoice = findViewById(R.id.current_child);
        Button flipBtn = findViewById(R.id.flip_button);

        tailsBtn.setEnabled(false);
        headsBtn.setEnabled(false);
        flipBtn.setEnabled(false);
        headsBtn.setVisibility(View.INVISIBLE);
        tailsBtn.setVisibility(View.INVISIBLE);
        askChildChoice.setVisibility(View.INVISIBLE);

        //if no configured children, enable the coin flip button.
        if (childrenManager.isChildListEmpty()) {
            enterPosTV.setVisibility(View.INVISIBLE);
            enterPosOne.setVisibility(View.INVISIBLE);
            enterPosTwo.setVisibility(View.INVISIBLE);
            confirmBtn.setVisibility(View.INVISIBLE);
            hasConfiguredChildren = false;
            flipBtn.setEnabled(true);
        }
        //otherwise, ask parents children before flipping
        //do this in when flip button is clicked

        setUpSounds();
        setUpConfirmBtn();
        setUpFlipBtn();
        setUpHeadsBtn();
        setUpTailsBtn();
    }

    private void setUpTailsBtn() {
        Button flipBtn = findViewById(R.id.flip_button);
        Button tailsBtn = findViewById(R.id.tails_btn);
        tailsBtn.setOnClickListener((v) -> {
            childChoice = "tails";
            flipBtn.setEnabled(true);
        });
    }

    private void setUpHeadsBtn() {
        Button flipBtn = findViewById(R.id.flip_button);
        Button headsBtn = findViewById(R.id.heads_btn);
        headsBtn.setOnClickListener((v) -> {
            childChoice = "heads";
            flipBtn.setEnabled(true);
        });
    }

    //When pressed, validate the input, set up kids.
    //Then, enable the 'flip coin' button and remove
    //unnecessary UI components from the screen.
    @SuppressLint("SetTextI18n")
    private void setUpConfirmBtn() {
        Button confirmBtn = findViewById(R.id.confirm_btn);
        Button flipBtn = findViewById(R.id.flip_button);
        Button tailsBtn = findViewById(R.id.tails_btn);
        Button headsBtn = findViewById(R.id.heads_btn);
        TextView askChildChoice = findViewById(R.id.current_child);
        confirmBtn.setOnClickListener((v) -> {
            if (!hasConfiguredChildren || validateInput(enterPosOne, enterPosTwo)) {
                //set up children
                Integer currentPosOne = (Integer.parseInt(enterPosOne.getText().toString()) - 1);
                childOne = childrenManager.getChild(currentPosOne);
                Integer currentPosTwo = (Integer.parseInt(enterPosTwo.getText().toString()) - 1);
                childTwo = childrenManager.getChild(currentPosTwo);

                askChildChoice.setText(childOne.getName() +
                        ", choose heads or tails, then press \"FLIP THE COIN\"");
                childrenManager.getChild(currentPosOne).setFlippedLast(true);

                flipBtn.setEnabled(true);
                tailsBtn.setEnabled(true);
                headsBtn.setEnabled(true);
                headsBtn.setVisibility(View.VISIBLE);
                tailsBtn.setVisibility(View.VISIBLE);
                askChildChoice.setVisibility(View.VISIBLE);

                //get rid of unnecessary UI components.
                enterPosTV.setVisibility(View.INVISIBLE);
                enterPosOne.setVisibility(View.INVISIBLE);
                enterPosTwo.setVisibility(View.INVISIBLE);
                confirmBtn.setVisibility(View.INVISIBLE);
            }
            //do nothing until valid input.
        });
    }

    private boolean validateInput(EditText childOne, EditText childTwo) {
        String nameOne = childOne.getText().toString();
        String nameTwo = childTwo.getText().toString();

        //check for empty inputs
        if (nameOne.isEmpty() || nameTwo.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "You didn't finish entering the positions!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int nameOneToInt = Integer.parseInt(nameOne) - 1;
        int nameTwoToInt = Integer.parseInt(nameTwo) - 1;

        //check for invalid inputs / out of bound inputs
        if (!childrenManager.isChildExist(nameOneToInt) || !childrenManager.isChildExist(nameTwoToInt)) {
            Toast.makeText(getApplicationContext(),
                    "You input a position that doesn't correlate to a child!", Toast.LENGTH_SHORT).show();
            return false;
        }

        //check if parent input same child/position
        if (nameOneToInt == nameTwoToInt) {
            Toast.makeText(getApplicationContext(),
                    "You need to enter 2 different positions!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

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

    private String getCreationTime() {
        LocalDateTime startTime;
        startTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLL dd @ h:mma");
        return formatter.format(startTime);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CoinFlip.class);
    }

    private static final Random random = new Random();
    private ImageView coin;

    @SuppressLint("SetTextI18n")
    private void setUpFlipBtn() {
        TextView askChildChoice = findViewById(R.id.current_child);
        ImageView coinsImage = findViewById(R.id.heads);
        Button flipBtn = findViewById(R.id.flip_button);

        flipBtn.setOnClickListener((v) -> {
            setCoin(coinsImage);
            flipTheCoin();
            coinSound.start();
            String flipTime = getCreationTime();

            if (hasConfiguredChildren) {

                //edit children attributes and update text view
                if (childOne.isFlippedLast()) {
                    //2nd kids turn
                    askChildChoice.setText(childTwo.getName() +
                            ", choose heads or tails, then press \"FLIP THE COIN\"");
                    childTwo.setFlippedLast(true);
                    childOne.setFlippedLast(false);
                    currentChild = childOne;
                }
                else if (childTwo.isFlippedLast()) {
                    //1st kids turn
                    askChildChoice.setText(childOne.getName() +
                            ", choose heads or tails, then press \"FLIP THE COIN\"");
                    childOne.setFlippedLast(true);
                    childTwo.setFlippedLast(false);
                    currentChild = childTwo;
                }

                //add information to flip history
                if (resultOfFlip.equals(childChoice)) {
                    listItems.add(flipTime + " | " + currentChild.getName() + " chose " + childChoice +
                            " and the result was " + resultOfFlip + ": ✅ Won!");
                }
                else {
                    listItems.add(flipTime + " | " + currentChild.getName() + " chose " + childChoice +
                            " and the result was " + resultOfFlip + ": ❌ Lost!");
                }
                adapter.notifyDataSetChanged();

                flipBtn.setEnabled(false);
            }
        });
    }

    public void setCoin(ImageView coin) {
        this.coin = coin;
    }

    private void flipTheCoin()
    {
        int side = random.nextInt(2);
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

                System.out.println(side);
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
                fadeIn.setDuration(1000);
                fadeIn.setFillAfter(true);

                coin.startAnimation(fadeIn);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        if (side == 1) {
            resultOfFlip = "heads";
        }
        else {
            resultOfFlip = "tails";
        }
        coin.startAnimation(fadeOut);
    }

    private void setUpSounds() {
        coinSound = MediaPlayer.create(getApplicationContext(), R.raw.coin_flip_sound);
    }
}






