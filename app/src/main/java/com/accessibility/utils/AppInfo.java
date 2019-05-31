package com.accessibility.utils;

import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public abstract class AppInfo {
    public String packageName;
    public String startComponent;
    public String mainComponent;
    public String newComponent;
    public String videoComponent;

    public abstract String getListViewId();
    public abstract AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper);
    public abstract AccessibilityNodeInfo getVideoSpecialViewById(OperatorHelper operatorHelper);
    public abstract String getArticlePageContentViewId();
    public abstract String getVideoPageContentVideoId();
    public abstract void doSomething(OperatorHelper operatorHelper);
    public abstract void doSomethingInDetailPage(OperatorHelper operatorHelper);

    protected AccessibilityNodeInfo filterNormalArticleNode(List<AccessibilityNodeInfo> nodeList) {
        for(int i=1, len=nodeList.size(); i<len; i++) {
            AccessibilityNodeInfo node = nodeList.get(i);

            if(node.findAccessibilityNodeInfosByText("广告").size() == 0) {
                return node;
            }
        }

        return null;
    }
}
