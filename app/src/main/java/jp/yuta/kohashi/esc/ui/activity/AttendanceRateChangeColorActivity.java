package jp.yuta.kohashi.esc.ui.activity;

import android.os.Bundle;

import jp.yuta.kohashi.esc.R;

public class AttendanceRateChangeColorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_rate_change_color);

        initToolbar();
        enableBackBtn();
        setTitle(getResources().getString(R.string.pref_attendance_color));
    }
}
