package jp.yuta.kohashi.esc.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.event.RefreshEvent;
import jp.yuta.kohashi.esc.event.UpdateAttendanceRateEvent;
import jp.yuta.kohashi.esc.model.AttendanceRate;
import jp.yuta.kohashi.esc.model.enums.AttendanceRateType;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.ui.adapter.AttendanceRateViewPagerAdapter;
import jp.yuta.kohashi.esc.ui.fragment.base.BaseFragment;
import jp.yuta.kohashi.esc.ui.view.CustomViewPager;
import jp.yuta.kohashi.esc.util.NotifyUtil;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * Created by yutakohashi on 2017/01/30.
 */

public class AttendanceRateParentFragment extends BaseFragment {
    private static final String TAG = AttendanceRateParentFragment.class.getSimpleName();

    // total data
    private TextView mTotalUnitNumTextView;
    private TextView mTotalAttendanceRateTextView;
    private TextView mTotalShortageUnitTextView;
    private TextView mTotalAttendanceNumTextView;
    private TextView mTotalAbsentTextView;

    private CustomViewPager mViewPager;
    private AttendanceRateViewPagerAdapter mAdapter;
    private TabLayout mTab;

    private Animation fadeInAnim;
    private Animation fadeOutAnim;
    private FrameLayout mFilterView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attendance_rate, container, false);

        mTotalUnitNumTextView = (TextView) v.findViewById(R.id.text_total_unit);
        mTotalAttendanceRateTextView = (TextView) v.findViewById(R.id.text_total_attendance_rate);
        mTotalShortageUnitTextView = (TextView) v.findViewById(R.id.text_total_shortageunit);
        mTotalAttendanceNumTextView = (TextView) v.findViewById(R.id.text_total_attendance_num);
        mTotalAbsentTextView = (TextView) v.findViewById(R.id.text_total_absent_num);

        mViewPager = (CustomViewPager) v.findViewById(R.id.attendance_viewpager);
        mTab = (TabLayout) v.findViewById(R.id.tablayout);
        mAdapter = new AttendanceRateViewPagerAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(mAdapter);
        mTab.setupWithViewPager(mViewPager);
        mTab.setTabTextColors(Color.GRAY, Color.WHITE);

        mFilterView = (FrameLayout)v.findViewById(R.id.attendance_filter);
        setTotalData();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        applySettings();

        if(isUpdate()){
            fadeInAnim = AnimationUtils.loadAnimation(getContext(),R.anim.anim_bottom_sheet_fade_in);
            mFilterView.setVisibility(View.VISIBLE);
            mFilterView.startAnimation(fadeInAnim);
            Log.d(TAG,"最新情報入った1");
            //出席の情報を更新する
            HttpConnector.request(HttpConnector.Type.ATTENDANCE_RATE, PrefUtil.getId(), PrefUtil.getPss(), new HttpConnector.Callback() {
                @Override
                public void callback(boolean bool) {
                    if(bool) {
                        PrefUtil.saveLatestUpdateData(Util.getCurrentTimeMillis());
                        Handler mHandler = new Handler();
                        Runnable runnable = new Runnable() {
                            public void run() {
                                setTotalData();
                                fadeOutAnim = AnimationUtils.loadAnimation(getContext(),R.anim.anim_bottom_sheet_fade_out);
                                mFilterView.setVisibility(View.INVISIBLE);
                                mFilterView.startAnimation(fadeOutAnim);
                                NotifyUtil.successUpdate();
                                // call event
                                EventBus.getDefault().post(new UpdateAttendanceRateEvent());
                            }
                        };
                        mHandler.postDelayed(runnable, 1000);

                    } else {
                        fadeOutAnim = AnimationUtils.loadAnimation(getContext(),R.anim.anim_bottom_sheet_fade_out);
                        mFilterView.setVisibility(View.INVISIBLE);
                        mFilterView.startAnimation(fadeOutAnim);
                        NotifyUtil.failureUpdate();
                    }

                }
            });
        }

    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onEvent(RefreshEvent event) {
        setTotalData();
    }

    int i = 1;
    /**
     * 合計データをViewに反映
     */
    private void setTotalData() {
        AttendanceRate model = PrefUtil.loadAttendanceTotalData();
        mTotalUnitNumTextView.setText(model.getUnit());
//        mTotalUnitNumTextView.setText(model.getUnit() + i++);
        mTotalAttendanceRateTextView.setText(model.getAttendanceRate());
        mTotalShortageUnitTextView.setText(model.getShortageseNumber());
        mTotalAttendanceNumTextView.setText(model.getAttendanceNumber());
        mTotalAbsentTextView.setText(model.getAbsentNumber());
    }

    private void applySettings(){
        // 設定情報を反映
        if (!PrefUtil.isDivideAttendance()) {
            mTab.getTabAt(AttendanceRateType.ALL.getId()).select();
            mViewPager.setSwipeEnable(false);
            mTab.setVisibility(View.GONE);
        } else {
            mTab.getTabAt(PrefUtil.getAttendanceTabPosition()).select();
            mViewPager.setSwipeEnable(true);
            mTab.setVisibility(View.VISIBLE);
        }
    }
}
