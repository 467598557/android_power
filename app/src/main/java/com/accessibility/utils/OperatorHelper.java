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
    public int runningCount = 0;
    public int maxRunningCount = 15;
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
                String curClassName = "";
                try {
                    switch (curStatus) {
                        case Constant.StatusInList:
                            AccessibilityNodeInfo rootNode = getRootNodeInfo();
                            String curPackage = rootNode.getPackageName().toString();
                            if(!curPackage.equals(curApp.packageName)) { // 可能有异常跳出
                                changeStatusToOpenningApp();
                                return;
                            }
//                            curClassName = rootNode.getClassName().toString();
//                            Log.d("@@@@ curClassNamein StatusInList", rootNode+";"+rootNode.getWindow());
//                            if(!curClassName.equals(curApp.mainComponent)) { // 如果当前页面并非主页面
//                                changeStatusToBackToMainActivity();
//                                return;
//                            }

                            try {
                                curApp.doSomething(instance);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (runningCount == 0) { // 滑动
                                scrollScreen(winWidth/3, winHeight/5*4, winWidth/3, winHeight/5);
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

                            judgeAppRunLoop();
                            break;
                        case Constant.StatusSignIn:
                            // 往上滑动，以免存在时差页面被滑下
                            if(runningCount <= 2) {
                                scrollScreen(winWidth/3, winHeight/5, winWidth/3, winHeight/5*4);
                            }

                            if(runningCount > 2) {
                                curApp.signin(instance);
                            }
                            if(runningCount >= maxRunningCount) {
                                // 签到
                                changeStatusToList();
                                return;
                            }
                            break;
                        case Constant.FindSohuRedPackageInList:
                            if (runningCount == 0) { // 滑动
                                scrollScreen(winWidth/3, winHeight/5*4, winWidth/3, winHeight/5);
                            }

                            if (runningCount >= maxRunningCount) {
                                if(curApp.doSomething(instance)) {
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
                            if(runningCount % 5 == 0) {
                                scrollScreen(winWidth/3, winHeight/5*4, winWidth/3, winHeight/5);
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
                            if(runningCount >= maxRunningCount) {
                                changeStatusToOpenningApp();
                                return;
                            }
                            break;
                        case Constant.StatusInCloseJuKanDianApp:
                            if(runningCount >= maxRunningCount) {
                                performClickActionByNodeListFirstChild(getRootNodeInfo().findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/sure_quit"));
                                changeStatusToWaiting();
                                return;
                            }
                            break;
                        case Constant.StatusInBackToMainActivity:
                            if(runningCount%2 == 0) {
                                backToPreviewWindow();
                            } else {
                                curClassName = getRootNodeInfo().getClassName().toString();
                                if(curClassName.equals(curApp.mainComponent)) {
                                    changeStatusToList();
                                    return;
                                }
                            }

                            if(runningCount >= maxRunningCount) {
                                changeStatusToList();
                                return;
                            }
                            break;
                        case Constant.StatusOpeningApp: // 等待什么都不做
                            if (runningCount == 0) {
                                appRunStartTime = System.currentTimeMillis();
                                Util.startActivity(curApp, service);
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
                    e.printStackTrace();
                }

                runningCount++;
            }
        };

        timer.schedule(timerTask, 0, TIMER_CHECK_INTERVAL);
    }

    private void judgeAppRunLoop() {
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
            changeStatusToWaiting();
            maxRunningCount = 5;
            // 强行退出
            backToSystemHome();
        }
    }

    public void backToPreviewWindow() {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    public void backToSystemHome() {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        if(curApp.packageName.equals("com.xiangzi.jukandian")) {
            curStatus = Constant.StatusInCloseJuKanDianApp;
        } else {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
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
                super.onCancelled(gestureDescription);
            }
        }, new Handler(Looper.getMainLooper()));

        return true;
    }

    public boolean clickInScreenPoint(final float x, final float y) {
        Path path=new Path();
        path.moveTo(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription  gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 50L, 50L))
                .build();
        service.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d("@@@", "点击x="+x+",y="+y+"成功了");
            }
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d("@@@", "点击x="+x+",y="+y+"失败了");
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

    public void changeStatusToBackToMainActivity() {
        curStatus = Constant.StatusInBackToMainActivity;
        runningCount = 0;
        maxRunningCount = 10;
    }

    public void changeStatusToOpenningApp() {
        curStatus = Constant.StatusOpeningApp;
        runningCount = 0;
        maxRunningCount = 15;
    }

    public void changeStatusToSignIn() {
        this.curStatus = Constant.StatusSignIn;
        this.runningCount = 0;
        this.maxRunningCount = 6;
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
