package jp.yuta.kohashi.esc.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.object.AttendanceRateObject;

import java.util.List;

/**
 * Created by Yuta on 2016/03/29
 * 出席照会FRAGMENTで使用.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.AttendanceRateViewHolder> {
    private List<AttendanceRateObject> items;
    private int lastPosition = -1;
    private Context context;

    public RecyclerViewAdapter (List<AttendanceRateObject> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AttendanceRateViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shuseki_rate_view, viewGroup, false);
        return new AttendanceRateViewHolder(v);
    }

    //ここでCARDVIEWのアイテムについて設定
    @Override
    public void onBindViewHolder(AttendanceRateViewHolder viewHolder, int i) {

        //教科名
        viewHolder.subjectName.setText(items.get(i).getSubjectName());
        //単位数
        viewHolder.unitNum.setText(items.get(i).getUnit());
        //出席率
        viewHolder.attendanceRate.setText(items.get(i).getAttendanceRate());
        //出席数
        viewHolder.attendanceNum.setText(items.get(i).getAttendanceNumber() );
        //欠席数
        viewHolder.abcentNum.setText(items.get(i).getAbsentNumber());
        //遅刻数
        viewHolder.lateNum.setText(items.get(i).getLateNumber());
        //公欠１
        viewHolder.kouketsuNum1.setText(items.get(i).getPublicAbsentNumber1());
        //公欠２
        viewHolder.kouketsuNum2.setText(items.get(i).getPublicAbsentNumber2());

        //出席率などに応じて色を替える
//        //単位を落としている場合背景をグレーにする
//        int  rate = Integer.parseInt(items.get(i).getAttendanceRate());
//        viewHolder.attendanceRateRootView.setBackgroundColor(context.getResources().getColor(R.color.white));
//        if(rate < 75){
////            viewHolder.container.setCardBackgroundColor(R.color.cardView_bg_golor);
//            viewHolder.attendanceRateRootView.setBackgroundColor(context.getResources().getColor(R.color.cardView_bg_golor_warning));
//
//        }else if(rate >= 75 && rate < 82){
////            viewHolder.container.setCardBackgroundColor(R.color.cardView_bg_golor_warning);
//            viewHolder.attendanceRateRootView.setBackgroundColor(context.getResources().getColor(R.color.cardView_bg_golor_warning));
//        }

        setAnimation(viewHolder.container, i);
    }

    //ViewHolder
    public static class AttendanceRateViewHolder extends RecyclerView.ViewHolder {
        CardView container;

        // Campos respectivos de un item
        public TextView subjectName;
        public TextView unitNum;
        public TextView attendanceRate;
        public TextView attendanceNum;
        public TextView abcentNum;
        public TextView lateNum;
        public TextView kouketsuNum1;
        public TextView kouketsuNum2;
        public RelativeLayout attendanceRateRootView;
        public LinearLayout attendanceSubRootView;

        public AttendanceRateViewHolder(View v) {
            super(v);
            container = (CardView) itemView.findViewById(R.id.card_view_shuseki_root);
            subjectName = (TextView)v.findViewById(R.id.subject_text_view);
            unitNum = (TextView)v.findViewById(R.id.unit_num);
            attendanceRate = (TextView)v.findViewById(R.id.attendance_rate);
            attendanceNum = (TextView)v.findViewById(R.id.attendance_num);
            abcentNum = (TextView)v.findViewById(R.id.abcent_num);
            lateNum = (TextView)v.findViewById(R.id.late_num);
            kouketsuNum1 = (TextView)v.findViewById(R.id.kouketsu_num1);
            kouketsuNum2 = (TextView)v.findViewById(R.id.kouketsu_num2);
            attendanceRateRootView = (RelativeLayout)v.findViewById(R.id.attendance_rate_root_view);
            attendanceSubRootView = (LinearLayout)v.findViewById(R.id.attendance_card_view_sub_root);
        }
    }

    //アニメーション
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition)
//        if(position < 5)
//        {
//            //アニメーション
//            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
//            viewToAnimate.startAnimation(animation);
////            lastPosition = position;
//        }
    }
}