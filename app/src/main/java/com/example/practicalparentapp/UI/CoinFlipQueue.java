package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import com.example.practicalparentapp.Model.Child;
import com.example.practicalparentapp.Model.ChildrenManager;
import com.example.practicalparentapp.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class handles changing/displaying the coin flip queue.
 * Supports displaying queue of children, and managing
 * the order of the queue, depending on the parent.
 */
public class CoinFlipQueue extends AppCompatActivity {
    private ChildrenManager childrenManager;
    private ArrayList<String> childQueueList;
    private ArrayAdapter<String> adapter;
    private Child childOne;
    private Child childTwo;
    private EditText enterPos;
    private ArrayList<Child> childList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_queue);
        setTitle("Current Coin Flip Queue");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        childrenManager = ChildrenManager.getInstance(this);
        childList = childrenManager.getChildList();

        childQueueList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, childQueueList);
        ListView list = findViewById(R.id.coin_flip_queue_list);
        list.setAdapter(adapter);

        //need to get the positional data from CoinFlip class. PutExtra?
        enterPos = findViewById(R.id.position_to_change);
        Bundle extras = getIntent().getExtras();
        Integer positionOne = (Integer) extras.get(CoinFlip.POSITION_ONE);
        Integer positionTwo = (Integer) extras.get(CoinFlip.POSITION_TWO);
        childOne = childrenManager.getChild(positionOne);
        childTwo = childrenManager.getChild(positionTwo);

        populateQueue();
        setUpConfirmChangeBtn();
    }

    //Note: adds in bottom to top order.
    private void populateQueue() {
        childQueueList.clear();
        if (childOne.isFlippedLast()) {
            childQueueList.add(childTwo.getName());
            childQueueList.add(childOne.getName());
        }
        else if (childTwo.isFlippedLast()){
            childQueueList.add(childOne.getName());
            childQueueList.add(childTwo.getName());
        }
        //if no flip has happened yet; default order
        else {
            childQueueList.add(childOne.getName());
            childQueueList.add(childTwo.getName());
        }
        adapter.notifyDataSetChanged();
    }

    //Note: coin flip only for 2 children, so FlippedLast should work.
    //Note: positions is relevant to configured order, not queue order.
    private void setUpConfirmChangeBtn() {
        Button btn = findViewById(R.id.confirm_change_btn);
        TextView position = findViewById(R.id.position_to_change);
        btn.setOnClickListener((v) -> {
            if (validateInput(enterPos)) {
                int childPos = Integer.parseInt(position.getText().toString()) - 1;

                //Update setFlippedLast values
                for (int i = 0; i < childList.size(); i++) {
                    childrenManager.getChild(i).setFlippedLast(true);
                }

                childrenManager.getChild(childPos).setFlippedLast(false);

                Toast.makeText(getApplicationContext(), "Coin Flip Queue updated!", Toast.LENGTH_SHORT).show();

                //set OldCoinFlip to true so CoinFlip class knows.
                childrenManager.setOldCoinFlip(true);
                finish();
            }
        });
    }

    private boolean validateInput(EditText enterPos) {
        String name = enterPos.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "You didn't finish entering the position", Toast.LENGTH_SHORT).show();
            return false;
        }

        int nameToInt = Integer.parseInt(name) - 1;

        if (childrenManager.isChildExist(nameToInt)) {
            Toast.makeText(getApplicationContext(),
                    "Your position is out of bounds", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CoinFlipQueue.class);
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