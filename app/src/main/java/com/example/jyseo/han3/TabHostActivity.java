package com.example.jyseo.han3;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class TabHostActivity extends AppCompatActivity implements View.OnClickListener{
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
    private boolean isConnected = false;
    private BluetoothSPP bluetoothSPP;

    public int hartrate = 80;
    public String id, nm, ph, ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_host);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isConnected = false;
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

    public void init() {
        startService(new Intent(this, BluetoothConnector.class));

        l1 = new Layout1(this);
        l2 = new Layout2(this);
        l3 = new Layout3(this);

        id = getIntent().getStringExtra("id");
        nm = "서지영"; //이름 바꾸는 부분, 회원가입에 항목이 없어서 이렇게 했습니다.
        ph = getSharedPreferences("phs", MODE_PRIVATE).getString(id, "none");
        ad = getSharedPreferences("ads", MODE_PRIVATE).getString(id, "none");
        l3.setUserData(id, nm, ph, ad);

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
        callLayout(L1);
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


















}

