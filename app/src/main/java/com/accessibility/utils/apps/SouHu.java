package com.accessibility.utils.apps;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AppInfo;
import com.accessibility.utils.OperatorHelper;

import java.util.List;

public class SouHu extends AppInfo {
    private boolean isCheckTodayExtraClicked = false;
    private boolean isGetBigRedBag = true;

    public SouHu() {
        this.packageName = "com.sohu.infonews";
        this.startComponent = "com.sohu.quicknews.splashModel.activity.SplashActivity";
        this.mainComponent = "com.sohu.quicknews.homeModel.activity.HomeActivity";
        this.articleComponent = "com.sohu.quicknews.articleModel.activity.DetailActivity";
        this.videoComponent = "com.sohu.quicknews.articleModel.activity.VideoDetailActivity";
    }

    @Override
    public AccessibilityNodeInfo getArticleSpecialViewById(OperatorHelper operatorHelper) {
        List<AccessibilityNodeInfo> nodeList = operatorHelper.findNodesById("com.sohu.infonews:id/btm_divider");
        AccessibilityNodeInfo node;
        for(int i=0, len=nodeList.size(); i<len; i++) {
            node = nodeList.get(i).getParent();
            boolean isCheckedVideo = true;
            if(operatorHelper.isJumpVideo) {
                isCheckedVideo = node.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/ic_play_img").size() == 0;
            }
            if(node.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/left_guess_tv").size() == 0
                && isCheckedVideo) { // 并且不是视频
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
        AccessibilityNodeInfo node;
        List<AccessibilityNodeInfo> nodeList;
        if(!this.isSignin) {
            if(clickFooterMenu(operatorHelper, root, 3)) {
                operatorHelper.changeStatusToSignIn();
                return true;
            }
        }

        // 答题赢狐币 窗口
        nodeList = root.findAccessibilityNodeInfosByText("答题赢狐币");
        if(nodeList.size() > 0) {
            operatorHelper.backToPreviewWindow();
            isGetBigRedBag = false;
            return true;
        }
        // 搜狐抢购商品入口关闭按钮
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/btn_left"));
        // 定量大红包按钮 || 答题送狐币
        if(isGetBigRedBag) operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/red_bags"));
        // 定量大红包开启弹窗
        if(operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/redbag_open"))) {
            // 定量大红包打开后弹窗
            operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/btn_receive"));
        }
        // 定量大红包领取之后的预览弹窗
        nodeList = root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/limit_result_uinavigation");
        if(nodeList.size() > 0) {
            operatorHelper.backToPreviewWindow();
        }

        // 执行弹窗判断
        // com.sohu.infonews:id/redbag_open 限量抢红包弹窗
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/redbag_open"));
        // com.sohu.infonews:id/btn_receive  领取分时金币或者限量红包弹窗后弹窗处理
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/btn_receive"));
        // com.sohu.infonews:id/energy_open 领取金币
        return operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/energy_open"));
    }

    @Override
    public void doSomethingInDetailPage(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return;
        }

        List<AccessibilityNodeInfo> nodeList;
        AccessibilityNodeInfo node;
        // 能量红包
        nodeList = root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/energy_redbag");
        if(nodeList.size() > 0) {
            boolean result = nodeList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            if(!result) {
                node = root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/counting_img").get(0);
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
        // 能量红包弹窗
        nodeList = root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/notice_btn");
        if(nodeList.size() > 0) {
            node = nodeList.get(0);
            if(!node.getText().equals("逛一逛")) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                operatorHelper.changeStatusToFindSohuRedPackageInList();
            }
        }
        // 搜狐金币当日领取完毕，点击测试
        if(operatorHelper.runningCount == 2) {
            nodeList = root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/counting_img");
            if(nodeList.size() > 0) {
                node = nodeList.get(0);
                isCheckTodayExtraClicked = operatorHelper.performClickActionByNode(node);
            }
        }
        if(isCheckTodayExtraClicked) {
            nodeList = operatorHelper.findNodesById("com.sohu.infonews:id/btn");
            if(null != nodeList && nodeList.size() > 0) {
                node = nodeList.get(0);
                if(node.getText().equals("立即领取")) {
                    operatorHelper.performClickActionByNode(node);
                } else {
                    nodeList = operatorHelper.findNodesById("com.sohu.infonews:id/total_progress_tv");
                    node = nodeList.get(0);
                    Log.d("@@@@", "totalSize:"+node.getText().toString());
                    // 验证是否领取完
                    if(node.getText().toString().indexOf("2640/2640") > 0) {
                        operatorHelper.backToPreviewWindow();
                        operatorHelper.judgeAppRunLoop(true); // 强行跳转至下一个app
                        return;
                    } else {
                        operatorHelper.performClickActionByNodeListFirstChild(operatorHelper.findNodesById("com.sohu.infonews:id/counting_dialog_close"));
                        // 新的应用安装包貌似改成了这个id
                        operatorHelper.performClickActionByNodeListFirstChild(operatorHelper.findNodesById("com.sohu.infonews:id/close"));
                    }
                }
            } else {
                operatorHelper.performClickActionByNodeListFirstChild(operatorHelper.findNodesById("com.sohu.infonews:id/counting_dialog_close"));
                operatorHelper.performClickActionByNodeListFirstChild(operatorHelper.findNodesById("com.sohu.infonews:id/close"));
            }
        }
    }

    @Override
    public boolean signin(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return false;
        }

        // 搜狐资讯，邀请好友红包，直接跳过
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/act_close_image"));
        if(operatorHelper.runningCount == operatorHelper.maxRunningCount-2) {
            List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/task_entrance_gv");
            if(nodeList.size() > 0) {
                AccessibilityNodeInfo btnGroup = nodeList.get(0);
                if (btnGroup.getChildCount() > 0) {
                    AccessibilityNodeInfo signBtn = btnGroup.getChild(0);
                    // 检测是否有异常情况
                    if(signBtn.findAccessibilityNodeInfosByText("签到").size() == 0) {
                        return false;
                    }

                    operatorHelper.performClickActionByNode(signBtn);
                    operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/btn_receive"));
                    this.isSignin = true;
                }
            }

            return true;
        }
        if(operatorHelper.runningCount == operatorHelper.maxRunningCount-1) {
            if(this.isSignin) {
                operatorHelper.backToPreviewWindow();
            }
        }
        if(operatorHelper.runningCount == operatorHelper.maxRunningCount) {
            operatorHelper.backToPreviewWindow();
            clickFooterMenu(operatorHelper, root, 0);
        }

        return true;
    }

    @Override
    public void doSomethingInOpeningApp(OperatorHelper operatorHelper) {
        AccessibilityNodeInfo root = operatorHelper.getRootNodeInfo();
        if(null == root) {
            return ;
        }

        this.isGetBigRedBag = true; // 每一个轮回开启， 以防止在中途被关闭
        // 升级按钮
        operatorHelper.performClickActionByNodeListFirstChild(root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/normaldlg_btn_close"));
    }

    private boolean clickFooterMenu(OperatorHelper operatorHelper, AccessibilityNodeInfo root, int index) {
        List<AccessibilityNodeInfo> nodeList = root.findAccessibilityNodeInfosByViewId("com.sohu.infonews:id/footer_view");
        if(nodeList.size() > 0) {
            AccessibilityNodeInfo footerNode = nodeList.get(0);
            if(footerNode.getChildCount() > index) {
                operatorHelper.performClickActionByNode(footerNode.getChild(index));
                return true;
            }
        }

        return false;
    }
}
