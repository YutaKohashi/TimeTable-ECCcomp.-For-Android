package jp.yuta.kohashi.esc.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.preference.PreferenceManager;
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

import org.w3c.dom.Text;

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

        //出席率テキスト

        //設定にチェックが入っているか
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
        boolean checkboxValue = spf.getBoolean("enable_color_attendance_rate", false);

        //設定で有効になっている場合のみ色分け
        if(checkboxValue){
            //出席率などに応じて色を替える
//        //単位を落としている場合背景をグレーにする
            int  rate = Integer.parseInt(items.get(i).getAttendanceRate());
//        viewHolder.attendanceRateRootView.setBackgroundColor(context.getResources().getColor(R.color.white));
            if(rate < 75){
//            単位を落とした時
                //ブロック自体をブラックアウトする
                BlackOutBlocks(viewHolder,context);
                ColorStateList oldColors =  viewHolder.attendanceRateText.getTextColors();
                viewHolder.attendanceRate.setTextColor(oldColors);
                viewHolder.abcentNum.setTextColor(oldColors);

            }else if(rate >= 75 && rate <= 86){
//            viewHolder.attendanceRateRootView.setBackgroundColor(context.getResources().getColor(R.color.cardView_bg_golor_end));
//            viewHolder.shusekiRateText.setBackgroundColor(context.getResources().getColor(R.color.cardView_bg_golor_end));
                viewHolder.attendanceRate.setTextColor(Color.RED);
                viewHolder.abcentNum.setTextColor(Color.RED);
                DefaultColorBlocks(viewHolder,context);

            }else if(rate < 100 && rate > 86){
//            出席率が危ない時
//            viewHolder.attendanceRateRootView.setBackgroundColor(context.getResources().getColor(R.color.cardView_bg_golor_warning));
//            viewHolder.shusekiRateText.setBackgroundColor(context.getResources().getColor(R.color.cardView_bg_golor_warning));

                DefaultColorBlocks(viewHolder,context);
                ColorStateList oldColors =  viewHolder.attendanceRateText.getTextColors();
                viewHolder.attendanceRate.setTextColor(context.getResources().getColor(R.color.colorPrimary7));
                viewHolder.abcentNum.setTextColor(oldColors);

            }else{
                //単位は大丈夫
                DefaultColorBlocks(viewHolder,context);
                ColorStateList oldColors =  viewHolder.attendanceRateText.getTextColors();
                viewHolder.attendanceRate.setTextColor(oldColors);
                viewHolder.abcentNum.setTextColor(oldColors);
//            viewHolder.attendanceRateRootView.setBackgroundColor(context.getResources().getColor(R.color.background_material_light));
//            viewHolder.shusekiRateText.setBackgroundColor(context.getResources().getColor(R.color.background_material_light));
            }
        }
        setAnimation(viewHolder.container, i);
    }

    //ViewHolder
    public static class AttendanceRateViewHolder extends RecyclerView.ViewHolder {
        CardView container;

        // Campos respectivos de un item
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

        //出席率テキスト
        public TextView shusekiRateText;

        //ブラックアウトするとき必要
        public TextView unitNumText;
        public TextView attendanceRateText;
        public TextView abcentNumText;
        public TextView lateText;
        public TextView kouketu1Text;
        public TextView kouketu2Text;
        public RelativeLayout shusekiRateBlockHeader;

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

            shusekiRateText = (TextView)v.findViewById(R.id.shuseki_rate_text);

            //ブラックアウトするときに必要
            unitNumText= (TextView)v.findViewById(R.id.unit_text);
            attendanceRateText= (TextView)v.findViewById(R.id.shuseki_text);
            abcentNumText= (TextView)v.findViewById(R.id.abcent_text);
            lateText= (TextView)v.findViewById(R.id.late_text);
            kouketu1Text= (TextView)v.findViewById(R.id.kouketsu1_text);
            kouketu2Text= (TextView)v.findViewById(R.id.kouketsu2_text);
            shusekiRateBlockHeader = (RelativeLayout)v.findViewById(R.id.shuseki_rate_block_header);
        }
    }

    private static void BlackOutBlocks(AttendanceRateViewHolder viewHolder,Context context){
        viewHolder.attendanceRateRootView.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.shusekiRateText.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.subjectName.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.unitNum.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.attendanceNum.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.abcentNum.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.lateNum.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.kouketsuNum1.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.kouketsuNum2.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));

        viewHolder.unitNumText.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.attendanceRateText.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.abcentNumText.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.lateText.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.kouketu1Text.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.kouketu2Text.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
        viewHolder.attendanceSubRootView.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));

//            教科名
        viewHolder.shusekiRateBlockHeader.setBackgroundColor(context.getResources().getColor(R.color.cardView_shuseki_rate_out));
    }

    private static void DefaultColorBlocks(AttendanceRateViewHolder viewHolder ,Context context){
        viewHolder.attendanceRateRootView.setBackgroundColor(0x00000000);
        viewHolder.shusekiRateText.setBackgroundColor(0x00000000);
        //クリア処理
        viewHolder.subjectName.setBackgroundColor(0x00000000);
        viewHolder.unitNum.setBackgroundColor(0x00000000);
        viewHolder.attendanceNum.setBackgroundColor(0x00000000);
        viewHolder.abcentNum.setBackgroundColor(0x00000000);
        viewHolder.lateNum.setBackgroundColor(0x00000000);
        viewHolder.kouketsuNum1.setBackgroundColor(0x00000000);
        viewHolder.kouketsuNum2.setBackgroundColor(0x00000000);

        viewHolder.unitNumText.setBackgroundColor(0x00000000);
        viewHolder.attendanceRateText.setBackgroundColor(0x00000000);
        viewHolder.abcentNumText.setBackgroundColor(0x00000000);
        viewHolder.lateText.setBackgroundColor(0x00000000);
        viewHolder.kouketu1Text.setBackgroundColor(0x00000000);
        viewHolder.kouketu2Text.setBackgroundColor(0x00000000);
        viewHolder.attendanceSubRootView.setBackgroundColor(0x00000000);

        viewHolder.shusekiRateBlockHeader.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary1));
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