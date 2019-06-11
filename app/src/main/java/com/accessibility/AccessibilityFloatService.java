package com.accessibility;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class AccessibilityFloatService extends Service {
    private Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
