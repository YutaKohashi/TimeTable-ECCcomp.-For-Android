package com.example.yutathinkpad.esc.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.yutathinkpad.esc.R;
import com.example.yutathinkpad.esc.http.GetAttendanceRateManager;

public class ProvisiontalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provisiontal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GetAttendanceRateManager getAttendanceRateManager = new GetAttendanceRateManager();
        getAttendanceRateManager.getAttendanceRate(ProvisiontalActivity.this,"2140257","455478");


    }

}
