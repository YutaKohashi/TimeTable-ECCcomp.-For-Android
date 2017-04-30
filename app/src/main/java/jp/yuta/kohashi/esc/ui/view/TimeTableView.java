package jp.yuta.kohashi.esc.ui.view;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.api.model.timeTable.TimeTable;
import jp.yuta.kohashi.esc.ui.adapter.TimeTableRecyclerAdapter;

/**
 * Created by yuta on 2017/02/13.
 */

public class TimeTableView extends RelativeLayout {
    private static final String TAG = TimeTableView.class.getSimpleName();
    private Context mContext;
    private View view;

    /**
     * 日曜日有効無効フラグ
     * default:enable
     */
    private boolean isEnableSunCol;

    /**
     * 土曜日有効無効フラグ
     * default:enable
     */
    private boolean isEnableSatCol;

    /**
     * 0限有効無効フラグ
     * default:enable
     */
    private boolean isEnableZeroGen;

    /**
     * 5限有効無効フラグ
     * default:enable
     */
    private boolean isEnableGoGen;



    /**
     * 月曜から日曜日の時間割リストをリストにして渡す
     */
    private List<List<TimeTable>> timeBlockLists;

    private List<RecyclerView> recyclerViewList;
    private List<TimeTableRecyclerAdapter> mAdapters;
    private ListenerInfo mListenerInfo;
    private int classRoomColor;

    private TextView mSunHeaderView;
    private TextView mSatHeaderView;


    //***************************************************************************************************
    // contractors

    public TimeTableView(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.view_time_table, this);
        mContext = context;
        isEnableSatCol = true;
        isEnableSunCol = true;
        isEnableZeroGen = true;
        isEnableGoGen = true;
    }

    public TimeTableView(Context context, boolean isEnableSatCol, boolean isEnableSunCol) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.view_time_table, this);
        mContext = context;

        this.isEnableSatCol = isEnableSatCol;
        this.isEnableSunCol = isEnableSunCol;
    }


    public TimeTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //***************************************************************************************************
    // public methods

    /**
     * 教室のバックグラウンドカラーを設定するメソッド
     * @param color
     */
    public void setClassRoomColor(@ColorRes int color){
        classRoomColor = color;
    }

    /**
     * データをセットするメソッド
     */
    public void setData(List<List<TimeTable>> lists) {
        loadList(lists);
        /**
         * データがセットされた段階で
         * レイアウトと紐付ける
         */
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
        void onCellClick(List<TimeTable> items, TimeTable model);
    }

    /**
     * すべての列のデータを読み込み直す
     */
    public void swapAll() {
//        for (int i = 0; i < timeBlockLists.size(); i++) {
        for (int i = 0; i <= 6; i++) {
            mAdapters.get(i).swap(timeBlockLists.get(i));
        }
    }

    /**
     * 表示する時間割をloadする
     *
     * @param lists
     */
    public  void loadList(List<List<TimeTable>> lists) {
        // 参照を引き継がない
        if (timeBlockLists == null) timeBlockLists = new ArrayList<>();
        timeBlockLists.clear();
        timeBlockLists.addAll(lists);
    }

    /**
     * 日曜土曜の有効無効設定
     */
    public void isEnableSunCol(boolean bool){
        if(!bool) {
            recyclerViewList.get(0).setVisibility(View.GONE);
            mSunHeaderView.setVisibility(View.GONE);
        } else {
            recyclerViewList.get(0).setVisibility(View.VISIBLE);
            mSunHeaderView.setVisibility(View.VISIBLE);
        }
        this.isEnableSunCol = bool;
    }

    public void isEnableSatCol(boolean bool){
        if(!bool) {
            recyclerViewList.get(6).setVisibility(View.GONE);
            mSatHeaderView.setVisibility(View.GONE);
        } else {
            recyclerViewList.get(6).setVisibility(View.VISIBLE);
            mSatHeaderView.setVisibility(View.VISIBLE);
        }
        this.isEnableSatCol = bool;
    }

    /**
     * 0限5限の有効無効設定
     */
    public void isEnableZeroGen(boolean bool){
        this.isEnableZeroGen = bool;
    }

    public void isEnableGoGen(boolean bool){
        this.isEnableGoGen = bool;
    }


    /**
     * すべてのデータをリセットし
     * 引数にもらったtimetableのリストで描画し直す
     * (例：オリジナルのリストをViewに反映),
     *
     * @param lists
     */
    public void allReset(List<List<TimeTable>> lists, CallBack callBack) {
        loadList(lists);
        swapAll();
        callBack.callback();
    }

    public interface CallBack {
        void callback();
    }

    public void notifyDataSetChanged(){
        try{
            for (int i = 0; i <= 6; i++) {
                mAdapters.get(i).notifyDataSetChanged();
            }
        }catch (Exception e){
            Log.d(TAG, "notifyDataSetChanged: " + e.toString());
        }
}


    //***************************************************************************************************
    // private methods



    private void initView() {
        recyclerViewList = new ArrayList<>();
        recyclerViewList.add((RecyclerView)view.findViewById(R.id.sun_col));
        recyclerViewList.add((RecyclerView) view.findViewById(R.id.mon_col));
        recyclerViewList.add((RecyclerView) view.findViewById(R.id.tue_col));
        recyclerViewList.add((RecyclerView) view.findViewById(R.id.wed_col));
        recyclerViewList.add((RecyclerView) view.findViewById(R.id.thur_col));
        recyclerViewList.add((RecyclerView) view.findViewById(R.id.fri_col));
        recyclerViewList.add((RecyclerView)view.findViewById(R.id.sat_col));
        mSunHeaderView = (TextView)findViewById(R.id.sun_col_name);
        mSatHeaderView = (TextView)findViewById(R.id.sat_col_name);

//        for (int i = 0; i < timeBlockLists.size(); i++) {
        for (int i = 0; i < recyclerViewList.size(); i++) {
            recyclerViewList.get(i).setHasFixedSize(true);
            createRecyclerView(recyclerViewList.get(i), timeBlockLists.get(i));
        }

        /**
         * 日曜日と土曜日の有効無効設定を反映
         */
//        isEnableSunCol(isEnableSunCol);
//        isEnableSatCol(isEnableSatCol);
    }


    /**
     * それぞれのRecyclerVIewにアダプターを適用してclickイベントの処理を分岐
     * <p>
     * isEnableBottomSheetのフラグに応じてcllickイベントを処理するかを決定
     *
     * @param recyclerView
     * @param list
     */
    private void createRecyclerView(RecyclerView recyclerView, List<TimeTable> list) {
        Log.d(TAG, "createRecyclerView: " + recyclerView.getId() + " " + list.size());
        if (mAdapters == null) mAdapters = new ArrayList<>();
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        TimeTableRecyclerAdapter adapter = new TimeTableRecyclerAdapter(list, classRoomColor , mContext) {
            @Override
            public void onItemClicked(@NonNull List<TimeTable> items, TimeTable model) {
                super.onItemClicked(items, model);
                mListenerInfo.mOnCellClickListener.onCellClick(items, model);
            }
        };
        adapter.isEnableZeroGen(this.isEnableZeroGen);
        adapter.isEnableGoGen(this.isEnableGoGen);
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
