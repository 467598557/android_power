package com.accessibility.utils.apps;

import com.accessibility.utils.AppInfo;

public class QuTouTiao extends AppInfo {
    public QuTouTiao() {
        this.packageName = "com.jifen.qukan";
        this.startComponent = "com.jifen.qkbase.main.MainActivity";
        this.mainComponent = "com.jifen.qkbase.main.MainActivity";
        this.newComponent = "com.jifen.qukan.content.newsdetail.news.NewsDetailNewActivity";
        this.videoComponent = "com.jifen.qukan.content.newsdetail.video.VideoNewsDetailNewActivity";
    }

    public String getListViewId() {
        return "com.jifen.qukan:id/no";
    }

    @Override
    public String getArticleSpecialViewId() {
        return "com.jifen.qukan:id/a59";
    }

    @Override
    public String getVideoSpecialViewId() {
        return "com.jifen.qukan:id/a3x";
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
