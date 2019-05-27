package com.accessibility.utils.apps;

import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

public class NiuNiuZiXun extends AppInfo {
    public NiuNiuZiXun() {
        this.packageName = "com.huolea.bull";
        this.startComponent = "com.huolea.bull.page.other.activity.SplashActivity";
        this.mainComponent = "com.huolea.bull.page.other.activity.MainActivity";
        this.newComponent = "com.kaijia.see.activity.ArticleWebActivity";
        this.videoComponent = "com.kaijia.see.activity.ArticleWebActivity";
    }

    @Override
    public String getListViewId() {
        return null;
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        return null;
    }

    @Override
    public AccessibilityNodeInfo getVideoSpecialViewById(OperatorHelper operatorHelper) {
        return null;
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
    public void doSomething(OperatorHelper operatorHelper) {

    }
}
