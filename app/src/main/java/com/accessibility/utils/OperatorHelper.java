package com.accessibility.utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OperatorHelper {
    private static final String TAG = OperatorHelper.class.getSimpleName();
    private AccessibilityService mService;
    private AccessibilityEvent mEvent;
    private Context mContext;
    private ArrayList<AppInfo> mAppList = Constant.getAppList();
    private int mCurAppIndex = 0;
    public boolean isRunning = false;
    private Timer timer;
    private TimerTask timerTask;
    private int runningCount = 0;
    private int maxRunningCount = 8;
    private long TIMER_CHECK_INTERVAL = 1000;
    private int curStatus = Constant.StatusOpeningApp;
    private String curType = "article";
    private int winWidth = 500;
    private int winHeight = 1500;
    private long mAppRunStartTime = 0; // 时间戳毫秒
    private long MaxAppRunTime = 1000*60*1; // 十分钟
    private OperatorHelper mInstance;
    private final int AppLength = mAppList.size();

    public OperatorHelper() {
        mInstance = this;
    }

    public void start(AccessibilityService service, AccessibilityEvent event) {
        if (isRunning) {
            Toast.makeText(mContext, "服务正在运行中", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("@@@ start", "start run operate");
        this.mService = service;
        this.mContext = service;
        this.mEvent = event;
        getWindowSize();
        isRunning = true;
        timer = new Timer();
        timerTask = new TimerTask() {
            @SuppressWarnings("static-access")
            @Override
            public void run() {
                if (!isRunning) {
                    freeTimeTask();
                    return;
                }

                getWindowSize();
                try {
                    switch (curStatus) {
                        case Constant.StatusInList:
                            mAppList.get(mCurAppIndex).doSomething(mInstance);

                            if (runningCount == 0) { // 滑动
                                scrollScreen(winWidth/2, winHeight/5, winWidth/2, winHeight/5*3);
                                runningCount++;
                            }
                            if (runningCount >= maxRunningCount) { // 等待且识别点击
                                boolean result = clickToDetailPage();
                                if(!result) {
                                    runningCount = 0;
                                    return;
                                }

                                if("article".equals(curType)) {
                                    curStatus = Constant.StatusInReadingArticle;
                                } else {
                                    curStatus = Constant.StatusInReadingVideo;
                                }
                                runningCount = 0;
                                maxRunningCount = 40;
                            }
                            break;
                        case Constant.StatusInReadingArticle:
                            if(runningCount % 5 == 0) {
                                scrollScreen(winWidth/2, winHeight-100, winWidth/2, winHeight/3);
                            }
                            // 滚动且监听查看更多
                            if (runningCount > maxRunningCount) { // 退回列表
                                backToPreviousActivity();
                                initDataBackToList();
                                return;
                            }
                            break;
                        case Constant.StatusInReadingVideo: // 回到列表
                            if (runningCount > maxRunningCount) { // 退回列表
                                backToPreviousActivity();
                                initDataBackToList();
                                return;
                            }
                            break;
                        case Constant.StatusWaiting:
                            break;
                        case Constant.StatusOpeningApp: // 等待什么都不做
                            if (runningCount == 0) {
                                mAppRunStartTime = System.currentTimeMillis();
                                AppInfo appInfo = mAppList.get(mCurAppIndex);
                                Util.startActivity(appInfo, mContext);
                                runningCount++;
                                return;
                            }

                            if (runningCount >= maxRunningCount) {
                                curStatus = Constant.StatusInList;
                                runningCount = 0;
                                maxRunningCount = 8;
                                return;
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                runningCount++;
                // 判断app生命周期
                if((mAppRunStartTime > 0) && (System.currentTimeMillis() - mAppRunStartTime > MaxAppRunTime)) {
                    mCurAppIndex++;
                    if(mCurAppIndex >= AppLength) {
                        mCurAppIndex = 0;
                    }
                    mAppRunStartTime = 0;
                    runningCount = 0;
                    maxRunningCount = 8;
                    curStatus = Constant.StatusOpeningApp;
                    Log.d("@@@ 更换app", ""+mCurAppIndex);
                    // 强行退出
                    mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                }
            }
        };

        timer.schedule(timerTask, 0, TIMER_CHECK_INTERVAL);
    }

    public void stop() {
        freeTimeTask();
        this.isRunning = false;
    }

    public void initDataBackToList() {
        this.runningCount = 0;
        this.maxRunningCount = 8;
        this.curStatus = Constant.StatusInList;
    }

    public void getWindowSize() {
        if(null == mService) {
            return;
        }

        try {
            WindowManager manager = (WindowManager)mService.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            winHeight = display.getHeight();
            winWidth = display.getWidth();
        } catch(Exception e) {
            Log.d("@@@", "getWindowSize error:"+e.getMessage());
            e.printStackTrace();
        }
    }

    public void freeTimeTask() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public boolean scrollScreen(float fromX, float fromY, float toX, float toY) {
        Log.d("@@@", "scrollScreen");
        Path path=new Path();
        path.moveTo(fromX, fromY);
        path.lineTo(toX, toY);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription  gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 50L, 350L, false))
                .build();
        mService.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }
        }, new Handler(Looper.getMainLooper()));

        return true;
    }

    public boolean clickToDetailPage() {
        AppInfo curApp = mAppList.get(mCurAppIndex);
        List<AccessibilityNodeInfo> nodeInfoList = findNodesById(curApp.getArticleSpecialViewId());
        AccessibilityNodeInfo nodeInfo = null;
        if(null != nodeInfoList && nodeInfoList.size() > 0) {
            nodeInfo = nodeInfoList.get(0).getParent();
            curType = "article";
        } else {
            nodeInfoList = findNodesById(curApp.getVideoSpecialViewId());
            if(null != nodeInfoList && nodeInfoList.size() > 0) {
                nodeInfo = nodeInfoList.get(0).getParent();
                curType = "video";
            }
        }

        if(null != nodeInfo) {
            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        return false;
    }

    public void backToPreviousActivity() {
        mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    public AccessibilityNodeInfo getRootNodeInfo() {
        AccessibilityNodeInfo nodeInfo = null;
        nodeInfo = mService.getRootInActiveWindow();

        return nodeInfo;
    }

    public List<AccessibilityNodeInfo> findNodesById(String viewId) {
        AccessibilityNodeInfo nodeInfo = getRootNodeInfo();
        if (nodeInfo != null) {
            return nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
        }

        Log.d("@@@", "未获取到当前root node info");
        return null;
    }

    public List<AccessibilityNodeInfo> findNodesByText(String text) {
        AccessibilityNodeInfo nodeInfo = getRootNodeInfo();
        if (nodeInfo != null) {
            return nodeInfo.findAccessibilityNodeInfosByText(text);
        }
        return null;
    }
}
