package com.example.yutathinkpad.esc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.yutathinkpad.esc.fragment.AttendanceRateFragment;
import com.example.yutathinkpad.esc.fragment.TimeTableFragment;

/**
 * Created by Yuta on 2016/03/26.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch(position){
            case 0:
                fragment = new TimeTableFragment();
                break;
            case 1:
                fragment = new AttendanceRateFragment();
                break;
            default:
                fragment = new Fragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String tabTitle = "";
        switch (position) {
            case 0:
                tabTitle = "時間割";
                break;
            case 1:
                tabTitle = "シラバス";
                break;


        }
        return tabTitle;
    }
}
