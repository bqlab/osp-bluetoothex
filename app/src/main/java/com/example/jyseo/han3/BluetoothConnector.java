package com.example.jyseo.han3;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class BluetoothConnector extends Service implements Runnable{

    public static int hartrate;
    public static boolean buzz;

    BluetoothSPP bluetoothSPP;
    boolean isConnected;

    Activity activity;

    public BluetoothConnector() {

    }

    public BluetoothConnector(final Activity activity) {
        this.activity = activity;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,"take me home", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {

    }

    public void setBluetoothSPP() throws Throwable {
        bluetoothSPP = new BluetoothSPP(this);
        if (!bluetoothSPP.isBluetoothAvailable()) {
            Toast.makeText(this, "블루투스를 사용할 수 없습니다.", Toast.LENGTH_LONG).show();

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
                Toast.makeText(BluetoothConnector.this, "디바이스와 연결되었습니다.", Toast.LENGTH_SHORT).show();
                new Thread(BluetoothConnector.this).start();
                isConnected = true;
            }

            @Override
            public void onDeviceDisconnected() {
                Toast.makeText(BluetoothConnector.this, "디바이스와의 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show();
                connectDevice();
            }

            @Override
            public void onDeviceConnectionFailed() {
                Toast.makeText(BluetoothConnector.this, "디바이스와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
                connectDevice();
            }
        });
    }

    public void connectBluetooth() {
        if (!bluetoothSPP.isBluetoothEnabled()) {//핸드폰에서 블루투스가 꺼져 있다면
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((TabHostActivity)getApplicationContext()).startActivityForResult(i, BluetoothState.REQUEST_ENABLE_BT);
        } else if (!bluetoothSPP.isServiceAvailable()) {
            bluetoothSPP.setupService();
            bluetoothSPP.startService(BluetoothState.DEVICE_OTHER); //안드로이드 기기가 아닌 아두이노 같은 장치도 연결이 가능하게 서비스 설정
            connectDevice();
        } else if (!isConnected) {
            connectDevice(); //디바이스 연결이 안 되었으면 연결시도
        }
    }

    public void connectDevice() {
        activity.startActivityForResult(new Intent(getApplicationContext(), DeviceList.class), BluetoothState.REQUEST_CONNECT_DEVICE);
        Toast.makeText(this, "연결할 디바이스를 선택하세요.", Toast.LENGTH_LONG).show();
    }
}
