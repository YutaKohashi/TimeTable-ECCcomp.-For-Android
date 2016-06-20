package jp.yuta.kohashi.esc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import jp.yuta.kohashi.esc.fragment.NewsSchoolFragment;
import jp.yuta.kohashi.esc.fragment.NewsTeacherFragment;

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

    public DesignPagerAdapter(FragmentManager fm) {
        super(fm);

        this.flag = flag;
    }

    //フラグメントによって変更する
    @Override
    public Fragment getItem(int position) {
//            return DesignFragment.newInstance(position);

        Fragment fragment = null;

        switch(position){
            case 0:
                fragment =   new NewsSchoolFragment();
                break;
            case 1:
                fragment =  new NewsTeacherFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";

        switch (position){
            case 0:
                title = "学校から";
                break;
            case 1:
                title = "担任から";
        }
        return title;
    }
}

