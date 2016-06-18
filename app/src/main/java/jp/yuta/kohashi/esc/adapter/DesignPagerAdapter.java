package jp.yuta.kohashi.esc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import jp.yuta.kohashi.esc.fragment.AttendanceRateFragment;
import jp.yuta.kohashi.esc.fragment.NewsFragment;
import jp.yuta.kohashi.esc.fragment.TimeTableFragment;

/**
 * Created by Yuta on 2016/06/18.
 */
//タブに関するアダプタ
public class DesignPagerAdapter extends FragmentPagerAdapter {
    private static final int TIME_TABLE_FLAG = 1;
    private static final int ATTENDANCE_RATE__FLAG = 2;
    private static final int NEWS_FLAG = 3;

    /**
     * １：時間割
     * ２：出席照会
     * ３：お知らせ
     */
    private int flag;

    public DesignPagerAdapter(FragmentManager fm, int flag) {
        super(fm);

        this.flag = flag;
    }

    //フラグメントによって変更する
    @Override
    public Fragment getItem(int position) {
//            return DesignFragment.newInstance(position);

        Fragment fragment = new Fragment();
        if(flag == TIME_TABLE_FLAG){
            fragment =  new TimeTableFragment();
        }else if (flag == ATTENDANCE_RATE__FLAG){
            fragment =  new AttendanceRateFragment();

        }else if (flag == NEWS_FLAG){
            switch(position){
                case 0:
                    fragment =   new NewsFragment();
                    break;
                case 1:
                    fragment =  new NewsFragment();
                    break;
                case 2:
                    fragment =  new NewsFragment();
                    break;
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        switch(flag){
            case TIME_TABLE_FLAG:
                return 1;
            case ATTENDANCE_RATE__FLAG:
                return 1;
            case NEWS_FLAG:
                return 3;

        }
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab " + position;
    }
}

