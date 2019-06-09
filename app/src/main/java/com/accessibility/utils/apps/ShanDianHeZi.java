package com.accessibility.utils.apps;

import android.graphics.Path;
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
//        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("c.l.a:id/recyvlerview");
//        if(nodeList.size() > 0) {
//            AccessibilityNodeInfo node = nodeList.get(0);
//            if(null != node) {
//                AccessibilityNodeInfo childNode;
//                for(int i=0, len=node.getChildCount(); i<len; i++) {
//                    childNode = node.getChild(i);
//
//                    if(childNode.findAccessibilityNodeInfosByText("广告").size() == 0) {
//                        return childNode;
//                    }
//                }
//            }
//        }
        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("c.l.a:id/from_text");
        AccessibilityNodeInfo node;
        for(int i=0, len=nodeList.size(); i<len; i++) {
            node = nodeList.get(i).getParent();
            if(node.isClickable() && node.findAccessibilityNodeInfosByText("广告").size() == 0
                    && node.getClassName().toString().equals("android.widget.LinearLayout")) {
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

        if(!this.isSignin) {
            clickMainMenuByIndex(operatorHelper, root, 3);
            operatorHelper.changeStatusToSignIn();
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
        AccessibilityNodeInfo root  = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

        if(operatorHelper.runningCount == operatorHelper.maxRunningCount-2) {
            List<AccessibilityNodeInfo> nodeList  = root.findAccessibilityNodeInfosByViewId("c.l.a:id/red_pack_signed_btn");
            if(nodeList.size() > 0) {
                AccessibilityNodeInfo signinBtn = nodeList.get(0);
                if(null != signinBtn) {
                    operatorHelper.performClickActionByNode(signinBtn); // 点击签到
                }
            }
        }

        if(operatorHelper.runningCount == operatorHelper.maxRunningCount - 1) {
            operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("c.l.a:id/close_sign_dialog_btn"));
        }

        if(operatorHelper.runningCount == operatorHelper.maxRunningCount) {
            // 回首页
            clickMainMenuByIndex(operatorHelper, root, 0);
            this.isSignin = true;
        }

        return true;
    }

    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {
    }

    private boolean clickMainMenuByIndex(OperatorHelper operatorHelper, AccessibilityNodeInfo root, int index) {
        List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByViewId("c.l.a:id/bottom_navigation");
        if(nodeList.size() > 0) {
            AccessibilityNodeInfo btnGroup = nodeList.get(nodeList.size() - 1);
            if(btnGroup.getChildCount() > index) {
                AccessibilityNodeInfo btn = btnGroup.getChild(index);
                operatorHelper.performClickActionByNode(btn);
                return true;
            }
        }

        return false;
    }
}
