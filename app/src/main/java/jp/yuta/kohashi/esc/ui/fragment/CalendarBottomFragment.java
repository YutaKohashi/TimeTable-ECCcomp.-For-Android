package jp.yuta.kohashi.esc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public static final String KEY_SCHEDULE_LIST = "key_schedule_list";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        scheduleRoot = (ScheduleRoot)bundle.getSerializable(KEY_SCHEDULE_LIST);
        return super.onCreateView(inflater, container, savedInstanceState);
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
//        List<ScheduleItem> list = new ArrayList<>();
//        /**
//         * 行事がない列を削除
//                */
//        for(ScheduleItem item :tmp){
//            String body = item.getBody();
//            if(!TextUtils.isEmpty(body)){
//                list.add(item);
//            }
//        }
        // 0日を削除
        if(tmp.get(tmp.size() - 1).getDay() == 0){
            tmp.remove(tmp.size() - 1);
        }
//        Log.d(CalendarBottomFragment.class.getSimpleName(),tmp.get(tmp.size() - 1).getDay());
//    return list;
        return tmp;
    }

    @Override
    protected void swap() {

    }

    @Override
    protected void getSavedItems() {

    }
}
