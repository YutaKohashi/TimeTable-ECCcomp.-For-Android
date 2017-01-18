package jp.yuta.kohashi.esc.ui.activity;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import jp.yuta.kohashi.esc.ui.activity.base.WebViewActivity;
import jp.yuta.kohashi.esc.util.NotifyUtil;
import jp.yuta.kohashi.esc.util.Util;

public class LisenceActivity extends WebViewActivity {

    private static final String TAG = LisenceActivity.class.getSimpleName();
    private static final String FILE_NAME ="lisence.html";
    private String html;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            html =  Util.loadTextAsset(FILE_NAME);
        } catch (IOException e) {
            NotifyUtil.failureFileLoad();
            Log.d(TAG,e.toString());
        }

        setHtml(html);
    }
}
