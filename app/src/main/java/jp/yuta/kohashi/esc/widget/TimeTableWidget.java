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
import jp.yuta.kohashi.esc.object.CustomTimeTableCell;
import jp.yuta.kohashi.esc.object.TimeBlock;
import jp.yuta.kohashi.esc.preference.LoadManager;

public class TimeTableWidget extends AppWidgetProvider {

    static final String prefName = "sample";
    /******        カスタム定数      *******/
    static final String CUSTOM_CELL_PREF_NAME ="customCell";
    private static final String KEY = "CUSTOM_TIME_TABLE";
    /**********************************/

    //月曜日から金曜日の曜日ごとのリスト
    List<TimeBlock> MondayList;
    List<TimeBlock> TuesdayList;
    List<TimeBlock> WednesdayList;
    List<TimeBlock> ThursdayList;
    List<TimeBlock> FridayList;

    LoadManager loadManager;
    List<CustomTimeTableCell> customTimeList;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            //ウィジェットレイアウトの初期化
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.time_table_widget);

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
            customTimeList = new ArrayList<>();
            customTimeList = loadManager.loadManagerWithPreferenceForCustomTimeTable(context,CUSTOM_CELL_PREF_NAME,KEY);
            try{
                customTimeList.size();
            }catch(Exception e){
                customTimeList = new ArrayList<>();
            }

            for(CustomTimeTableCell preCell:customTimeList){
                int x =  preCell.getX();
                int y = preCell.getY();

                switch (x){
                    case 0:
                        List<TimeBlock> list = new ArrayList<>(MondayList);
                        MondayList.clear();
                        for(int i = 0;i < list.size();i++){
                            if(i == y){
                                TimeBlock timeBlock = new TimeBlock();
                                timeBlock.setSubject(preCell.getSubject());
                                timeBlock.setClassRoom(preCell.getRoom());
                                MondayList.add(timeBlock);
                            }else{
                                MondayList.add(list.get(i));
                            }
                        }
                        break;
                    case 1:
                        List<TimeBlock> list1 = new ArrayList<>(TuesdayList);
                        TuesdayList.clear();
                        for(int i = 0;i < list1.size();i++){
                            if(i == y){
                                TimeBlock timeBlock = new TimeBlock();
                                timeBlock.setSubject(preCell.getSubject());
                                timeBlock.setClassRoom(preCell.getRoom());
                                TuesdayList.add(timeBlock);
                            }else{
                                TuesdayList.add(list1.get(i));
                            }
                        }
                        break;
                    case 2:
                        List<TimeBlock> list2 = new ArrayList<>(WednesdayList);
                        WednesdayList.clear();
                        for(int i = 0;i < list2.size();i++){
                            if(i == y){
                                TimeBlock timeBlock = new TimeBlock();
                                timeBlock.setSubject(preCell.getSubject());
                                timeBlock.setClassRoom(preCell.getRoom());
                                WednesdayList.add(timeBlock);
                            }else{
                                WednesdayList.add(list2.get(i));
                            }
                        }
                        break;
                    case 3:
                        List<TimeBlock> list3 = new ArrayList<>(ThursdayList);
                        ThursdayList.clear();
                        for(int i = 0;i < list3.size();i++){
                            if(i == y){
                                TimeBlock timeBlock = new TimeBlock();
                                timeBlock.setSubject(preCell.getSubject());
                                timeBlock.setClassRoom(preCell.getRoom());
                                ThursdayList.add(timeBlock);
                            }else{
                                ThursdayList.add(list3.get(i));
                            }
                        }
                        break;
                    case 4:
                        List<TimeBlock> list4 = new ArrayList<>(FridayList);
                        FridayList.clear();
                        for(int i = 0;i < list4.size();i++){
                            if(i == y){
                                TimeBlock timeBlock = new TimeBlock();
                                timeBlock.setSubject(preCell.getSubject());
                                timeBlock.setClassRoom(preCell.getRoom());
                                FridayList.add(timeBlock);
                            }else{
                                FridayList.add(list4.get(i));
                            }
                        }
                        break;

                }
            }

            try{
                createColTimeTable(context,MondayList,R.id.mon_col_widget,remoteViews);
                createColTimeTable(context,TuesdayList,R.id.tue_col_widget,remoteViews);
                createColTimeTable(context,WednesdayList,R.id.wed_col_widget,remoteViews);
                createColTimeTable(context,ThursdayList,R.id.thur_col_widget,remoteViews);
                createColTimeTable(context,FridayList,R.id.fri_col_widget,remoteViews);


                appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
            }catch(NullPointerException e){

            }

//            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }

    private void createColTimeTable(Context context,List<TimeBlock> list,int colId,RemoteViews remoteViews){
        RemoteViews col = new RemoteViews(context.getPackageName(), R.layout.time_table_widget_col);
        try{
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
        }catch (NullPointerException ex){

        }


    }


}

