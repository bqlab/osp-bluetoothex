package com.example.jyseo.han3;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TabHostActivity extends AppCompatActivity implements View.OnClickListener, Runnable {
    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;
    private final int FRAGMENT3 = 3;

    private ImageView bt_tab1;
    private ImageView bt_tab2;
    private ImageView bt_tab3;

    private Fragment1 f1;
    private Fragment2 f2;
    private Fragment3 f3;

    private TextView f1Hartrate;

    private BluetoothService bluetoothService;
    private boolean isConnected;
    private int hartrate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_host);
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_tab1:
                // '버튼1' 클릭 시 '프래그먼트1' 호출
                callFragment(FRAGMENT1);
                break;

            case R.id.bt_tab2:
                // '버튼2' 클릭 시 '프래그먼트2' 호출
                callFragment(FRAGMENT2);
                break;

            case R.id.bt_tab3:
                //버튼3 클릭시 프레그먼트3 호출
                callFragment(FRAGMENT3);
        }
    }

    @Override
    public void run() {
        while (isConnected) {
            try {
                Thread.sleep(500);
                syncData();
                f1.setHartrate(hartrate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {
        isConnected = true;
        bluetoothService.setActivity(this);

        f1 = new Fragment1();
        f2 = new Fragment2();
        f3 = new Fragment3();

        // 위젯에 대한 참조
        bt_tab1 = (ImageView) findViewById(R.id.bt_tab1);
        bt_tab2 = (ImageView) findViewById(R.id.bt_tab2);
        bt_tab3 = (ImageView) findViewById(R.id.bt_tab3);
        // 탭 버튼에 대한 리스너 연결
        bt_tab1.setOnClickListener(this);
        bt_tab2.setOnClickListener(this);
        bt_tab3.setOnClickListener(this);
        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(FRAGMENT2);

    }

    public void syncData() {
        hartrate = BluetoothService.hartrate;
    }

    private void callFragment(int frament_no) {
        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            case 1:
                // '프래그먼트1' 호출
                transaction.replace(R.id.fragment_container, f1);
                transaction.commit();
                break;

            case 2:
                // '프래그먼트2' 호출
                transaction.replace(R.id.fragment_container, f2);
                transaction.commit();
                break;

            case 3:
                //프레그먼트3 호출
                transaction.replace(R.id.fragment_container, f3);
                transaction.commit();
                break;
        }
    }
}

