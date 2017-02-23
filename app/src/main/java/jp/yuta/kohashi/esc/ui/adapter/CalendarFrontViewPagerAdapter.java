package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.schedule.CalendarList;
import jp.yuta.kohashi.esc.ui.view.CalendarView;
import jp.yuta.kohashi.esc.util.Util;

/**
 * Created by Yuta on 2016/04/04.
 */
public class CalendarFrontViewPagerAdapter extends BaseCalendarViewPagerAdapter {

    //    private ClickPagerImgListener listener;


    public CalendarFrontViewPagerAdapter(Context context, CalendarList model) {
        super(context);
        calendarList = model;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CalendarView calendarView = new CalendarView(mContext, null);
        calendarView.setMonth(
                Calendar.getInstance().get(Calendar.YEAR),
                Util.getTabPositionToMonth(position),
                calendarList.get(Util.getTabPositionToMonth(position)));
        container.addView(calendarView);
        return calendarView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    /**
     * タブのタイトルを設定
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        String month = "";
        switch (Util.getTabPositionToMonth(position)) {
            case 1:
                month = mContext.getResources().getString(R.string.january);
                break;
            case 2:
                month = mContext.getResources().getString(R.string.february);
                break;
            case 3:
                month = mContext.getResources().getString(R.string.march);
                break;
            case 4:
                month = mContext.getResources().getString(R.string.april);
                break;
            case 5:
                month = mContext.getResources().getString(R.string.may);
                break;
            case 6:
                month = mContext.getResources().getString(R.string.june);
                break;
            case 7:
                month = mContext.getResources().getString(R.string.july);
                break;
            case 8:
                month = mContext.getResources().getString(R.string.august);
                break;
            case 9:
                month = mContext.getResources().getString(R.string.september);
                break;
            case 10:
                month = mContext.getResources().getString(R.string.october);
                break;
            case 11:
                month = mContext.getResources().getString(R.string.november);
                break;
            case 12:
                month = mContext.getResources().getString(R.string.december);
                break;
        }

        return month;
    }
}
