package com.accessibility.utils.apps;

import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

import java.util.List;

public class YueTouTiao extends AppInfo {
    public YueTouTiao() {
        this.packageName = "com.expflow.reading";
        this.startComponent = "com.expflow.reading.activity.SplashActivity";
        this.mainComponent = "com.expflow.reading.activity.MainActivity";
        this.articleComponent = "com.expflow.reading.activity.DetailNewsActivity";
        this.videoComponent = "com.expflow.reading.activity.H5DetailVideoActivity";
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("com.expflow.reading:id/irv_headline");
        AccessibilityNodeInfo node;
        if(nodeList.size() > 0) {
            node = nodeList.get(0);
            nodeList = node.findAccessibilityNodeInfosByViewId("com.expflow.reading:id/layout_item_news");
            for(int i=0, len=nodeList.size(); i<len; i++) {
                node = nodeList.get(i);
                if(node.findAccessibilityNodeInfosByText("广告").size() == 0 &&
                        node.findAccessibilityNodeInfosByText("置顶").size() == 0) {
                    return node;
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
        if(null == root) {
            return false;
        }

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
        // 某某弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.expflow.reading:id/iv_dismiss"));
        // 领取列表金币
        nodeList = root.findAccessibilityNodeInfosByViewId("com.expflow.reading:id/ll_layout");
        for(int i=0, len=nodeList.size(); i<len; i++) {
            node = nodeList.get(i);
            if(node.findAccessibilityNodeInfosByText("领金币").size() > 0) {
                operatorHelper.performClickActionByNode(node);
            }
        }
        // 领完金币后弹窗，继续阅读
        nodeList = root.findAccessibilityNodeInfosByViewId("com.expflow.reading:id/tv_read");
        if(nodeList.size() > 0) {
            node = nodeList.get(0);
            if(node.getText().toString().equals("继续阅读")) {
                operatorHelper.performClickActionByNode(node);
            }
        }
        // 领取金币
        if(!this.isSignin && this.readingArticleCount == 2) {
            if(operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.expflow.reading:id/layout_invite"))) {
                operatorHelper.changeStatusToSignIn();
                return true;
            }
        }

        return true;
    }

    @Override
    public void doSomethingInDetailPage(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return;
        }

        if(operatorHelper.runningCount == operatorHelper.maxRunningCount) {
            this.readingArticleCount += 1;
        }
    }

    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return;
        }


    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root  = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
        if(operatorHelper.runningCount == operatorHelper.maxRunningCount - 3) {
            nodeList = root.findAccessibilityNodeInfosByViewId("com.expflow.reading:id/llSignin");
            if(nodeList.size() > 0) {
                node = nodeList.get(0);
                if(node.getText().toString().equals("立即签到")) {
                    operatorHelper.performClickActionByNode(node);
                    this.isSignin = true;
                } else if(node.getText().toString().equals("已签到")) {
                    this.isSignin = true;
                }
            }
        }
        // 金币领取之后弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.expflow.reading:id/iv_close"));

        if(operatorHelper.runningCount == operatorHelper.maxRunningCount) {
            operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.expflow.reading:id/layout_news"));
        }

        return true;
    }
}
