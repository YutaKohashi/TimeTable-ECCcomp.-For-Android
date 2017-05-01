package jp.yuta.kohashi.esc.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import jp.yuta.kohashi.esc.Const;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.network.RequestURL;
import jp.yuta.kohashi.esc.network.api.model.news.NewsItem;
import jp.yuta.kohashi.esc.ui.activity.base.BaseActivity;
import jp.yuta.kohashi.esc.ui.fragment.AttendanceRateParentFragment;
import jp.yuta.kohashi.esc.ui.fragment.CalendarFragment;
import jp.yuta.kohashi.esc.ui.fragment.NewsParentFragment;
import jp.yuta.kohashi.esc.ui.fragment.TimeTableFragment;
import jp.yuta.kohashi.esc.ui.service.EccNewsManageService;
import jp.yuta.kohashi.esc.ui.view.CustomBottomNavigationView;
import jp.yuta.kohashi.esc.util.NotifyUtil;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

//import jp.yuta.kohashi.esc.model.NewsItem;


/***
 * このActivityに時間割や出席照会のFragmentがのる
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String GET_ATTENDANCE_RATE = "get_attendance_rate";
    public static final String SELECT_TAB_NEWS = "select_tab_news"; //　通知をタップして起動した場合

    /*ツールバー・ナビゲーションドロワー・トグル・レイアウト*/
    private NavigationView mNavDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CustomBottomNavigationView mBottomNavView;
    private Fragment transitionFragment;

    private int currentTab;

    private TimeTableFragment timeTableFragment;
    private AttendanceRateParentFragment attendanceRateFragment;
    private NewsParentFragment newsParentFragment;
    private CalendarFragment calendarFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        setToolbarTitle(getResources().getString(R.string.toolbar_default_title));

//        ドロワーの時
        initDrawer();
        initBottomNavView();

        replaceFragment(new TimeTableFragment());

        isGetAttendanceRate(); //ログイン時のみ
        currentTab = -1;

        //サービス起動チェック
        if (!Util.isStartService() && PrefUtil.isNotifyNews()) {
            new EccNewsManageService().startResident(MainActivity.this);
        }

        // 通知バーから起動されたか
        isSelectTabNews();
    }


    /**
     * ツールバー、ナビゲーションドロワーを初期化
     */
    private void initDrawer() {
        mNavDrawer = (NavigationView) findViewById(R.id.navigation_drawer);
        mNavDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void initBottomNavView() {
        mBottomNavView = (CustomBottomNavigationView) findViewById(R.id.bottom_nav_view);
        mBottomNavView.setOnNavigationItemSelectedListener(this);
        mBottomNavView.disableShiftMode();
    }

    /*
     * NavigationViewのClickイベント
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (currentTab == item.getItemId()) return true;

        switch (item.getItemId()) {
            case R.id.nav_item_time_table: // 時間割
                if (timeTableFragment == null) timeTableFragment = new TimeTableFragment();
                replaceFragment(timeTableFragment);
                currentTab = item.getItemId();
                break;
            case R.id.nav_item_attendance_rate: // 出席照会
                if (attendanceRateFragment == null)
                    attendanceRateFragment = new AttendanceRateParentFragment();
                replaceFragment(attendanceRateFragment);
                currentTab = item.getItemId();
                break;
            case R.id.nav_item_news: // お知らせ
                if (newsParentFragment == null) newsParentFragment = new NewsParentFragment();
                replaceFragment(newsParentFragment);
                currentTab = item.getItemId();
                break;
            case R.id.nav_item_schedule: //スケジュール
                if (calendarFragment == null) calendarFragment = new CalendarFragment();
                replaceFragment(new CalendarFragment());
                currentTab = item.getItemId();
                break;
            case R.id.nav_item_profile: //プロフィール
                showProfile();
                closeDrawer();
                break;
            case R.id.nav_item_web: //WEB版
                showWeb();
                closeDrawer();
                break;
            case R.id.nav_item_settings: // 設定
                showSettings();
                closeDrawer();
                break;
        }

        return true;
    }

    /**
     * フラグメントreplaceメソッド
     *
     * @param fragment
     */
    private void replaceFragment(Fragment fragment) {
        transitionFragment = null;
        transitionFragment = fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment, "");
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * ドロワーを開く
     */
    private void drawerOpen() {
        mDrawerLayout.closeDrawer(GravityCompat.END);
    }

    /**
     * ドロワーを閉じる
     */
    private void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * ログイン時に出席照会の取得に失敗した時
     * スナックバーを表示する
     */
    private void isGetAttendanceRate() {
        Intent intent = getIntent();
        boolean bool = intent.getBooleanExtra(GET_ATTENDANCE_RATE, true);

        //取得に失敗
        if (!bool) {
            NotifyUtil.failureAttendanceRate();
            intent.removeExtra(GET_ATTENDANCE_RATE);
        }
    }

    /**
     * 通知から起動した場合お知らせタブを選択する
     */
    private void isSelectTabNews() {
        Intent intent = getIntent();
        boolean bool = intent.getBooleanExtra(SELECT_TAB_NEWS, false);
        intent.removeExtra(SELECT_TAB_NEWS);
        if (bool) {
            View view = mBottomNavView.findViewById(R.id.nav_item_news);
            if (view != null) view.performClick();

            final NewsItem item = (NewsItem) intent.getSerializableExtra(NewsDetailActivity.NEWS_MODEL);
            intent.removeExtra(NewsDetailActivity.NEWS_MODEL);
            if(item != null) {
                // 詳細記事を表示
                if (!Util.netWorkCheck()) {
                    NotifyUtil.failureNetworkConnection();
                    return;
                }

                Intent intent1 = new Intent(MainActivity.this, NewsDetailActivity.class);
                intent1.putExtra(NewsDetailActivity.NEWS_MODEL, item);
                startActivity(intent1);
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return false;
    }

    /**
     * ChoromeCustomTabsを表示
     */
    private void showWeb() {
        Uri uri = Uri.parse(RequestURL.ESC_LOGIN);
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

    private void showProfile() {
        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
    }

    /**
     * 設定Activityを表示
     */
    private void showSettings() {
        Intent intent = new Intent(MainActivity.this, PreferenceActivity.class);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //ドロワーに名前と学籍番号を入れる
        String userId = PrefUtil.getId();
        String name = PrefUtil.getUserName();
        ((TextView) findViewById(R.id.name_text)).setText(name);
        ((TextView) findViewById(R.id.num_text)).setText(userId);
    }

}
