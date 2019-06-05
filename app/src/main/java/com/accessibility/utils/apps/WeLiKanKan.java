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

        return this.filterNormalArticleNode(nodeInfoList);
    }

    @Override
    public AccessibilityNodeInfo getVideoSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeInfoList = operatorHelper.findNodesById("com.jifen.qukan:id/a3x");

        return this.filterNormalArticleNode(nodeInfoList);
    }

    @Override
    public boolean doSomething(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

        List<AccessibilityNodeInfo> nodeInfoList;
        AccessibilityNodeInfo nodeInfo;
        // cn.weli.story:id/ic_close 金币弹窗确定
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("cn.weli.story:id/ic_close"));
        // cn.weli.story:id/rl_head_line 金币()
        nodeInfoList = root.findAccessibilityNodeInfosByViewId("cn.weli.story:id/rl_head_line");
        if(nodeInfoList.size() > 0) {
            nodeInfo = nodeInfoList.get(0);
            if(nodeInfo.findAccessibilityNodeInfosByText("+").size() > 0) { // 确认是金币
                nodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            } else if(nodeInfo.findAccessibilityNodeInfosByText("签到").size() > 0) { // 确认是签到
                operatorHelper.performClickActionByNode(nodeInfo);
                operatorHelper.changeStatusToSignIn();
                return true;
            }
        }
        // cn.weli.story:id/text_ok 文章列表文章领取后确定
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("cn.weli.story:id/text_ok"));
        // cn.weli.story:id/ll_open 文章列表金币
        nodeInfoList = root.findAccessibilityNodeInfosByViewId("cn.weli.story:id/ll_open");
        if(nodeInfoList.size() > 0) {
            boolean clickResult = nodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            if(!clickResult) {
                nodeInfoList = root.findAccessibilityNodeInfosByViewId("cn.weli.story:id/layout");
                for(int i=0, len=nodeInfoList.size(); i<len; i++) {
                    nodeInfo = nodeInfoList.get(i);
                    if(nodeInfo.findAccessibilityNodeInfosByText("领金币").size()>0) {
                        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK) ;
                        break;
                    }
                }
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

        List<AccessibilityNodeInfo> nodeList;
        // cn.weli.story:id/ll_height_more 查看更多
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("cn.weli.story:id/ll_height_more"));
    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        operatorHelper.backToPreviewWindow();
        this.isSignin = true;

        return true;
    }

    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {

    }
}
