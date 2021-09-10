package com.stych.android;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initiate();
        defaultHeaderInitiate(getResources().getString(R.string.terms));
    }

    private void initiate() {
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
        });
        webView.loadUrl("https://stych.nyc3.digitaloceanspaces.com/app/v1/terms.html");
    }
}