package com.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import com.accessibility.utils.OperatorHelper;

public class AccessibilityMainService extends AccessibilityService {
    private static  final String TAG = AccessibilityMainService.class.getSimpleName();
    private OperatorHelper operatorHelper = new OperatorHelper();

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d("@@@", "onServiceConnected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        int eventType = event.getEventType();
        CharSequence classNameChr = event.getClassName();
        String className = classNameChr.toString();
        if(!packageName.equals("com.accessibility")) {
            operatorHelper.check(packageName, className);
            return;
        }

        Log.d("@@@@", "onAccessibilityEvent: "+packageName+":"+className);
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if(className.equals("com.accessibility.AccessibilityStartActivity")) {
                    Log.d("@@@ start run job", packageName + ":" + eventType+":"+className);
                    operatorHelper.start(this, event);
                } else if (className.equals("com.accessibility.AccessibilityStopActivity")) {
                    Log.d("@@@ stop run job", packageName + ":" + eventType+":"+className);
                    operatorHelper.stop();
                }
                break;
        }
    }

    @Override
    public void onInterrupt() {
        Log.d("@@@", "onInterrupt");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("@@@", "onDestroy");
    }
}
