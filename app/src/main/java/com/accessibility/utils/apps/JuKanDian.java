package com.accessibility.utils.apps;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

public class JuKanDian extends AppInfo {
    public JuKanDian() {
        this.packageName = "com.xiangzi.jukandian";
        this.startComponent = "com.xiangzi.jukandian.activity.V2WelcomeActivity";
        this.mainComponent = "com.xiangzi.jukandian.activity.MainActivity";
        this.newComponent = "com.xiangzi.jukandian.activity.WebViewActivity";
        this.videoComponent = "com.xiangzi.jukandian.activity.WebViewActivity";
    }

    @Override
    public String getListViewId() {
        return "";
    }

    @Override
    public String getArticleSpecialViewId() {
        return "com.xiangzi.jukandian:id/item_artical_right_parent";
    }

    @Override
    public String getVideoSpecialViewId() {
        return "com.xiangzi.jukandian:id/item_artical_right_parent";
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
