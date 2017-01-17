package jp.yuta.kohashi.esc.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import jp.yuta.kohashi.esc.Const;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.NewsModel;
import jp.yuta.kohashi.esc.util.RegexManager;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String NEWS_MODEL = "newsModel";
    public static final String NEWS_HTML = "newsHtml";
    private static final int FONT_SIZE_WEB_VIEW = 16; //webView font size
    private String html;
    private Toolbar mToolbar;
    private Button mDownloadBtn;
    private NewsModel newsModel;
    private List<String> downloadUrls;
    private List<String> downloadTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        if (downloadTitles == null) {
            downloadUrls = new ArrayList<>();
            downloadTitles = new ArrayList<>();
        } else {
            downloadUrls.clear();
            downloadTitles.clear();
        }

        Intent intent = getIntent();
        newsModel = (NewsModel) intent.getSerializableExtra(NEWS_MODEL);
        html = intent.getStringExtra(NEWS_HTML);

        initToolbar();
        initView();
    }

    private void initView() {
        getDownloadTitleUrl(html);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setDefaultFontSize(FONT_SIZE_WEB_VIEW);

        TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
        TextView dateTextView = (TextView) findViewById(R.id.date_text_view);
        mDownloadBtn = (Button) findViewById(R.id.btn_download);
        mDownloadBtn.setOnClickListener(this);

        if (downloadCount() == 0) {
            mDownloadBtn.setText("添付ファイルはありません");
            mDownloadBtn.setEnabled(false);
        } else {
            mDownloadBtn.setText("添付ファイル " + downloadCount() + " 件あります");
            mDownloadBtn.setEnabled(true);
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL("", getMainText(html), "text/html", "UTF-8", "");
        webView.setHorizontalScrollBarEnabled(false);

        mToolbar.setTitle(newsModel.getTitle());
        titleTextView.setText(newsModel.getTitle());
        dateTextView.setText(newsModel.getDate());

        // disable scroll on touch
        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
    }

    private void initToolbar() {
        //ツールバーをActionBarとして扱う
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(newsModel.getTitle());
            mToolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(mToolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private String getMainText(String html) {
        html = RegexManager.replaceCRLF(html, true);
        return RegexManager.narrowingValues("<p class=\"body clear\">", "</p>", html);
    }

    private void getDownloadTitleUrl(String html) {
        html = RegexManager.replaceCRLF(html, true);
        String narrowHtml = RegexManager.narrowingValues("<h3>添付ファイル</h3>", "<li class=\"clear\">", html);
        Matcher match = RegexManager.getGroupValues("<a href=\"(.+?)\">", narrowHtml);
        while (match.find()) {
            downloadUrls.add(match.group(1));
        }

        Matcher match2 = RegexManager.getGroupValues("<a href=\"[^\"]*\">(.+?)</a>", narrowHtml);
        while (match2.find()) {
            downloadTitles.add(match2.group(1));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_download) {
            if (downloadTitles.size() > 0) {
                showListDialog();
            }
        }
    }

    /**
     * ダウンロードリストをダイアログで表示
     */
    private void showListDialog() {
        new MaterialDialog.Builder(this)
                .title("添付ファイルをダウンロード")
                .items(downloadTitles)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        downloadFile(downloadUrls.get(which));
                    }
                })
                .show();
    }

    /**
     * 　添付ファイルをダウンロード
     *
     * @param url
     */
    private void downloadFile(String url) {
        Uri uri = Uri.parse(url);
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setShowTitle(false)
                .enableUrlBarHiding()
                .setCloseButtonIcon(null)
                .setToolbarColor(getResources().getColor(android.R.color.white))
                .build();
        customTabsIntent.intent.setData(uri);
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (TextUtils.equals(packageName, Const.CHROME_PACKAGE_NAME))
                customTabsIntent.intent.setPackage(Const.CHROME_PACKAGE_NAME);
        }
        customTabsIntent.launchUrl(NewsDetailActivity.this, uri);
    }


    /**
     * ダウンロード数を返す
     *
     * @return
     */
    private int downloadCount() {
        return downloadTitles.size();
    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        finish();
//    }
}
