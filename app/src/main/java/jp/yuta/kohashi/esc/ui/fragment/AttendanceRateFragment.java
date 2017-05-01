package jp.yuta.kohashi.esc.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.event.RefreshEvent;
import jp.yuta.kohashi.esc.event.UpdateAttendanceRateEvent;
import jp.yuta.kohashi.esc.model.AttendanceRate;
import jp.yuta.kohashi.esc.model.enums.AttendanceRateType;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.ui.adapter.AttendanceRateRecyclerAdapter;
import jp.yuta.kohashi.esc.ui.fragment.base.BaseRefreshRecyclerViewFragment;
import jp.yuta.kohashi.esc.util.NotifyUtil;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceRateFragment extends BaseRefreshRecyclerViewFragment {
    private static final String TAG = AttendanceRateFragment.class.getSimpleName();
    private AttendanceRateRecyclerAdapter mRecyclerAdapter;
    private String userId;
    private String password;
    private AttendanceRateType type;
    private  List<AttendanceRate> items;

    public static AttendanceRateFragment newInstance(AttendanceRateType type) {
        AttendanceRateFragment fragment = new AttendanceRateFragment();
        fragment.type = type;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        userId = PrefUtil.getId();
        password = PrefUtil.getPss();

        return v;
    }

    @Override
    public void createItems() {
        getSavedItems();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerAdapter = new AttendanceRateRecyclerAdapter(items, getActivity());
        if(items.size() > 0) inVisibleEmptyTextView();
        else visibleEmptyTextView();
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        swap();
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();

    }

    @Subscribe
    public void onEvent(UpdateAttendanceRateEvent event) {
        swap();
    }

    //　Pull To Refresh
    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!Util.netWorkCheck()) {
            NotifyUtil.failureNetworkConnection();
            endRefresh();
            return;
        }

        disableScroll();
        HttpConnector.request(HttpConnector.Type.ATTENDANCE_RATE, userId, password, bool -> {
            if (bool) {
                swap();
                if(items.size() > 0) inVisibleEmptyTextView();
                else visibleEmptyTextView();
                PrefUtil.saveLatestUpdateData(Util.getCurrentTimeMillis());
                NotifyUtil.successUpdate();

                // call event
                EventBus.getDefault().post(new RefreshEvent());
            } else {
                NotifyUtil.failureUpdate();
            }
            endRefresh();
            enableScroll();
        });
    }

    @Override
    protected void swap() {
        getSavedItems();
        mRecyclerAdapter.swap(items);
    }

    @Override
    protected void getSavedItems(){
        if(items == null) items = new ArrayList<>();
        else items.clear();
        List<AttendanceRate> temp = PrefUtil.loadAttendanceRateModelList();
        if (type == AttendanceRateType.ALL) {
            items.addAll(temp);
        } else {
            for (AttendanceRate item : temp) {
                if (item.getType() == type)
                    items.add(item);
            }
        }
    }

}
