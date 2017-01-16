package jp.yuta.kohashi.esc.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import jp.yuta.kohashi.esc.R;

public class NewsWebViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    ImageButton imgButton;
    TextView titleTextView;
    TextView dateTextView;

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web_view);

        //タイトル設定
        toolbar= (Toolbar)findViewById(R.id.toolbar_news_web);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        String html = intent.getStringExtra("html");

        webView = (WebView)findViewById(R.id.webView3);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
        webView.setHorizontalScrollBarEnabled(false);

        String title = intent.getStringExtra("title");
        toolbar.setTitle(title);

        titleTextView = (TextView)findViewById(R.id.title_name);
        titleTextView.setText(title);
        dateTextView = (TextView)findViewById(R.id.date_text_news);
        String date = intent.getStringExtra("date");
        dateTextView.setText(date);
        imgButton = (ImageButton)findViewById(R.id.web_containts__back_button);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // disable scroll on touch
        webView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });


    }
}
