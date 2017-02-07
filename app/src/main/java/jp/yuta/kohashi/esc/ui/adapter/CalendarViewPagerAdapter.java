package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import jp.yuta.kohashi.esc.model.schedule.CalendarList;
import jp.yuta.kohashi.esc.ui.fragment.CalendarBottomFragment;
import jp.yuta.kohashi.esc.util.Util;

/**
 * Created by yutakohashi on 2017/02/06.
 */

public class CalendarViewPagerAdapter extends BaseFragmentPagerAdapter{
    private static final String TAG = CalendarViewPagerAdapter.class.getSimpleName();

    private CalendarList mCalendarList;

    public CalendarViewPagerAdapter(FragmentManager fm, CalendarList list,Context context) {
        super(fm,context);
        mPagerCount = 12;
        mCalendarList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return  new CalendarBottomFragment().newInstance(mCalendarList.get(Util.getTabPositionToMonth(position)));
    }
}
