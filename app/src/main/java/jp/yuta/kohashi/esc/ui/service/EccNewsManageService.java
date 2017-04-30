package jp.yuta.kohashi.esc.ui.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.Const;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.network.api.model.news.NewsItem;
import jp.yuta.kohashi.esc.ui.activity.LoginCheckActivity;
import jp.yuta.kohashi.esc.ui.activity.MainActivity;
import jp.yuta.kohashi.esc.ui.activity.NewsDetailActivity;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

//import jp.yuta.kohashi.esc.model.NewsItem;

public class EccNewsManageService extends BasePeriodicService {
    private final String TAG = EccNewsManageService.class.getSimpleName();

    public static BasePeriodicService activeService;
    NotificationManager mNotifyMgr;
    private static final int mNotificationId = 001;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(EccNewsManageService.class.getSimpleName(), "サービス起動");
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    //    ミリ秒
    @Override
    protected long getInterval() {
        return Const.SERVICE_INTERVAL_MILLISECONDS;
    }

    @Override
    protected void execTask() {
        activeService = this; //set service
        task();
        makeNextPlan();
    }

    @Override
    protected void makeNextPlan() {
        this.scheduleNextTime();
    }

    /**
     * 常駐を解除
     */
    public static void stopResidentIfActive(Context context) {
        if (activeService != null) {
            activeService.stopResident(context);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(EccNewsManageService.class.getSimpleName(), "サービス終了");
    }

    private void task() {
        Log.d(TAG, "execute task ");
        final List<NewsItem> schoolNewsList1 = PrefUtil.loadSchoolNewsList();
        final List<NewsItem> tanninNewsList1 = PrefUtil.loadTanninNewsList();

        HttpConnector.request(HttpConnector.Type.NEWS_SCHOOL_TEACHER, PrefUtil.getId(),PrefUtil.getPss(), (bool -> {
            if (bool) {
                List<NewsItem> newSchoolNewsList1 = PrefUtil.loadSchoolNewsList();
                List<NewsItem> newTanninNewsList1 = PrefUtil.loadTanninNewsList();
//                            /**
//                             * STUB  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
//                             */
//                            NewsItem stub = new NewsItem("ダミー","2020.12.20","http://google.com");
//                            newSchoolNewsList1.add(stub);
//                            /**
//                             *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
//                             */
                List<NewsItem> compareNewsList = new ArrayList<>();
                //　一つにまとめる
                compareNewsList.addAll(compareList(schoolNewsList1, newSchoolNewsList1));
                compareNewsList.addAll(compareList(tanninNewsList1, newTanninNewsList1));

                if (compareNewsList.size() >= 1) {
                    showNotification(compareNewsList);
                }
            }
        }));
    }

    /**
     * 通知を表示する
     * list > 0
     */
    public void showNotification(@NonNull List<NewsItem> list) {
        Intent intent = new Intent(this, LoginCheckActivity.class);
        intent.putExtra(MainActivity.SELECT_TAB_NEWS, true);
        if (list.size() == 1) {
            // only list size = 1
            intent.putExtra(NewsDetailActivity.NEWS_MODEL, list.get(0)); // to NewsDetailActivity
        }
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.notify_new_news))
                .setContentText(createNotifyText(list))
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.icon))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_mail_outline)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(createNotifyText(list)))
                .setAutoCancel(true);

        //通知のカスタマイズ
        Notification notify = mBuilder.build();
        if (PrefUtil.isNotifyNews()) { //必須
            mNotifyMgr.notify(mNotificationId, notify);
        }
    }

    public List<NewsItem> compareList(List<NewsItem> beforeList, List<NewsItem> afterList) {
        List<NewsItem> resultList = new ArrayList<>();
        for (NewsItem after : afterList) {
            if (!NewsItem.containsList(after, beforeList)) {
                resultList.add(after);
            }
        }
        return resultList;
    }

    /**
     * 通知するためのテキストを作成
     *
     * @return
     */
    private String createNotifyText(List<NewsItem> list) {
        String detailText = "";
        for (NewsItem item : list) {
            detailText += "・ ";
            detailText += item.getTitle();
            detailText += "\n";
        }
        return detailText;
    }
}
