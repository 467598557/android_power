package com.accessibility.utils.apps;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

import java.util.List;

public class NiuNiuZiXun extends AppInfo {
    public NiuNiuZiXun() {
        this.packageName = "com.huolea.bull";
        this.startComponent = "com.huolea.bull.page.other.activity.SplashActivity";
        this.mainComponent = "com.huolea.bull.page.other.activity.MainActivity";
        this.newComponent = "com.kaijia.see.activity.ArticleWebActivity";
        this.videoComponent = "com.kaijia.see.activity.ArticleWebActivity";
    }

    @Override
    public String getListViewId() {
        return null;
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("com.huolea.bull:id/title_tv");
        AccessibilityNodeInfo node;
        for(int i=0, len=nodeList.size(); i<len; i++) {
            node = nodeList.get(i).getParent();
            if(!node.getClassName().equals("android.widget.RelativeLayout")) {
                node = node.getParent();
            }
            if(node.findAccessibilityNodeInfosByText("广告").size() == 0 && node.findAccessibilityNodeInfosByText("阅读赚金币").size() == 0) {
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
        if(null == root) {
            return false;
        }

        List<AccessibilityNodeInfo> nodeInfoList;
        AccessibilityNodeInfo nodeInfo;
        if(!this.isSignin) {
            nodeInfo = root.findAccessibilityNodeInfosByViewId("com.huolea.bull:id/id_layout_navigation_task_layout").get(0);
            if(null != nodeInfo) {
                operatorHelper.performClickActionByNode(nodeInfo);
                operatorHelper.changeStatusToSignIn();
                return true;
            }
        }
        // 金币领取后再点击后弹窗
        nodeInfoList = root.findAccessibilityNodeInfosByViewId("com.huolea.bull:id/id_dialog_datecoins_close");
        if(nodeInfoList.size() > 0) {
            nodeInfoList.get(0).getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        // 金币领取后弹窗 com.huolea.bull:id/id_dialog_datecoins_cancel
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.huolea.bull:id/id_dialog_datecoins_cancel"));
        // 邀请好友弹窗关闭按钮
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.huolea.bull:id/id_dialog_activity_close"));
        //  金币容器
        nodeInfoList = root.findAccessibilityNodeInfosByViewId("com.huolea.bull:id/id_fragment_information_datecoins_layout");
        if(nodeInfoList.size() > 0) {
            nodeInfo = nodeInfoList.get(0);
            if(nodeInfo.findAccessibilityNodeInfosByText("领取").size() > 0) { // 再次确认是金币
                nodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
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

        List<AccessibilityNodeInfo> nodeInfoList;
        AccessibilityNodeInfo node;
    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null != root) {
            return false;
        }

        List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByViewId("com.huolea.bull:id/id_layout_navigation_news_layout");
        if(nodeList.size() == 0) {
            return false;
        }

        AccessibilityNodeInfo node = nodeList.get(0);
        if(null != node) {
            operatorHelper.performClickActionByNode(node);
            // 回到咨询列表页面
            operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.huolea.bull:id/id_layout_navigation_news_layout"));
        }

        this.isSignin = true;
        return true;
    }

    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {
    }
}
