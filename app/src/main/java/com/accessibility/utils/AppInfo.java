package com.accessibility.utils;

import android.graphics.Path;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public abstract class AppInfo {
    public String packageName;
    public String startComponent;
    public String mainComponent;
    public String articleComponent;
    public String videoComponent;   
    public boolean isSignin = false;

    public abstract AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper);
    public abstract AccessibilityNodeInfo getVideoSpecialViewById(OperatorHelper operatorHelper);
    public abstract boolean doSomething(OperatorHelper operatorHelper);
    public abstract void doSomethingInDetailPage(OperatorHelper operatorHelper);
    public abstract void doSomethingInOpeningApp(OperatorHelper operatorHelper);
    public abstract boolean signin(OperatorHelper operatorHelper);

    protected AccessibilityNodeInfo filterNormalArticleNode(List<AccessibilityNodeInfo> nodeList) {
        for(int i=0, len=nodeList.size(); i<len; i++) {
            AccessibilityNodeInfo node = nodeList.get(i);

            if(node.findAccessibilityNodeInfosByText("广告").size() == 0) {
                return node;
            }
        }

        return null;
    }
}
