package com.example.jyseo.han3;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.lang.reflect.AccessibleObject;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class BluetoothService extends Service {
    private BluetoothSPP bluetoothSPP;
    private Activity activity;

    public static boolean isConnected;
    public static int hartrate;

    @Override
    public void onCreate() {
        super.onCreate();
        setBluetoothSPP();
        connectBluetooth();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hartrate = 0;
        isConnected = false;
        bluetoothSPP.stopService();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!bluetoothSPP.isBluetoothEnabled()) {
            startService(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        } else {
            if (!bluetoothSPP.isServiceAvailable()) {
                bluetoothSPP.setupService();
                bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    private void setBluetoothSPP() {
        bluetoothSPP = new BluetoothSPP(activity);
        if (!bluetoothSPP.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext(), "블루투스를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            activity.finish();
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
                Toast.makeText(getApplicationContext(), "블루투스 연결을 성공하였습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext(), "블루투스 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext(), "블루투스 연결을 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connectBluetooth() {

        if (bluetoothSPP.getServiceState() == BluetoothState.STATE_CONNECTED)
            bluetoothSPP.disconnect();
        else
            startService(new Intent(getApplicationContext(), DeviceList.class));
    }
}
