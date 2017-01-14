package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.yuta.kohashi.esc.Const;
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

    public CalendarViewPagerAdapter(Context context) {
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CalendarView calendarView = new CalendarView(mContext, null);
        calendarView.setMonth(Const.YEAR,getMonth(position));
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
        return String.valueOf(Util.getTabPositionToMonth(position));
    }
//
//    public void setListener( ClickPagerImgListener listener )
//    {
//        this.listener = listener;
//    }
//
//    public interface ChangePage
//    {
//        public void change( int position );
//    }

//    private final List<Fragment> fragmentList = new ArrayList<>();
//    private final List<String> fragmentTitleList = new ArrayList<>();
//
//    public CalendarViewPagerAdapter(FragmentManager manager){
//        super(manager);
//    }
//
//    @Override
//    public Fragment getItem(int position){
//        return fragmentList.get(position);
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return false;
//    }
//
//    @Override
//    public int getCount() {
//        return fragmentList.size();
//    }
//
//
//
//
//    public void addFrag(Fragment fragment, String title){
//        fragmentList.add(fragment);
//        fragmentTitleList.add(title);
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return fragmentTitleList.get(position);
//    }
}
