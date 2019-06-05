package com.accessibility.utils.apps;

import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

import java.util.List;

// #TODO 待处理
public class HuiTouTiao extends AppInfo {
    public HuiTouTiao() {
        this.packageName = "com.sohu.infonews";
        this.startComponent = "com.sohu.quicknews.splashModel.activity.SplashActivity";
        this.mainComponent = "com.sohu.quicknews.homeModel.activity.HomeActivity";
    }

    @Override
    public String getListViewId() {
        return "";
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("com.sohu.infonews:id/btm_divider");
        AccessibilityNodeInfo node;
        for(int i=0, len=nodeList.size(); i<len; i++) {
            node = nodeList.get(i).getParent();
            if(node.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/left_guess_tv").size() == 0) {
                return node;
            }
        }

        return null;
    }

    @Override
    public AccessibilityNodeInfo getVideoSpecialViewById(OperatorHelper operatorHelper) {
        return getArticleSpecialViewById(operatorHelper);
    }

    @Override
    public boolean doSomething(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

        return true;
    }

    @Override
    public void doSomethingInDetailPage(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return;
        }

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
    }


    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {

    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        return true;
    }
}
