package com.accessibility.utils;

public abstract class AppInfo {
    public String packageName;
    public String startComponent;
    public String mainComponent;
    public String newComponent;
    public String videoComponent;

    public abstract String getListViewId();
    public abstract String getArticleSpecialViewId();
    public abstract String getVideoSpecialViewId();
    public abstract String getArticlePageContentViewId();
    public abstract String getVideoPageContentVideoId();
    public abstract void doSomething(OperatorHelper operatorHelper);
}
