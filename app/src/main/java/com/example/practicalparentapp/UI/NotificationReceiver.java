package com.example.practicalparentapp.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
