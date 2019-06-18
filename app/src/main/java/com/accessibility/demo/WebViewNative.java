package com.accessibility.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.accessibility.R;

public class WebViewNative extends Activity implements View.OnClickListener {
    WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_native);

        mWebview = findViewById(R.id.show_webview_wv);
        mWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebview.loadUrl("http://www.baidu.com");

        findViewById(R.id.webview_btn1).setOnClickListener(this);
        findViewById(R.id.webview_btn2).setOnClickListener(this);
        findViewById(R.id.webview_btn3).setOnClickListener(this);
        findViewById(R.id.webview_btn4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.webview_btn1:
                mWebview.loadUrl("http://www.baidu.com");
                break;
            case R.id.webview_btn2:
                mWebview.loadUrl("http://m.sohu.com/");
                break;
            case R.id.webview_btn3:
                mWebview.loadUrl("https://weibo.com/u/2745871101/home");
                break;
            case R.id.webview_btn4:
                mWebview.loadUrl("https://m.mi.com/");
                break;
        }
    }
}
