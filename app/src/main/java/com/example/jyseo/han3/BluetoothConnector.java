package com.example.jyseo.han3;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class BluetoothConnector extends Service implements Runnable{

    public static int hart = 0;
    public static boolean isHart = false;
    public static boolean isbuzzed = false;
    public static boolean isConnected = false;

    BluetoothSPP bluetoothSPP;
    Activity activity;

    public BluetoothConnector() {

    }

    public BluetoothConnector(final Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        setBluetoothSPP();
        connectBluetooth();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        while (isConnected) {
            try {
                Thread.sleep(1000);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {
        if (!isConnected)
            connectDevice();
    }

    public void setBluetoothSPP() {
        bluetoothSPP = new BluetoothSPP(this);
        if (!bluetoothSPP.isBluetoothAvailable()) {
            Toast.makeText(activity, "블루투스를 사용할 수 없습니다.", Toast.LENGTH_LONG).show();

        }
        bluetoothSPP.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                int r = Integer.parseInt(message);

                if (r == 254)
                    isHart = true;
                else if (r == 255)
                    isHart = false;

                if (isHart)
                    hart = r;
                else
                    isbuzzed = true;

            }
        });
        bluetoothSPP.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            @Override
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(activity, "디바이스와 연결되었습니다.", Toast.LENGTH_SHORT).show();
                new Thread(BluetoothConnector.this).start();
                BluetoothConnector.isConnected = true;
            }

            @Override
            public void onDeviceDisconnected() {
                Toast.makeText(activity, "디바이스와의 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show();
                connectDevice();
            }

            @Override
            public void onDeviceConnectionFailed() {
                Toast.makeText(activity, "디바이스와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
                connectDevice();
            }
        });
    }

    public void connectBluetooth() {
        if (!bluetoothSPP.isBluetoothEnabled()) {//핸드폰에서 블루투스가 꺼져 있다면
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(i, BluetoothState.REQUEST_ENABLE_BT);
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
                activity.finish();
            }
        }
    }
}
