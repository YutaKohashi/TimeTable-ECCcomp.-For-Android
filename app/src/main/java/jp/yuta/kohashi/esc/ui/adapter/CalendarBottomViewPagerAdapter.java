package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.api.model.schedule.ScheduleItem;
import jp.yuta.kohashi.esc.network.api.model.schedule.ScheduleRoot;

/**
 * Created by yutakohashi on 2017/02/05.
 */

public class CalendarBottomViewPagerAdapter extends BaseCalendarViewPagerAdapter {

    private CalendarRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;

    public CalendarBottomViewPagerAdapter(Context context, List<ScheduleRoot> scheduleRoots) {
        super(context);
        this.scheduleRoots = scheduleRoots;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = _inflater.inflate(R.layout.fragment_recycler_view, null);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        List<ScheduleItem> list = getMonthSchedule(Util.getTabPositionToMonth(position));
        List<ScheduleItem> list = scheduleRoots.get(position).getSchedules().get(0).getDetails();
        mRecyclerAdapter = new CalendarRecyclerAdapter(list, mContext);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        container.addView(mRecyclerView);
        return container;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
