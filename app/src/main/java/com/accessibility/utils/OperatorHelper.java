package com.accessibility.utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
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
    private AccessibilityService service;
    private ArrayList<AppInfo> appList = null;
    private int curAppIndex = 0;
    private AppInfo curApp = null;
    public boolean isRunning = false;
    private Timer timer;
    private TimerTask timerTask;
    private int runningCount = 0;
    private int maxRunningCount = 15;
    private long TIMER_CHECK_INTERVAL = 1000;
    private int curStatus = Constant.StatusOpeningApp;
    private String curType = "article";
    public int winWidth = 500;
    public int winHeight = 1500;
    private long appRunStartTime = 0; // 时间戳毫秒
    private int maxAppRunTime = 1000*60*10; // 十分钟
    private int maxLoopCount = 0; // 默认0为一直运行
    private int curLoopCount = 0;
    private OperatorHelper instance;

    public OperatorHelper() {
        instance = this;
    }

    public void start(final AccessibilityService service, AccessibilityEvent event) {
        if (isRunning) {
            Toast.makeText(service, "服务正在运行中", Toast.LENGTH_SHORT).show();
            return;
        }

        Context appContext = service.getApplicationContext();
        appList = Constant.getAppList(appContext);
        curApp = appList.get(curAppIndex);
        maxAppRunTime = (int)SPUtil.get(appContext, Constant.AppRunMinuteCount, new Integer(0))*60*1000;
        maxLoopCount = (int)SPUtil.get(appContext, Constant.LoopCount, new Integer(0));
        curLoopCount = 0;
        Log.d("@@@ start", "start run operate");
        this.service = service;
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
                            curApp.doSomething(instance);

                            if (runningCount == 0) { // 滑动
                                scrollScreen(winWidth/2, winHeight/5*4, winWidth/2, winHeight/5);
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
                        case Constant.FindSohuRedPackageInList:
                            if (runningCount == 0) { // 滑动
                                scrollScreen(winWidth/2, winHeight/5*3, winWidth/2, winHeight/5);
                                runningCount++;
                            }

                            if (runningCount >= maxRunningCount) {
                                if(curApp.doSomething(instance)) {
                                    changeStatusToList();
                                } else {
                                    runningCount = 0;  // 继续跳
                                    return;
                                }
                            }
                            break;
                        case Constant.StatusInReadingArticle:
                            curApp.doSomethingInDetailPage(instance);
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
                            curApp.doSomethingInDetailPage(instance);
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
                                appRunStartTime = System.currentTimeMillis();
                                Util.startActivity(curApp, service);
                                runningCount++;
                                return;
                            }

                            if (runningCount >= maxRunningCount) {
                                curStatus = Constant.StatusInList;
                                runningCount = 0;
                                maxRunningCount = 4;
                                return;
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                runningCount++;
                // 判断app生命周期
                if((appRunStartTime > 0) && (System.currentTimeMillis() - appRunStartTime > maxAppRunTime)) {
                    curAppIndex++;
                    if(curAppIndex >= appList.size()) {
                        curAppIndex = 0;
                        curLoopCount++;
                        // 检测最大循环次数
                        // 0默认无限循环
                        if(maxRunningCount > 0 && curLoopCount >= maxRunningCount) {
                            stop();
                            backToSystemHome();
                            return;
                        }
                    }
                    // 获取当前app信息
                    curApp = appList.get(curAppIndex);
                    appRunStartTime = 0;
                    runningCount = 0;
                    maxRunningCount = 15;
                    curStatus = Constant.StatusOpeningApp;
                    Log.d("@@@ 更换app", ""+curAppIndex);
                    // 强行退出
                    backToSystemHome();
                }
            }
        };

        timer.schedule(timerTask, 0, TIMER_CHECK_INTERVAL);
    }

    public void backToSystemHome() {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        // #TODO 聚看点，需要单独处理退出逻辑
    }

    public void stop() {
        freeTimeTask();
        this.isRunning = false;
    }

    public void initDataBackToList() {
        this.runningCount = 0;
        this.maxRunningCount = 4;
        this.curStatus = Constant.StatusInList;
    }

    public void getWindowSize() {
        if(null == service) {
            return;
        }

        try {
            WindowManager manager = (WindowManager)service.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
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
        Path path=new Path();
        path.moveTo(fromX, fromY);
        path.lineTo(toX, toY);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription  gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 50L, 350L, false))
                .build();
        service.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
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
        AppInfo curApp = appList.get(curAppIndex);
        AccessibilityNodeInfo nodeInfo = curApp.getArticleSpecialViewById(instance);
        if(null != nodeInfo) {
            curType = "article";
        } else {
            nodeInfo = curApp.getVideoSpecialViewById(instance);
            if(null != nodeInfo) {
                curType = "video";
            }
        }

        Log.d("@@@@ clickToDetailPage", ""+nodeInfo.isClickable());
        return performClickActionByNode(nodeInfo);
    }

    public boolean performClickActionByNode(AccessibilityNodeInfo node) {
        if(null != node) {
            return node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        return false;
    }

    public boolean performClickActionByNodeListFirstChild(List<AccessibilityNodeInfo> nodeList) {
        if(nodeList.size() > 0) {
            return nodeList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        return false;
    }

    public boolean performClickActionByNodeListFirstChildParent(List<AccessibilityNodeInfo> nodeList) {
        if(nodeList.size() > 0) {
            return nodeList.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        return false;
    }

    public void backToPreviousActivity() {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    public AccessibilityNodeInfo getRootNodeInfo() {
        AccessibilityNodeInfo nodeInfo = null;
        nodeInfo = service.getRootInActiveWindow();

        return nodeInfo;
    }

    public void changeStatusToList() {
        this.curStatus = Constant.StatusInList;
        this.runningCount = 0;
        this.maxRunningCount = 8;
    }

    public void changeStatusToFindSohuRedPackageInList() {
        this.curStatus = Constant.FindSohuRedPackageInList;
        this.runningCount = 0;
        this.maxRunningCount = 5;
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
