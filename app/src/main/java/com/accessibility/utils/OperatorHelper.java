package com.accessibility.utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.graphics.Path;
import android.os.Build;
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
    public int runningCount = 0;
    public int maxRunningCount = 15;
    private long TIMER_CHECK_INTERVAL = 1500;
    private int curStatus = Constant.StatusOpeningApp;
    private String curType = "article";
    public int winWidth = 500;
    public int winHeight = 1500;
    private long appRunStartTime = 0; // 时间戳毫秒
    private long maxAppRunTime = 1000 * 60 * 10; // 十分钟
    private long baseMaxAppRunTime = maxAppRunTime;
    private int maxLoopCount = 0; // 默认0为一直运行
    public int curLoopCount = 0;
    public boolean isJumpVideo = false;
    private static OperatorHelper instance;
    private static boolean isInitData = false;

    public OperatorHelper() {
        instance = this;
    }

    public static OperatorHelper getInstance() {
        return instance;
    }

    public void start(final AccessibilityService service, AccessibilityEvent event) {
        if (isRunning) {
            Toast.makeText(service, "服务正在运行中", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if(!isInitData) {
                Context appContext = service.getApplicationContext();
                appList = Constant.getAppList(appContext);
                maxAppRunTime = baseMaxAppRunTime = (int) SPUtil.get(appContext, Constant.AppRunMinuteCount, new Integer(0)) * 60 * 1000;
                curAppIndex = (int) SPUtil.get(appContext, Constant.AppBeginRunIndex, new Integer(0));
                maxLoopCount = (int) SPUtil.get(appContext, Constant.LoopCount, new Integer(0));
                isJumpVideo = (boolean)SPUtil.get(appContext, Constant.AppJumpVideo, new Boolean(true));
                if (curAppIndex >= appList.size()) {
                    curAppIndex = appList.size() - 1;
                }
                curApp = appList.get(curAppIndex);
                curLoopCount = 0;
                getWindowSize();
                appRunStartTime = System.currentTimeMillis();

                isInitData = true;
            }

            this.service = service;
            isRunning = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        freeTimeTask();
        timer = new Timer();
        timerTask = new TimerTask() {
            @SuppressWarnings("static-access")
            @Override
            public void run() {
                if (!isRunning) {
                    freeTimeTask();
                    return;
                }

                Log.d("@@@@", "run ----"+curStatus+":"+curAppIndex+":"+appRunStartTime +":"+  runningCount + ":"+maxRunningCount);
                try {
                    getWindowSize();
                    String curClassName = "";
                    AccessibilityNodeInfo rootNode = getRootNodeInfo();
                    if (null == rootNode) {
                        if(curStatus != Constant.StatusOpeningApp) {
                            backToPreviewWindow();
                        }
                        Log.d("@@@@", "no root");
                        return;
                    } else {
                        // 可能有异常跳出
                        String curPackage = rootNode.getPackageName().toString();
                        if (curStatus != Constant.StatusOpeningApp && appRunStartTime != 0 && !curPackage.equals(curApp.packageName)) {
                            Log.d("@@@@", "可能有异常跳出："+curPackage);
                            backToPreviewWindow();
                            changeStatusToOpenningApp(false);
                            return;
                        }
                    }
                    if(rootNode.findAccessibilityNodeInfosByViewId("android:id/button1").size() > 0) {
                        List<AccessibilityNodeInfo> nodeList = rootNode.findAccessibilityNodeInfosByViewId("miui:id/message");
                        if(nodeList.size() > 0) {
                            AccessibilityNodeInfo node = nodeList.get(0);
                            if(node.getText().toString().indexOf("已停止运行") > 0) {
                                performClickActionByNode(rootNode.findAccessibilityNodeInfosByViewId("android:id/button1").get(0));
                                return;
                            }
                        }
                    }
                    switch (curStatus) {
                        case Constant.StatusInList:
                            try {
                                curApp.doSomething(instance);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (runningCount == 0) { // 滑动
//                                scrollScreen(winWidth / 4, winHeight / 5 * 3, winWidth / 4, (float)(winHeight / 5 * 1.5));
                                scrollScreen(18, winHeight / 5 * 3, 18, (float)(winHeight / 5 * 1.5));
                            }
                            if (runningCount >= maxRunningCount) { // 等待且识别点击
                                boolean result = clickToDetailPage();
                                if (!result) {
                                    runningCount = 0;
                                    return;
                                }

                                if ("article".equals(curType)) {
                                    curStatus = Constant.StatusInReadingArticle;
                                } else {
                                    curStatus = Constant.StatusInReadingVideo;
                                }
                                runningCount = 0;
                                maxRunningCount = 25 + (int)Math.round(Math.random()*15);
                            }

                            judgeAppRunLoop(false);
                            break;
                        case Constant.StatusSignIn:
                            // 往上滑动，以免存在时差页面被滑下
                            if (runningCount <= 2) {
                                scrollScreen(winWidth / 4, winHeight / 5, winWidth / 4, winHeight / 5 * 4);
                            }

                            curApp.signin(instance);
                            if (runningCount >= maxRunningCount) {
                                // 签到
                                changeStatusToList();
                                return;
                            }
                            break;
                        case Constant.FindSohuRedPackageInList:
                            if (runningCount == 0) { // 滑动
//                                scrollScreen(winWidth / 3, winHeight / 5 * 4, winWidth / 3, winHeight / 5);
                                scrollScreen(18, winHeight / 5 * 4, 18, (   float)(winHeight / 5*2.5));
                            }

                            if (runningCount >= maxRunningCount) {
                                if (curApp.doSomething(instance)) {
                                    changeStatusToList();
                                    return;
                                } else {
                                    runningCount = 0;  // 继续跳
                                    return;
                                }
                            }
                            break;
                        case Constant.StatusInReadingArticle:
                            try {
                                curApp.doSomethingInDetailPage(instance);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (runningCount % 3 == 0) {
//                                scrollScreen(winWidth / 3, winHeight / 5 * 4, winWidth / 3, (float)(winHeight / 5*2.5));
                                scrollScreen(18, winHeight / 5 * 4, 18, (float)(winHeight / 5*2.5));
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
                            if (runningCount >= maxRunningCount) {
                                changeStatusToOpenningApp(true);
                                return;
                            }
                            break;
                        case Constant.StatusInCloseJuKanDianApp:
                            if (runningCount >= maxRunningCount) {
                                performClickActionByNodeListFirstChild(getRootNodeInfo().findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/sure_quit"));
                                changeStatusToWaiting();
                                return;
                            }
                            break;
                            case Constant.StatusInCloseYueTouTiaoApp:
                                if (runningCount >= maxRunningCount) {
                                    performClickActionByNodeListFirstChild(getRootNodeInfo().findAccessibilityNodeInfosByViewId("com.expflow.reading:id/tv_logout"));
                                    changeStatusToWaiting();
                                    return;
                                }
                                break;
                        case Constant.StatusInBackToMainActivity:
                            if (runningCount % 2 == 0) {
                                backToPreviewWindow();
                            } else {
                                curClassName = getRootNodeInfo().getClassName().toString();
                                if (curClassName.equals(curApp.mainComponent)) {
                                    changeStatusToList();
                                    return;
                                }
                            }

                            if (runningCount >= maxRunningCount) {
                                changeStatusToList();
                                return;
                            }
                            break;
                        case Constant.StatusOpeningApp: // 等待什么都不做
                            if (runningCount == 0) {
                                if(!Util.startActivity(curApp, service)) {
                                    judgeAppRunLoop(true);
                                    return;
                                }

                                runningCount++;
                                return;
                            }

                            curApp.doSomethingInOpeningApp(instance);
                            if (runningCount >= maxRunningCount) {
                                changeStatusToList();
                                maxRunningCount = 4;
                                return;
                            }
                            break;
                    }
                } catch (Exception e) {
//                    Log.d("@@@@ ", "main loop error:" + e.getMessage().toString());
                    e.printStackTrace();
                }

                runningCount++;
            }
        };

        timer.schedule(timerTask, 0, TIMER_CHECK_INTERVAL);
    }

    public void judgeAppRunLoop(boolean force) {
        // 判断app生命周期
        if (force || (appRunStartTime > 0) && (System.currentTimeMillis() - appRunStartTime > maxAppRunTime)) {
            curAppIndex++;
            maxAppRunTime = baseMaxAppRunTime + (long)Math.round(Math.random()*5)*1000*60; // 时间随机，再加五分钟以内
            if (curAppIndex >= appList.size()) {
                curAppIndex = 0;
                curLoopCount++;
                // 检测最大循环次数
                // 0默认无限循环
                if (maxLoopCount > 0 && curLoopCount >= maxLoopCount) {
                    stop();
                    backToSystemHome();
                    return;
                }
            }
            // 获取当前app信息
            curApp = appList.get(curAppIndex);
            appRunStartTime = 0;
            changeStatusToWaiting();
            maxRunningCount = 5;
            // 强行退出
            backToSystemHome();
        }
    }

    public void backToPreviewWindow() {
        Log.d("@@@@", "backToPreviewWindow");
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    public void backToSystemHome() {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        if (curApp.packageName.equals("com.xiangzi.jukandian")) {
            curStatus = Constant.StatusInCloseJuKanDianApp;
        } else if(curApp.packageName.equals("com.expflow.reading")) {
            curStatus = Constant.StatusInCloseYueTouTiaoApp;
        } else {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }

    public void stop() {
        freeTimeTask();
        this.isRunning = false;
    }

    public void check(String packageName, String className, AccessibilityService service) {
//        Log.d("@@@@ check", packageName+":"+className+":"+curStatus+":"+isRunning);
        if(!isRunning) {
            return;
        }

        this.service = service;
        switch (curStatus) {
            case Constant.StatusInReadingArticle:
            case Constant.StatusInReadingVideo:
//                Log.d("@@@@ check", "" + (className.equals("android.app.Dialog") || className.equals("android.view.View")));
                if(runningCount > 5 && (className.equals("android.app.Dialog") || className.equals("android.view.View"))) {
                    Log.d("@@@@", "check dialog and back up："+packageName+":"+className);
//                    Log.d("@@@@", "保存图片:"+":"+findNodesById("com.huolea.bull:id/id_dialog_save_pic_btn").size());
                    if(findNodesById("com.huolea.bull:id/id_dialog_save_pic_btn").size() > 0) {
                        backToPreviewWindow();                    }
                }
                // 十秒之后再检测
                if(runningCount == 10) {
                    if(curApp.articleComponent.equals("") && curApp.videoComponent.equals("")) {
                        break;
                    }
                    if(!curApp.articleComponent.equals(className) && !curApp.videoComponent.equals(className)) {
                        backToPreviewWindow();
                    }
                }
                break;
            case Constant.StatusInList:
                break;
        }
    }

    public void initDataBackToList() {
        this.runningCount = 0;
        this.maxRunningCount = 4;
        this.curStatus = Constant.StatusInList;
    }

    public void getWindowSize() {
        if (null == service) {
            return;
        }

        try {
            WindowManager manager = (WindowManager) service.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            winHeight = display.getHeight();
            winWidth = display.getWidth();
        } catch (Exception e) {
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
        try {
            if (Build.VERSION.SDK_INT < 26) {
                Util.execShellCmd("input swipe " + fromX + " " + fromY + " " + toX + " " + toY +" 80");
            } else {
                Path path = new Path();
                path.moveTo(fromX, fromY);
                path.lineTo(toX, toY);
                GestureDescription.Builder builder = new GestureDescription.Builder();
                GestureDescription gestureDescription = builder
                        .addStroke(new GestureDescription.StrokeDescription(path, 50L, 350L, false))
                        .build();
                service.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
                    @Override
                    public void onCompleted(GestureDescription gestureDescription) {
                        super.onCompleted(gestureDescription);
                    }

                    public void onCancelled(GestureDescription gestureDescription) {
                        super.onCancelled(gestureDescription);
                    }
                }, new Handler(Looper.getMainLooper()));
            }
        } catch (Exception e) {
            Log.d("@@@@ ", "screen scroll error:" + e.getMessage().toString());
            e.printStackTrace();
        }

        return true;
    }

    public boolean clickInScreenPoint(final float x, final float y) {
        if (Build.VERSION.SDK_INT < 26) {
            Util.execShellCmd("input tap " + x + " " + y);
        } else {
            Path path = new Path();
            path.moveTo(x, y);
            GestureDescription.Builder builder = new GestureDescription.Builder();
            GestureDescription gestureDescription = builder
                    .addStroke(new GestureDescription.StrokeDescription(path, 50L, 50L))
                    .build();
            service.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
                @Override
                public void onCompleted(GestureDescription gestureDescription) {
                    super.onCompleted(gestureDescription);
                }

                public void onCancelled(GestureDescription gestureDescription) {
                    super.onCancelled(gestureDescription);
                }
            }, new Handler(Looper.getMainLooper()));
        }

        return true;
    }

    public boolean clickToDetailPage() {
        AppInfo curApp = appList.get(curAppIndex);
        AccessibilityNodeInfo nodeInfo = curApp.getArticleSpecialViewById(instance);
        if (null != nodeInfo) {
            curType = "article";
        } else if(!isJumpVideo) {
            nodeInfo = curApp.getVideoSpecialViewById(instance);
            if (null != nodeInfo) {
                curType = "video";
            }
        }

        return performClickActionByNode(nodeInfo);
    }

    public boolean performClickActionByNode(AccessibilityNodeInfo node) {
        if (null != node) {
            return node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        return false;
    }

    public boolean performClickActionByNodeListFirstChild(List<AccessibilityNodeInfo> nodeList) {
        if (nodeList.size() > 0) {
            return nodeList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        return false;
    }

    public boolean performClickActionByNodeListFirstChildParent(List<AccessibilityNodeInfo> nodeList) {
        if (nodeList.size() > 0) {
            return nodeList.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        return false;
    }

    public void backToPreviousActivity() {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    public AccessibilityNodeInfo getRootNodeInfo() {
        if (null == service) {
            return null;
        }

        AccessibilityNodeInfo nodeInfo = null;
        nodeInfo = service.getRootInActiveWindow();

        return nodeInfo;
    }

    public void changeStatusToList() {
        this.curStatus = Constant.StatusInList;
        this.runningCount = 0;
        this.maxRunningCount = 8;
    }

    public void changeStatusToBackToMainActivity() {
        curStatus = Constant.StatusInBackToMainActivity;
        runningCount = 0;
        maxRunningCount = 10;
    }

    public void changeStatusToOpenningApp(boolean isInitStartTime) {
        Log.d("@@@@", "changeStatusToOpenningApp");
        if(isInitStartTime) {
            appRunStartTime = System.currentTimeMillis();
        }
        curStatus = Constant.StatusOpeningApp;
        runningCount = 0;
        maxRunningCount = 15;
    }

    public void changeStatusToSignIn() {
        this.curStatus = Constant.StatusSignIn;
        this.runningCount = 0;
        this.maxRunningCount = 10;
    }

    public void changeStatusToWaiting() {
        this.curStatus = Constant.StatusWaiting;
        this.runningCount = 0;
        this.maxRunningCount = 3;
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

        return new ArrayList<AccessibilityNodeInfo>();
    }

    public List<AccessibilityNodeInfo> findNodesByText(String text) {
        AccessibilityNodeInfo nodeInfo = getRootNodeInfo();
        if (nodeInfo != null) {
            return nodeInfo.findAccessibilityNodeInfosByText(text);
        }
        return null;
    }
}
