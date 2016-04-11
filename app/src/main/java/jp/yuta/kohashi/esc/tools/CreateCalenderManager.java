package jp.yuta.kohashi.esc.tools;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import java.util.Calendar;

import jp.yuta.kohashi.esc.R;

/**
 * Created by Yuta on 2016/04/04.
 */
public class CreateCalenderManager {

    LinearLayout rowLayout;
    public void createCalender(int year,int mon,Context context){
        //get root calender View

        Calendar calendar  = Calendar.getInstance();


        //引数で渡された月を設定
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,mon-1);
        calendar.set(Calendar.DATE,1);

        //月末日付を取得
        int lastDay = calendar.getActualMaximum(Calendar.DATE);

        //月初の曜日を取得
        //日曜日が1
        int firstWeekNo = calendar.get(Calendar.DAY_OF_WEEK);

        //各行ごとにレイアウトを初期化
        rowLayout = (LinearLayout)((Activity)context).findViewById(R.id.one_week);
        rowLayout.removeAllViews();

        rowLayout = (LinearLayout)((Activity)context).findViewById(R.id.two_week);
        rowLayout.removeAllViews();

        rowLayout = (LinearLayout)((Activity)context).findViewById(R.id.three_week);
        rowLayout.removeAllViews();

        rowLayout = (LinearLayout)((Activity)context).findViewById(R.id.four_week);
        rowLayout.removeAllViews();

        rowLayout = (LinearLayout)((Activity)context).findViewById(R.id.five_week);
        rowLayout.removeAllViews();

        rowLayout = (LinearLayout)((Activity)context).findViewById(R.id.six_week);
        rowLayout.removeAllViews();


        //2重ループ
        //6行7列
        int dayCount = 1;
        int row = 0;
        //行
        for(int col = 0;col < 6; col++){
            switch(col){
                case 0:
                    rowLayout = (LinearLayout)((Activity)context).findViewById(R.id.one_week);
                    break;
                case 1:
                    rowLayout = (LinearLayout)((Activity)context).findViewById(R.id.two_week);
                    break;
                case 2:
                    rowLayout = (LinearLayout)((Activity)context).findViewById(R.id.three_week);
                    break;
                case 3:
                    rowLayout = (LinearLayout)((Activity)context).findViewById(R.id.four_week);
                    break;
                case 4:
                    rowLayout = (LinearLayout)((Activity)context).findViewById(R.id.five_week);
                    break;
                case 5:
                    rowLayout = (LinearLayout)((Activity)context).findViewById(R.id.six_week);
                    break;
            }


            row = 0;
            if(col == 0 && row == 0){
                while(row < firstWeekNo - 1){
                    row++;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    Button btn = new Button(context);
                    btn.setGravity(Gravity.CENTER);
                    btn.setVisibility(View.INVISIBLE);
                    btn.setLayoutParams(params);
                    btn.setClickable(false);
                    rowLayout.addView(btn);
                }
            }

            //列
            for(;row < 7;row++){


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
               Button btn1 = new Button(context);
                btn1.setText(String.valueOf(dayCount));
                btn1.setGravity(Gravity.CENTER);
                btn1.setLayoutParams(params);
                btn1.setClickable(false);
                btn1.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary8));
                btn1.setTextColor(context.getResources().getColor(R.color.white));
                btn1.setTextSize(17);

                //日曜は赤
                if(row ==0){
                    btn1.setTextColor(context.getResources().getColor(R.color.red));
                }

                rowLayout.addView(btn1);
//                LinearLayout.
//
//                btn.setText(String.valueOf(dayCount));
//                btn.setGravity(Gravity.CENTER);
//
//
//                rowlayout.addView(btn);



                if(dayCount == lastDay){
                    break;
                }
                dayCount++;
            }

            if(row < 7 && dayCount == lastDay){
                break;
            }
        }



    }
}
