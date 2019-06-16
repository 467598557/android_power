package com.accessibility.utils.apps;

import android.accessibilityservice.AccessibilityService;
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
        this.articleComponent = "com.jifen.qukan.content.newsdetail.news.NewsDetailNewActivity";
        this.videoComponent = "com.jifen.qukan.content.newsdetail.video.VideoNewsDetailNewActivity";
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeInfoList = operatorHelper.findNodesById("com.jifen.qukan:id/a5n");
        AccessibilityNodeInfo node;
        for(int i=nodeInfoList.size()-1; i>-1; i--) {
            node = nodeInfoList.get(i).getParent();
            if(node.findAccessibilityNodeInfosByText("广告").size() == 0) {
                return node;
            }
        }

        return null;
    }

    @Override
    public AccessibilityNodeInfo getVideoSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("com.jifen.qukan:id/a5n");
        // com.jifen.qukan:id/a5k 播放图标
        AccessibilityNodeInfo node;
        AccessibilityNodeInfo nodeParent;
        // com.jifen.qukan:id/a5h 标题
        for(int i=0, len=nodeList.size(); i<len; i++) {
            node = nodeList.get(i);
            nodeParent = node.getParent();
            if(nodeParent.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/a5k").size() == 0) {
                node = nodeParent.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/a5h").get(0);
                if(null != node && node.isEnabled()) { // 读过之后，enabled字段会更新
                    return nodeParent;
                }
            }
        }

        return null;
    }

    @Override
    public boolean doSomething(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

        List<AccessibilityNodeInfo> nodeInfoList;
        AccessibilityNodeInfo node;
        // 是否有签到
        nodeInfoList = root.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/jt");
        if(nodeInfoList.size() > 0) {
            node = nodeInfoList.get(0);
            if(null != node && node.getText().equals("去签到")) {
                operatorHelper.performClickActionByNode(node);
                operatorHelper.changeStatusToSignIn();
                return true;
            }
        }
        // 签到提醒弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/ic"));
        // 列表输入好友邀请码领金币弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/a1w"));
        // 执行更新app弹窗判断
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByText("以后更新"));
        // 锁屏看新闻弹窗判断
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/ty"));

        // 执行领取分时金币逻辑
        nodeInfoList = root.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/x0");
        if(nodeInfoList.size() > 0) {
            AccessibilityNodeInfo nodeInfo = nodeInfoList.get(0);
            nodeInfoList = nodeInfo.findAccessibilityNodeInfosByText("领取");
            if(nodeInfoList.size() > 0) { // 确认特征点，然后领取金币
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

        return true;
    }

    @Override
    public void doSomethingInDetailPage(OperatorHelper operatorHelper) {
    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        if(operatorHelper.runningCount < operatorHelper.maxRunningCount) {
            operatorHelper.performClickActionByNodeListFirstChild(operatorHelper.findNodesById("com.jifen.qukan:id/adq"));
            return false;
        }

        operatorHelper.backToPreviewWindow();
        this.isSignin = true;

        return true;
    }

    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {

    }
}
