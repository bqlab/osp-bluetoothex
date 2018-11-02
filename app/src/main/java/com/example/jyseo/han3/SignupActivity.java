package com.example.jyseo.han3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    EditText signupId;
    EditText signupPw;
    EditText signupRpw;
    EditText signupPhone;
    EditText signupAddress;

    Button signupDone;
    ImageView arrow;

    String id, pw, phone, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
    }

    private void init() {
        arrow = findViewById(R.id.arrow);
        signupId = findViewById(R.id.signup_id);
        signupPw = findViewById(R.id.signup_pw);
        signupRpw = findViewById(R.id.signup_rpw);
        signupPhone = findViewById(R.id.signup_phone);
        signupAddress = findViewById(R.id.signup_address);
        signupDone = findViewById(R.id.signup_done);

        arrow.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        signupDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

    private void checkUserData() {
        id = signupId.getText().toString();
        pw = signupPw.getText().toString();
        phone = signupPhone.getText().toString();
        address = signupAddress.getText().toString();

        if (!id.equals("") || !pw.equals("") || !phone.equals("") || !address.equals("")) {
            Toast.makeText(this, "빈 칸을 모두 채워야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pw.equals(signupRpw.getText().toString())) {
            Toast.makeText(this,"비밀번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        getSharedPreferences("ids", MODE_PRIVATE).g
    }
}
