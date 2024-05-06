package com.namespace.ad_ble;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
public class MainControl extends AppCompatActivity {
    Vibrator vibrator;
    int move_state=0;
    ImageView joystick_background=null;
    ImageView joystick=null;
    TextView info=null;
    private double centerPoint=300;//LinearLayout组件最中心坐标，X和Y都是300
    public static int[] wheelData=new int[]{0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_control);

        NavgationBar navgationBar = new NavgationBar(this, findViewById(android.R.id.content));
        navgationBar.replaceView(R.id.view_top);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //绑定组件id
        joystick_background= findViewById(R.id.iv_joystick_background);
        joystick=findViewById(R.id.iv_joystick);
        info=findViewById(R.id.tv_joystick_info);

        joystick_background.setAlpha(0.1f);

        //LinearLayout的触屏事件:只需要:按下，抬起，移动三个事件
        joystick_background.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //判断事件类型
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN://按下
                        vibrator.vibrate(10);

                    case MotionEvent.ACTION_MOVE://移动
                        double angle = getAngle(motionEvent.getX(), motionEvent.getY());
                        double distance = getDistance(motionEvent.getX(), motionEvent.getY());
                        if(distance > 300) {
                            // 如果距离大于300，将距离设置为300，使得中心轮盘停在边界上
                            if(move_state==0){
                                move_state=1;
                                vibrator.vibrate(50);
                            }
                            distance = 300;
                        }else{
                            move_state =0;
                        }
                        // 计算新的X和Y坐标
                        double newX = centerPoint + distance * Math.cos(Math.toRadians(angle));
                        double newY = centerPoint + distance * Math.sin(Math.toRadians(angle));
                        // 移动中心轮盘
                        joystick.setTranslationX((float)(newX - centerPoint));
                        joystick.setTranslationY((float)(newY - centerPoint));
                        // 更新TextView的文本信息
                        int mappedX = (int) (127 * (newX / 300));
                        int mappedY = (int) (127 * (newY / 300));
                        info.setText("X:"+String.valueOf((int)mappedX)
                                +"+"+"Y:"+String.valueOf((int)mappedY));
//                            wheelData[0]=mappedX;
//                            wheelData[1]=mappedY;
                        break;
                    case MotionEvent.ACTION_UP://抬起操作
                        //不要忘记，中心轮盘要归中
                        joystick.setTranslationX(0);
                        joystick.setTranslationY(0);
                        info.setText("X:"+ 127
                                +"+"+"Y:"+ 127);
                        wheelData[0]=0;
                        wheelData[1]=0;
                        vibrator.vibrate(50);
                        break;
                    default://其他操作，不做响应
                        break;
                }
                return true;//表示触摸事件已处理，则可以进行移动的响应
            }
        });

    }
    //获得轮盘的角度
    private double getAngle(double X,double Y){
        double angle=0;
        //根据反三角函数来转化坐标成为角度，arctan()函数
        angle=Math.atan2(Y-centerPoint,X-centerPoint);//都是与中心坐标而言
        //上面是转化为弧度制，接下来转化为角度值
        angle=Math.toDegrees(angle);
        return angle;
    }
    //获得轮盘的移动距离
    private double getDistance(double X,double Y){
        double distance=0;
        //根据两点之间的坐标来得到距离值
        distance=Math.sqrt(Math.pow(X-centerPoint,2)+Math.pow(Y-centerPoint,2));
        return distance;
    }

}