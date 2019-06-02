package com.accessibility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
                EditText loopCount = (EditText)findViewById(R.id.loop_count);
                EditText appRunMinuteCount = (EditText)findViewById(R.id.run_minute_count);
                Context appContext = getApplicationContext();
                // 设置值到缓存
                SPUtil.putAndApply(appContext, Constant.LoopCount, Integer.valueOf(loopCount.getText().toString()));
                SPUtil.putAndApply(appContext, Constant.AppRunMinuteCount, Integer.valueOf(appRunMinuteCount.getText().toString()));
                SPUtil.putAndApply(appContext, Constant.AppAddChengZiKuaiBao, chengzikuaibao.isChecked());
                SPUtil.putAndApply(appContext, Constant.AppAddJuKanDian, jukandian.isChecked());
                SPUtil.putAndApply(appContext, Constant.AppAddNiuNiuZiXun, niuniuzixun.isChecked());
                SPUtil.putAndApply(appContext, Constant.AppAddQuTouTiao, qutoutiao.isChecked());
                SPUtil.putAndApply(appContext, Constant.AppAddSouHuZiXun, souhuzixun.isChecked());
                SPUtil.putAndApply(appContext, Constant.AppAddWeiLiKanKan, weilikankan.isChecked());

                Log.d("@@@ LoopCount", appRunMinuteCount.getText().toString());
                Log.d("@@@ AppRunMinuteCount", loopCount.getText().toString());
                Intent intent = new Intent(getApplicationContext(),  AccessibilityStartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
