package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.enums.AttendanceRateType;
import jp.yuta.kohashi.esc.ui.fragment.AttendanceRateFragment;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * Created by yutakohashi on 2017/01/30.
 */

public class AttendanceRateViewPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGER_COUNT = 3;
    private static final int DISABLE_PAGER_COUNT = 1;
    private Context mContext;

    public AttendanceRateViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        AttendanceRateFragment fragment;
        switch(position){
            case 0:
                fragment = AttendanceRateFragment.newInstance(AttendanceRateType.ALL);
                break;
            case 1:
                fragment = AttendanceRateFragment.newInstance(AttendanceRateType.ZENKI);
                break;
            case 2:
                fragment = AttendanceRateFragment.newInstance(AttendanceRateType.KOUKI);
                break;
            default:
                fragment = new AttendanceRateFragment();

        }
        return fragment;
    }

    @Override
    public int getCount() {
        int count;
        if(PrefUtil.isDivideAttendance()) {
            count =  PAGER_COUNT;
        } else {
            count =  DISABLE_PAGER_COUNT;
        }

        return count;
    }



    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = mContext.getResources().getString(R.string.title_all_data);
                break;
            case 1:
                title = mContext.getResources().getString(R.string.title_zenki_data);
                break;
            case 2:
                title = mContext.getResources().getString(R.string.title_kouki_data);
                break;
        }
        return title;
    }
}
