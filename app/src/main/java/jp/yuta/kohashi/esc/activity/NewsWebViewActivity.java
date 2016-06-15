package jp.yuta.kohashi.esc.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import jp.yuta.kohashi.esc.R;

public class NewsWebViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ImageButton imgButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web_view);

        //タイトル設定
        toolbar= (Toolbar)findViewById(R.id.toolbar_news_web);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        String html = intent.getStringExtra("html");
        WebView webview = (WebView)findViewById(R.id.webView3);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");

        toolbar.setTitle(intent.getStringExtra("title"));

        imgButton = (ImageButton)findViewById(R.id.web_containts__back_button);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // disable scroll on touch
        webview.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

    }


}
