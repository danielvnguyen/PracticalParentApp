package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.practicalparentapp.R;

public class CoinFlip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        setTitle("Flip A Coin");
    }
}