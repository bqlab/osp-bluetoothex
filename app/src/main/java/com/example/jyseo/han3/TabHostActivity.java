package com.example.jyseo.han3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class TabHostActivity extends AppCompatActivity implements View.OnClickListener, Runnable, TimePickerDialog.OnTimeSetListener {
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

    public int hart = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_host);
        Log.d("오늘 요일은?", String.valueOf(Calendar.DAY_OF_WEEK));
        init();
        setBluetoothSPP();
        connectBluetooth();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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


    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, i);
        c.set(Calendar.MINUTE, i1);
        c.set(Calendar.SECOND, 0);

        l2.updateTimeText(c);
        l2.startAlarm(c);
    }

    public void init() {
        isConnected = true;
        new Thread(this).start();

        bluetoothSPP = new BluetoothSPP(this);

        fc = findViewById(R.id.fragment_container);

        bt_tab1 = (ImageView) findViewById(R.id.bt_tab1);
        bt_tab2 = (ImageView) findViewById(R.id.bt_tab2);
        bt_tab3 = (ImageView) findViewById(R.id.bt_tab3);

        bt_tab1.setOnClickListener(this);
        bt_tab2.setOnClickListener(this);
        bt_tab3.setOnClickListener(this);

        l1 = new Layout1(this);
        l2 = new Layout2(this);
        l3 = new Layout3(this);

        fc.addView(l1);
        fc.addView(l2);
        fc.addView(l3);
        callLayout(L1);

        l2.setActivity(this);
        l2.layout2Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment timePicker = new AlarmTimePicker();
                timePicker.show(getSupportFragmentManager(), "타임피커");
            }
        });
        l2.layout2Week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeekLayout w = new WeekLayout(TabHostActivity.this);
                new AlertDialog.Builder(TabHostActivity.this)
                        .setTitle("알람을 활성화 할 요일을 선택하세요.")
                        .setView(w)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        NotifyService.id = getIntent().getStringExtra("id");
        NotifyService.nm = "서지영"; //이름 바꾸는 부분, 회원가입에 항목이 없어서 이렇게 했습니다.
        NotifyService.ph = getSharedPreferences("phs", MODE_PRIVATE).getString(NotifyService.id, "none");
        NotifyService.ad = getSharedPreferences("ads", MODE_PRIVATE).getString(NotifyService.id, "none");
        l3.setUserData(NotifyService.id, NotifyService.nm, NotifyService.ph, NotifyService.ad);
    }

    private void callLayout(int layout_no) {
        switch (layout_no) {
            case 1:
                l1.setVisibility(View.VISIBLE);
                l2.setVisibility(View.GONE);
                l3.setVisibility(View.GONE);
                break;
            case 2:
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.VISIBLE);
                l3.setVisibility(View.GONE);
                break;
            case 3:
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.GONE);
                l3.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setBluetoothSPP() {
        if (!bluetoothSPP.isBluetoothAvailable()) {
            Toast.makeText(this, "블루투스를 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
        bluetoothSPP.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                hart = Integer.parseInt(message);
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
    }

    public void connectDevice() {
        startActivityForResult(new Intent(getApplicationContext(), DeviceList.class), BluetoothState.REQUEST_CONNECT_DEVICE);
        Toast.makeText(this, "연결할 디바이스를 선택하세요.", Toast.LENGTH_LONG).show();
    }

    public void checkHartrate() {
        l1.setHartrate(Integer.toString(hart));
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

