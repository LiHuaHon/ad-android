package com.namespace.ad_ble;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class NavgationBar {
    Context context;
    View rootView;
    public NavgationBar(Context mContext, View rootView) {
        this.context = mContext;
        this.rootView = rootView;
    }
    public void replaceView(int oldViewId) {
        // 获取 LayoutInflater 实例
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 使用 inflater 加载新的布局
        View newView = inflater.inflate(R.layout.item_navigation_bar, null);
        // 获取要替换的 view
        View oldView = rootView.findViewById(oldViewId);
        // 获取 oldView 的父布局
        ViewGroup parent = (ViewGroup) oldView.getParent();
        // 获取 oldView 在父布局中的位置
        int index = parent.indexOfChild(oldView);
        // 将 oldView 的布局参数复制到 newView
        newView.setLayoutParams(oldView.getLayoutParams());
        // 从父布局中移除 oldView
        parent.removeView(oldView);
        // 将新的 view 添加到父布局中 oldView 原来的位置
        parent.addView(newView, index);
        // 设置 Button 和 Spinner 的点击事件
        setClickEvents(newView);
    }

    private void setClickEvents(View view) {
        Button button = view.findViewById(R.id.btn_connect_state);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理 Button 的点击事件
            }
        });
        Spinner spinner = view.findViewById(R.id.s_go_toMain);
        // 创建一个 ArrayAdapter 使用 spinner_map.xml 中的字符串数组
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.main_array, android.R.layout.simple_spinner_item);
        // 指定当下拉列表弹出时，显示的布局样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将 ArrayAdapter 添加到 Spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 在这里处理 Spinner 的选项选择事件
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 在这里处理 Spinner 没有选项被选择的情况
            }
        });
    }
}