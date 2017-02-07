package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.schedule.CalendarItem;
import jp.yuta.kohashi.esc.model.schedule.CalendarList;
import jp.yuta.kohashi.esc.util.Util;

/**
 * Created by yutakohashi on 2017/02/05.
 */

public class CalendarBottomViewPagerAdapter extends BaseCalendarViewPagerAdapter {

    private CalendarRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;

    public CalendarBottomViewPagerAdapter(Context context, CalendarList calendarList){
        super(context);
        this.calendarList = calendarList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout =  _inflater.inflate(R.layout.fragment_recycler_view, null);
        mRecyclerView = (RecyclerView)layout.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        List<CalendarItem> list = getMonthSchedule(Util.getTabPositionToMonth(position));
        mRecyclerAdapter = new CalendarRecyclerAdapter(list, mContext);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        container.addView(mRecyclerView);
        return container;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }


    /**
     * 引数の月からスケジュールをを取得
     *
     * @param month
     * @return
     */
    private List<CalendarItem> getMonthSchedule(int month) {
        return calendarList.get(month);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
