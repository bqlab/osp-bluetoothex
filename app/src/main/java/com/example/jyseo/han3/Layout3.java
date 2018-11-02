package com.example.jyseo.han3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;


public class Layout3 extends FrameLayout {
    Context context;

    Button btn_logout;
    TextView l3ID, l3NM, l3PH, l3AD;
    String id;

    public Layout3(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.layout_layout3, this);

        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();

            }
        });

        l3ID = findViewById(R.id.l3_id);
        l3NM = findViewById(R.id.l3_nm);
        l3PH = findViewById(R.id.l3_ph);
        l3AD = findViewById(R.id.l3_ad);
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("로그아웃");
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setUserData(String id, String nm, String ph, String ad) {
        l3ID.setText(id);
        l3NM.setText(nm);
        l3PH.setText(ph);
        l3AD.setText(ad);
    }
}
