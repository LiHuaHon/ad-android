package com.namespace.ad_ble;

import static com.namespace.ad_ble.BtThread.ConnectThread.bluetoothSocket;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.namespace.ad_ble.BtThread.ConnectStateThread;
import com.namespace.ad_ble.BtThread.ConnectThread;
import com.namespace.ad_ble.BtThread.ConnectedThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {
    String driveName = "AdDrive";
    public boolean connect_state=false;
    public static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//符合UUID格式就行。
    Intent intent = null;
    //蓝牙操作
    BluetoothAdapter bluetoothAdapter = null;
    List<String> devicesNames = new ArrayList<>();
    ArrayList<BluetoothDevice> readyDevices = null;
    ArrayAdapter<String> btNames = null;
    ImageView iv_connectState=null;
    //自定义线程类的初始化
    static ConnectThread connectThread=null;
    static ConnectStateThread connectStateThread=null;
    static ConnectedThread connectedThread=null;
    private Context context;
    public interface ConnectCallback {
        void onConnectTimeOut();
        void onConnectSuccess();
    }

    private ConnectCallback connectCallback;
    public BluetoothActivity(Context mContext,ConnectCallback mConnectCallback) {
        this.context = mContext;
        this.connectCallback = mConnectCallback;
    }
    //    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adcontrol);
        iv_connectState=findViewById(R.id.iv_connectState);
        start_connect();
    }
    public void start_connect (){
        //获取本地蓝牙适配器的信息
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //先打开蓝牙
        if (!bluetoothAdapter.isEnabled()) {
            intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);//不用管，填1就好，表示打开蓝牙的
        }
        @SuppressLint("MissingPermission")
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        readyDevices = new ArrayList();
        if (pairedDevices != null && pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                readyDevices.add(device);
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                devicesNames.add(device.getName());
                btNames=new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,devicesNames);
            }
            Log.i("blueDevice",devicesNames.toString());
        }else{
            Toast.makeText(context, "没有设备已经配对！", Toast.LENGTH_SHORT).show();
        }

        int index=0;
        for (String listName : devicesNames) {
            if (listName.contains(driveName)) {
                //先判断是否有连接，我们只要一个连接，在这个软件内只允许有一个连接
                if(connectThread!=null){//如果不为空，就断开这个连接
                    connectThread.cancel();
                    connectThread=null;
                }
                //开始根据driveName连接新的设备对象
                connectThread=new ConnectThread(readyDevices.get(index));
                connectThread.start();//start（）函数开启线程，执行操作
                Log.i("blueDevice","配对列表存在"+readyDevices.get(index).getName());
                connectStateThread=new ConnectStateThread(this,bluetoothSocket);
                connectStateThread.start();//start（）函数开启线程，执行操作
                return;
            }
            index++;
        }
    }

    public void thread_connect_time_out () {
        connectCallback.onConnectTimeOut();
        Log.i("blueDevice","连接失败");

    }
    public void thread_connect_success () {
        if(bluetoothSocket!=null&&bluetoothSocket.isConnected()){//先判断连接上了
            connectedThread=new ConnectedThread(bluetoothSocket);
            connectedThread.start();
            connectCallback.onConnectSuccess();
            Log.i("blueDevice","已开启数据线程");
        }
    }
//    public void set_connect_state(int connect_model_){
//        if (connect_model_==0){
//            connect_state=true;
//            iv_connectState.setImageResource(R.drawable.connect);
////            Toast.makeText(BluetoothActivity.this, "设备连接成功", Toast.LENGTH_SHORT).show();
//        }else if (connect_model_==1){
//            connect_state=false;
//            iv_connectState.setImageResource(R.drawable.disconnect);
////            Toast.makeText(BluetoothActivity.this, "连接失败，未找到配对设备", Toast.LENGTH_SHORT).show();
//        }else if (connect_model_==2){
//            connect_state=false;
//            iv_connectState.setImageResource(R.drawable.disconnect);
////            Toast.makeText(BluetoothActivity.this, "连接超时，设备离线", Toast.LENGTH_SHORT).show();
//        }
//    }
}