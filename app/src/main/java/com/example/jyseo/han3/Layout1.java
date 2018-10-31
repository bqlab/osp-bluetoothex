package com.example.jyseo.han3;

import android.app.Activity;
import android.content.Context;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Layout1 extends FrameLayout {
    private Context context;
    private TextView f1Hartrate;

    public Layout1(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.layout_layout1, this);
        this.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        f1Hartrate = findViewById(R.id.f1_hartrate);
    }

    public void setHartrate(int hartrate) {
        f1Hartrate.setText(hartrate);
    }
}
