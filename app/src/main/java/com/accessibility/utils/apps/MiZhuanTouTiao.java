package com.accessibility.utils.apps;

import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

import java.util.List;

public class MiZhuanTouTiao extends AppInfo {
    public MiZhuanTouTiao() {
        this.packageName = "me.toutiaoapp";
        this.startComponent = "me.toutiaoapp.ui.activity.CoverActivity";
        this.mainComponent = "me.toutiaoapp.ui.activity.TabFragmentActivity";
        this.articleComponent = "";
        this.videoComponent = "";
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("android:id/list");
        AccessibilityNodeInfo node;
        if (nodeList.size() > 0) {
            node = nodeList.get(0);
            if (node.getChildCount() > 0) {
                AccessibilityNodeInfo child;
                for (int i = 0, len = node.getChildCount(); i < len; i++) {
                    child = node.getChild(i);
                    if (child.findAccessibilityNodeInfosByText("广告").size() == 0 &&
                            child.findAccessibilityNodeInfosByText("置顶").size() == 0) {
                        return child;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public AccessibilityNodeInfo getVideoSpecialViewById(OperatorHelper operatorHelper) {
        return getArticleSpecialViewById(operatorHelper);
    }

    @Override
    public boolean doSomething(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if (null == root) {
            return false;
        }

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
        // 下载推荐弹窗 me.toutiaoapp:id/tt_insert_dislike_icon_img
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("me.toutiaoapp:id/tt_insert_dislike_icon_img"));
        // 退出头条弹窗，确认键
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("me.toutiaoapp:id/btnExit"));
        // 首次打开app弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("me.toutiaoapp:id/hongbao_close"));
        // 领取左上角金币
        nodeList = root.findAccessibilityNodeInfosByViewId("me.toutiaoapp:id/time_gift_receive");
        if (nodeList.size() > 0) {
            node = nodeList.get(0);
            if (node.findAccessibilityNodeInfosByText("30").size() > 0) {
                operatorHelper.performClickActionByNode(node);
            }
        }
        // 领取左上角金币后弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("me.toutiaoapp:id/tt_insert_dislike_icon_img"));
        // 看视频引导弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("me.toutiaoapp:id/gotit"));
        // 有可能是广告窗口的关闭按钮
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("closeBtn"));
        // 签到 android:id/tabs
        if (!this.isSignin) {
            nodeList = root.findAccessibilityNodeInfosByViewId("android:id/tabs");
            if (nodeList.size() > 0) {
                node = nodeList.get(0);
                if (node.getChildCount() > 2) {
                    Log.d("@@@@", "nodeList.size()"+ node.getChildCount());
                    AccessibilityNodeInfo menu;
                    for (int i = 0, len = node.getChildCount(); i < len; i++) {
                        menu = node.getChild(i);
                        if (menu.findAccessibilityNodeInfosByText("任务中心").size() > 0) {
                            Log.d("@@@@", "任务中心：" + menu);
                            clickMenuByPoint(operatorHelper, menu);
                            operatorHelper.changeStatusToSignIn();
                            break;
                        }
                    }
                    return true;
                }
            }
        }

        return true;
    }

    @Override
    public void doSomethingInDetailPage(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if (null == root) {
            return;
        }

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
    }


    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if (null == root) {
            return;
        }

        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("me.toutiaoapp:id/hongbao_close"));
    }

    private void clickMenuByPoint(OperatorHelper operatorHelper, AccessibilityNodeInfo menu) {
        Rect rect = new Rect();
        menu.getBoundsInScreen(rect);
        Log.d("@@@@", "clickMenuByPoint:"+rect.left+":"+rect.right+":"+rect.top+":"+rect.bottom);
        float x = rect.left + (rect.right - rect.left) / 2;
        float y = rect.top + (rect.bottom - rect.top) / 2;
        Log.d("@@@@", "clickMenuByPoint :"+x+":"+y);
        operatorHelper.clickInScreenPoint(x, y);
    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        // me.toutiaoapp:id/check_btn
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if (null == root) {
            return false;
        }

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
        if (operatorHelper.runningCount == operatorHelper.maxRunningCount - 3) {
            nodeList = root.findAccessibilityNodeInfosByViewId("me.toutiaoapp:id/check_btn");
            if (nodeList.size() > 0) {
                node = nodeList.get(0);
                // 是否已签到
                if (node.getText().toString().indexOf("签到") >= 0) {
                    operatorHelper.performClickActionByNode(node);
                }

                this.isSignin = true;
            }
        }
        // 签到后的弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("me.toutiaoapp:id/share_close"));
        // 签到后的弹窗关闭后的弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("closeBtn"));

        if (operatorHelper.runningCount == operatorHelper.maxRunningCount) {
            nodeList = root.findAccessibilityNodeInfosByViewId("android:id/tabs");
            if (nodeList.size() > 0) {
                node = nodeList.get(0);
                if (node.getChildCount() > 2) {
                    AccessibilityNodeInfo menu;
                    for (int i = 0, len = node.getChildCount(); i < len; i++) {
                        menu = node.getChild(i);
                        if (menu.findAccessibilityNodeInfosByText("头条").size() > 0) {
                            Log.d("@@@@", "头条：" + menu);
                            clickMenuByPoint(operatorHelper, menu);
                            operatorHelper.changeStatusToSignIn();
                            break;
                        }
                    }
                }
            }
        }

        return true;
    }
}
