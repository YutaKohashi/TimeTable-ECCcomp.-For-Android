package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by yutakohashi on 2017/02/06.
 */

public abstract class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    protected int mPagerCount;
    protected Context mContext;

    public BaseFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return mPagerCount;
    }
}
