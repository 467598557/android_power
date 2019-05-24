package com.accessibility.utils;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

public class Util {
    private static final String TAG ="Util" ;

    public static boolean startActivity(AppInfo appInfo, Context context) {
        try {
            Log.d("@@@ startActivity", appInfo.packageName+":"+appInfo.startComponent);
//            Intent intent = new Intent();
//            intent.setAction("Android.intent.action.VIEW");
//            intent.setClassName(appInfo.packageName,
//                    appInfo.startComponent);
//            context.startActivity(intent);


            Intent intent = new Intent(Intent.ACTION_MAIN);
            /**知道要跳转应用的包命与目标Activity*/
            intent.setPackage(context.getPackageName());
            ComponentName componentName = new ComponentName("com.jifen.qukan", "com.jifen.qkbase.main.MainActivity");
            intent.setComponent(componentName);
            context.startActivity(intent);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean openActivity(AppInfo appInfo) {
        try {
            Log.d("@@@ startActivity", appInfo.packageName+":"+appInfo.startComponent);
//            Intent intent = new Intent();
//            intent.setAction("Android.intent.action.VIEW");
//            intent.setClassName(appInfo.packageName,
//                    appInfo.startComponent);
//            context.startActivity(intent);


            Intent intent = new Intent(Intent.ACTION_MAIN);
/**知道要跳转应用的包命与目标Activity*/
//            intent.setPackage(context.getPackageName());
//            Log.d(TAG, "startActivity: "+context.getPackageName());
            ComponentName componentName = new ComponentName("com.jifen.qukan", "com.jifen.qkbase.main.MainActivity");
            intent.setComponent(componentName);
//            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
