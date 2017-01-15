package jp.yuta.kohashi.esc.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.ui.fragment.AttendanceRateFragment;
import jp.yuta.kohashi.esc.ui.fragment.TimeTableFragment;
import jp.yuta.kohashi.esc.ui.fragment.CalendarFragment;
import jp.yuta.kohashi.esc.ui.fragment.news.NewsParentFragment;


/***
 * このActivityに時間割や出席照会のFragmentがのる
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    /*ツールバー・ナビゲーションドロワー・トグル・レイアウト*/
    private Toolbar mToolbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private BottomNavigationView mBottomNavView;
//    private FragmentManager fragmentManager;
    private Fragment transitionFragment;
    private View appBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_nav_drawer);
        setContentView(R.layout.activity_main_nav_bottom);

        //ツールバーをActionBarとして扱う
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

//        ドロワーの時
//        initDrawer();
        initBottomNavView();

    }

    public void onStart(){
        super.onStart();

        Fragment fragment = new TimeTableFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragment, "dd")
                .addToBackStack(null)
                .commit();
    }


    /**
     * ツールバー、ナビゲーションドロワーを初期化
     */
    private void initDrawer() {
        mDrawer = (NavigationView) findViewById(R.id.navigation_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void initBottomNavView() {
        mBottomNavView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        mBottomNavView.setOnNavigationItemSelectedListener(this);
        appBar = findViewById(R.id.appbar);
    }


    /**
     * NavigationViewのClickイベント
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_bottom_sheet_in);

        switch (item.getItemId()) {
            case R.id.nav_item_time_table: // 時間割
                transitionFragment  = new TimeTableFragment();
                break;
            case R.id.nav_item_attendance_rate: // 出席照会
                transitionFragment  = new AttendanceRateFragment();
                break;
            case R.id.nav_item_news: // お知らせ
                transitionFragment  = new NewsParentFragment();
                break;
            case R.id.nav_item_schedule: //スケジュール
                transitionFragment  = new CalendarFragment();
                break;
            case R.id.nav_item_web: //WEB版
                break;
            case R.id.nav_item_settings: // 設定

        }

        try {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, transitionFragment, "")
                    .addToBackStack(null)
                    .commit();
        }catch(NullPointerException e){
            Log.d(TAG,e.toString());
        }

        return true;
    }

    private void drawerOpen(){

    }

    private void closeDrawer(){
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
//
//            // ボタンが押されたとき
//            final MaterialDialog dialog = new MaterialDialog(this);
//            dialog
//                    .setTitle("アプリケーション終了")
//                    .setMessage("アプリケーションを終了してよろしいですか？")
//                    .setPositiveButton("YES", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            finish();
//                            moveTaskToBack(true);
//
//                        }
//                    })
//                    .setNegativeButton("NO", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
////
//                        }
//                    }).show();
            this.finish();
            return true;
        }
        return false;
    }
}
