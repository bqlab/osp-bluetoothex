package com.example.jyseo.han3;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

class NotificationHelper extends ContextWrapper {
    public static final String chanelID = "al";
    public static final String channelName = "알람";

    private NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            createChannel();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel n = new NotificationChannel(chanelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        n.enableLights(true);
        n.enableVibration(true);
        n.setLightColor(R.color.colorPrimary);
        n.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getNotificationManager().createNotificationChannel(n);
    }

    public NotificationManager getNotificationManager() {
        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        return new NotificationCompat.Builder(getApplicationContext(), chanelID)
                .setContentTitle("알람")
                .setContentText("약을 복용할 시간입니다.")
                .setSmallIcon(R.mipmap.ic_launcher_round);
    }
}
