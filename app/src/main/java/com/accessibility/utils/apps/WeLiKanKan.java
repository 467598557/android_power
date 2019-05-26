package com.accessibility.utils.apps;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

public class WeLiKanKan extends AppInfo {
    public WeLiKanKan() {
        this.packageName = "cn.weli.story";
        this.startComponent = "cn.etouch.ecalendar.MainActivity";
        this.mainComponent = "cn.etouch.ecalendar.MainActivity";
        this.newComponent = "cn.etouch.ecalendar.tools.life.LifeDetailsActivity";
        this.videoComponent = "com.lightsky.video.videodetails.ui.activity.VideoDetailsActivity";
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

    @Override
    public void doSomething(OperatorHelper operatorHelper) {

    }
}
