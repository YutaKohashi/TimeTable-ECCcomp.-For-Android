package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.api.model.timeTable.TimeTable;
import jp.yuta.kohashi.esc.util.Util;

/**
 * Created by yutakohashi on 2017/01/13.
 */

public class TimeTableRecyclerAdapter extends RecyclerView.Adapter<TimeTableRecyclerAdapter.TimeViewHolder> {

    private List<TimeTable> items;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int color;

    /**
     * 0限有効無効フラグ
     * default:true
     */
    private boolean isEnableZeroGen;

    /**
     * 5限有効無効フラグ
     * default:true
     */
    private boolean isEnableGoGen;

    // タップされたときに呼び出されるメソッド
    public void onItemClicked(@NonNull List<TimeTable> items, TimeTable model) {
    }

    public TimeTableRecyclerAdapter(List<TimeTable> items, int color, Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        if (this.items == null) this.items = new ArrayList<>();
        this.items.clear();
        this.items.addAll(items);
        this.mContext = context;
        this.color = color;
        isEnableZeroGen = true;
        isEnableGoGen = true;
    }

    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.cell_time_table, parent, false);
        final TimeViewHolder holder = new TimeViewHolder(v);
        // onCreateViewHolder でリスナーをセット
        holder.itemView.setOnClickListener(v1 -> {
            int position = holder.getAdapterPosition();
            if (!isEnableZeroGen) position += 1;
            onItemClicked(items, items.get(position));
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(TimeViewHolder holder, int position) {
        if (!isEnableZeroGen) position += 1;

        holder.subjectName.setText(items.get(position).getLessonName());
        holder.roomName.setText(items.get(position).getRoom());

        holder.roomName.setBackgroundColor(Util.getColor(color));
    }

    @Override
    public int getItemCount() {
        int count = items.size();
        if (!isEnableZeroGen) count -= 1;
        if(!isEnableGoGen) count -= 1;
        return count;
    }

    public void swap(List<TimeTable> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 0限5限の有効無効設定
     */
    public void isEnableZeroGen(boolean bool){
        isEnableZeroGen = bool;
    }
    public void isEnableGoGen(boolean bool){
        isEnableGoGen = bool;
    }



    //***************************************************************************************************
    // ViewHolder
    class TimeViewHolder extends RecyclerView.ViewHolder {

        FrameLayout container;
        View root;
        TextView subjectName;
        TextView roomName;

        TimeViewHolder(View v) {
            super(v);
            root  = v.findViewById(R.id.root);
            container = (FrameLayout) v.findViewById(R.id.card_view_time_block);
            subjectName = (TextView) v.findViewById(R.id.text_subject);
            roomName = (TextView) v.findViewById(R.id.text_classRoom);
        }
    }

}
