package jp.yuta.kohashi.esc.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.network.api.model.schedule.ScheduleItem;
import jp.yuta.kohashi.esc.network.api.model.schedule.ScheduleRoot;
import jp.yuta.kohashi.esc.ui.adapter.CalendarRecyclerAdapter;
import jp.yuta.kohashi.esc.ui.fragment.base.BaseRecyclerViewFragment;

/**
 * Created by yutakohashi on 2017/02/06.
 */

public class CalendarBottomFragment extends BaseRecyclerViewFragment {

    private ScheduleRoot scheduleRoot;
    private CalendarRecyclerAdapter mRecyclerAdapter;

    public void setList(ScheduleRoot scheduleRoot){
        this.scheduleRoot = scheduleRoot;
    }

    @Override
    public void createItems() {}

    @Override
    public void initView(View v) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerAdapter = new CalendarRecyclerAdapter(createList(),getContext());
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    private List<ScheduleItem> createList(){
        List<ScheduleItem> tmp =  scheduleRoot.getSchedules().get(0).getDetails();
        List<ScheduleItem> list = new ArrayList<>();
        /**
         * 行事がない列を削除
         */
        for(ScheduleItem item :tmp){
            String body = item.getBody();
            if(!TextUtils.isEmpty(body)){
                list.add(item);
            }
        }
        return list;
    }

    @Override
    protected void swap() {

    }

    @Override
    protected void getSavedItems() {

    }
}
