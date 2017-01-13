package jp.yuta.kohashi.esc.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.AttendanceRateModel;
import jp.yuta.kohashi.esc.ui.adapter.AttendanceRateRecyclerAdapter;
import jp.yuta.kohashi.esc.util.preference.PrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceRateFragment extends Fragment {

    // total data
    private TextView mTotalUnitNumTextView;
    private TextView mTotalAttendanceRateTextView;
    private TextView mTotalShortageUnitTextView;
    private TextView mTotalAttendanceNumTextView;
    private TextView mTotalAbsentTextView;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance_rate, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        List<AttendanceRateModel> list = PrefManager.loadAttendanceRateModelList();

        mRecyclerView = (RecyclerView)view.findViewById(R.id.attendance_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AttendanceRateRecyclerAdapter(list,getActivity());
        mRecyclerView.setAdapter(adapter);

        mTotalUnitNumTextView = (TextView) view.findViewById(R.id.text_total_unit);
        mTotalAttendanceRateTextView = (TextView) view.findViewById(R.id.text_total_attendance_rate);
        mTotalShortageUnitTextView = (TextView) view.findViewById(R.id.text_total_shortageunit);
        mTotalAttendanceNumTextView = (TextView) view.findViewById(R.id.text_total_attendance_num);
        mTotalAbsentTextView = (TextView) view.findViewById(R.id.text_total_absent_num);

        setTotalData();
    }

    /**
     * 合計データをViewに反映
     */
    private void setTotalData() {
        AttendanceRateModel model = PrefManager.loadAttendanceTotalData();
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

}
