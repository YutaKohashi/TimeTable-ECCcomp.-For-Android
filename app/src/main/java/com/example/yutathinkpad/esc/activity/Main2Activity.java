package com.example.yutathinkpad.esc.activity;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.yutathinkpad.esc.NavigationDrawerFragment;
import com.example.yutathinkpad.esc.R;
import com.example.yutathinkpad.esc.http.UpdateTimeTable;

public class Main2Activity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);


        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        drawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout),toolbar);

        //通信
        UpdateTimeTable utt = new UpdateTimeTable();
        utt.upDateTimeTable(Main2Activity.this);





    }
}
