package jp.yuta.kohashi.esc.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;

import jp.yuta.kohashi.esc.Const;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.schedule.CalendarList;
import jp.yuta.kohashi.esc.ui.adapter.CalendarFrontViewPagerAdapter;
import jp.yuta.kohashi.esc.ui.adapter.CalendarViewPagerAdapter;
import jp.yuta.kohashi.esc.util.Util;

/**
 * Created by yutakohashi on 2017/01/14.
 */

public class CalendarFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener , View.OnClickListener {
    private static final String TAG = CalendarFragment.class.getSimpleName();

    private Button mPrevBtn;
    private Button mNextBtn;
//    private RecyclerView mRecyclerView;
    private TabLayout mTabLayout;
    private TabLayout mInvisibleTabLayout;
    private ViewPager mFrontViewPager;
    private ViewPager mBottomViewPager;
    private CalendarFrontViewPagerAdapter mFrontPagerAdapter;
//    private CalendarBottomViewPagerAdapter mBottomPagerAdapter;
//    private CalendarRecyclerAdapter mRecyclerAdapter;
    private CalendarViewPagerAdapter mBottoPagerAdapter;
    int currentPage;
    private CalendarList calendarList;
    private boolean flag = true;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_calendar_parent, container, false);
        calendarList = getSchedule(); //スケジュールを取得

        mPrevBtn = (Button) mView.findViewById(R.id.prev_btn);
        mNextBtn = (Button)mView.findViewById(R.id.next_btn);
        mPrevBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mFrontViewPager = (ViewPager) mView.findViewById(R.id.calendar_front_viewpager);
        mFrontPagerAdapter = new CalendarFrontViewPagerAdapter(getContext(), calendarList);
        mFrontViewPager.addOnPageChangeListener(new PageChangeListener());
        mFrontViewPager.setAdapter(mFrontPagerAdapter);
        mTabLayout = (TabLayout) mView.findViewById(R.id.tab_calendar);
        mInvisibleTabLayout = (TabLayout) mView.findViewById(R.id.tab_calendar_invisible);
        mTabLayout.setupWithViewPager(mFrontViewPager);
        //ViewTreeObserverをフック
        mTabLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mBottomViewPager = (ViewPager) mView.findViewById(R.id.calendar_bottom_viewpager);
        mBottoPagerAdapter = new CalendarViewPagerAdapter(getChildFragmentManager(),calendarList,getActivity());
        mBottomViewPager.setAdapter(mBottoPagerAdapter);
        mInvisibleTabLayout.setupWithViewPager(mBottomViewPager);
        //ViewTreeObserverをフック
        mInvisibleTabLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);


        mFrontViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                mBottomViewPager.scrollTo(mFrontViewPager.getScrollX(), mBottomViewPager.getScrollY());
            }

            @Override
            public void onPageSelected(final int position) {

            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mBottomViewPager.setCurrentItem(mFrontViewPager.getCurrentItem(), false);
                }
            }
        });

        mBottomViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                mFrontViewPager.scrollTo(mBottomViewPager.getScrollX(), mFrontViewPager.getScrollY());
            }

            @Override
            public void onPageSelected(final int position) {

            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mFrontViewPager.setCurrentItem(mBottomViewPager.getCurrentItem(), false);
                }
            }
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG,"-----------------     SELECTED TAB     -----------------");
                movePage1(tab.getPosition());
//                mInvisibleTabLayout.getTabAt(tab.getPosition()).select();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d(TAG,"-----------------     UNSELECTED TAB     -----------------");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d(TAG,"-----------------     RELEASETED TAB     -----------------");
            }
        });
        currentPage = 0;
        return mView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.prev_btn:
                if(--currentPage < 0) currentPage++;
                else movePage(currentPage);
                break;
            case R.id.next_btn:
                if(++currentPage < 12)  movePage(currentPage);
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

//            List<CalendarItem> list = getMonthSchedule(Util.getTabPositionToMonth(position));
//            mRecyclerAdapter.swap(list);
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
            movePage1(position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

//    /**
//     * 引数の月からスケジュールをを取得
//     *
//     * @param month
//     * @return
//     */
//    private List<CalendarItem> getMonthSchedule(int month) {
//        return calendarList.get(month);
//    }

    /**
     * Asettsからスケジュールを取得
     *
     * @return
     */
    private CalendarList getSchedule() {
        String jsonText = "";
        Gson gson = new Gson();
        try {
            jsonText = Util.loadTextAsset(Const.SCHEDULE_FILE_NAME);
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }

        CalendarList listModel = gson.fromJson(jsonText, CalendarList.class);

        return listModel;
    }

    private void movePage(int position){
        mFrontViewPager.setCurrentItem(position, false);
        mTabLayout.setScrollPosition(position, 0, true); // 注
    }
    private void movePage1(int position){
        mBottomViewPager.setCurrentItem(position, false);
//        mInvisibleTabLayout.setScrollPosition(position, 0, true);
    }


    private class CustomViewPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private ViewPager from;
        private ViewPager target;   // synchronized view
        private int offset1;
        private int offset2;

        CustomViewPagerOnPageChangeListener(ViewPager from, ViewPager target){
            this.from = from;
            this.target = target;
            from.addOnPageChangeListener(this);
        }

        // ページスクロール中に呼び出される
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(target.isFakeDragging()){
                if(offset1 == -1){
                    offset1 = positionOffsetPixels;
                    return;
                }
                target.fakeDragBy(positionOffsetPixels);
            }
        }

        // ページが切り替わった時に呼び出される
        @Override
        public void onPageSelected(int position) {
//            if (from.isFakeDragging()) {
//                return;
//            }
            if (!target.isFakeDragging()) {
                target.setCurrentItem(position);
            }
        }

        // スクロール状態が変化したときに呼び出される
        @Override
        public void onPageScrollStateChanged(int state) {
            if(from.isFakeDragging()){
                //	true if currently in a fake drag, false otherwise.
                return;
            }

            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                // fromと同期してtargetのスクロールを開始
                target.beginFakeDrag();
                offset1 = -1;
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                // fromと同期してtargetのスクロールを終了する
                if(target.isFakeDragging())
                target.endFakeDrag();
            }
        }
    }
}
