package jp.yuta.kohashi.esc.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import jp.yuta.kohashi.esc.R;

public abstract class BaseWebViewActivity extends BaseActivity {
    private static final int FONT_SIZE_WEB_VIEW = 16;
    protected WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mWebView = (WebView)findViewById(R.id.web_view);
        mWebView.getSettings().setDefaultFontSize(FONT_SIZE_WEB_VIEW);
        mWebView.setHorizontalScrollBarEnabled(false);

        initToolbar();
        enableBackBtn();
    }

    protected void setHtml(String link){
        mWebView.loadUrl(link);
    }

    protected void disableCopyPaste(){
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {

            public boolean onLongClick(View v) {
                return true;
            }
        });
    }
}
