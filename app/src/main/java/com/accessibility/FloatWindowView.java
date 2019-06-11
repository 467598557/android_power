package com.accessibility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.accessibility.utils.OperatorHelper;

public class FloatWindowView extends LinearLayout {

    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;

    public FloatWindowView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window, this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        Button close = (Button) findViewById(R.id.close);
        Button stopServiceBtn = (Button) findViewById(R.id.stop_service);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
                FloatWindowManager.removeBigWindow(context);
                Intent intent = new Intent(getContext(), AccessibilityFloatService.class);
                context.stopService(intent);
            }
        });
        stopServiceBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击返回的时候，移除大悬浮窗，创建小悬浮窗
//                FloatWindowManager.removeBigWindow(context);
                Log.d("@@@@", "sxxxx"+OperatorHelper.getInstance());
            }
        });
    }
}