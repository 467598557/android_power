package com.accessibility.utils.apps;

import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

import java.util.List;

public class ChengZiKuaiBao extends AppInfo {
    public ChengZiKuaiBao() {
        this.packageName = "com.quyu.youliao";
        this.startComponent = "com.koala.gold.toutiao.MainActivity";
        this.mainComponent = "com.koala.gold.toutiao.activity.HomeActivity";
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeInfoList = operatorHelper.findNodesById("com.quyu.youliao:id/content_view");
        AccessibilityNodeInfo node;
        for(int i=0, len=nodeInfoList.size(); i<len; i++) {
            node = nodeInfoList.get(i);
            if(!node.isSelected() && node.findAccessibilityNodeInfosByText("广告").size() == 0) {
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

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
        // com.quyu.youliao:id/iv_close 列表页新人福利社等弹窗
        nodeList = root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/iv_close");
        if(nodeList.size() > 0) {
            AccessibilityNodeInfo specialNode;
            for(int i=0, len=nodeList.size(); i<len; i++) {
                node = nodeList.get(i);
                specialNode = node.getParent().getParent();
                // 如果不是文章或者广告的条目
                if(specialNode.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/ll_info_layout").size() == 0 &&
                    specialNode.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/ll_ad_v").size() == 0) {
                    operatorHelper.performClickActionByNode(node);
                }
            }
        }

        if(!this.isSignin) {
            Log.d("@@@@", "点击任务按钮");
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

        // 查看更多  com.quyu.youliao:id/ll_expand
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/ll_expand"));
    }

    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {

    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root  = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

        if(operatorHelper.runningCount == operatorHelper.maxRunningCount-2) {
            // 任务界面拆红包弹窗
            operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("c.l.a:id/btn_close"));
            List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/btn_sign");
            if(nodeList.size() > 0) {
                AccessibilityNodeInfo signBtn = nodeList.get(0);
                operatorHelper.performClickActionByNode(signBtn);
            }
        }

        if(operatorHelper.runningCount == operatorHelper.maxRunningCount) {
            if(clickMainMenuByIndex(operatorHelper, root, 0)) {
                this.isSignin = true;
            }
        }

        return true;
    }

    private boolean clickMainMenuByIndex(OperatorHelper operatorHelper, AccessibilityNodeInfo root, int index) {
        List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/title_container");
        if(nodeList.size() > 0) {
            // 取最后一个
            AccessibilityNodeInfo btnGroup = nodeList.get(nodeList.size() - 1);
            if(btnGroup.getChildCount() > index) {
                return operatorHelper.performClickActionByNode(btnGroup.getChild(index));
            }
        }

        return false;
    }
}
