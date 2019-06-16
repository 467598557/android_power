package com.accessibility.utils.apps;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;
import java.util.List;

public class ShanDianHeZi extends AppInfo {
    public ShanDianHeZi() {
        this.packageName = "c.l.a";
        this.startComponent = "c.l.a.views.AppBoxWelcomeActivity";
        this.mainComponent = "c.l.a.views.AppBoxHomeActivity";
        this.articleComponent = "android.widget.LinearLayout";
        this.videoComponent = "android.widget.FrameLayout";
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("c.l.a:id/from_text");
        AccessibilityNodeInfo node;
        for(int i=0, len=nodeList.size(); i<len; i++) {
            node = nodeList.get(i).getParent();
            boolean isCheckedVideo = true;
            if(operatorHelper.isJumpVideo) {
                isCheckedVideo = node.findAccessibilityNodeInfosByViewId("c.l.a:id/play").size() == 0
                        && (node.findAccessibilityNodeInfosByViewId("c.l.a:id/time").size() == 0);
            }

            if(node.isClickable() && node.findAccessibilityNodeInfosByText("广告").size() == 0
                    && node.getClassName().toString().equals("android.widget.LinearLayout") && isCheckedVideo
                    && node.findAccessibilityNodeInfosByText("抖音短视频").size() == 0
                    // 必须是三张图片的，尽量避开视频
                    && node.findAccessibilityNodeInfosByViewId("c.l.a:id/img_container").size() > 0) {
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

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
        if(operatorHelper.isJumpVideo) {
            nodeList = root.findAccessibilityNodeInfosByViewId("c.l.a:id/viedoContainer");
            if(nodeList.size() > 0) {
                operatorHelper.changeStatusToList();
                return;
            }
        }
        // 定时大红包 c.l.a:id/reward_text
        if(operatorHelper.runningCount <= 1) {
            // 定时大红包 计算位置
            int winHeight = operatorHelper.winHeight;
            int winWidth = operatorHelper.winWidth;
            float y = 0;
            if(winHeight > 1920) {
                y = (float)(winHeight*0.735+50);
            } else if(winHeight > 1520) {
                y = winHeight - 520;
            } else {
                y = winHeight - 350;
            }

            operatorHelper.clickInScreenPoint(winWidth-50, y);
        }

        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("c.l.a:id/button"));
    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root  = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

        // 大红包弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("c.l.a:id/image1"));
        // 大红包弹窗后的窗口
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("c.l.a:id/btn_get_money"));
        if(operatorHelper.runningCount == operatorHelper.maxRunningCount-2) {
            List<AccessibilityNodeInfo> nodeList  = root.findAccessibilityNodeInfosByViewId("c.l.a:id/red_pack_signed_btn");
            if(nodeList.size() > 0) {
                AccessibilityNodeInfo signinBtn = nodeList.get(0);
                if(null != signinBtn) {
                    operatorHelper.performClickActionByNode(signinBtn); // 点击签到
                }
            }
        }

        if(operatorHelper.runningCount == operatorHelper.maxRunningCount - 1) {
            operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("c.l.a:id/close_sign_dialog_btn"));
        }

        if(operatorHelper.runningCount == operatorHelper.maxRunningCount) {
            // 回首页
            clickMainMenuByIndex(operatorHelper, root, 0);
            this.isSignin = true;
        }

        return true;
    }

    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {
    }

    private boolean clickMainMenuByIndex(OperatorHelper operatorHelper, AccessibilityNodeInfo root, int index) {
        List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByViewId("c.l.a:id/bottom_navigation");
        if(nodeList.size() > 0) {
            AccessibilityNodeInfo btnGroup = nodeList.get(nodeList.size() - 1);
            if(btnGroup.getChildCount() > index) {
                AccessibilityNodeInfo btn = btnGroup.getChild(index);
                operatorHelper.performClickActionByNode(btn);
                return true;
            }
        }

        return false;
    }
}
