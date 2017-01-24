package jp.yuta.kohashi.esc.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.TimeBlockModel;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * Implementation of App Widget functionality.
 */
public class TimeTableWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Widgetのレイアウトを取得
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_root_time_table);

        List<List<TimeBlockModel>> lists = PrefUtil.loadTimeBlockList(context);
        try {
            createColTimeTable(context, lists.get(0), R.id.mon_col_widget, remoteViews);
            createColTimeTable(context, lists.get(1), R.id.tue_col_widget, remoteViews);
            createColTimeTable(context, lists.get(2), R.id.wed_col_widget, remoteViews);
            createColTimeTable(context, lists.get(3), R.id.thur_col_widget, remoteViews);
            createColTimeTable(context, lists.get(4), R.id.fri_col_widget, remoteViews);
        }catch(Exception e){
            // failure load
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_root_time_table);
        }
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            //サービス起動チェック
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

    private static void createColTimeTable(Context context, List<TimeBlockModel> list, int colId, RemoteViews remoteViews) throws Exception {
        RemoteViews col = new RemoteViews(context.getPackageName(), R.layout.widget_col_time_table);
        for (int i = 0; i < list.size(); i++) {
            TimeBlockModel timeBlock = list.get(i);
            String subStr = timeBlock.getSubject();
            String roomStr = timeBlock.getClassRoom();

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

