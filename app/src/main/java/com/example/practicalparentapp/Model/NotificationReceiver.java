package com.example.practicalparentapp.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.practicalparentapp.UI.TimeoutTimer;

import java.io.IOException;

/**
 * This class handles displaying the timer notification to the user.
 */
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
