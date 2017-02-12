package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.TimeBlockItem;

/**
 * Created by yutakohashi on 2017/01/13.
 */

public class TimeTableRecyclerAdapter extends RecyclerView.Adapter<TimeTableRecyclerAdapter.TimeViewHolder> {

    private List<TimeBlockItem> items;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int color;


    // タップされたときに呼び出されるメソッド
    protected void onItemClicked(@NonNull List<TimeBlockItem> items, TimeBlockItem model) {
    }

    public TimeTableRecyclerAdapter(List<TimeBlockItem> items, int color, Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        if(this.items == null) this.items = new ArrayList<>();
        this.items.clear();
        this.items.addAll(items);
        this.mContext = context;
        this.color = color;
    }

    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.cell_time_table, parent, false);
        final TimeViewHolder holder = new TimeViewHolder(v);
        // onCreateViewHolder でリスナーをセット
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                onItemClicked(items, items.get(position));
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(TimeViewHolder holder, int position) {
        holder.subjectName.setText(items.get(position).getSubject());
        holder.roomName.setText(items.get(position).getClassRoom());
        holder.roomName.setBackgroundColor(mContext.getResources().getColor(color));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void swap(List<TimeBlockItem> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    //ViewHolder
    public static class TimeViewHolder extends RecyclerView.ViewHolder {

        CardView container;
        // Campos respectivos de un item
        public TextView subjectName;
        public TextView roomName;

        public TimeViewHolder(View v) {
            super(v);
            container = (CardView) v.findViewById(R.id.card_view_time_block);
            subjectName = (TextView) v.findViewById(R.id.text_subject);
            roomName = (TextView) v.findViewById(R.id.text_classRoom);
        }
    }

}
