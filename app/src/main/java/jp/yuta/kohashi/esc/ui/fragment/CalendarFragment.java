package jp.yuta.kohashi.esc.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.api.model.schedule.ScheduleCategory;
import jp.yuta.kohashi.esc.network.api.model.schedule.ScheduleItem;
import jp.yuta.kohashi.esc.network.api.model.schedule.ScheduleRoot;
import jp.yuta.kohashi.esc.ui.adapter.CalendarFrontViewPagerAdapter;
import jp.yuta.kohashi.esc.ui.adapter.CalendarViewPagerAdapter;
import jp.yuta.kohashi.esc.ui.fragment.base.BaseFragment;
import jp.yuta.kohashi.esc.ui.view.SynchronizedViewPager;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * Created by yutakohashi on 2017/01/14.
 */

public class CalendarFragment extends BaseFragment implements ViewTreeObserver.OnGlobalLayoutListener, View.OnClickListener {
    private static final String TAG = CalendarFragment.class.getSimpleName();

    private Button mPrevBtn;
    private Button mNextBtn;
    private TabLayout mFrontTabLayout;
    private SynchronizedViewPager mFrontViewPager;
    private SynchronizedViewPager mBottomViewPager;
    private CalendarFrontViewPagerAdapter mFrontPagerAdapter;
    private CalendarViewPagerAdapter mBottomPagerAdapter;
    int currentPage;
//    private CalendarList calendarList;
    private boolean flag = true;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_calendar_parent, container, false);
//        calendarList = getSchedule(); //スケジュールを取得
        List<ScheduleRoot> scheduleRootList = PrefUtil.loadSchedule();

        /**
         * Exceptionが出ないようにscheduleRootListを再成形
         */
        scheduleRootList = fixScheduleList(scheduleRootList);


        mPrevBtn = (Button) mView.findViewById(R.id.prev_btn);
        mNextBtn = (Button) mView.findViewById(R.id.next_btn);
        mPrevBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);

        mFrontViewPager = (SynchronizedViewPager) mView.findViewById(R.id.calendar_front_viewpager);
        mBottomViewPager = ((SynchronizedViewPager) mView.findViewById(R.id.calendar_bottom_viewpager))
                .setTargetViewPager(mFrontViewPager);
        mFrontViewPager.setTargetViewPager(mBottomViewPager);

        // TODO: 2017/05/02
        mFrontPagerAdapter = new CalendarFrontViewPagerAdapter(getContext(), scheduleRootList);

        mFrontViewPager.setAdapter(mFrontPagerAdapter);
        mFrontTabLayout = (TabLayout) mView.findViewById(R.id.tab_calendar);
        mFrontTabLayout.setupWithViewPager(mFrontViewPager);

        // TODO: 2017/05/02
        mBottomPagerAdapter = new CalendarViewPagerAdapter(getChildFragmentManager(), scheduleRootList, getActivity());
        mBottomViewPager.setAdapter(mBottomPagerAdapter);
        //ViewTreeObserverをフック
        mFrontTabLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mFrontViewPager.addOnPageChangeListener(new PageChangeListener());

        currentPage = 0;
        return mView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prev_btn:
                if (--currentPage < 0) currentPage++;
                else movePage(currentPage);
                break;
            case R.id.next_btn:
                if (++currentPage < 12) movePage(currentPage);
                else currentPage--;
                break;
        }
    }

    /**
     * ViewPagerのページが変わったときに呼び出される
     */
    class PageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            currentPage = position;
        }
    }

    /**
     * フラグメント起動時にタブをスクロール
     */
    @Override
    public void onGlobalLayout() {
        if (flag) {
            flag = false;
            int position = Util.getMonthToTabPosition(Calendar.getInstance().get(Calendar.MONTH) + 1);
            movePage(position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mFrontTabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    private void movePage(int position) {
        System.out.println("CalendarFragment movePage called");
        moveFrontPage(position);
    }

    private void moveFrontPage(int position) {
        mFrontViewPager.setCurrentItem(position, false);
        mFrontTabLayout.setScrollPosition(position, 0, true); // 注
    }

    private void moveBottomPage(int position) {
        mBottomViewPager.setCurrentItem(position, false);
    }
//
private static List<ScheduleRoot>  fixScheduleList(@NonNull List<ScheduleRoot> list){
    List<ScheduleRoot> scheduleRoots = new ArrayList<>();
    for(int i = 0 ; i< 12; i++){
        ScheduleRoot scheduleRoot;
        try{
            scheduleRoot = list.get(i);
        } catch (Exception e){
            scheduleRoot = new ScheduleRoot();
        }

        if(scheduleRoot.getSchedules() == null) scheduleRoot.setSchedules(new ArrayList<>());
        if(scheduleRoot.getSchedules().size() == 0) scheduleRoot.getSchedules().add(new ScheduleCategory());
        List<ScheduleItem> tmp = scheduleRoot.getSchedules().get(0).getDetails();
        List<ScheduleItem> scheduleItems = new ArrayList<>();
        for(int j = 0; j < 31 ; j++){
            ScheduleItem scheduleItem;
            try{
                scheduleItem = tmp.get(j);
            } catch (Exception e){
                scheduleItem = new ScheduleItem();
            }
            scheduleItems.add(scheduleItem);
        }
        scheduleRoot.getSchedules().get(0).setDetails(scheduleItems);
        scheduleRoots.add(scheduleRoot);
    }
    return scheduleRoots;
}
}
