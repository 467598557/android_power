package com.accessibility.utils;

import android.content.Context;

import com.accessibility.utils.apps.ChengZiKuaiBao;
import com.accessibility.utils.apps.JuKanDian;
import com.accessibility.utils.apps.NiuNiuZiXun;
import com.accessibility.utils.apps.QuTouTiao;
import com.accessibility.utils.apps.ShanDianHeZi;
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
    public final static int StatusSignIn = 7;
    public final static String LoopCount = "loop_count";
    public final static String AppRunMinuteCount = "app_run_minute_count";
    public final static String AppAddChengZiKuaiBao = "app_add_chengzikuaibao";
    public final static String AppAddJuKanDian = "app_add_jukandian";
    public final static String AppAddNiuNiuZiXun = "app_add_niuniuzixun";
    public final static String AppAddQuTouTiao = "app_add_qutoutiao";
    public final static String AppAddSouHuZiXun = "app_add_souhuzixun";
    public final static String AppAddWeiLiKanKan = "app_add_weilikankan";
    public final static String AppAddShanDianHeZi = "app_add_shandianhezi";
    public static ArrayList<AppInfo> mAppList = new ArrayList<AppInfo>();

    public static ArrayList<AppInfo> getAppList(Context context) {
        if (mAppList.size() == 0) {
            if((boolean)SPUtil.get(context, Constant.AppAddSouHuZiXun, new Boolean(true))) {
                mAppList.add(new SouHu());
            }
            if((boolean)SPUtil.get(context, Constant.AppAddChengZiKuaiBao, new Boolean(true))) {
                mAppList.add(new ChengZiKuaiBao());
            }
            if((boolean)SPUtil.get(context, Constant.AppAddWeiLiKanKan, new Boolean(true))) {
                mAppList.add(new WeLiKanKan());
            }
            if((boolean)SPUtil.get(context, Constant.AppAddNiuNiuZiXun, new Boolean(true))) {
                mAppList.add(new NiuNiuZiXun());
            }
            if((boolean)SPUtil.get(context, Constant.AppAddJuKanDian, new Boolean(true))) {
                mAppList.add(new JuKanDian());
            }
            if((boolean)SPUtil.get(context, Constant.AppAddQuTouTiao, new Boolean(true))) {
                mAppList.add(new QuTouTiao());
            }
            if((boolean)SPUtil.get(context, Constant.AppAddShanDianHeZi, new Boolean(true))) {
                mAppList.add(new ShanDianHeZi());
            }
        }

        return mAppList;
    }
}
