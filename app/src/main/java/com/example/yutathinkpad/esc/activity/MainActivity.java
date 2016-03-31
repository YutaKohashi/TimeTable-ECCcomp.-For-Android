package com.example.yutathinkpad.esc.activity;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.yutathinkpad.esc.R;
import com.example.yutathinkpad.esc.fragment.AttendanceRateFragment;
import com.example.yutathinkpad.esc.fragment.TimeTableFragment;
import com.example.yutathinkpad.esc.preference.LoadManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    static final String PREF_NAME_ID_PASS = "ip";
    Toolbar toolbar;
    TextView textView;
    TextView textViewUserName;
    private NavigationView drawer;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedId;

    static int selectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setToolbar();
        initView();

        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        //default it set first item as selected
        selectedId=savedInstanceState ==null ? R.id.navigation_item_1: savedInstanceState.getInt("SELECTED_ID");
        itemSelection(selectedId);

        //レイアウトへid,名前の設定
        LoadManager loadManager = new LoadManager();
        List<String> ipList = new ArrayList<>();
        ipList = loadManager.loadManagerWithPreferenceForString(MainActivity.this,PREF_NAME_ID_PASS,"ip");
        textView = (TextView)findViewById(R.id.num_text);
        textView.setText(ipList.get(0));

        SharedPreferences data = getSharedPreferences("username", Context.MODE_PRIVATE);
        String userName = data.getString("username","NO_NAME");
        textViewUserName = (TextView)findViewById(R.id.name_text);
       textViewUserName.setText(userName);
        //通信
        //UpdateTimeTableManager utt = new UpdateTimeTableManager();
        //utt.upDateTimeTable(MainActivity.this);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        // addを呼んでいるので、重なる
        transaction.add(R.id.fragment_container, new TimeTableFragment(),"ddd");
        transaction.addToBackStack(null);
        transaction.commit();


    }

    private void setToolbar() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

    }

    private void initView() {
        drawer= (NavigationView) findViewById(R.id.navigation_drawer);
        drawer.setNavigationItemSelectedListener(this);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    Fragment fragment1;
    private void itemSelection(int mSelectedId) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        switch(mSelectedId){

            case R.id.navigation_item_1:

                if(selectedItem == 0){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return;
                }
                fragment1 = new TimeTableFragment();
                selectedItem = 0;
                break;

            case R.id.navigation_item_2:
                // addを呼んでいるので、重なる
                if(selectedItem == 1){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return;
                }
                selectedItem = 1;
                //transaction.replace(R.id.fragment_container, new AttendanceRateFragment(),"ddd");
                fragment1 = new AttendanceRateFragment();

                //transaction.commit();

                break;

//            case R.id.navigation_item_3:
//
//                fragment1 =  new SyllabusFragment();
//                break;
//
//            case R.id.navigation_item_4:
//                fragment1 =  new Fragment();
//                break;
            case R.id.navigation_item_5:
                Intent intent =new Intent(MainActivity.this,PreferenceRelationActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.pull_in_up , R.anim.none_anim);
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            case R.id.navigation_item_6:
                Intent intent2 =new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent2);

                overridePendingTransition(R.anim.pull_in_up , R.anim.none_anim);
                drawerLayout.closeDrawer(GravityCompat.START);
                return;

            default:
                break;

        }
        //drawerLayout.closeDrawer(GravityCompat.START);
        // ↓ここに移動
//        transaction.setCustomAnimations(
//                R.anim.fade_in,
//                R.anim.fade_out);
//        transaction.replace(R.id.fragment_container,fragment1);
//        transaction.addToBackStack(null);
//        transaction.commit();
        manager.beginTransaction()
                .replace(R.id.fragment_container,fragment1, "dd")
                .addToBackStack(null)
                .commit();
        drawerLayout.closeDrawer(GravityCompat.START);
//        transaction.replace(R.id.fragment_container,fragment,"ddd");
//        transaction.commit();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        selectedId=menuItem.getItemId();
        itemSelection(selectedId);
        return true;
//        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("SELECTED_ID",selectedId);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        // 戻るボタンが押されたとき
        if(e.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // ボタンが押されたとき
            return true;

        }
        return super.dispatchKeyEvent(e);
    }
}
