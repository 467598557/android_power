package com.accessibility.utils;

import android.view.accessibility.AccessibilityNodeInfo;

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
}
