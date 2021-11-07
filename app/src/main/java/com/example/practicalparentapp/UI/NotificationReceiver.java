package com.example.practicalparentapp.UI;

import static android.content.Context.VIBRATOR_SERVICE;

import static com.example.practicalparentapp.R.*;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.widget.Toast;

import com.example.practicalparentapp.R;

import java.io.IOException;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        TimeoutTimer.getVibrator().cancel();
        TimeoutTimer.getAlarm().stop();
        try {
            TimeoutTimer.getAlarm().prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
