package jp.yuta.kohashi.esc.ui.fragment;


import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.AttendanceRate;
import jp.yuta.kohashi.esc.model.enums.AttendanceRateType;
import jp.yuta.kohashi.esc.ui.adapter.AttendanceDivideRecyclerAdapter;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceDividePrefFragment extends Fragment implements SwitchCompat.OnCheckedChangeListener, View.OnClickListener {

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
        View v1 = view.findViewById(R.id.include_switch);
        ((TextView) v1.findViewById(R.id.title_text_view)).setText(getResources().getString(R.string.pref_divide));
        ((ImageView) v1.findViewById(R.id.image_view)).setImageDrawable(Util.getDrawable(R.drawable.ic_view_compact));

        View v2 = view.findViewById(R.id.include_arrow);
        ((TextView) v2.findViewById(R.id.title_text_view)).setText(getResources().getString(R.string.pref_default_tab));
        ((ImageView) v2.findViewById(R.id.image_view)).setImageDrawable(Util.getDrawable(R.drawable.ic_turned_in));
        v2.setOnClickListener(this);
        // 設定情報を復元
        mSwitchCompat = (SwitchCompat) view.findViewById(R.id.switch_view);
        mSwitchCompat.setOnCheckedChangeListener(this);
        mSwitchCompat.setChecked(PrefUtil.isDivideAttendance());

        List<AttendanceRate> items = PrefUtil.loadAttendanceRateModelList();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.divide_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new AttendanceDivideRecyclerAdapter(items, getContext()) {
            @Override
            protected void onItemChecked(int position, @NonNull List<AttendanceRate> items, boolean b, AttendanceRateType type) {
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
    private List<AttendanceRate> updateModels(List<AttendanceRate> items, AttendanceRate model, AttendanceRateType type) {
        List<AttendanceRate> temp = new ArrayList<>();

        for (AttendanceRate item : items) {
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
        List<AttendanceRate> items = mAdapter.getItems();
        PrefUtil.saveAttendanceRate(items);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        PrefUtil.saveAttendanceDivide(b);
    }

    @Override
    public void onClick(View view) {
        int[][] states = new int[][] {
                new int[] {}
        };

        int[] colors = new int[] {
                Util.getColor(R.color.colorPrimary)
        };

        ColorStateList stateList = new ColorStateList(states, colors);

        int position = PrefUtil.getAttendanceTabPosition();
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .items(R.array.attendance_rate_type_list)
                .choiceWidgetColor(stateList)
                .contentColor(Util.getColor(R.color.diag_text_color))
                .itemsCallbackSingleChoice(position, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        PrefUtil.saveAttendanceTabPosition(which);
                        return true;
                    }
                });
        MaterialDialog dialog = builder.build();
        dialog.show();
    }
}
