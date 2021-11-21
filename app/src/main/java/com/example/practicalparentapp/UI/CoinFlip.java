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
    private static final Random random = new Random();
    private ImageView coin;
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
    public static final String POSITION_ONE = "Child one";
    public static final String POSITION_TWO = "Child two";
    private ArrayList<Child> childList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Flip Coin");

        childrenManager = ChildrenManager.getInstance(this);
        childList = childrenManager.getChildList();

        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listItems);
        ListView flipHistory = findViewById(R.id.flipHistory);
        flipHistory.setAdapter(adapter);

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

        setUpSounds();
        setUpConfirmBtn();
        setUpFlipBtn();
        setUpHeadsBtn();
        setUpTailsBtn();
        setUpChangeChildBtn();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        if (childrenManager.isOldCoinFlip()) {
            /*
            - get textview and update it + currentChild.
            - go through childList and see which child is 'false'.
             */
            TextView askChildChoice = findViewById(R.id.current_child);
            for (int i = 0; i < childList.size(); i++) {
                if (!childrenManager.getChild(i).isFlippedLast()) {
                    currentChild = childrenManager.getChild(i);
                }
            }
            askChildChoice.setText(currentChild.getName() +
                    ", choose heads or tails, then press \"FLIP THE COIN\"");
        }
    }

    private void setUpChangeChildBtn() {
        Button btn = findViewById(R.id.change_child_btn);
        btn.setOnClickListener((v) -> {
            Intent intent = CoinFlipQueue.makeIntent(this);
            intent.putExtra(POSITION_ONE, Integer.parseInt(enterPosOne.getText().toString()) - 1);
            intent.putExtra(POSITION_TWO, Integer.parseInt(enterPosTwo.getText().toString()) - 1);
            startActivity(intent);
        });
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

    @SuppressLint("SetTextI18n")
    private void setUpConfirmBtn() {
        Button confirmBtn = findViewById(R.id.confirm_btn);
        Button tailsBtn = findViewById(R.id.tails_btn);
        Button headsBtn = findViewById(R.id.heads_btn);
        Button changeBtn = findViewById(R.id.change_child_btn);
        TextView askChildChoice = findViewById(R.id.current_child);
        confirmBtn.setOnClickListener((v) -> {
            if (!hasConfiguredChildren || validateInput(enterPosOne, enterPosTwo)) {

                Integer currentPosOne = (Integer.parseInt(enterPosOne.getText().toString()) - 1);
                childOne = childrenManager.getChild(currentPosOne);
                Integer currentPosTwo = (Integer.parseInt(enterPosTwo.getText().toString()) - 1);
                childTwo = childrenManager.getChild(currentPosTwo);

                askChildChoice.setText(childOne.getName() +
                        ", choose heads or tails, then press \"FLIP THE COIN\"");
                childrenManager.getChild(currentPosOne).setFlippedLast(false);
                currentChild = childOne;

                tailsBtn.setEnabled(true);
                headsBtn.setEnabled(true);
                headsBtn.setVisibility(View.VISIBLE);
                tailsBtn.setVisibility(View.VISIBLE);
                askChildChoice.setVisibility(View.VISIBLE);
                changeBtn.setVisibility(View.VISIBLE);
                

                enterPosTV.setVisibility(View.INVISIBLE);
                enterPosOne.setVisibility(View.INVISIBLE);
                enterPosTwo.setVisibility(View.INVISIBLE);
                confirmBtn.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean validateInput(EditText childOne, EditText childTwo) {
        String nameOne = childOne.getText().toString();
        String nameTwo = childTwo.getText().toString();

        if (nameOne.isEmpty() || nameTwo.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "You didn't finish entering the positions!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int nameOneToInt = Integer.parseInt(nameOne) - 1;
        int nameTwoToInt = Integer.parseInt(nameTwo) - 1;

        if (childrenManager.isChildExist(nameOneToInt) || childrenManager.isChildExist(nameTwoToInt)) {
            Toast.makeText(getApplicationContext(),
                    "You input a position that doesn't correlate to a child!", Toast.LENGTH_SHORT).show();
            return false;
        }

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
                currentChild.setFlippedLast(true);

                if (resultOfFlip.equals(childChoice)) {
                    listItems.add(flipTime + " | " + currentChild.getName() + " chose " + childChoice +
                            " and the result was " + resultOfFlip + ": ✅ Won!");
                }
                else {
                    listItems.add(flipTime + " | " + currentChild.getName() + " chose " + childChoice +
                            " and the result was " + resultOfFlip + ": ❌ Lost!");
                }
                adapter.notifyDataSetChanged();

                //if child one just flipped
                if (childOne == currentChild) {
                    askChildChoice.setText(childTwo.getName() +
                            ", choose heads or tails, then press \"FLIP THE COIN\"");
                    childTwo.setFlippedLast(false);
                    childOne.setFlippedLast(true);
                    currentChild = childTwo;
                }
                else if (childTwo == currentChild) {
                    askChildChoice.setText(childOne.getName() +
                            ", choose heads or tails, then press \"FLIP THE COIN\"");
                    childOne.setFlippedLast(false);
                    childTwo.setFlippedLast(true);
                    currentChild = childOne;
                }

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






