package jp.yuta.kohashi.esc.ui.activity;

import android.os.Bundle;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.ui.activity.base.BaseActivity;

public class AttendanceDivideActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_divide);

        initToolbar();
        enableBackBtn();
        setTitle(getResources().getString(R.string.pref_attendance_divide));
    }
}
