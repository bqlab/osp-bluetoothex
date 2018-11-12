package com.example.jyseo.han3;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        String[] week = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};
        String today = week[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        if (context.getSharedPreferences("week", Context.MODE_PRIVATE).getBoolean(today, true)) {
            NotificationHelper notificationHelper = new NotificationHelper(context);
            NotificationCompat.Builder b = notificationHelper.getChannelNotification();
            notificationHelper.getNotificationManager().notify(1, b.build());
        }
    }
}
