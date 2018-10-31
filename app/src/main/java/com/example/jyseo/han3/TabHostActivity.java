package com.example.jyseo.han3;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TabHostActivity extends AppCompatActivity implements View.OnClickListener, Runnable {
    private final int L1 = 1;
    private final int L2 = 2;
    private final int L3 = 3;

    private ImageView bt_tab1;
    private ImageView bt_tab2;
    private ImageView bt_tab3;

    private Layout1 l1;
    private Layout2 l2;
    private Layout3 l3;

    private TextView f1Hartrate;
    private boolean isConnected;
    private int hartrate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_host);
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_tab1:
                // '버튼1' 클릭 시 '프래그먼트1' 호출
                callLayout(L1);
                break;

            case R.id.bt_tab2:
                // '버튼2' 클릭 시 '프래그먼트2' 호출
                callLayout(L2);
                break;

            case R.id.bt_tab3:
                //버튼3 클릭시 프레그먼트3 호출
                callLayout(L3);
        }
    }

    @Override
    public void run() {
        while (isConnected) {
            try {
                Thread.sleep(500);
                syncData();
                l1.setHartrate(hartrate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {
        isConnected = true;

        l1 = new Layout1(this);
        l2 = new Layout2(this);
        l3 = new Layout3(this);

        // 위젯에 대한 참조
        bt_tab1 = (ImageView) findViewById(R.id.bt_tab1);
        bt_tab2 = (ImageView) findViewById(R.id.bt_tab2);
        bt_tab3 = (ImageView) findViewById(R.id.bt_tab3);
        // 탭 버튼에 대한 리스너 연결
        bt_tab1.setOnClickListener(this);
        bt_tab2.setOnClickListener(this);
        bt_tab3.setOnClickListener(this);
        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callLayout(L2);

    }

    public void syncData() {
        hartrate = BluetoothService.hartrate;
    }

    private void callLayout(int layout_no) {
        switch (layout_no) {
            case 1:
                break;

            case 2:
                break;

            case 3:
                break;
        }
    }
}

