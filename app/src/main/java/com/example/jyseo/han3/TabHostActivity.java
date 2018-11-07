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
    private boolean isConnected = false;
    private BluetoothSPP bluetoothSPP;

    public int hartrate = 80;
    public String id, nm, ph, ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_host);
        init();
        setBluetoothSPP();
        connectBluetooth();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isConnected = false;
    }

    @Override
    public void onClick(View v) {
        if (!isConnected)
            connectDevice();
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
                Thread.sleep(1000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkHartrate();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {
        new BluetoothConnector(this);

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

    public void setBluetoothSPP() {
        bluetoothSPP = new BluetoothSPP(this);
        if (!bluetoothSPP.isBluetoothAvailable()) {
            Toast.makeText(this, "블루투스를 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
        bluetoothSPP.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                hartrate = Integer.parseInt(message);
            }
        });
        bluetoothSPP.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            @Override
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(TabHostActivity.this, "디바이스와 연결되었습니다.", Toast.LENGTH_SHORT).show();
                new Thread(TabHostActivity.this).start();
                isConnected = true;
            }

            @Override
            public void onDeviceDisconnected() {
                Toast.makeText(TabHostActivity.this, "디바이스와의 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show();
                connectDevice();
            }

            @Override
            public void onDeviceConnectionFailed() {
                Toast.makeText(TabHostActivity.this, "디바이스와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
                connectDevice();
            }
        });
    }

    public void connectBluetooth() {
        try {
            if (!bluetoothSPP.isBluetoothEnabled()) {//핸드폰에서 블루투스가 꺼져 있다면
                Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(i, BluetoothState.REQUEST_ENABLE_BT);
            } else if (!bluetoothSPP.isServiceAvailable()) {
                bluetoothSPP.setupService();
                bluetoothSPP.startService(BluetoothState.DEVICE_OTHER); //안드로이드 기기가 아닌 아두이노 같은 장치도 연결이 가능하게 서비스 설정
                connectDevice();
            } else if (!isConnected) {
                connectDevice(); //디바이스 연결이 안 되었으면 연결시도
            }
        } catch (Exception e){
            Toast.makeText(this, "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_LONG).show();
            finishAffinity();
        }
    }

    public void connectDevice() {
        startActivityForResult(new Intent(getApplicationContext(), DeviceList.class), BluetoothState.REQUEST_CONNECT_DEVICE);
        Toast.makeText(this, "연결할 디바이스를 선택하세요.", Toast.LENGTH_LONG).show();
    }

    public void checkHartrate() {
        l1.setHartrate(Integer.toString(hartrate));
        if ((hartrate < 40 || hartrate > 140) && !isNoticed) {
            String oneonenine = "000"; //이 부분 119로 바꾸시면 돼요
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(ph, null, "디바이스가 40을 초과하거나 140 마만의 심박수를 감지했습니다.", null, null);
            smsManager.sendTextMessage(oneonenine, null, "디바이스가 40을 초과하거나 140 마만의 심박수를 감지했습니다.", null, null);
            startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + oneonenine)));
            startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ph)));
            isNoticed = true;
        } else if (!(hartrate < 40 || hartrate > 140) && isNoticed)
            isNoticed = false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK) {
                bluetoothSPP.connect(data);
            }
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetoothSPP.setupService();
                bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
                Toast.makeText(this, "블루투스가 활성화되었습니다.", Toast.LENGTH_SHORT).show();
                connectDevice();
            } else {
                Toast.makeText(this, "블루투스가 비활성화되어 있습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}

