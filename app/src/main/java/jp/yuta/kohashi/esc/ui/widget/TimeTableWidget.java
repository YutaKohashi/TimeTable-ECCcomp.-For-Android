package jp.yuta.kohashi.esc.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.api.model.timeTable.TimeTable;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * Implementation of App Widget functionality.
 */
public class TimeTableWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Widgetのレイアウトを取得
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_root_time_table);

        List<List<TimeTable>> lists = PrefUtil.loadTimeBlockList(context);
        try {
            createColTimeTable(context, lists.get(1), R.id.mon_col_widget, remoteViews);
            createColTimeTable(context, lists.get(2), R.id.tue_col_widget, remoteViews);
            createColTimeTable(context, lists.get(3), R.id.wed_col_widget, remoteViews);
            createColTimeTable(context, lists.get(4), R.id.thur_col_widget, remoteViews);
            createColTimeTable(context, lists.get(5), R.id.fri_col_widget, remoteViews);
        }catch(Exception e){
            // failure load
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_root_time_table);
        }
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            //ウィジェットをアップデート
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static void createColTimeTable(Context context, List<TimeTable> list, int colId, RemoteViews remoteViews) throws Exception {
        RemoteViews col = new RemoteViews(context.getPackageName(), R.layout.widget_col_time_table);
        int i = 0;
        int limit = list.size();
        if(!PrefUtil.isEnableGoGen()) limit -=1;
        if(!PrefUtil.isEnableZeroGen()) i = 1;
        for (; i < limit; i++) {
            TimeTable timeBlock = list.get(i);
            String subStr = timeBlock.getLessonName();
            String roomStr = timeBlock.getRoom();

            //時間割セル
            RemoteViews cell = new RemoteViews(context.getPackageName(), R.layout.widget_cell_time_table);
            cell.setTextViewText(R.id.text_subject_widget, subStr);
            cell.setTextColor(R.id.text_subject_widget, ContextCompat.getColor(context, R.color.widget_cell_text_color));
            cell.setTextViewText(R.id.text_classRoom_widget, roomStr);
            cell.setTextColor(R.id.text_classRoom_widget, ContextCompat.getColor(context, R.color.widget_cell_text_color));

            col.addView(R.id.widget_col, cell);
        }
        remoteViews.addView(colId, col);
    }
}

