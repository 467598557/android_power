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
    public void doSomething(OperatorHelper operatorHelper) {
        //   领取
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        Log.d("@@@ getClassName", ""+root.getClassName());
        // 执行弹窗判断

        // 执行领取分时金币逻辑
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/rl_lingqu_par"));
    }

    @Override
    public void doSomethingInDetailPage(OperatorHelper operatorHelper) {
    }
}
