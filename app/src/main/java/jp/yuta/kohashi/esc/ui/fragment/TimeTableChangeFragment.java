package jp.yuta.kohashi.esc.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.api.model.timeTable.TimeTable;
import jp.yuta.kohashi.esc.ui.view.TimeTableView;
import jp.yuta.kohashi.esc.util.NotifyUtil;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;


public class TimeTableChangeFragment extends Fragment implements TimeTableInputDialogFragment.Callback, TimeTableView.OnCellClickListener {

    private List<List<TimeTable>> timeBlockLists = new ArrayList<>();
    private List<TimeTable> clickedItems;
    private TimeTableView mTimeTableView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table_change, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mTimeTableView = new TimeTableView(getActivity());
        mTimeTableView.setClassRoomColor(R.color.bg_classroom_blue);
        mTimeTableView.setData(PrefUtil.loadTimeBlockList());
        mTimeTableView.setOnCellClickListener(this);
        ((FrameLayout) view.findViewById(R.id.container)).addView(mTimeTableView);
    }

    /**
     * セルクリック時
     * ダイアログ表示
     *
     * @param items
     * @param model
     */
    @Override
    public void onCellClick(List<TimeTable> items, TimeTable model) {
        clickedItems = items;
        TimeTableInputDialogFragment diag = new TimeTableInputDialogFragment().newInstance(TimeTableChangeFragment.this);
        diag.setCancelable(false);
        diag.setInfo(model);
        diag.setCallback(TimeTableChangeFragment.this);
        diag.show((getActivity()).getSupportFragmentManager(), "");
    }

    /**
     * ダイアログコールバック
     *
     * @param before
     * @param after
     */
    @Override
    public void positive(TimeTable before, TimeTable after) {
        // データが変更されている場合のみ
        if (!before.equals(after)) {
            List<TimeTable> list = createSaveList(after);
            saveList(list);
            NotifyUtil.saveData();
            mTimeTableView.loadList(PrefUtil.loadTimeBlockList());
            mTimeTableView.swapAll();
        } else {
            NotifyUtil.notChangeData();
        }
    }

    @Override
    public void negative() {
    }

    /**
     * 保存するリストを作成
     *
     * @param item
     * @return
     */
    private List<TimeTable> createSaveList(TimeTable item) {
        List<TimeTable> items = new ArrayList<>();
        for (TimeTable m : clickedItems) {
            if (item.getTerm() == m.getTerm()) {
                items.add(item);
            } else {
                items.add(m);
            }
        }
        return items;
    }

    // execute from activity
    public void allReset() {
        timeBlockLists = PrefUtil.loadOriginalTimeBlockList();
        mTimeTableView.allReset(timeBlockLists, new TimeTableView.CallBack() {
            @Override
            public void callback() {
                // オリジナルリストをデフォルトリストに反映
                for (List<TimeTable> list : timeBlockLists) {
                    saveList(list);
                }
                NotifyUtil.allReset();
            }
        });
    }

    /**
     * ダイアログから受け取った変更を保存する
     *
     * @param items
     */
    private void saveList(List<TimeTable> items) {
        // 曜日別
        switch (items.get(0).getWeek()) {
            case 1:
                PrefUtil.saveTimeTableMon(items);
                break;
            case 2:
                PrefUtil.saveTimeTableTue(items);
                break;
            case 3:
                PrefUtil.saveTimeTableWed(items);
                break;
            case 4:
                PrefUtil.saveTimeTableThur(items);
                break;
            case 5:
                PrefUtil.saveTimeTableFri(items);
                break;
        }
    }
}
