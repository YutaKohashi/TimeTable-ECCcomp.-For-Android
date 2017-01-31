package jp.yuta.kohashi.esc.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.event.BusHolder;
import jp.yuta.kohashi.esc.event.RefreshEvent;
import jp.yuta.kohashi.esc.model.AttendanceRateModel;
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
    private  List<AttendanceRateModel> items;

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
        getItems();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerAdapter = new AttendanceRateRecyclerAdapter(items, getActivity());
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getItems();
        mRecyclerAdapter.swap(items);
    }

    //　Pull To Refresh
    @Override
    public void onRefresh() {
        if (!Util.netWorkCheck()) {
            NotifyUtil.failureNetworkConnection();
            endRefresh();
            return;
        }

        disableScroll();
        new HttpConnector().request(HttpConnector.Type.ATTENDANCE_RATE, userId, password, new HttpConnector.Callback() {
            @Override
            public void callback(boolean bool) {
                if (bool) {
                    getItems();
                    Log.d(TAG,String.valueOf(items.size()));
                    //更新処理
                    mRecyclerAdapter.swap(items);
                    Log.d(TAG,String.valueOf(items.size()));
                    NotifyUtil.successUpdate();
                    // call event
                    BusHolder.get().post(new RefreshEvent());
                } else {
                    NotifyUtil.failureUpdate();
                }
                endRefresh();
                enableScroll();
            }
        });
    }

    /**
     * 任意のアイテムリストを返す
     * @return
     */
    private void getItems(){
        if(items == null){
            items = new ArrayList<>();
        }
        items.clear();

        List<AttendanceRateModel> temp = PrefUtil.loadAttendanceRateModelList();
        if (type == AttendanceRateType.ALL) {
            items.addAll(temp);
        } else {
            for (AttendanceRateModel item : temp) {
                if (item.getType() == type)
                    items.add(item);
            }
        }
    }
}
