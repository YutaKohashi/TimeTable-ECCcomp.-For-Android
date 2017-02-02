package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.AttendanceRate;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * Created by yutakohashi on 2017/01/14.
 */

public class AttendanceRateRecyclerAdapter extends RecyclerView.Adapter<AttendanceRateRecyclerAdapter.AttendanceViewHolder> {

    private List<AttendanceRate> items;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    // タップされたときに呼び出されるメソッド
//    protected void onItemLongClicked(int position, @NonNull List<AttendanceRate> items) {}

    public AttendanceRateRecyclerAdapter(List<AttendanceRate> items, Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.items = new ArrayList<>();
        this.items.addAll(items);
        this.mContext = context;
    }



    @Override
    public AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.cell_attendance_rate, parent, false);
        final AttendanceRateRecyclerAdapter.AttendanceViewHolder holder = new AttendanceRateRecyclerAdapter.AttendanceViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(AttendanceViewHolder holder, int position) {
        //教科名
        holder.subjectName.setText(items.get(position).getSubjectName());
        //単位数
        holder.unitNum.setText(items.get(position).getUnit());
        //出席率
        holder.attendanceRate.setText(items.get(position).getAttendanceRate());
        //出席数
        holder.attendanceNum.setText(items.get(position).getAttendanceNumber());
        //欠席数
        holder.abcentNum.setText(items.get(position).getAbsentNumber());
        //遅刻数
        holder.lateNum.setText(items.get(position).getLateNumber());
        //公欠１
        holder.kouketsuNum1.setText(items.get(position).getPublicAbsentNumber1());
        //公欠２
        holder.kouketsuNum2.setText(items.get(position).getPublicAbsentNumber2());

        blackout(holder,position);
        changeBackgroundColor(holder, position);
    }

    /**
     * 設定な異様に応じて色を変更する
     * @param holder
     * @param position
     */
    private void changeBackgroundColor(AttendanceViewHolder holder, int position){
        if(PrefUtil.isChangeColor()) {
            int color = -1;
            int rate = Integer.valueOf(items.get(position).getAttendanceRate());
            if (rate < 75) {
                if(!PrefUtil.isBlackout()){
                    color = PrefUtil.loadColorU75();
                } else {
                    color = mContext.getResources().getColor(R.color.bg_blackout);
                }
            } else if (rate < 81) {
                color = PrefUtil.loadColorU81();
            } else if (rate < 90) {
                color = PrefUtil.loadColorU90();
            } else {
                color = -1;
            }
            if (color != -1) {
                holder.titleContainer.setBackgroundColor(color);
            } else {
                holder.titleContainer.setBackgroundColor(mContext.getResources().getColor(R.color.bg_title));

            }
        }
    }
    /**
     * 設定内容に応じてブラックアウトする
     */
    private void blackout(AttendanceViewHolder holder, int position){
        if(PrefUtil.isBlackout()){
            int rate = Integer.valueOf(items.get(position).getAttendanceRate());
            if (rate < 75) {
                holder.container.setCardBackgroundColor(mContext.getResources().getColor(R.color.bg_blackout));
                holder.titleContainer.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
            } else {
                holder.container.setCardBackgroundColor(mContext.getResources().getColor(R.color.bg_default_card_view));
                holder.titleContainer.setBackgroundColor(mContext.getResources().getColor(R.color.bg_title));
            }
        } else {
            holder.container.setCardBackgroundColor(mContext.getResources().getColor(R.color.bg_default_card_view));
            holder.titleContainer.setBackgroundColor(mContext.getResources().getColor(R.color.bg_title));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void refresh(){
        notifyDataSetChanged();
    }

    public void swap(List<AttendanceRate> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    //ViewHolder
    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {

        public CardView container;
        public TextView subjectName;
        public TextView unitNum;
        public TextView attendanceRate;     //出席率を表示するTextView
        public TextView attendanceNum;
        public TextView abcentNum;
        public TextView lateNum;
        public TextView kouketsuNum1;
        public TextView kouketsuNum2;
        public RelativeLayout attendanceRateRootView;
        public LinearLayout attendanceSubRootView;
        public RelativeLayout titleContainer;

        public AttendanceViewHolder(View v) {
            super(v);
            container = (CardView) itemView.findViewById(R.id.card_view_shuseki_root);
            subjectName = (TextView) v.findViewById(R.id.subject_text_view);
            unitNum = (TextView) v.findViewById(R.id.unit_num);
            attendanceRate = (TextView) v.findViewById(R.id.attendance_rate);
            attendanceNum = (TextView) v.findViewById(R.id.attendance_num);
            abcentNum = (TextView) v.findViewById(R.id.abcent_num);
            lateNum = (TextView) v.findViewById(R.id.late_num);
            kouketsuNum1 = (TextView) v.findViewById(R.id.kouketsu_num1);
            kouketsuNum2 = (TextView) v.findViewById(R.id.kouketsu_num2);
            attendanceRateRootView = (RelativeLayout) v.findViewById(R.id.attendance_rate_root_view);
            attendanceSubRootView = (LinearLayout) v.findViewById(R.id.attendance_card_view_sub_root);
            titleContainer = (RelativeLayout)v.findViewById(R.id.shuseki_rate_block_header);
        }
    }
}
