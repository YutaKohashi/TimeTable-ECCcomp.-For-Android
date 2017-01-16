package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.schedule.CalendarItemModel;

/**
 * Created by Yuta on 2016/04/06.
 */
public class CalendarRecyclerAdapter extends RecyclerView.Adapter<CalendarRecyclerAdapter.CalendarRecyclerViewHolder> {
    private List<CalendarItemModel> items;
    private Context context;


    public CalendarRecyclerAdapter(List<CalendarItemModel> items, Context context){
        this.items = items;
        this.context = context;
    }

    @Override
    public CalendarRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from((viewGroup.getContext()))
                .inflate(R.layout.cell_calendar_list,viewGroup,false);
        return new CalendarRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CalendarRecyclerViewHolder holder, int position) {
        holder.dateText.setText(items.get(position).getDate());
        holder.containtsText.setText(items.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CalendarRecyclerViewHolder extends RecyclerView.ViewHolder {
        CardView container;

        // Campos respectivos de un item
        public TextView dateText;
        public TextView containtsText;

        public CalendarRecyclerViewHolder(View v) {
            super(v);

            container = (CardView) itemView.findViewById(R.id.tab_syllabus_cardView);
            dateText = (TextView)v.findViewById(R.id.date_text);
            containtsText = (TextView)v.findViewById(R.id.contents_text);
        }
    }

    public void swap(List<CalendarItemModel> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
}
