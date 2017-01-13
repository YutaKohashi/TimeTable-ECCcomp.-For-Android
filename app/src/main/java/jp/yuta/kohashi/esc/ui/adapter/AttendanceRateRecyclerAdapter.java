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

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.AttendanceRateModel;

/**
 * Created by yutakohashi on 2017/01/14.
 */

public class AttendanceRateRecyclerAdapter extends RecyclerView.Adapter<AttendanceRateRecyclerAdapter.AttendanceViewHolder> {

    private List<AttendanceRateModel> items;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public AttendanceRateRecyclerAdapter(List<AttendanceRateModel> items, Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.mContext = context;
    }

    @Override
    public AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.cell_attendance_rate, parent, false);
        return new AttendanceViewHolder(v);
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

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //ViewHolder
    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {

        CardView container;
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
        }
    }
}
