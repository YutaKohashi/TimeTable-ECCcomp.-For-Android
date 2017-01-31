package jp.yuta.kohashi.esc.ui.fragment;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.yuta.kohashi.esc.Const;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.schedule.CalendarItemModel;
import jp.yuta.kohashi.esc.model.schedule.CalendarListModel;
import jp.yuta.kohashi.esc.ui.adapter.CalendarRecyclerAdapter;
import jp.yuta.kohashi.esc.ui.adapter.CalendarViewPagerAdapter;
import jp.yuta.kohashi.esc.util.Util;

/**
 * Created by yutakohashi on 2017/01/14.
 */

public class CalendarFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener , View.OnClickListener {
    private static final String TAG = CalendarFragment.class.getSimpleName();

    private Button mPrevBtn;
    private Button mNextBtn;
    private RecyclerView mRecyclerView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private CalendarViewPagerAdapter mPagerAdapter;
    private CalendarRecyclerAdapter mRecyclerAdapter;
    int currentPage;
    private CalendarListModel calendarListModel;
    private boolean flag = true;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_calendar_parent, container, false);
        calendarListModel = getSchedule(); //スケジュールを取得

        mPrevBtn = (Button) mView.findViewById(R.id.prev_btn);
        mNextBtn = (Button)mView.findViewById(R.id.next_btn);
        mPrevBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mViewPager = (ViewPager) mView.findViewById(R.id.calendar_viewpager);
        mTabLayout = (TabLayout) mView.findViewById(R.id.tab_calendar);
        mPagerAdapter = new CalendarViewPagerAdapter(getContext(),calendarListModel);
        mViewPager.addOnPageChangeListener(new PageChangeListener());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        //ViewTreeObserverをフック
        mTabLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);

        currentPage = 0;

        mRecyclerAdapter = new CalendarRecyclerAdapter(new ArrayList<CalendarItemModel>(), getContext());

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.calendar_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mRecyclerAdapter);

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

            List<CalendarItemModel> list = getMonthSchedule(Util.getTabPositionToMonth(position));
            mRecyclerAdapter.swap(list);
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
            mTabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    /**
     * 引数の月からスケジュールをを取得
     *
     * @param month
     * @return
     */
    private List<CalendarItemModel> getMonthSchedule(int month) {
        return calendarListModel.get(month);
    }

    /**
     * Asettsからスケジュールを取得
     *
     * @return
     */
    private CalendarListModel getSchedule() {
        String jsonText = "";
        Gson gson = new Gson();
        try {
            jsonText = Util.loadTextAsset(Const.SCHEDULE_FILE_NAME);
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }

        CalendarListModel listModel = gson.fromJson(jsonText, CalendarListModel.class);

        return listModel;
    }

    private void movePage(int position){
        mViewPager.setCurrentItem(position, false);
        mTabLayout.setScrollPosition(position, 0, true); // 注
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        switch(newConfig.orientation){
            case Configuration.ORIENTATION_PORTRAIT:

            case Configuration.ORIENTATION_LANDSCAPE:
//                FragmentManager manager = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft = manager.beginTransaction();
//                Fragment newFragment = this;
//                this.onDestroy();
//                ft.remove(this);
//                ft.replace(mView.getId(),newFragment);
//                //container is the ViewGroup of current fragment
//                ft.addToBackStack(null);
//                ft.commit();
//                // 横長
//                break;

        }
        super.onConfigurationChanged(newConfig);

    }
}
