package com.accessibility.utils.apps;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

import java.util.List;

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
        List<AccessibilityNodeInfo> nodeInfoList = operatorHelper.findNodesById("com.huolea.bull:id/title_tv");
        AccessibilityNodeInfo node;
        for(int i=0, len=nodeInfoList.size(); i<len; i++) {
            node = nodeInfoList.get(i).getParent();
            if(!node.getClassName().equals("android.widget.RelativeLayout")) {
                node = node.getParent();
            }
            if(node.findAccessibilityNodeInfosByText("广告").size() == 0 && node.findAccessibilityNodeInfosByText("阅读赚金币").size() == 0) {
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
    public String getArticlePageContentViewId() {
        return null;
    }

    @Override
    public String getVideoPageContentVideoId() {
        return null;
    }

    @Override
    public void doSomething(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return;
        }

        List<AccessibilityNodeInfo> nodeInfoList;
        AccessibilityNodeInfo nodeInfo;
        // 金币领取后再点击后弹窗
        nodeInfoList = root.findAccessibilityNodeInfosByViewId("com.huolea.bull:id/id_dialog_datecoins_close");
        if(nodeInfoList.size() > 0) {
            nodeInfoList.get(0).getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        // 金币领取后弹窗 com.huolea.bull:id/id_dialog_datecoins_cancel
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.huolea.bull:id/id_dialog_datecoins_cancel"));
        //  金币容器
        nodeInfoList = root.findAccessibilityNodeInfosByViewId("com.huolea.bull:id/id_fragment_information_datecoins_layout");
        if(nodeInfoList.size() > 0) {
            nodeInfo = nodeInfoList.get(0);
            if(nodeInfo.findAccessibilityNodeInfosByText("领取").size() > 0) { // 再次确认是金币
                nodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }


    }

    @Override
    public void doSomethingInDetailPage(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            Log.d("@@@", "doSomethingInDetailPage in NiuNiuZiXun, root = null");
            return;
        }

        List<AccessibilityNodeInfo> nodeInfoList;
        // 展开全文
        nodeInfoList = root.findAccessibilityNodeInfosByViewId("com.huolea.bull:id/id_activity_news_details_web");
        AccessibilityNodeInfo node = nodeInfoList.get(0);
//        Log.d("@@@", "doSomethingInDetailPage"+node);
//        Log.d("@@@", "查看更多按钮获取"+node.findAccessibilityNodeInfosByText("展开全文"));
    }
}
