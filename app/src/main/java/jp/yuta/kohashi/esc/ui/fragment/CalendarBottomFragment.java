package jp.yuta.kohashi.esc.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.List;

import jp.yuta.kohashi.esc.model.schedule.CalendarItem;
import jp.yuta.kohashi.esc.ui.adapter.CalendarRecyclerAdapter;
import jp.yuta.kohashi.esc.ui.fragment.base.BaseRecyclerViewFragment;

/**
 * Created by yutakohashi on 2017/02/06.
 */

public class CalendarBottomFragment extends BaseRecyclerViewFragment {

    private List<CalendarItem> list;
    private CalendarRecyclerAdapter mRecyclerAdapter;

    public static CalendarBottomFragment newInstance(List<CalendarItem> calendarItems){
        CalendarBottomFragment fragment = new CalendarBottomFragment();
        fragment.list  = calendarItems;
        return fragment;
    }

    @Override
    public void createItems() {}

    @Override
    public void initView(View v) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerAdapter = new CalendarRecyclerAdapter(list,getContext());
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    protected void swap() {

    }

    @Override
    protected void getSavedItems() {

    }
}
