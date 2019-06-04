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
        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("c.l.a:id/title");
        AccessibilityNodeInfo node;
        for(int i=0, len=nodeList.size(); i<len; i++) {
            node = nodeList.get(i);
            int whileCount = 0;
            while(true) {
                node = node.getParent();
                if(null != node && node.isClickable() && node.findAccessibilityNodeInfosByText("广告").size() == 0) {
                    return node;
                }

                whileCount++;
                if(whileCount > 5) {
                    break;
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
}
