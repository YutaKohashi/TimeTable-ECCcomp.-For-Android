package jp.yuta.kohashi.esc.ui.activity;

import android.os.Bundle;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.ui.activity.base.BaseWebViewActivity;

public class LicenceActivity extends BaseWebViewActivity {

    private static final String TAG = LicenceActivity.class.getSimpleName();
    private static final String FILE_LINK = "file:///android_asset/licence.html";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disableCopyPaste();
        setToolbarTitle(getResources().getString(R.string.pref_lisence) );
        setHtml(FILE_LINK);
    }
}
