package com.accessibility.utils.apps;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

import java.util.List;

public class QuTouTiao extends AppInfo {
    public QuTouTiao() {
        this.packageName = "com.jifen.qukan";
        this.startComponent = "com.jifen.qkbase.main.MainActivity";
        this.mainComponent = "com.jifen.qkbase.main.MainActivity";
        this.newComponent = "com.jifen.qukan.content.newsdetail.news.NewsDetailNewActivity";
        this.videoComponent = "com.jifen.qukan.content.newsdetail.video.VideoNewsDetailNewActivity";
    }

    public String getListViewId() {
        return "com.jifen.qukan:id/no";
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeInfoList = operatorHelper.findNodesById("com.jifen.qukan:id/a59");
        if(nodeInfoList.size() > 0) {
            return nodeInfoList.get(0).getParent();
        }

        return null;
    }

    @Override
    public AccessibilityNodeInfo getVideoSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeInfoList = operatorHelper.findNodesById("com.jifen.qukan:id/a3x");
        if(nodeInfoList.size() > 0) {
            return nodeInfoList.get(0).getParent();
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
        Log.d("@@@ doSomething in QuTouTiao", "");
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        List<AccessibilityNodeInfo> nodeInfoList;
        // 执行更新app弹窗判断
        nodeInfoList = root.findAccessibilityNodeInfosByText("以后更新");
        if(nodeInfoList.size() > 0) {
            Log.d("@@@", "检测到app更新面板，以后更新");
            nodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        // 执行领取分时金币逻辑
        nodeInfoList = root.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/w0");
        if(nodeInfoList.size() > 0) {
            AccessibilityNodeInfo nodeInfo = nodeInfoList.get(0);
            nodeInfoList = nodeInfo.findAccessibilityNodeInfosByText("领取");
            if(nodeInfoList.size() > 0) { // 确认特征点，然后领取金币
                Log.d("@@@", "领取金币");
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }
}
