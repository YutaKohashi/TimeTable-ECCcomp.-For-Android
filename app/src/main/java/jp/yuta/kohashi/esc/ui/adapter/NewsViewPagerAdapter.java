package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.ui.fragment.NewsListFragment;

/**
 * Created by Yuta on 2016/06/18.
 */
//タブに関するアダプタ
public class NewsViewPagerAdapter extends BaseFragmentPagerAdapter {

    public NewsViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm,context);
        mPagerCount = 2;
    }

    //フラグメントによって変更する
    @Override
    public Fragment getItem(int position) {
        NewsListFragment fragment = null;
        switch(position){
            case 0:
                fragment =   new NewsListFragment().newInstance(0);
                break;
            case 1:
                fragment =  new NewsListFragment().newInstance(1);
                break;
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";

        switch (position){
            case 0:
                title = mContext.getResources().getString(R.string.from_school);
                break;
            case 1:
                title = mContext.getResources().getString(R.string.from_tannin);
        }
        return title;
    }
}

