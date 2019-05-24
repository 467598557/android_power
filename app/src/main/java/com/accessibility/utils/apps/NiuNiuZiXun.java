package com.accessibility.utils.apps;

import com.accessibility.utils.AppInfo;

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
    public String getArticleSpecialViewId() {
        return null;
    }

    @Override
    public String getVideoSpecialViewId() {
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
}
