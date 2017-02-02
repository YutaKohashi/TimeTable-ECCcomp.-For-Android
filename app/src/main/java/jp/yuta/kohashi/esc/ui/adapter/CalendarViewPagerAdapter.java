package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
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
public class CalendarViewPagerAdapter extends PagerAdapter {

    private static final int TAB_COUNT = 12;

    //    private ClickPagerImgListener listener;
    LayoutInflater _inflater = null;
    private Context mContext;
    private CalendarList calendarList;

    public CalendarViewPagerAdapter(Context context, CalendarList model) {
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        calendarList = model;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CalendarView calendarView = new CalendarView(mContext, null);
        calendarView.setMonth(Calendar.getInstance().get(Calendar.YEAR),getMonth(position), calendarList.get(Util.getTabPositionToMonth(position)));
        container.addView(calendarView);
        return calendarView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    /**
     * ポジションから月を取得するメソッド
     * @param position
     * @return
     */
    private int getMonth(int position){
        return Util.getTabPositionToMonth(position);
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
