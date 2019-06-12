package com.accessibility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class AccessibilityStopActivity extends Activity {
    private Timer timer;
    private TimerTask timerTask;
    private final int TIMER_CHECK_INTERVAL = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_stop);
        Log.d("@@@@", "AccessibilityStopActivity created");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        finish();
//
//        timer = new Timer();
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                finish();
//            }
//        };
//        timer.schedule(timerTask, 0, TIMER_CHECK_INTERVAL);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("@@@@", "AccessibilityStopActivity  onDestroy");

        if(null != timer) {
            timer.cancel();
        }
        if(null != timerTask) {
            timerTask.cancel();
        }
    }
}
