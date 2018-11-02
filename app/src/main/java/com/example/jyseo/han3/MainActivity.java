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

    EditText mainId, mainPw;
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
        loginBtn = (Button) findViewById(R.id.loginBtn);
        mainId = findViewById(R.id.main_id);
        mainPw = findViewById(R.id.main_pw);

        signupTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SIGNUP);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputData();
            }
        });
    }

    public void checkInputData() {
        id = mainId.getText().toString();
        pw = mainPw.getText().toString();

        if (id.equals("") || pw.equals("")) {
            Toast.makeText(this, "빈 칸을 모두 채워야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (getSharedPreferences("ids", MODE_PRIVATE).getString(id, "none").equals("none")) {
            Toast.makeText(this, "해당 아이디가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!getSharedPreferences("ids", MODE_PRIVATE).getString(id, "none").equals(pw)) {
            Toast.makeText(this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(getApplicationContext(), TabHostActivity.class);
        i.putExtra("id", id);
        startActivityForResult(i, REQUEST_CODE_LOGIN);
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);
        }
    }
}
