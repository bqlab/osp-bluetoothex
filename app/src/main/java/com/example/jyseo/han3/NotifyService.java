package com.example.jyseo.han3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;

import static com.example.jyseo.han3.App.CHANNEL_ID;

public class NotifyService extends Service {

    public static int data = 0, hart = 0;
    public static String flag = "hart";

    public static boolean buzz = false;
    public static boolean isBuzzed = false;
    public static boolean isNoticed = false;
    public static boolean isConnected = false;

    public static String id, nm, ph, ad;

    NotificationManager notificationManager;
    NotificationChannel notificationChannel;

    @Override
    public void onCreate() {
        super.onCreate();
        checkAndroidVersion();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String content = intent.getStringExtra("content");
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent p = PendingIntent.getActivity(this, 0, i, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(p)
                .build();
        startForeground(1, notification);
        //알림서비스를 이용하기 위한 기본세팅

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isConnected) {
                        Thread.sleep(1000); //1초에 한번씩 반복하게 만듦
                        checkData();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return START_NOT_STICKY;
    }

    public void checkData() {
        if (data == 255)
            flag = "hart";
        else if (data == 254)
            flag = "buzz";
        else {
            if (flag.equals("hart")) {
                hart = data;
                if ((hart < 40 || hart > 140) && !isNoticed) {
                    String s = "디바이스가 40을 초과하거나 140 마만의 심박수를 감지했습니다. 사용자의 주소는 " + ad + "입니다.";
                    emergencyNotify(s);
                    isNoticed = true;
                } else if (!(hart < 40 || hart > 140) && isNoticed)
                    isNoticed = false;
            } else if (flag.equals("buzz")) {
                if (buzz && !isBuzzed) {
                    buzz = false;
                    isBuzzed = true;
                    String s = "디바이스의 부저 버튼이 눌렸습니다. 사용자의 주소는 " + ad + "입니다.";
                    emergencyNotify(s);
                }
                if (!buzz && isBuzzed)
                    isBuzzed = false;
            }
        }
    }

    public void checkAndroidVersion() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("em", "긴급알림", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("긴급한 상황을 알립니다.");
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }//안드로이드 8.0 이상일 경우에는 이렇게 노티피케이션 채널을 만들어야 함
    }

    public void emergencyNotify(String content) { //알림을 만드는 함수
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.notify(0, new NotificationCompat.Builder(this, "em")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(content)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build());
        } else {
            notificationManager.notify(0, new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(content)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .build());
        }

        String oneonenine = "000"; //이 부분 119로 바꾸시면 돼요
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(ph, null, content, null, null);
        smsManager.sendTextMessage(oneonenine, null, content, null, null);
        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ph)));
    }
}
