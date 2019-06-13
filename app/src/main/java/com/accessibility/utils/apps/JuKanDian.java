package com.accessibility.utils.apps;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

import java.util.List;

public class JuKanDian extends AppInfo {
    public JuKanDian() {
        this.packageName = "com.xiangzi.jukandian";
        this.startComponent = "com.xiangzi.jukandian.activity.V2WelcomeActivity";
        this.mainComponent = "com.xiangzi.jukandian.activity.MainActivity";
        this.articleComponent = "com.xiangzi.jukandian.activity.WebViewActivity";
        this.videoComponent = "com.xiangzi.jukandian.activity.WebViewActivity";
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeInfoList = operatorHelper.findNodesById("com.xiangzi.jukandian:id/item_artical_three_parent");

        return this.filterNormalArticleNode(nodeInfoList);
    }

    @Override
    public AccessibilityNodeInfo getVideoSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeInfoList = operatorHelper.findNodesById("com.xiangzi.jukandian:id/item_artical_three_parent");

        return this.filterNormalArticleNode(nodeInfoList);
    }

    @Override
    public boolean doSomething(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
        // 如果有关闭弹窗，则优先关闭
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/sure_quit"));
        // 签到成功后广告弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/close_dialog_layout"));
        // 关闭阅读时长换金币提示
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/image_user_task_pop_close"));
        // 关闭升级弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/image_update_cancle"));
        // 要点推送弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/dismisstv"));
        // 执行弹窗判断
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/dialog_close"));
        // 执行领取分时金币逻辑
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/rl_lingqu_par"));

        // com.xiangzi.jukandian:id/tosign 签到
        nodeList = root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/tosign");
        if(nodeList.size() > 0) {
            node = nodeList.get(0);
            if(null != node) {
                operatorHelper.performClickActionByNode(node);
                operatorHelper.changeStatusToSignIn();
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

        // 要点推送弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/dismisstv"));
    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        if(operatorHelper.runningCount < operatorHelper.maxRunningCount) {
            return false;
        }

        this.isSignin = true;

        return true;
    }

    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {
        // 处理一下等待过程中的下载窗口
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return;
        }

        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/image_update_cancle"));
    }
}
