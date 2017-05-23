package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

import jp.yuta.kohashi.esc.network.api.model.schedule.ScheduleRoot;
import jp.yuta.kohashi.esc.ui.fragment.CalendarBottomFragment;

/**
 * Created by yutakohashi on 2017/02/06.
 */

public class CalendarViewPagerAdapter extends BaseFragmentPagerAdapter{
    private static final String TAG = CalendarViewPagerAdapter.class.getSimpleName();

//    private CalendarList mCalendarList;
    private List<ScheduleRoot> scheduleRoots;

    public CalendarViewPagerAdapter(FragmentManager fm, List<ScheduleRoot> scheduleRoots ,Context context) {
        super(fm,context);
        mPagerCount = 12;
//        mCalendarList = list;
        this.scheduleRoots = scheduleRoots;
    }

    @Override
    public Fragment getItem(int position) {
        CalendarBottomFragment fragment = new CalendarBottomFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CalendarBottomFragment.KEY_SCHEDULE_LIST,scheduleRoots.get(position));
        fragment.setArguments(bundle);
//        fragment.setList(scheduleRoots.get(position));
        return fragment;
    }
}
