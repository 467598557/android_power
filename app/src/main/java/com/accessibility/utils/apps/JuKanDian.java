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
        this.newComponent = "com.xiangzi.jukandian.activity.WebViewActivity";
        this.videoComponent = "com.xiangzi.jukandian.activity.WebViewActivity";
    }

    @Override
    public String getListViewId() {
        return "";
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
    public String getArticlePageContentViewId() {
        return null;
    }

    @Override
    public String getVideoPageContentVideoId() {
        return null;
    }

    @Override
    public boolean doSomething(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

        // com.xiangzi.jukandian:id/tosign 签到
        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
        nodeList = root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/tosign");
        node = nodeList.get(0);
        if(null != node) {
            operatorHelper.performClickActionByNode(node);
            operatorHelper.changeStatusToSignIn();
            return true;
        }

        // 执行弹窗判断
        boolean result = operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/dialog_close"));
        Log.d("@@@",  "JuKanDian执行弹窗判断---"+result);
        // 执行领取分时金币逻辑
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/rl_lingqu_par"));

        return true;
    }

    @Override
    public void doSomethingInDetailPage(OperatorHelper operatorHelper) {
    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        operatorHelper.backToPreviewWindow();
        this.isSignin = true;

        return true;
    }
}
