package jp.yuta.kohashi.esc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.ProgressView;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.network.api.model.news.NewsDetail;
import jp.yuta.kohashi.esc.network.api.model.news.NewsItem;
import jp.yuta.kohashi.esc.ui.activity.base.BaseActivity;
import jp.yuta.kohashi.esc.util.NotifyUtil;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

import static android.view.View.GONE;

//import jp.yuta.kohashi.esc.model.NewsItem;

public class NewsDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String NEWS_MODEL = "newsDetail";

    private Button mDownloadBtn;
    private TextView mBodyTextView;
    private NewsItem newsItem;
    private NewsDetail newsDetail;
    private String userId;
    private String password;
    private ProgressView mProgressView;
//    private MaterialProgressBar mMaterialProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        userId = PrefUtil.getId();
        password = PrefUtil.getPss();
        newsDetail = null;
        Intent intent = getIntent();
        newsItem = (NewsItem) intent.getSerializableExtra(NEWS_MODEL);


        initToolbar();
        enableBackBtn();
        setToolbarTitle(newsItem.getTitle());

        initView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (newsDetail == null){
            /**
             * インターネット接続チェックはAcitivty遷移前に行う
             */
            new Thread(() -> {
                HttpConnector.request(newsItem.getId(), userId, password, newsDetail -> {
                    runOnUiThread(() -> {
                        if (newsDetail != null) {
                            this.newsDetail = newsDetail;
                            runOnUiThread(() -> {
                                setBodyText();
                            });

                        } else {
                            // ニュース詳細取得失敗
                            NotifyUtil.failureGetNewsDetail();
                            finish();
                        }
                    });
                });
            }).start();

        } else {
            setBodyText();
        }
    }

    /**
     * 記事の内容を表示
     * 添付ファイルボタンの表示
     */
    private void setBodyText(){
//        mProgressView.stop();
        mProgressView.setVisibility(View.INVISIBLE);
//        mMaterialProgressBar.setVisibility(View.INVISIBLE);
        String body = newsDetail.getBody();
        body = body.replace("&lt;","<");
        body = body.replace("&gt;",">");
        mBodyTextView.setText(body);

        if (newsDetail.getNewsFiles().size() == 0) {
            mDownloadBtn.setVisibility(GONE);
        } else {
            mDownloadBtn.setVisibility(View.VISIBLE);
            mDownloadBtn.setText(getResources().getString(R.string.attachment_file, newsDetail.getNewsFiles().size()));
            mDownloadBtn.setEnabled(true);
        }
    }

    private void initView() {
        TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
        TextView dateTextView = (TextView) findViewById(R.id.date_text_view);
        mDownloadBtn = (Button) findViewById(R.id.btn_download);
        mDownloadBtn.setVisibility(GONE);
        mDownloadBtn.setOnClickListener(this);
        mBodyTextView = (TextView)findViewById(R.id.body_text_view);
        mProgressView = (ProgressView)findViewById(R.id.progress_view);
        mProgressView.start();
//        mMaterialProgressBar = (MaterialProgressBar)findViewById(R.id.material_progress_bar);
//        mMaterialProgressBar.setVisibility(View.VISIBLE);

        titleTextView.setText(newsItem.getTitle());
        dateTextView.setText(newsItem.getUpdated_date());

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_download) {
            if (newsDetail.getNewsFiles().size() > 0) {
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
                .items(newsDetail.getNewsFiles())
                .itemsCallback(((dialog, itemView, position, text) -> {
//                    downloadFile(downloadUrls.get(position));
                }))
                .show();
    }



    /**
     * 　添付ファイルをダウンロード
     *
     * @param url
     */
    private void downloadFile(String url) {

    }
}
