package com.accessibility.utils;

import com.accessibility.utils.apps.ChengZiKuaiBao;
import com.accessibility.utils.apps.JuKanDian;
import com.accessibility.utils.apps.NiuNiuZiXun;
import com.accessibility.utils.apps.QuTouTiao;
import com.accessibility.utils.apps.SouHu;
import com.accessibility.utils.apps.WeLiKanKan;

import java.util.ArrayList;

public class Constant {
    public final static int StatusOpeningApp = 1;
    public final static int StatusInList = 2;
    public final static int StatusInReadingArticle = 3;
    public final static int StatusInReadingVideo = 4;
    public final static int StatusWaiting = 5;
    public final static int FindSohuRedPackageInList = 6;
    public static ArrayList<AppInfo> mAppList = new ArrayList<AppInfo>();

    public static ArrayList<AppInfo> getAppList() {
        if (mAppList.size() == 0) {
            mAppList.add(new ChengZiKuaiBao());
            mAppList.add(new WeLiKanKan());
            mAppList.add(new NiuNiuZiXun());
            mAppList.add(new SouHu());
            mAppList.add(new JuKanDian());
            mAppList.add(new QuTouTiao());
        }

        return mAppList;
    }
}
