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
        if(nodeInfoList.size() > 0) {
            return nodeInfoList.get(0);
        }

        return null;
    }

    @Override
    public AccessibilityNodeInfo getVideoSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeInfoList = operatorHelper.findNodesById("com.xiangzi.jukandian:id/item_artical_three_parent");
        if(nodeInfoList.size() > 0) {
            return nodeInfoList.get(0);
        }

        return null;
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
        // 执行弹窗判断

        // 执行领取分时金币逻辑
        List<AccessibilityNodeInfo> nodeInfoList = root.findAccessibilityNodeInfosByViewId("com.xiangzi.jukandian:id/rl_lingqu_par");
        if(nodeInfoList.size() > 0) {
            AccessibilityNodeInfo nodeInfo = nodeInfoList.get(0);
            Log.d("@@@", "领取金币");
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
//            Log.d("@@@ doSomething in JuKanDian", "没有分时金币可领取");
        }
    }
}
