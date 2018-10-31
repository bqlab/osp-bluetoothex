package com.example.jyseo.han3;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class Layout2 extends FrameLayout {
    private Context context;

    public Layout2(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.layout_layout2, this);
        this.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
}
