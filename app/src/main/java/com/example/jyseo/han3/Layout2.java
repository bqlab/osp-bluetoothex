package com.example.jyseo.han3;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Layout2 extends LinearLayout implements TimePickerDialog.OnTimeSetListener {
    Context context;
    FragmentActivity activity;

    TextView layout2Title;
    Button layout2Time, layout2Week;

    public Layout2(Context context) {
        super(context);
        init();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, i);
        c.set(Calendar.MINUTE, i1);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        startAlarm(c);
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_layout2, this);
        this.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        layout2Title = findViewById(R.id.layout2_title);
        layout2Time = findViewById(R.id.layout2_time);
        layout2Week = findViewById(R.id.layout2_week);

        layout2Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new AlarmTimePicker();
                timePicker.show(Layout2.this.activity.getSupportFragmentManager(), "타임피커");
            }
        });
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public void updateTimeText(Calendar c) {
        String s = "알람이 설정되었습니다. -> ";
        s += SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(c.getTime());
        layout2Title.setText(s);
    }

    public void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(activity, AlarmReceiver.class);
        PendingIntent p = PendingIntent.getBroadcast(activity, 1, i, 0);
        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), p);
    }

    public void canceAlarm () {
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(activity, AlarmReceiver.class);
        PendingIntent p = PendingIntent.getBroadcast(activity, 1, i, 0);
        assert alarmManager != null;
        alarmManager.cancel(p);
        layout2Title.setText("알람이 취소되었습니다.");
    }
}
