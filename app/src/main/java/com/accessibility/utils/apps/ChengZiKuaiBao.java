package com.accessibility.utils.apps;

import android.graphics.Rect;
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
    public String getListViewId() {
        return "";
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
    public String getArticlePageContentViewId() {
        return null;
    }

    @Override
    public String getVideoPageContentVideoId() {
        return null;
    }

    @Override
    public boolean doSomething(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

//         com.quyu.youliao:id/iv_close 列表页新人福利社弹窗
        List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/iv_close");
        AccessibilityNodeInfo node;
        if(nodeList.size() > 0) {
            for(int i=0, len=nodeList.size(); i<len; i++) {
                node = nodeList.get(i);
                Rect bounds = new Rect();
                node.getBoundsInScreen(bounds);
                if(bounds.left < operatorHelper.winWidth/2+80) {
                    operatorHelper.performClickActionByNode(node);
                    break;
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

        // 查看更多  com.quyu.youliao:id/ll_expand
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/ll_expand"));
    }
}
