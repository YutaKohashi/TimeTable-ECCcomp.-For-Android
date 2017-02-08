package jp.yuta.kohashi.esc.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
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
import jp.yuta.kohashi.esc.model.NewsItem;
import jp.yuta.kohashi.esc.ui.activity.base.BaseActivity;
import jp.yuta.kohashi.esc.util.RegexUtil;

import static android.view.View.GONE;

public class NewsDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String NEWS_MODEL = "newsItem";
    public static final String NEWS_HTML = "newsHtml";
    private static final int FONT_SIZE_WEB_VIEW = 16; //webView font size
    private String html;
    private Button mDownloadBtn;
    private NewsItem newsItem;
    private List<String> downloadUrls;
    private List<String> downloadTitles;

    private WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        newsItem = (NewsItem) intent.getSerializableExtra(NEWS_MODEL);
        html = intent.getStringExtra(NEWS_HTML);

        initToolbar();
        enableBackBtn();
        setToolbarTitle(newsItem.getTitle());

        initView();
    }

    private void initView() {
        getDownloadTitleUrl(html);

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setDefaultFontSize(FONT_SIZE_WEB_VIEW);

        TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
        TextView dateTextView = (TextView) findViewById(R.id.date_text_view);
        mDownloadBtn = (Button) findViewById(R.id.btn_download);
        mDownloadBtn.setOnClickListener(this);

        if (downloadCount() == 0) {
            mDownloadBtn.setVisibility(GONE);
        } else {
            mDownloadBtn.setVisibility(View.VISIBLE);
            mDownloadBtn.setText(getResources().getString(R.string.attachment_file, downloadCount()));
            mDownloadBtn.setEnabled(true);
        }

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadDataWithBaseURL("", getMainText(html), "text/html", "UTF-8", "");
        mWebView.setHorizontalScrollBarEnabled(false);

        mToolbar.setTitle(newsItem.getTitle());
        titleTextView.setText(newsItem.getTitle());
        dateTextView.setText(newsItem.getDate());

        mWebView.setOnTouchListener(((view, motionEvent) ->
                (motionEvent.getAction() == MotionEvent.ACTION_MOVE)));

    }

    private String getMainText(String html) {
        html = RegexUtil.replaceCRLF(html, true);
        return RegexUtil.narrowingValues("<p class=\"body clear\">", "</p>", html);
    }

    private void getDownloadTitleUrl(String html) {
        html = RegexUtil.replaceCRLF(html, true);
        String narrowHtml = RegexUtil.narrowingValues("<h3>添付ファイル</h3>", "</ul>", html);
        Matcher match = RegexUtil.getGroupValues("<a href=\"(.+?)\">", narrowHtml);
        while (match.find()) {
            downloadUrls.add(match.group(1));
        }

        Matcher match2 = RegexUtil.getGroupValues("<a href=\"[^\"]*\">(.+?)</a>", narrowHtml);
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
                .title(getResources().getString(R.string.download_attachment_file))
                .items(downloadTitles)
                .itemsCallback(((dialog, itemView, position, text) -> {
                    downloadFile(downloadUrls.get(position));
                }))
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mWebView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    /**
     * ダウンロード数を返す
     *
     * @return
     */
    private int downloadCount() {
        return downloadTitles.size();
    }

}
