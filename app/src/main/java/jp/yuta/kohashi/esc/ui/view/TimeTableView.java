package jp.yuta.kohashi.esc.ui.view;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.TimeBlockItem;
import jp.yuta.kohashi.esc.ui.adapter.TimeTableRecyclerAdapter;

/**
 * Created by yuta on 2017/02/13.
 */

public class TimeTableView extends RelativeLayout {

    private Context mContext;

    private View view;
    private List<List<TimeBlockItem>> timeBlockLists;
    private List<RecyclerView> recyclerViewList;
    private List<TimeTableRecyclerAdapter> mAdapters;
    private ListenerInfo mListenerInfo;
    private int classRoomColor;

    public TimeTableView(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.view_time_table, this);
        mContext = context;
    }

    public TimeTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setClassRoomColor(@ColorRes int color){
        classRoomColor = color;
    }

    /**
     * データをセットするメソッド
     */
    public void setData(List<List<TimeBlockItem>> lists) {
        loadList(lists);
        initView();
    }

    /**
     * セルクリックじのリスナ
     *
     * @param l
     */
    public void setOnCellClickListener(@Nullable OnCellClickListener l) {
        if (!isClickable()) {
            setClickable(true);
        }
        getListenerInfo().mOnCellClickListener = l;
    }

    public interface OnCellClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param items
         * @param model
         */
        void onCellClick(List<TimeBlockItem> items, TimeBlockItem model);
    }

    public void swapAll() {
        for (int i = 0; i < timeBlockLists.size(); i++) {
            mAdapters.get(i).swap(timeBlockLists.get(i));
        }
    }

    /**
     * 表示する時間割をloadする
     *
     * @param lists
     */
    public void loadList(List<List<TimeBlockItem>> lists) {
        if (timeBlockLists == null) timeBlockLists = new ArrayList<>();
        timeBlockLists.clear();
        timeBlockLists.addAll(lists);
    }

    /**
     * オリジナルのリストをViewに反映し,
     *
     * @param lists
     */
    public void allReset(List<List<TimeBlockItem>> lists, CallBack callBack) {
        loadList(lists);
        swapAll();
        callBack.callback();
    }

    public interface CallBack {
        void callback();
    }

    private void initView() {
        recyclerViewList = new ArrayList<>();
        recyclerViewList.add((RecyclerView) view.findViewById(R.id.mon_col));
        recyclerViewList.add((RecyclerView) view.findViewById(R.id.tue_col));
        recyclerViewList.add((RecyclerView) view.findViewById(R.id.wed_col));
        recyclerViewList.add((RecyclerView) view.findViewById(R.id.thur_col));
        recyclerViewList.add((RecyclerView) view.findViewById(R.id.fri_col));

        for (int i = 0; i < timeBlockLists.size(); i++) {
            recyclerViewList.get(i).setHasFixedSize(true);
            createRecyclerView(recyclerViewList.get(i), timeBlockLists.get(i));
        }
    }


    /**
     * それぞれのRecyclerVIewにアダプターを適用してclickイベントの処理を分岐
     * <p>
     * isEnableBottomSheetのフラグに応じてcllickイベントを処理するかを決定
     *
     * @param recyclerView
     * @param list
     */
    private void createRecyclerView(RecyclerView recyclerView, List<TimeBlockItem> list) {
        if (mAdapters == null) mAdapters = new ArrayList<>();
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        TimeTableRecyclerAdapter adapter = new TimeTableRecyclerAdapter(list, classRoomColor , mContext) {
            @Override
            protected void onItemClicked(@NonNull List<TimeBlockItem> items, TimeBlockItem model) {
                super.onItemClicked(items, model);
                mListenerInfo.mOnCellClickListener.onCellClick(items, model);
            }
        };
        mAdapters.add(adapter);
        recyclerView.setAdapter(adapter);
    }

    /**
     * RecyclerViewのレイアウトマネージャにScroll無効化処理を追加
     *
     * レイアウトマネーじゃで適用
     */
    private class CustomLinearLayoutManager extends LinearLayoutManager {
        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

    /**
     * 　リスナを定義
     */
    ListenerInfo getListenerInfo() {
        if (mListenerInfo != null) {
            return mListenerInfo;
        }
        mListenerInfo = new ListenerInfo();
        return mListenerInfo;
    }

    static class ListenerInfo {
        public OnCellClickListener mOnCellClickListener;
    }
}
