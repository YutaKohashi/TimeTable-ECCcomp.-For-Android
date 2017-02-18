package jp.yuta.kohashi.esc.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.TimeBlockItem;
import jp.yuta.kohashi.esc.ui.view.TimeTableBottomSheet;
import jp.yuta.kohashi.esc.ui.view.TimeTableView;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeTableFragment extends Fragment implements TimeTableView.OnCellClickListener {

    private TimeTableView mTimeTableView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimeTableView.setData(PrefUtil.loadTimeBlockList());
    }

    private View initView() {
        mTimeTableView = new TimeTableView(getActivity());
        mTimeTableView.setClassRoomColor(R.color.bg_classroom_green);
        mTimeTableView.setOnCellClickListener(this);
        return mTimeTableView;
    }

    /**
     * セルクリック時
     *
     * @param items
     * @param model
     */
    @Override
    public void onCellClick(List<TimeBlockItem> items, TimeBlockItem model) {
        if (TextUtils.isEmpty(model.getSubject())) return;

        new TimeTableBottomSheet.Builder(this)
                .subject(model.getSubject())
                .teacher(model.getTeacherName())
                .time(getTime(model.getRowNum()))
                .build()
                .show();
    }


    @NonNull
    private String getTime(int i) {
        switch (i) {
            case 0:
                return getResources().getString(R.string.time_zero_class);
            case 1:
                return getResources().getString(R.string.time_one_class);
            case 2:
                return getResources().getString(R.string.time_two_class);
            case 3:
                return getResources().getString(R.string.time_three_class);
            case 4:
                return getResources().getString(R.string.time_four_class);
            case 5:
                return getResources().getString(R.string.time_five_class);
            default:
                return "";
        }
    }
}
