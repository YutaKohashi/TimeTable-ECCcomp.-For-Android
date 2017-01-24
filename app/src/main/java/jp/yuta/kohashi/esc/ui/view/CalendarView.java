package jp.yuta.kohashi.esc.ui.view;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.schedule.CalendarItemModel;
import jp.yuta.kohashi.esc.util.Util;

/**
 * Created by yutakohashi on 2017/01/14.
 */

public class CalendarView extends LinearLayout {
    private static final String TAG = CalendarView.class.getSimpleName();

    private int year;
    private int month;
    View view;
    private LinearLayout rowLayout;

    /**
     * 引数AttributeSetが必須
     *
     * @param context
     * @param attrs
     */
    public CalendarView(Context context, AttributeSet attrs) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.view_custom_calendar, this);
    }

    /**
     * 年月を設定するメソッド
     *
     * @param year
     * @param month
     */
    public void setMonth(int year, int month, List<CalendarItemModel> scheduleList) {
        this.year = year;
        this.month = month;
        //get root calender View

        List<String> dayList = new ArrayList<>(); //　バッジを付けるリスト
        for(CalendarItemModel model:scheduleList){
            dayList.add(model.getDate());
        }

        Calendar calendar = Calendar.getInstance();

        //現在の月が４月から１２月のとき
        if (Calendar.getInstance().get(Calendar.MONTH) + 1 >= 4) {
            if (month <= 3) {
                year += 1;
                Log.d(TAG,"Const.MONTH >= 4     " +String.valueOf(year));
            }
        } else {
            //1月から３月のとき
            if (month >= 4) {
                year -= 1;
                Log.d(TAG,"3>=Const.MONTH >= 1   " + String.valueOf(year));
            }
        }

        //カレンダーに表示する年月日を設定
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, 1);

        //月末日付を取得
        int lastDay = calendar.getActualMaximum(Calendar.DATE);

        //月初の曜日を取得
        //日曜日が1
        int firstWeekNo = calendar.get(Calendar.DAY_OF_WEEK);

        //2重ループ
        //6行7列
        int dayCount = 1;
        int row = 0;
        //行
        for (int col = 0; col < 6; col++) {
            switch (col) {
                case 0:
                    rowLayout = (LinearLayout) view.findViewById(R.id.one_week);
                    break;
                case 1:
                    rowLayout = (LinearLayout) view.findViewById(R.id.two_week);
                    break;
                case 2:
                    rowLayout = (LinearLayout) view.findViewById(R.id.three_week);
                    break;
                case 3:
                    rowLayout = (LinearLayout) view.findViewById(R.id.four_week);
                    break;
                case 4:
                    rowLayout = (LinearLayout) view.findViewById(R.id.five_week);
                    break;
                case 5:
                    rowLayout = (LinearLayout) view.findViewById(R.id.six_week);
                    break;
            }

            row = 0;
            if (col == 0 && row == 0) {
                while (row < firstWeekNo - 1) {
                    row++;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    TextView textView = new TextView(getContext());
                    textView.setGravity(Gravity.CENTER);
                    textView.setVisibility(View.INVISIBLE);
                    textView.setLayoutParams(params);
                    textView.setClickable(false);
                    rowLayout.addView(textView);
                }
            }

//            GetValuesBase getValuesBase = new GetValuesBase();
            //列
            for (; row < 7; row++) {

                //画面サイズに応じて大きさを調整
                float displayScale = Util.getDisplayScale();
                int size = (int) (28 * displayScale + 0.5f);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        size, size);
                TextView textView1 = new TextView(getContext());
                textView1.setText(String.valueOf(dayCount));
                textView1.setGravity(Gravity.CENTER);
                textView1.setLayoutParams(params);
                textView1.setClickable(false);
                textView1.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary8));
                textView1.setTextColor(getContext().getResources().getColor(R.color.white));
                textView1.setTextSize(17);

                LinearLayout root = new LinearLayout(getContext());
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                params.gravity = Gravity.CENTER;
                root.setGravity(Gravity.CENTER); //子TextViewを中央に
                root.setLayoutParams(params1);
                root.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary8));
                root.addView(textView1);

                //日曜は赤
                if (row == 0) {
                    textView1.setTextColor(getContext().getResources().getColor(R.color.red));
                }

//                dateListに含まれる場合
                if(dayList.contains(String.format("%02d",dayCount))){
                    // btn1.setBackground(context.getResources().getDrawable(R.drawable.event_mark_bg));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        //APIレベルがJELLEYBEAN以上の時
                        textView1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_calendar_budge));
                    }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        //APIレベルがICSの時
                        textView1.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_calendar_budge));
                    }
                }

                rowLayout.addView(root);

                if (dayCount == lastDay) {
                    break;
                }
                dayCount++;
            }

            if (row < 7 && dayCount == lastDay) {
                break;
            }
        }

    }
}
