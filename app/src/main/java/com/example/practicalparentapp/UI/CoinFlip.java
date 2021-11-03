package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.practicalparentapp.R;

import java.util.Objects;
import java.util.Random;

public class CoinFlip extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        setTitle("Flip Coin");

        setUpFlipBtn();
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
        flip_btn.setOnClickListener((v) -> {
            CoinFl(coinsImage);
            flipTheCoin();
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
}






