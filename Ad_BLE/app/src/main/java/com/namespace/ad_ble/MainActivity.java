package com.namespace.ad_ble;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    boolean retry_is_legal=false;
    boolean is_connect=false;
    BluetoothActivity bluetoothManager=null;
    Button btn_retry=null;
    TextView tv_show=null;
    ImageView iv_state=null;
    ProgressBar pb_wait=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_show=findViewById(R.id.tv_show);
        iv_state=findViewById(R.id.iv_state);
        pb_wait=findViewById(R.id.pb_wait);
        btn_retry =findViewById(R.id.btn_retry);
        try_connect();//尝试一次连接
        btn_retry.setOnClickListener(this);
    }

    void try_connect(){
        pb_wait.setVisibility(View.VISIBLE);
        iv_state.setVisibility(View.GONE);
        tv_show.setText("正在尝试连接至AdDrive");
        tv_show.setTextColor(0xff000000);
        retry_is_legal = false;
        // 创建 BluetoothActivity 的实例，并传入接口的实现
        BluetoothActivity bluetoothActivity = new BluetoothActivity(this,new BluetoothActivity.ConnectCallback() {
            @Override
            public void onConnectTimeOut() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb_wait.setVisibility(View.GONE);
                        iv_state.setVisibility(View.VISIBLE);
                        iv_state.setImageResource(R.drawable.ui_close);
                        tv_show.setTextColor(0xeff35252);
                        tv_show.setText("连接失败,请重试");
                        retry_is_legal=true;
                    }
                });
            }
            @Override
            public void onConnectSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb_wait.setVisibility(View.GONE);
                        iv_state.setVisibility(View.VISIBLE);
                        iv_state.setImageResource(R.drawable.ui_check);
                        tv_show.setTextColor(0xff40cc7b);
                        tv_show.setText("准备就绪");
                        is_connect=true;
                    }
                });
            }
        });
        bluetoothActivity.start_connect();
    }

    @Override
    public void onClick(View v) {
        if(retry_is_legal){
            try_connect();
        }else if(is_connect){
            Intent intent = new Intent(MainActivity.this, MainControl.class);
            startActivity(intent);
            finish();
        }

    }
}