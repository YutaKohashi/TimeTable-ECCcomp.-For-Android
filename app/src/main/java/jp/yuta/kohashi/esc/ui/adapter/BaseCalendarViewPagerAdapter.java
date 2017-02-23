package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;

import jp.yuta.kohashi.esc.model.schedule.CalendarList;

/**
 * Created by yutakohashi on 2017/02/05.
 */

public abstract class BaseCalendarViewPagerAdapter extends PagerAdapter {
    private static final int TAB_COUNT = 12;

    protected LayoutInflater _inflater = null;
    protected  Context mContext;
    protected CalendarList calendarList;

    public BaseCalendarViewPagerAdapter(Context context) {
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
