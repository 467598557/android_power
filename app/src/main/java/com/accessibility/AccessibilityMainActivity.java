package com.accessibility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.accessibility.utils.Constant;
import com.accessibility.utils.SPUtil;

public class AccessibilityMainActivity extends Activity implements View.OnClickListener {
    private View mOpenSetting;
    private static final int NO_1 =0x1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_main);
        initView();
    }

    private void initView() {
        mOpenSetting = findViewById(R.id.open_accessibility_setting);
        mOpenSetting.setOnClickListener(this);
        findViewById(R.id.trigger_accessibility_start_event).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.open_accessibility_setting:
                OpenAccessibilitySettingHelper.jumpToSettingPage(getApplicationContext());
                break;
            case R.id.trigger_accessibility_start_event:
                CheckBox chengzikuaibao = (CheckBox)findViewById(R.id.chengzikuaibao);
                CheckBox jukandian = (CheckBox)findViewById(R.id.jukandian);
                CheckBox niuniuzixun = (CheckBox)findViewById(R.id.niuniuzixun);
                CheckBox qutoutiao = (CheckBox)findViewById(R.id.qutoutiao);
                CheckBox souhuzixun = (CheckBox)findViewById(R.id.souhuzixun);
                CheckBox weilikankan = (CheckBox)findViewById(R.id.weilikankan);
                CheckBox shandianhezi = (CheckBox)findViewById(R.id.shandianhezi);
                EditText loopCount = (EditText)findViewById(R.id.loop_count);
                EditText beginRunIndex = (EditText)findViewById(R.id.begin_run_index);
                EditText appRunMinuteCount = (EditText)findViewById(R.id.run_minute_count);
                Context appContext = getApplicationContext();
                // 设置值到缓存
                SPUtil.putAndApply(appContext, Constant.AppBeginRunIndex, Integer.valueOf(beginRunIndex.getText().toString()));
                SPUtil.putAndApply(appContext, Constant.LoopCount, Integer.valueOf(loopCount.getText().toString()));
                SPUtil.putAndApply(appContext, Constant.AppRunMinuteCount, Integer.valueOf(appRunMinuteCount.getText().toString()));
                SPUtil.putAndApply(appContext, Constant.AppAddChengZiKuaiBao, chengzikuaibao.isChecked());
                SPUtil.putAndApply(appContext, Constant.AppAddJuKanDian, jukandian.isChecked());
                SPUtil.putAndApply(appContext, Constant.AppAddNiuNiuZiXun, niuniuzixun.isChecked());
                SPUtil.putAndApply(appContext, Constant.AppAddQuTouTiao, qutoutiao.isChecked());
                SPUtil.putAndApply(appContext, Constant.AppAddSouHuZiXun, souhuzixun.isChecked());
                SPUtil.putAndApply(appContext, Constant.AppAddShanDianHeZi, shandianhezi.isChecked());
                SPUtil.putAndApply(appContext, Constant.AppAddWeiLiKanKan, weilikankan.isChecked());

                Intent intent = new Intent(getApplicationContext(),  AccessibilityStartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);

                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(getApplicationContext())) {
                        Intent intent2 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent2, 1);
                    } else {
                        FloatWindowManager.createBigWindow(getApplicationContext());
                    }
                } else {
                    FloatWindowManager.createBigWindow(getApplicationContext());
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
