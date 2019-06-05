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
        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("com.sohu.infonews:id/btm_divider");
        AccessibilityNodeInfo node;
        for(int i=0, len=nodeList.size(); i<len; i++) {
            node = nodeList.get(i).getParent();
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
    public boolean doSomething(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        AccessibilityNodeInfo node;
        List<AccessibilityNodeInfo> nodeList;
//        if(!this.isSignin) {
//            nodeList = root.findAccessibilityNodeInfosByText("任务");
//            for(int i=0, len=nodeList.size(); i<len; i++) {
//                node = nodeList.get(i).getParent().getParent();
//                if(node.isClickable()) {
//                    operatorHelper.performClickActionByNode(node);
//                    operatorHelper.changeStatusToSignIn();
//                    return true;
//                }
//            }
//        }

        // 定量大红包按钮
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/red_bags"));
        // 定量大红包开启弹窗
        if(operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/redbag_open"))) {
            // 定量大红包打开后弹窗
            operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/btn_receive"));
            operatorHelper.performClickActionByNode(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/limit_result_uinavigation").get(0).getChild(0));
        }

        // 执行弹窗判断
        // com.sohu.infonews:id/redbag_open 限量抢红包弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/redbag_open"));
        // com.sohu.infonews:id/btn_receive  领取分时金币或者限量红包弹窗后弹窗处理
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/btn_receive"));
        // com.sohu.infonews:id/energy_open 领取金币
        return operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/energy_open"));
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
            operatorHelper.changeStatusToFindSohuRedPackageInList();
        }
    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/task_entrance_gv");
        AccessibilityNodeInfo node = nodeList.get(0).getChild(0);
        operatorHelper.performClickActionByNode(node);
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/btn_receive"));
        operatorHelper.backToPreviewWindow(); // 回到任务主页
        operatorHelper.backToPreviewWindow(); // 回到默认推荐页面
        this.isSignin = true;

        return true;
    }

    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {

    }
}
