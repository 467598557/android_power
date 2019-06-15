package com.accessibility.utils;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.accessibility.AccessibilityStartActivity;
import com.accessibility.AccessibilityStopActivity;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class Util {
    private static final String TAG = "Util";

    public static boolean startActivity(AppInfo appInfo, Context context) {
        try {
            Log.d("@@@ startActivity", appInfo.packageName + ":" + appInfo.startComponent);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            /**知道要跳转应用的包命与目标Activity*/
            intent.setPackage(context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName componentName = new ComponentName(appInfo.packageName, appInfo.startComponent);
            intent.setComponent(componentName);
            context.startActivity(intent);

            return true;
        } catch (Exception e) {
            Log.d("@@@@", "start Activity error");
            e.printStackTrace();
            return false;
        }
    }

    public static void startBeginTaskActivity(Context context) {
        Intent intent = new Intent(context,  AccessibilityStartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void stopTaskActivity(Context context) {
        Intent intent = new Intent(context,  AccessibilityStopActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void execShellCmd(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            Log.d("@@@@ execShellCmd fail", t.getMessage());
            t.printStackTrace();
        }
    }
}
