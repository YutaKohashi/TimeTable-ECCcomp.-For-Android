package jp.yuta.kohashi.esc.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.object.TimeBlock;
import jp.yuta.kohashi.esc.preference.LoadManager;

public class TimeTableWidget extends AppWidgetProvider {

    static final String prefName = "sample";


    //月曜日から金曜日の曜日ごとのリスト
    List<TimeBlock> MondayList;
    List<TimeBlock> TuesdayList;
    List<TimeBlock> WednesdayList;
    List<TimeBlock> ThursdayList;
    List<TimeBlock> FridayList;

    LoadManager loadManager;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            //ウィジェットレイアウトの初期化
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.time_table_widget);

//            //今日の曜日を取得
//            Calendar cal = Calendar.getInstance();
//            int week = cal.get(Calendar.DAY_OF_WEEK);
//            switch(week){
//                case 2:
//                    //月曜日
//                    remoteViews.setInt(R.id.mon_col_name_widget,"setBackgroundColor",R.drawable.week_header_right_color);
//                    remoteViews.setInt(R.id.tue_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.wed_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.thur_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.fri_col_name_widget,"setBackgroundColor",R.drawable.week_header_nonline);
//                    break;
//                case 3:
//                    remoteViews.setInt(R.id.mon_col_name_widget,"setBackgroundColor",R.color.colorPrimary1);
//                    remoteViews.setInt(R.id.tue_col_name_widget,"setBackgroundColor",R.drawable.week_header_right_color);
//                    remoteViews.setInt(R.id.wed_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.thur_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.fri_col_name_widget,"setBackgroundColor",R.drawable.week_header_nonline);
//                    break;
//                case 4:
//                    remoteViews.setInt(R.id.mon_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.tue_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.wed_col_name_widget,"setBackgroundColor",R.drawable.week_header_right_color);
//                    remoteViews.setInt(R.id.thur_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.fri_col_name_widget,"setBackgroundColor",R.drawable.week_header_nonline);
//                    break;
//                case 5:
//                    remoteViews.setInt(R.id.mon_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.tue_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.wed_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.thur_col_name_widget,"setBackgroundColor",R.drawable.week_header_right_color);
//                    remoteViews.setInt(R.id.fri_col_name_widget,"setBackgroundColor",R.drawable.week_header_nonline);
//                    break;
//                case 6:
//                    remoteViews.setInt(R.id.mon_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.tue_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.wed_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.thur_col_name_widget,"setBackgroundColor",R.drawable.week_header_right);
//                    remoteViews.setInt(R.id.fri_col_name_widget,"setBackgroundColor",R.drawable.week_header_nonline_color);
//                    break;

//            }


            MondayList = new ArrayList<>();
            TuesdayList = new ArrayList<>();
            WednesdayList = new ArrayList<>();
            ThursdayList = new ArrayList<>();
            FridayList = new ArrayList<>();


            loadManager = new LoadManager();

            MondayList = loadManager.loadManagerWithPreferenceForTimeBlock(context, prefName, "monList");
            TuesdayList = loadManager.loadManagerWithPreferenceForTimeBlock(context, prefName, "tueList");
            WednesdayList = loadManager.loadManagerWithPreferenceForTimeBlock(context, prefName, "wedList");
            ThursdayList = loadManager.loadManagerWithPreferenceForTimeBlock(context, prefName, "thurList");
            FridayList = loadManager.loadManagerWithPreferenceForTimeBlock(context, prefName, "friList");

//            monLayout.removeAllViews();

            createColTimeTable(context,MondayList,R.id.mon_col_widget,remoteViews);
            createColTimeTable(context,TuesdayList,R.id.tue_col_widget,remoteViews);
            createColTimeTable(context,WednesdayList,R.id.wed_col_widget,remoteViews);
            createColTimeTable(context,ThursdayList,R.id.thur_col_widget,remoteViews);
            createColTimeTable(context,FridayList,R.id.fri_col_widget,remoteViews);


            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
//            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }

    private void createColTimeTable(Context context,List<TimeBlock> list,int colId,RemoteViews remoteViews){
        RemoteViews col = new RemoteViews(context.getPackageName(), R.layout.time_table_widget_col);
        for (int i = 0; i < list.size(); i++) {

            TimeBlock timeBlock = list.get(i);
            String subStr = timeBlock.getSubject();
            String roomStr = timeBlock.getClassRoom();

            //時間割セル
            RemoteViews cell = new RemoteViews(context.getPackageName(), R.layout.time_table_block_for_widget);
            cell.setTextViewText(R.id.text_subject_widget, subStr);
            cell.setTextColor(R.id.text_subject_widget, ContextCompat.getColor(context, R.color.widget_cell_text_color));
            cell.setTextViewText(R.id.text_classRoom_widget, roomStr);
            cell.setTextColor(R.id.text_classRoom_widget, ContextCompat.getColor(context, R.color.widget_cell_text_color));

            col.addView(R.id.widget_col, cell);

        }
        remoteViews.addView(colId, col);
    }


}

