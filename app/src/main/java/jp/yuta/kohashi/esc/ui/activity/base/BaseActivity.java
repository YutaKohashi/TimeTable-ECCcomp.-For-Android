package jp.yuta.kohashi.esc.ui.activity.base;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import jp.yuta.kohashi.esc.R;

/**
 * Created by yutakohashi on 2017/01/18.
 */

public class BaseActivity extends AppCompatActivity {

    public Toolbar mToolbar;

    public void initToolbar() {
        //ツールバーをActionBarとして扱う
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(mToolbar);
        }
    }

    /**
     * ツールバーのバックボタンを有効に
     */
    public void enableBackBtn() {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);
        }
    }

    public void setToolbarTitle(String title) {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setTitle(title);
        }else {
            Log.d(BaseActivity.class.getSimpleName(), "toolbar is null");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
