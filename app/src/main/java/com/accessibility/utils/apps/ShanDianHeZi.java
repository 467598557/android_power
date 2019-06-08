package com.accessibility.utils.apps;

import android.graphics.Rect;
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
        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("c.l.a:id/recyvlerview");
        if(nodeList.size() > 0) {
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

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
        if(!this.isSignin) {
            nodeList = root.findAccessibilityNodeInfosByViewId("c.l.a:id/tab_text");
            for(int i=0, len=nodeList.size(); i<len; i++) {
                node = nodeList.get(i);
                if(node.getText().equals("任务")) {
                    int count = 0;
                    while(count < 20 && null != node) {
                        node = node.getParent();
                        if(null != node && node.isClickable()) {
                            break;
                        }

                        count++;
                    }

                    if(null != node && node.isClickable()) {
                        operatorHelper.performClickActionByNode(node);
                        operatorHelper.changeStatusToSignIn();
                        return true;
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
        AccessibilityNodeInfo node;
        // 定时大红包 c.l.a:id/reward_text
        if(operatorHelper.runningCount <= 1) {
            // 定时大红包 计算位置
            operatorHelper.clickInScreenPoint(operatorHelper.winWidth-50, (float)(operatorHelper.winHeight*0.735+50));
        }
    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        if(operatorHelper.runningCount < operatorHelper.maxRunningCount) {
            return false;
        }

        AccessibilityNodeInfo root  = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

        List<AccessibilityNodeInfo> nodeList  = root.findAccessibilityNodeInfosByViewId("c.l.a:id/red_pack_signed_btn");
        if(nodeList.size() > 0) {
            AccessibilityNodeInfo signinBtn = nodeList.get(0);
            if(null != signinBtn) {
                operatorHelper.performClickActionByNode(signinBtn); // 点击签到
                operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("c.l.a:id/close_sign_dialog_btn"));
            }
        }

        AccessibilityNodeInfo node;
        nodeList = root.findAccessibilityNodeInfosByViewId("c.l.a:id/tab_text");
        // 回首页
        for(int i=0, len=nodeList.size(); i<len; i++) {
            node = nodeList.get(i);
            if(node.getText().equals("首页")) {
                int count = 0;
                while(count < 20 && null != node) {
                    node = node.getParent();
                    if(null != node && node.isClickable()) {
                        break;
                    }

                    count++;
                }

                if(null != node && node.isClickable()) {
                    operatorHelper.performClickActionByNode(node);
                    break;
                }
            }
        }
        this.isSignin = true;

        return true;
    }

    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {

    }
}
