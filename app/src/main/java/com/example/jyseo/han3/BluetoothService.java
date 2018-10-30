package com.example.jyseo.han3;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;

import java.util.logging.Handler;

public class BluetoothService {
    private BluetoothAdapter bluetoothAdapter;
    private Activity activity;
    private Handler handler;

    public BluetoothService(Activity activity, Handler handler) {
        this.activity = activity;
        this.handler = handler;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void connectBluetooth() {
        if(bluetoothAdapter != null) {
            if(bluetoothAdapter.isEnabled()) {
                
            } else {
                activity.startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            }
        } else
            Log.d("BluetoothService", "Bluetooth isn't available.");
    }
}
