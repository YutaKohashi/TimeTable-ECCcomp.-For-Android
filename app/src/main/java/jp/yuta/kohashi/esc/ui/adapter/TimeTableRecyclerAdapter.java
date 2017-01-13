package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.TimeBlockModel;

/**
 * Created by yutakohashi on 2017/01/13.
 */

public class TimeTableRecyclerAdapter extends RecyclerView.Adapter<TimeTableRecyclerAdapter.TimeViewHolder> {

    private List<TimeBlockModel> items;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    // タップされたときに呼び出されるメソッドを定義
    protected void onItemClicked(@NonNull TimeBlockModel model) {
    }

    public TimeTableRecyclerAdapter(List<TimeBlockModel> items, Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.mContext = context;
    }

//    TimeTableRecyclerAdapter.TimeViewHolder holder;
    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        holder = TimeTableRecyclerAdapter.TimeViewHolder.create(inflater, parent);
//        mLayoutInflater = LayoutInflater.from(context);
        View v = mLayoutInflater.inflate(R.layout.cell_time_table, parent, false);
        final TimeViewHolder holder = new TimeViewHolder(v);
        // onCreateViewHolder でリスナーをセット
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                onItemClicked(items.get(position));
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(TimeViewHolder holder, int position) {
        holder.subjectName.setText(items.get(position).getSubject());
        holder.roomName.setText(items.get(position).getClassRoom());
    }

    @Override
    public int getItemCount() {
        return items.size();
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
