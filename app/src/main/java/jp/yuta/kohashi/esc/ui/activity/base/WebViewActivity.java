package jp.yuta.kohashi.esc.ui.activity.base;

import android.os.Bundle;
import android.webkit.WebView;

import jp.yuta.kohashi.esc.R;

public class WebViewActivity extends BaseActivity {
    protected static final int FONT_SIZE_WEB_VIEW = 16;
    protected WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mWebView = (WebView)findViewById(R.id.web_view);
        mWebView.getSettings().setDefaultFontSize(FONT_SIZE_WEB_VIEW);

        initToolbar();
        enableBackBtn();
    }

    protected void setHtml(String html){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
        mWebView.setHorizontalScrollBarEnabled(false);
    }

}
