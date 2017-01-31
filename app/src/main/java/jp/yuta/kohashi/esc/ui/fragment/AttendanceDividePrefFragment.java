package jp.yuta.kohashi.esc.ui.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.AttendanceRateModel;
import jp.yuta.kohashi.esc.model.enums.AttendanceRateType;
import jp.yuta.kohashi.esc.ui.adapter.AttendanceDivideRecyclerAdapter;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceDividePrefFragment extends Fragment implements SwitchCompat.OnCheckedChangeListener {

    private static final String TAG = AttendanceDividePrefFragment.class.getSimpleName();

    RecyclerView mRecyclerView;
    AttendanceDivideRecyclerAdapter mAdapter;
    SwitchCompat mSwitchCompat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance_divide, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {

        ((TextView) view.findViewById(R.id.title_text_view)).setText(getResources().getString(R.string.pref_divide));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            ((ImageView) view.findViewById(R.id.image_view)).setImageDrawable(getResources().getDrawable(R.drawable.ic_view_compact, null));
        else
            ((ImageView) view.findViewById(R.id.image_view)).setImageDrawable(getResources().getDrawable(R.drawable.ic_view_compact));
        // 設定情報を復元
        mSwitchCompat = (SwitchCompat) view.findViewById(R.id.switch_view);
        mSwitchCompat.setOnCheckedChangeListener(this);
        mSwitchCompat.setChecked(PrefUtil.isDivideAttendance());

        List<AttendanceRateModel> items = PrefUtil.loadAttendanceRateModelList();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.divide_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new AttendanceDivideRecyclerAdapter(items, getContext()) {
            @Override
            protected void onItemChecked(int position, @NonNull List<AttendanceRateModel> items, boolean b, AttendanceRateType type) {
                super.onItemChecked(position, items, b, type);
                if (b) {
                    mAdapter.swap(updateModels(items, items.get(position), type));
                }
            }
        };

        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * チェック項目を更新
     *
     * @param items
     * @param model
     * @param type
     * @return
     */
    private List<AttendanceRateModel> updateModels(List<AttendanceRateModel> items, AttendanceRateModel model, AttendanceRateType type) {
        List<AttendanceRateModel> temp = new ArrayList<>();

        for (AttendanceRateModel item : items) {
            if (model.getSubjectName().equals(item.getSubjectName())) {
                item.setType(type);
            }
            temp.add(item);
        }

        return temp;
    }

    /**
     * 保存処理
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        // save
        List<AttendanceRateModel> items = mAdapter.getItems();
        PrefUtil.saveAttendanceRate(items);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        PrefUtil.saveAttendanceDivide(b);
        Log.d(TAG,"onCheckedChanged : "  + b);
    }
}
