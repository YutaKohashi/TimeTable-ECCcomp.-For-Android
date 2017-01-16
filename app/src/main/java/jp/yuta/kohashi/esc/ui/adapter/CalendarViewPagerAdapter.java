package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.yuta.kohashi.esc.Const;
import jp.yuta.kohashi.esc.model.schedule.CalendarListModel;
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
    private CalendarListModel calendarListModel;

    public CalendarViewPagerAdapter(Context context, CalendarListModel model) {
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        calendarListModel = model;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CalendarView calendarView = new CalendarView(mContext, null);
        calendarView.setMonth(Const.YEAR,getMonth(position),calendarListModel.get(Util.getTabPositionToMonth(position)));
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
        return String.valueOf(Util.getTabPositionToMonth(position)) + "月";
    }
}
