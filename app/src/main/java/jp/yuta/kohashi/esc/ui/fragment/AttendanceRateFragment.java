package jp.yuta.kohashi.esc.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.AttendanceRateModel;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.ui.adapter.AttendanceRateRecyclerAdapter;
import jp.yuta.kohashi.esc.util.NotifyUtil;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceRateFragment extends Fragment implements PullRefreshLayout.OnRefreshListener{

    // total data
    private TextView mTotalUnitNumTextView;
    private TextView mTotalAttendanceRateTextView;
    private TextView mTotalShortageUnitTextView;
    private TextView mTotalAttendanceNumTextView;
    private TextView mTotalAbsentTextView;

    private RecyclerView mRecyclerView;
    private AttendanceRateRecyclerAdapter mRecyclerAdapter;

    private PullRefreshLayout mPullrefreshLayout;

    private String userId;
    private String password;
    private ScrollController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance_rate, container, false);
        userId = PrefUtil.getId();
        password = PrefUtil.getPss();
        controller =  new ScrollController();
        initView(view);

        return view;
    }

    private void initView(View view) {
        List<AttendanceRateModel> list = PrefUtil.loadAttendanceRateModelList();

        mRecyclerView = (RecyclerView)view.findViewById(R.id.attendance_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnItemTouchListener(controller);
        mRecyclerAdapter = new AttendanceRateRecyclerAdapter(list,getActivity());
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mTotalUnitNumTextView = (TextView) view.findViewById(R.id.text_total_unit);
        mTotalAttendanceRateTextView = (TextView) view.findViewById(R.id.text_total_attendance_rate);
        mTotalShortageUnitTextView = (TextView) view.findViewById(R.id.text_total_shortageunit);
        mTotalAttendanceNumTextView = (TextView) view.findViewById(R.id.text_total_attendance_num);
        mTotalAbsentTextView = (TextView) view.findViewById(R.id.text_total_absent_num);

        mPullrefreshLayout = (PullRefreshLayout)view.findViewById(R.id.refresh);
        mPullrefreshLayout.setOnRefreshListener(this);
        setTotalData();
    }

    /**
     * 合計データをViewに反映
     */
    private void setTotalData() {
        AttendanceRateModel model = PrefUtil.loadAttendanceTotalData();
        mTotalUnitNumTextView.setText(model.getUnit());
        mTotalAttendanceRateTextView.setText(model.getAttendanceRate());
        mTotalShortageUnitTextView.setText(model.getShortageseNumber());
        mTotalAttendanceNumTextView.setText(model.getAttendanceNumber());
        mTotalAbsentTextView.setText(model.getAbsentNumber());
    }

    /**
     * 合計データを表示するビュー
     */
    private void initTotalDataView() {

    }

    //　Pull To Refresh
    @Override
    public void onRefresh() {
        if(!Util.netWorkCheck()){
            NotifyUtil.failureNetworkConnection();
            endRefresh();
            return;
        }

        controller.disableScroll();
        new HttpConnector().request(HttpConnector.Type.ATTENDANCE_RATE, userId, password, new HttpConnector.Callback() {
            @Override
            public void callback(boolean bool) {
                if(bool){
                    //更新処理
                    mRecyclerAdapter.swap(PrefUtil.loadAttendanceRateModelList());
                    //合計データ
                    setTotalData();
                    NotifyUtil.successUpdate();
                } else{
                    NotifyUtil.failureUpdate();
                }
                endRefresh();
                controller.enableScroll();
            }
        });
    }

    /**
     * リフレッシュを終了するメソッド
     */
    private void endRefresh(){
        mPullrefreshLayout.setRefreshing(false);
    }

}
