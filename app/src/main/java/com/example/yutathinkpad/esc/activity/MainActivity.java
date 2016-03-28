package com.example.yutathinkpad.esc.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.yutathinkpad.esc.adapter.MainFragmentPagerAdapter;
import com.example.yutathinkpad.esc.fragment.NavigationDrawerFragment;
import com.example.yutathinkpad.esc.R;
import com.example.yutathinkpad.esc.preference.LoadManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String PREF_NAME_ID_PASS = "ip";
    Toolbar toolbar;
    TextView textView;
    TextView textViewUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);


        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        drawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout),toolbar);

        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

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


    }
}
