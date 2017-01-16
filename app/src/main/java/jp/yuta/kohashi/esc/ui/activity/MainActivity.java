package jp.yuta.kohashi.esc.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

import jp.yuta.kohashi.esc.Const;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.service.RequestURL;
import jp.yuta.kohashi.esc.ui.fragment.AttendanceRateFragment;
import jp.yuta.kohashi.esc.ui.fragment.CalendarFragment;
import jp.yuta.kohashi.esc.ui.fragment.TimeTableFragment;
import jp.yuta.kohashi.esc.ui.fragment.news.NewsParentFragment;


/***
 * このActivityに時間割や出席照会のFragmentがのる
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String GET_ATTENDANCE_RATE = "get_attendance_rate";

    /*ツールバー・ナビゲーションドロワー・トグル・レイアウト*/
    private Toolbar mToolbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private BottomNavigationView mBottomNavView;
    private Menu mOverflowMenu;
    private Fragment transitionFragment;
    private View mRootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_nav_drawer);
        setContentView(R.layout.activity_main_nav_bottom);

        initToolbar();

//        ドロワーの時
//        initDrawer();
        initBottomNavView();

        replaceFragment(new TimeTableFragment());

        isGetAttendanceRate(); //ログイン時のみ
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
    }

    private void initToolbar(){
        //ツールバーをActionBarとして扱う
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
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
            replaceFragment(transitionFragment);
        }catch(Exception e){
            Log.d(TAG,e.toString());
        }

        return true;
    }

    /**
     * フラグメントreplaceメソッド
     * @param fragment
     */
    private void replaceFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment,"");
        ft.addToBackStack(null);
        ft.commit();
    }

    private void drawerOpen(){mDrawerLayout.closeDrawer(GravityCompat.END);}

    private void closeDrawer(){
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * ログイン時に出席照会の取得に失敗した時
     * スナックバーを表示する
     */
    private void isGetAttendanceRate(){
        Intent intent = getIntent();
        boolean bool = intent.getBooleanExtra(GET_ATTENDANCE_RATE,true);

        //取得に失敗
        if(!bool){
            Snackbar.make(findViewById(R.id.activity_main),"出席照会の取得に失敗しました。",Snackbar.LENGTH_SHORT).show();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items_overflow, menu);
        mOverflowMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        final int action = event.getAction();
        final int keyCode = event.getKeyCode();
        if (action == KeyEvent.ACTION_UP) {
            // メニュー表示
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                if (mOverflowMenu != null) {
                    mOverflowMenu.performIdentifierAction(R.id.nav_item_news, 0);
                }
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.overflow_item_web:
                showWeb();
                break;
            case R.id.overflow_item_settings:
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    private void showWeb(){
        Uri uri = Uri.parse(RequestURL.ESC_TO_PAGE);
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary))
                .build();
        customTabsIntent.intent.setData(uri);
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (TextUtils.equals(packageName, Const.CHROME_PACKAGE_NAME))
                customTabsIntent.intent.setPackage(Const.CHROME_PACKAGE_NAME);
        }
        customTabsIntent.launchUrl(MainActivity.this, uri);
    }
}
