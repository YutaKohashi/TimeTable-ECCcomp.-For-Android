package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.enums.AttendanceRateType;
import jp.yuta.kohashi.esc.ui.fragment.AttendanceRateFragment;

/**
 * Created by yutakohashi on 2017/01/30.
 */

public class AttendanceRateViewPagerAdapter extends BaseFragmentPagerAdapter {

    public AttendanceRateViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm, context);
        mPagerCount = 3;
    }

    @Override
    public Fragment getItem(int position) {
        AttendanceRateFragment fragment;
        switch (position) {
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
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
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
