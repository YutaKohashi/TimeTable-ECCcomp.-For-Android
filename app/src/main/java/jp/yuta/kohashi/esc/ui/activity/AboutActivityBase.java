package jp.yuta.kohashi.esc.ui.activity;

import android.os.Bundle;

import jp.yuta.kohashi.esc.R;

public class AboutActivityBase extends BaseWebViewActivity {
    private static final String TAG = AboutActivityBase.class.getSimpleName();
    private static final String FILE_NAME ="file:///android_asset/about.html";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disableCopyPaste();
        setToolbarTitle(getResources().getString(R.string.pref_about) );
        setHtml(FILE_NAME);
    }
}
