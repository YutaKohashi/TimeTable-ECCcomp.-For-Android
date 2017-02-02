package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.AttendanceRate;
import jp.yuta.kohashi.esc.model.enums.AttendanceRateType;

/**
 * Created by yutakohashi on 2017/01/30.
 */

public class AttendanceDivideRecyclerAdapter extends RecyclerView.Adapter<AttendanceDivideRecyclerAdapter.AttendanceDivideViewHolder> {

    private List<AttendanceRate> items;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    // タップされたときに呼び出されるメソッド
    protected void onItemChecked(int position, @NonNull List<AttendanceRate> items, boolean b, AttendanceRateType type) {}

    public AttendanceDivideRecyclerAdapter(List<AttendanceRate> items, Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.mContext = context;
    }

    @Override
    public AttendanceDivideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.cell_attendance_divide, parent, false);
        final AttendanceDivideRecyclerAdapter.AttendanceDivideViewHolder holder = new AttendanceDivideRecyclerAdapter.AttendanceDivideViewHolder(v);

        holder.zenki.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) holder.kouki.setChecked(false);
                int position = holder.getAdapterPosition();
                onItemChecked(position, items, b, AttendanceRateType.ZENKI);
            }
        });

        holder.kouki.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) holder.zenki.setChecked(false);
                int position = holder.getAdapterPosition();
                onItemChecked(position, items, b, AttendanceRateType.KOUKI);

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(AttendanceDivideViewHolder holder, int position) {
        //教科名
        holder.subjectName.setText(items.get(position).getSubjectName());

        if (items.get(position).getType() == AttendanceRateType.ZENKI) {
            holder.zenki.setChecked(true);
            holder.kouki.setChecked(false);
        } else {
            holder.zenki.setChecked(false);
            holder.kouki.setChecked(true);
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public void refresh() {
        notifyDataSetChanged();
    }

    public void swap(List<AttendanceRate> items) {
        this.items.clear();
        this.items.addAll(items);
//        notifyDataSetChanged();
    }

    public List<AttendanceRate> getItems() {
        return items;
    }

    //ViewHolder
    public static class AttendanceDivideViewHolder extends RecyclerView.ViewHolder {

        public CardView container;
        public TextView subjectName;
        public RadioButton zenki;
        public RadioButton kouki;

        public AttendanceDivideViewHolder(View v) {
            super(v);
            container = (CardView) itemView.findViewById(R.id.card_view);
            subjectName = (TextView) itemView.findViewById(R.id.subject_text_view);
            zenki = (RadioButton) itemView.findViewById(R.id.radio_button_zenki);
            kouki = (RadioButton) itemView.findViewById(R.id.radio_button_kouki);
        }
    }
}
