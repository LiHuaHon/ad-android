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
    boolean retry_is_legal=false;//重新连接否合法 = 非法
    boolean is_connect=false;//是否已经连接= 未连接
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
            public void onConnectTimeOut() {//连接超时
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb_wait.setVisibility(View.GONE);//等待圆圈关闭显示
                        iv_state.setVisibility(View.VISIBLE);//显示图像
                        iv_state.setImageResource(R.drawable.ui_close); //改变图像显示为 错误
                        tv_show.setTextColor(0xeff35252); //设置字体颜色
                        tv_show.setText("连接失败,请重试"); //设置文字信息
                        retry_is_legal=true; //改变 重新连接否合法 = 合法
                    }
                });
            }
            @Override
            public void onConnectSuccess() {//连接成功
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb_wait.setVisibility(View.GONE);//等待圆圈关闭显示
                        iv_state.setVisibility(View.VISIBLE);//显示图像
                        iv_state.setImageResource(R.drawable.ui_check);//改变图像显示为 正确
                        tv_show.setTextColor(0xff40cc7b);//设置字体颜色
                        tv_show.setText("准备就绪");//设置文字信息
                        is_connect=true;//改变 是否连接否 = 已连接
                    }
                });
            }
        });
        bluetoothActivity.start_connect();
    }

    @Override
    public void onClick(View v) {
        if(retry_is_legal){//连接失败
            try_connect();
        }else if(is_connect){//连接成功
            Intent intent = new Intent(MainActivity.this, MainControl.class);
            startActivity(intent);
            finish();
        }

    }
}