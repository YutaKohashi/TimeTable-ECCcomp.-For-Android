package jp.yuta.kohashi.esc.ui.activity;

import android.os.Bundle;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.ui.activity.base.BaseActivity;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 30 / 04 / 2017
 */
public class TimeTableEnableDisableActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_enable_disable);

        initToolbar();
        enableBackBtn();
        setTitle(getResources().getString(R.string.pref_enable_time_table));
    }
}
