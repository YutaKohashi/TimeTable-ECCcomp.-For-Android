package jp.yuta.kohashi.esc.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.TimeBlockModel;
import jp.yuta.kohashi.esc.ui.adapter.TimeTableRecyclerAdapter;
import jp.yuta.kohashi.esc.util.NotifyUtil;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;


public class TimeTableChangeFragment extends Fragment implements TimeTableInputDialogFragment.Callback {

    private List<List<TimeBlockModel>> timeBlockLists = new ArrayList<>();

    private RecyclerView mMonRecyclerView;
    private RecyclerView mTueRecyclerView;
    private RecyclerView mWedRecyclerView;
    private RecyclerView mThurRecyclerView;
    private RecyclerView mFriRecyclerView;

    List<TimeTableRecyclerAdapter> mAdapters;
    private List<TimeBlockModel> clickedItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table_change, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        loadLists();

        mMonRecyclerView = (RecyclerView) view.findViewById(R.id.mon_col);
        mTueRecyclerView = (RecyclerView) view.findViewById(R.id.tue_col);
        mWedRecyclerView = (RecyclerView) view.findViewById(R.id.wed_col);
        mThurRecyclerView = (RecyclerView) view.findViewById(R.id.thur_col);
        mFriRecyclerView = (RecyclerView) view.findViewById(R.id.fri_col);

        mMonRecyclerView.setHasFixedSize(true);
        mTueRecyclerView.setHasFixedSize(true);
        mWedRecyclerView.setHasFixedSize(true);
        mThurRecyclerView.setHasFixedSize(true);
        mFriRecyclerView.setHasFixedSize(true);

        mAdapters = new ArrayList<>();

        createRecyclerView(mMonRecyclerView, timeBlockLists.get(0));
        createRecyclerView(mTueRecyclerView, timeBlockLists.get(1));
        createRecyclerView(mWedRecyclerView, timeBlockLists.get(2));
        createRecyclerView(mThurRecyclerView, timeBlockLists.get(3));
        createRecyclerView(mFriRecyclerView, timeBlockLists.get(4));

    }


    private void createRecyclerView(RecyclerView recyclerView, List<TimeBlockModel> list) {
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        TimeTableRecyclerAdapter adapter = new TimeTableRecyclerAdapter(list, R.color.bg_classroom_blue, getActivity()) {
            @Override
            protected void onItemClicked(@NonNull List<TimeBlockModel> items, TimeBlockModel model) {
                super.onItemClicked(items, model);
                clickedItems = items;
                TimeTableInputDialogFragment diag = new TimeTableInputDialogFragment().newInstance(TimeTableChangeFragment.this);
                diag.setCancelable(false);
                diag.setInfo(model);
                diag.setCallback(TimeTableChangeFragment.this);
                diag.show((getActivity()).getSupportFragmentManager(), "");
            }
        };
        mAdapters.add(adapter);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 保存でーたから時間割リストのリストを取得
     */
    private void loadLists() {
        List<List<TimeBlockModel>> lists = PrefUtil.loadTimeBlockList();
        timeBlockLists.clear();
        timeBlockLists.addAll(lists);
    }

    private void loadOriginalLists() {
        List<List<TimeBlockModel>> lists = PrefUtil.loadOriginalTimeBlockList();
        timeBlockLists.clear();
        timeBlockLists.addAll(lists);
    }


    // execute from activity
    public void allReset() {
        loadOriginalLists();
        swapAll();

        // オリジナルリストをデフォルトリストに反映
        for (List<TimeBlockModel> list : timeBlockLists) {
            saveList(list);
        }

        NotifyUtil.allReset();
    }

    /**
     * リールドのtimeBlockLIstsでswapする
     */
    private void swapAll() {
        for (int i = 0; i < 5; i++) {
            mAdapters.get(i).swap(timeBlockLists.get(i));
        }
    }


    //**
    //region Callback from Dialog
    //**


    @Override
    public void positive(TimeBlockModel before, TimeBlockModel after) {
        // データが変更されている場合のみ
        if (!before.equals(after)) {
            List<TimeBlockModel> list = createSaveList(after);
            saveList(list);
            NotifyUtil.saveData();
            loadLists();
            swapAll();
//
//            TODO :    ウィジェット更新
//            int widgetIDs[] = AppWidgetManager.getInstance(App.getAppContext()).getAppWidgetIds(new ComponentName(getContext(), TimeTableWidget.class));
//            //　ウィジェット更新
//            for(int i:widgetIDs) {
//                TimeTableWidget.updateAppWidget(App.getAppContext(),AppWidgetManager.getInstance(App.getAppContext()),i);
//            }
//

//            int widgetIDs[] = AppWidgetManager.getInstance(App.getAppContext()).getAppWidgetIds(new ComponentName(getContext(), TimeTableWidget.class));
//
//            for (int id : widgetIDs)
//                AppWidgetManager.getInstance(App.getAppContext()).notifyAppWidgetViewDataChanged(id, R.id.widget_view);

        } else {
            NotifyUtil.notChangeData();
        }
    }

    @Override
    public void negative() {
    }


    /**
     * ダイアログから受け取った変更を保存する
     *
     * @param items
     */
    private void saveList(List<TimeBlockModel> items) {
        // 曜日別
        switch (items.get(0).getColNum()) {
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

    /**
     * 保存するリストを作成
     *
     * @param item
     * @return
     */
    private List<TimeBlockModel> createSaveList(TimeBlockModel item) {
        List<TimeBlockModel> items = new ArrayList<>();
        for (TimeBlockModel m : clickedItems) {
            if (item.getRowNum() == m.getRowNum()) {
                items.add(item);
            } else {
                items.add(m);
            }
        }
        return items;
    }

    //**
    //endregion
    //**


    /**
     * RecyclerViewのレイアウトマネージャにScroll無効化処理を追加
     */
    public class CustomLinearLayoutManager extends LinearLayoutManager {
        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }
}
