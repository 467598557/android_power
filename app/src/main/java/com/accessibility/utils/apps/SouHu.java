package com.accessibility.utils.apps;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

import java.util.List;

public class SouHu extends AppInfo {
    public SouHu() {
        this.packageName = "com.sohu.infonews";
        this.startComponent = "com.sohu.quicknews.splashModel.activity.SplashActivity";
        this.mainComponent = "com.sohu.quicknews.homeModel.activity.HomeActivity";
    }

    @Override
    public String getListViewId() {
        return "";
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeInfoList = operatorHelper.findNodesById("com.sohu.infonews:id/btm_divider");
        AccessibilityNodeInfo node;
        for(int i=nodeInfoList.size()-1; i>-1; i--) {
            node = nodeInfoList.get(i).getParent();
            if(node.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/left_guess_tv").size() == 0) {
                return node;
            }
        }

        return null;
    }

    @Override
    public AccessibilityNodeInfo getVideoSpecialViewById(OperatorHelper operatorHelper) {
        return getArticleSpecialViewById(operatorHelper);
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
        // com.sohu.infonews:id/redbag_open 限量抢红包弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/redbag_open"));
        // com.sohu.infonews:id/btn_receive  领取分时金币或者限量红包弹窗后弹窗处理
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/btn_receive"));
        // com.sohu.infonews:id/energy_open 领取金币
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/energy_open"));
        // 限量抢红包小入口按钮
    }

    @Override
    public void doSomethingInDetailPage(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return;
        }

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
        // 能量红包
        nodeList = root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/energy_redbag");
        if(nodeList.size() > 0) {
            boolean result = nodeList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            if(!result) {
                node = root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/counting_img").get(0);
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
        // 能量红包弹窗
        nodeList = root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/notice_btn");
        if(nodeList.size() > 0) {
            nodeList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            operatorHelper.changeStatusToList();
        }
    }
}
