package com.example.jyseo.han3;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothState;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_SIGNUP = 101;
    public static final int REQUEST_CODE_LOGIN = 102;

    EditText mainInId, mainInPw;
    TextView signupTextview;
    Button loginBtn;
    String id, pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        requestPermission();
    }

    public void init() {
        signupTextview = (TextView) findViewById(R.id.signupText);
        signupTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SIGNUP);
            }
        });

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSharedPreferences("ids", MODE_PRIVATE).getString("sibal","").equals("")) {}
                else {
                    Intent i = new Intent(getApplicationContext(), TabHostActivity.class);
                    i.putExtra("id", id);
                    startActivityForResult(i, REQUEST_CODE_LOGIN);
                }

            }
        });
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
    }
}
