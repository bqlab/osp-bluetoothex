package com.example.jyseo.han3;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    Activity activity;

    TextView layout2Title;
    Button layout2Time, layout2Week;

    public Layout2(Context context) {
        super(context);
    }

    public Layout2(Context context, Activity activity) {
        super(context);
        init(context, activity);
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

    public void init(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

        layout2Title = findViewById(R.id.layout2_title);
        layout2Time = findViewById(R.id.layout2_time);
        layout2Week = findViewById(R.id.layout2_week);

        layout2Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new AlarmTimePicker();
                timePicker.show(Layout2.this.activity.getFragmentManager(), "타임피커");
            }
        });

        LayoutInflater.from(getContext()).inflate(R.layout.layout_layout2, this);
        this.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void updateTimeText(Calendar c) {
        String s = "알람이 설정되었습니다. -> ";
        s += SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(c);
        layout2Title.setText(s);
    }

    public void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
    }

}
