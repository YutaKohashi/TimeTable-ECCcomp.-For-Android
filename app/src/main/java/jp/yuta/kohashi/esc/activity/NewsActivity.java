package jp.yuta.kohashi.esc.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jp.yuta.kohashi.esc.R;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.none_anim, R.anim.push_out_up);
    }

}
