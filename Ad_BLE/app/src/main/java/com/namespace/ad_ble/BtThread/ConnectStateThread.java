package com.namespace.ad_ble.BtThread;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import com.namespace.ad_ble.BluetoothActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//连接蓝牙设备的操作线程类
public class ConnectStateThread extends Thread{
    BluetoothActivity mbluetoothActivity=null;
    BluetoothSocket mbluetoothSocket;
    public ConnectStateThread(BluetoothActivity bluetoothActivity,BluetoothSocket bluetoothSocket){
        mbluetoothActivity=bluetoothActivity;
        mbluetoothSocket=bluetoothSocket;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void run() {
        int time_out=5000;//单位 ms
        int time=0;
        super.run();
        if (mbluetoothActivity==null)
            return;
        while (true){
            if(time++>time_out){
                mbluetoothActivity.thread_connect_time_out();
                return;
            }else if(mbluetoothSocket.isConnected()){
                mbluetoothActivity.thread_connect_success();
                return;
            };
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
