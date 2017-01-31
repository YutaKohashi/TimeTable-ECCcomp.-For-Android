package jp.yuta.kohashi.esc.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.event.BusHolder;
import jp.yuta.kohashi.esc.event.RefreshEvent;
import jp.yuta.kohashi.esc.model.AttendanceRateModel;
import jp.yuta.kohashi.esc.ui.adapter.AttendanceRateViewPagerAdapter;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * Created by yutakohashi on 2017/01/30.
 */

public class AttendanceRateParentFragment extends Fragment {

    // total data
    private TextView mTotalUnitNumTextView;
    private TextView mTotalAttendanceRateTextView;
    private TextView mTotalShortageUnitTextView;
    private TextView mTotalAttendanceNumTextView;
    private TextView mTotalAbsentTextView;

   private ViewPager mViewPager;
    private AttendanceRateViewPagerAdapter mAdapter;
    private TabLayout mTab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attendance_rate, container, false);

        mTotalUnitNumTextView = (TextView) v.findViewById(R.id.text_total_unit);
        mTotalAttendanceRateTextView = (TextView) v.findViewById(R.id.text_total_attendance_rate);
        mTotalShortageUnitTextView = (TextView) v.findViewById(R.id.text_total_shortageunit);
        mTotalAttendanceNumTextView = (TextView) v.findViewById(R.id.text_total_attendance_num);
        mTotalAbsentTextView = (TextView) v.findViewById(R.id.text_total_absent_num);

        mViewPager = (ViewPager) v.findViewById(R.id.attendance_viewpager);
        mTab = (TabLayout) v.findViewById(R.id.tablayout);
        mAdapter = new AttendanceRateViewPagerAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(mAdapter);
        mTab.setupWithViewPager(mViewPager);
        mTab.setTabTextColors(Color.GRAY, Color.WHITE);

        setTotalData();
        return v;
    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mAdapter = new AttendanceRateViewPagerAdapter(getChildFragmentManager(), getActivity());
//        mViewPager.setAdapter(adapter);
//    }



    @Subscribe
    public void subscribe(RefreshEvent event) {
        setTotalData();
    }

    @Override
    public void onResume() {
        super.onResume();
        BusHolder.get().register(this);

//        mAdapter = new AttendanceRateViewPagerAdapter(getChildFragmentManager(), getActivity());
////        mViewPager = (ViewPager) v.findViewById(R.id.attendance_viewpager);
//        mViewPager.setAdapter(mAdapter);
////        mTab = (TabLayout) v.findViewById(R.id.tablayout);
//        mTab.setupWithViewPager(mViewPager);
//        mTab.setTabTextColors(Color.GRAY, Color.WHITE);
//        if(!PrefUtil.isDivideAttendance()){
//            mTab.getTabAt(0).select();
//        }

    }

    @Override
    public void onPause() {
        BusHolder.get().unregister(this);
        super.onPause();
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
}
