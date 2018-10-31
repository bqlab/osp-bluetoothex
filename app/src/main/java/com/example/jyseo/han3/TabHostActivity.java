package com.example.jyseo.han3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TabHostActivity extends AppCompatActivity implements View.OnClickListener, Runnable {
    private final int L1 = 1;
    private final int L2 = 2;
    private final int L3 = 3;

    private FrameLayout fc;
    private ImageView bt_tab1;
    private ImageView bt_tab2;
    private ImageView bt_tab3;

    private Layout1 l1;
    private Layout2 l2;
    private Layout3 l3;

    private boolean isNoticed = false;
    private String phone = "01062040454";
    private String message = "테스트 오지게 박습니다 행님!";

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
        while (BluetoothService.isConnected) {
            try {
                Thread.sleep(1000);
                l1.setHartrate(BluetoothService.hartrate);
                checkHartrate(BluetoothService.hartrate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {
        l1 = new Layout1(this);
        l2 = new Layout2(this);
        l3 = new Layout3(this);

        // 위젯에 대한 참조
        fc = findViewById(R.id.fragment_container);
        bt_tab1 = (ImageView) findViewById(R.id.bt_tab1);
        bt_tab2 = (ImageView) findViewById(R.id.bt_tab2);
        bt_tab3 = (ImageView) findViewById(R.id.bt_tab3);

        // 탭 버튼에 대한 리스너 연결
        bt_tab1.setOnClickListener(this);
        bt_tab2.setOnClickListener(this);
        bt_tab3.setOnClickListener(this);

        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callLayout(L2);
        startService();
    }

    private void callLayout(int layout_no) {
        fc.removeAllViews();
        switch (layout_no) {
            case 1:
                fc.addView(l1);
                break;
            case 2:
                fc.addView(l2);
                break;

            case 3:
                fc.addView(l3);
                break;
        }
    }

    public void checkHartrate(int hartrate) {
        if ((hartrate < 40 || hartrate > 140) && !isNoticed) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, message, null, null);
        } else if (!(hartrate < 40 || hartrate > 140) && isNoticed)
            isNoticed = false;
    }
}

