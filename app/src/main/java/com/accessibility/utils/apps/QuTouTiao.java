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
    public String getArticleSpecialViewId() {
        return "com.jifen.qukan:id/a59";
    }

    @Override
    public String getVideoSpecialViewId() {
        return "com.jifen.qukan:id/a3x";
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
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        // 执行弹窗判断

        // 执行领取分时金币逻辑
        List<AccessibilityNodeInfo> nodeInfoList = root.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/w0");
        if(nodeInfoList.size() > 0) {
            AccessibilityNodeInfo nodeInfo = nodeInfoList.get(0);
            if(nodeInfo.findAccessibilityNodeInfosByText("领取").size() > 0) { // 确认特征点，然后领取金币
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        } else {
            Log.d("@@@ doSomething in QUTOUTIAO", "没有分时金币可领取");
        }
    }
}
