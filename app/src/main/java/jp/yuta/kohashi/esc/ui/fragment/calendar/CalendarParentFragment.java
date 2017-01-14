package jp.yuta.kohashi.esc.ui.fragment.calendar;

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

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
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

public class CalendarParentFragment extends Fragment {
    private static final String TAG = CalendarParentFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private CalendarViewPagerAdapter mPagerAdapter;
    private CalendarRecyclerAdapter mRecyclerAdapter;
    int currentPage;
    private CalendarListModel calendarListModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar_parent, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.calendar_viewpager);
        mPagerAdapter = new CalendarViewPagerAdapter(getContext());
        mViewPager.addOnPageChangeListener(new PageChangeListener());
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout = (TabLayout) view.findViewById(R.id.tab_calendar);
        mTabLayout.setupWithViewPager(mViewPager);

        currentPage = 0;

        calendarListModel = getSchedule(); //スケジュールを取得

        mRecyclerAdapter = new CalendarRecyclerAdapter(new ArrayList<CalendarItemModel>(), getContext());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.calendar_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mRecyclerAdapter);

        int i = Util.getMonthToTabPosition(Const.MONTH);
        selectPage(i);

        return view;
    }
    private void selectPage(int pageIndex){
        mViewPager.setCurrentItem(pageIndex);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    class PageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            // Page change Operation!
            super.onPageSelected(position);
            currentPage = position;

            List<CalendarItemModel> list = getMonthSchedule(Util.getTabPositionToMonth(position));
            mRecyclerAdapter.swap(list);
        }
    }

    private List<CalendarItemModel> getMonthSchedule(int month) {
        return calendarListModel.get(month);
    }

    private CalendarListModel getSchedule() {
        String jsonText = "";
        Gson gson = new Gson();
        try {
            jsonText = Util.loadTextAsset(Const.SCHEDULE_FILE_NAME, getContext());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }

        CalendarListModel listModel = gson.fromJson(jsonText, CalendarListModel.class);

        return listModel;
    }
}
