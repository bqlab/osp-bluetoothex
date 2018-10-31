package com.example.jyseo.han3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Fragment1 extends Fragment {
    private TextView f1Hartrate;
    private int hartrate;

    public Fragment1() {
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment1, container, false);
    }

    public void init() {
        f1Hartrate = getActivity().findViewById(R.id.f1_hartrate);
    }

    public void setHartrate(int hartrate) {
        this.hartrate = hartrate;
        f1Hartrate.setText(hartrate);
    }
}
