package jp.yuta.kohashi.esc.ui.activity;

import android.os.Bundle;

import jp.yuta.kohashi.esc.R;

public class PreferenceActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        initToolbar();
        enableBackBtn();
        setToolbarTitle(getResources().getString(R.string.toolbar_title_settings));
    }

}
