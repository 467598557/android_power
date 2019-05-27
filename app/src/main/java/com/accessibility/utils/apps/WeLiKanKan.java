package com.accessibility.utils.apps;

import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

import java.util.List;

public class WeLiKanKan extends AppInfo {
    public WeLiKanKan() {
        this.packageName = "cn.weli.story";
        this.startComponent = "cn.etouch.ecalendar.MainActivity";
        this.mainComponent = "cn.etouch.ecalendar.MainActivity";
        this.newComponent = "cn.etouch.ecalendar.tools.life.LifeDetailsActivity";
        this.videoComponent = "com.lightsky.video.videodetails.ui.activity.VideoDetailsActivity";
    }

    @Override
    public String getListViewId() {
        return null;
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeInfoList = operatorHelper.findNodesById("cn.weli.story:id/layout");
        if(nodeInfoList.size() > 0) {
            return nodeInfoList.get(0);
        }

        return null;
    }

    @Override
    public AccessibilityNodeInfo getVideoSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeInfoList = operatorHelper.findNodesById("com.jifen.qukan:id/a3x");
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
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return;
        }

        List<AccessibilityNodeInfo> nodeInfoList;
        // cn.weli.story:id/ic_close 金币弹窗确定
        nodeInfoList = root.findAccessibilityNodeInfosByViewId("cn.weli.story:id/ic_close");
        if(nodeInfoList.size() > 0) {
            nodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        // #TODO 这里需要重点判断一下，此id会被重复利用
        // cn.weli.story:id/rl_head_line 金币()
//        nodeInfoList = root.findAccessibilityNodeInfosByViewId("cn.weli.story:id/rl_head_line");
//        if(nodeInfoList.size() > 0) {
//            nodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        }
        // cn.weli.story:id/text_ok 文章列表文章领取后确定
        nodeInfoList = root.findAccessibilityNodeInfosByViewId("cn.weli.story:id/text_ok");
        if(nodeInfoList.size() > 0) {
            nodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        // cn.weli.story:id/ll_open 文章列表金币
        nodeInfoList = root.findAccessibilityNodeInfosByViewId("cn.weli.story:id/ll_open");
        if(nodeInfoList.size() > 0) {
            nodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }
}
