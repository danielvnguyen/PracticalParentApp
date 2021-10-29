package com.example.practicalparentapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.practicalparentapp.R;

import java.util.Random;

public class MainMenu extends AppCompatActivity {


    // private static final Random random= new Random();
    // private ImageView coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpConfigChildrenBtn();

        setUpCoinFlipBtn();

        setUpTimeoutTimerBtn();
    }

    private void setUpTimeoutTimerBtn() {
        Button timeOutBtn = findViewById(R.id.timeoutBtn);
        timeOutBtn.setOnClickListener((v) -> {
            Intent intent = TimeoutTimer.makeIntent(this);
            startActivity(intent);
        } );
    }

    public void setUpCoinFlipBtn() {
        Button flip_btn = findViewById(R.id.flipCoinBtn);
        // ImageView coin = (ImageView) findViewById(R.id.coin);
        //flip_btn.setOnClickListener(new View.OnClickListener()
        flip_btn.setOnClickListener((v) -> {
            Intent intent = CoinFlip.makeIntent(this);
            startActivity(intent);
        } );
    }



    private void setUpConfigChildrenBtn() {
        Button btn = findViewById(R.id.configureChildrenBtn);
        btn.setOnClickListener((v2) -> {
            Intent intent2 = ConfigureChildren.makeIntent(this);
            startActivity(intent2);



    /*private void flipCoin()
    {
        Animation fadeOut=new AlphaAnimation(1,0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);
        fadeOut.setFillAfter(true);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
               // coin.setImageResource(RANDOM.nextFloat()>0.5f?R.drawable.tails:R.drawable.heads);

                int side=random.nextInt(2);

                if(side==1)
                {
                    coin.setImageResource(R.drawable.heads);
                    Toast.makeText(getApplicationContext(),"Heads", Toast.LENGTH_SHORT).show();
                }
                else if(side==0)
                {
                    coin.setImageResource(R.drawable.tails);
                    Toast.makeText(getApplicationContext(),"Tails", Toast.LENGTH_SHORT).show();

                }
                Animation fadeIn=new AlphaAnimation(0,1);
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


      /*  int side=random.nextInt(2);

        if(side==1)
        {
            coin.setImageResource(R.drawable.heads);
            Toast.makeText(getApplicationContext(),"Heads", Toast.LENGTH_SHORT).show();
        }
        else if(side==0)
        {
            coin.setImageResource(R.drawable.tails);
            Toast.makeText(getApplicationContext(),"Tails", Toast.LENGTH_SHORT).show();

        }*/
        });
    }
}