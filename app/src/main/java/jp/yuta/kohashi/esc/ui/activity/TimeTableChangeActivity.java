package jp.yuta.kohashi.esc.ui.activity;

import android.os.Bundle;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.ui.activity.base.BaseActivity;

public class TimeTableChangeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_time_table);

        initToolbar();
        enableBackBtn();
        setTitle(getResources().getString(R.string.pref_change_time_table));
    }
}
