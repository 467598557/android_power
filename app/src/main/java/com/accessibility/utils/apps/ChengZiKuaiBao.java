package com.accessibility.utils.apps;

import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

import java.util.List;

public class ChengZiKuaiBao extends AppInfo {
    public ChengZiKuaiBao() {
        this.packageName = "com.quyu.youliao";
        this.startComponent = "com.koala.gold.toutiao.MainActivity";
        this.mainComponent = "com.koala.gold.toutiao.activity.HomeActivity";
        this.articleComponent = "com.koala.gold.toutiao.activity.ArticleDetailActivity";
        this.videoComponent = "";
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeInfoList = operatorHelper.findNodesById("com.quyu.youliao:id/content_view");
        AccessibilityNodeInfo node;
        for(int i=1, len=nodeInfoList.size(); i<len; i++) {
            node = nodeInfoList.get(i);
            AccessibilityNodeInfo child = null;
            if(node.getChildCount() > 0) {
                child = node.getChild(0);
            }
            if(null != child && !child.isSelected() && node.findAccessibilityNodeInfosByText("广告").size() == 0) {
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

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
        if(root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/ll_share_layout").size() > 0) {
            operatorHelper.backToPreviewWindow();
            return false;
        }
        // com.quyu.youliao:id/iv_close 列表页新人福利社等弹窗
        nodeList = root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/iv_close");
        if(nodeList.size() > 0) {
            AccessibilityNodeInfo specialNode;
            List<AccessibilityNodeInfo> titleList;
            for(int i=0, len=nodeList.size(); i<len; i++) {
                node = nodeList.get(i);
                specialNode = node.getParent().getParent();
                // 如果不是文章或者广告的条目
                if(specialNode.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/ll_info_layout").size() == 0 &&
                    specialNode.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/ll_ad_v").size() == 0 &&
                        specialNode.findAccessibilityNodeInfosByText("广告").size() == 0) {
                    operatorHelper.performClickActionByNode(node);
                }
            }
        }

        if(!this.isSignin) {
            clickMainMenuByIndex(operatorHelper, root, 3);
            operatorHelper.changeStatusToSignIn();
        }

        return true;
    }

    @Override
    public void doSomethingInDetailPage(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return;
        }

        List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/iv_click_text");
        if(nodeList.size() > 0) {
            operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/iv_redbag"));
        }

        // 查看更多  com.quyu.youliao:id/ll_expand
        if(operatorHelper.runningCount % 2 == 0 && operatorHelper.runningCount % 3 != 0) {
            nodeList = root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/ll_expand");
            if(nodeList.size() > 0) {
                AccessibilityNodeInfo node = nodeList.get(0);
                if(node.getText().toString().indexOf("点击查看全文") >= 0) {
                    operatorHelper.performClickActionByNode(node);
                }
            }
        }
    }

    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return;
        }

        // com.quyu.youliao:id/root_layout 新人福利社
        if(operatorHelper.runningCount == 10) {
            List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/title_container");
            if(nodeList.size() > 0) {
                AccessibilityNodeInfo btnGroup = nodeList.get(0);
                AccessibilityNodeInfo node;

                int childCount = btnGroup.getChildCount();
                int curLoopCount = operatorHelper.curLoopCount;
                if(childCount > curLoopCount) {
                    node = btnGroup.getChild(curLoopCount);
                } else {
                    node = btnGroup.getChild(curLoopCount % childCount);
                }

                if(null != node) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root  = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

        if(operatorHelper.runningCount == operatorHelper.maxRunningCount-2) {
            // 任务界面拆红包弹窗
            operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("c.l.a:id/btn_close"));
            List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/btn_sign");
            if(nodeList.size() > 0) {
                AccessibilityNodeInfo signBtn = nodeList.get(0);
                operatorHelper.performClickActionByNode(signBtn);
            }
        }

        if(operatorHelper.runningCount == operatorHelper.maxRunningCount) {
            if(clickMainMenuByIndex(operatorHelper, root, 0)) {
                this.isSignin = true;
            }
        }

        return true;
    }

    private boolean clickMainMenuByIndex(OperatorHelper operatorHelper, AccessibilityNodeInfo root, int index) {
        List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByViewId("com.quyu.youliao:id/title_container");
        if(nodeList.size() > 0) {
            // 取最后一个
            AccessibilityNodeInfo btnGroup = nodeList.get(nodeList.size() - 1);
            if(btnGroup.getChildCount() > index) {
                return operatorHelper.performClickActionByNode(btnGroup.getChild(index));
            }
        }

        return false;
    }
}
