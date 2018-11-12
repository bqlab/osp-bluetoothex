package com.example.jyseo.han3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class WeekLayout extends FrameLayout {

    Button sun, mon, tue, wed, thu, fri, sat;

    public WeekLayout(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        setLayoutInflater();

        sun = findViewById(R.id.week_sun);
        mon = findViewById(R.id.week_mon);
        tue = findViewById(R.id.week_tue);
        wed = findViewById(R.id.week_wed);
        thu = findViewById(R.id.week_thu);
        fri = findViewById(R.id.week_fri);
        sat = findViewById(R.id.week_sat);

        setWeekButton(sun, "sun");
        setWeekButton(mon, "mon");
        setWeekButton(tue, "tue");
        setWeekButton(wed, "wed");
        setWeekButton(thu, "thu");
        setWeekButton(fri, "fri");
        setWeekButton(sat, "sat");

    }

    public void setLayoutInflater() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_week, this);
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void setWeekButton(final Button button, final String key) {
        final boolean b = getContext().getSharedPreferences("week", Context.MODE_PRIVATE).getBoolean(key, true);
        if (b)
            button.setTextColor(getResources().getColor(R.color.colorAccent));
        else
            button.setTextColor(getResources().getColor(R.color.colorBlack));

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b)
                    button.setTextColor(getResources().getColor(R.color.colorBlack));
                else
                    button.setTextColor(getResources().getColor(R.color.colorAccent));

                getContext().getSharedPreferences("week", Context.MODE_PRIVATE).edit().putBoolean(key, !b).apply();
            }
        });
    }
}
