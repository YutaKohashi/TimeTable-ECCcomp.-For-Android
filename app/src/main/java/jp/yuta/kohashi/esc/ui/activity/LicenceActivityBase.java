package jp.yuta.kohashi.esc.ui.activity;

import android.os.Bundle;

import jp.yuta.kohashi.esc.R;

public class LicenceActivityBase extends BaseWebViewActivity {

    private static final String TAG = LicenceActivityBase.class.getSimpleName();
    private static final String FILE_LINK = "file:///android_asset/licence.html";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disableCopyPaste();
        setToolbarTitle(getResources().getString(R.string.pref_lisence) );
        setHtml(FILE_LINK);
    }
}
