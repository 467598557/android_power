package com.accessibility.utils.apps;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

import java.util.List;

public class ShanDianHeZi extends AppInfo {
    public ShanDianHeZi() {
        this.packageName = "c.l.a";
        this.startComponent = "c.l.a.views.AppBoxWelcomeActivity";
        this.mainComponent = "c.l.a.views.AppBoxHomeActivity";
    }

    @Override
    public String getListViewId() {
        return "";
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
//        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("c.l.a:id/title");
        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("c.l.a:id/recyvlerview");
        AccessibilityNodeInfo node = nodeList.get(0);
        if(null != node) {
            AccessibilityNodeInfo childNode;
            for(int i=0, len=node.getChildCount(); i<len; i++) {
                childNode = node.getChild(i);

                if(childNode.findAccessibilityNodeInfosByText("广告").size() == 0) {
                    return childNode;
                }
            }
        }

//        for(int i=0, len=nodeList.size(); i<len; i++) {
//            node = nodeList.get(i).getParent().getParent();
//            if(null != node && node.findAccessibilityNodeInfosByText("广告").size() == 0) {
//                return node;
//            }
//        }

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

//        List<AccessibilityNodeInfo> nodeList;
//        AccessibilityNodeInfo node;
//        if(!this.isSignin) {
//            node = findMainMenuByText(root, "任务");
//            if(null != node) {
//                operatorHelper.performClickActionByNode(node);
//                operatorHelper.changeStatusToSignIn();
//                return true;
//            }
//        }

        return true;
    }

    @Override
    public void doSomethingInDetailPage(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return;
        }

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root  = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

        AccessibilityNodeInfo signinBtn = root.findAccessibilityNodeInfosByViewId("c.l.a:id/red_pack_signed_btn").get(0);
        operatorHelper.performClickActionByNode(signinBtn); // 点击签到
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("c.l.a:id/close_sign_dialog_btn"));
        AccessibilityNodeInfo node = findMainMenuByText(root, "首页");
        operatorHelper.performClickActionByNode(node);
        this.isSignin = true;

        return true;
    }

    private AccessibilityNodeInfo findMainMenuByText(AccessibilityNodeInfo root, String text) {
        List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByText(text);
        AccessibilityNodeInfo node;
        for(int i=0, len=nodeList.size(); i<len; i++) {
            node = nodeList.get(i).getParent().getParent().getParent();
            if(node.isClickable()) {
                return node;
            }
        }

        return null;
    }

    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {

    }
}
