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
import jp.yuta.kohashi.esc.model.NewsItem;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.ui.activity.LoginCheckActivity;
import jp.yuta.kohashi.esc.ui.activity.MainActivity;
import jp.yuta.kohashi.esc.ui.activity.NewsDetailActivity;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

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
//        final List<String> schoolNewsList = getTitleList(PrefUtil.loadSchoolNewsList());
//        final List<String> tanninNewsList = getTitleList(PrefUtil.loadTanninNewsList());

        //TODO : fix
        final List<NewsItem> schoolNewsList1 = PrefUtil.loadSchoolNewsList();
        final List<NewsItem> tanninNewsList1 = PrefUtil.loadTanninNewsList();

        //TODO::::::

        HttpConnector.request(
                HttpConnector.Type.NEWS_SCHOOL_TEACHER,
                PrefUtil.getId(),
                PrefUtil.getPss(),
                new HttpConnector.Callback() {
                    @Override
                    public void callback(boolean bool) {
                        if (bool) {
//                            List<String> newSchoolNewsList = getTitleList(PrefUtil.loadSchoolNewsList());
//                            List<String> newTanninNewsList = getTitleList(PrefUtil.loadTanninNewsList());
//
//                            List<String> compareSchoolList = compareList(schoolNewsList, newSchoolNewsList);
//                            List<String> compareTanninList = compareList(tanninNewsList, newTanninNewsList);
//                            String detailText = createNotifyText(compareSchoolList, compareTanninList);
//
//                            if (!TextUtils.isEmpty(detailText.trim())) {
//                                showNotification(detailText);
//                            }
                            //TODO : fix
                            List<NewsItem> newSchoolNewsList1 = PrefUtil.loadSchoolNewsList();
                            List<NewsItem> newTanninNewsList1 = PrefUtil.loadTanninNewsList();
//                            List<NewsItem> compareSchoolList1 = compareList1(schoolNewsList1,newSchoolNewsList1);
//                            List<NewsItem> compareTanninList1 = compareList1(tanninNewsList1,newTanninNewsList1);
                            /**
                             * STUB  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
                             */
                            NewsItem stub = new NewsItem("ダミー","2020.12.20","http://google.com");
                            newSchoolNewsList1.add(stub);
                            /**
                             *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
                             */
                            List<NewsItem> compareNewsList = new ArrayList<>();
                            //　一つにまとめる
                            compareNewsList.addAll(compareList(schoolNewsList1, newSchoolNewsList1));
                            compareNewsList.addAll(compareList(tanninNewsList1, newTanninNewsList1));
//                            if (compareNewsList.size() == 1) {
//                                showNotification(compareNewsList.get(0));
//                            } else if (compareNewsList.size() > 1) {
//                                showNotification(compareNewsList);
//                            }
                            if (compareNewsList.size() >= 1) {
                                showNotification(compareNewsList);
                            }
                            //TODO::::::

                        }
                    }
                }
        );
    }

    /**
     * 通知を表示する
     * list > 0
     */
    public void showNotification(@NonNull List<NewsItem> list) {
//        Log.d(TAG, "detailText : " + detailText);
//        Log.d(TAG + "isempty", "" + TextUtils.isEmpty(detailText));
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
            detailText += "● ";
            detailText += item.getTitle();
            detailText += "\n";
        }

        return detailText;
    }


//    /**
//     * ２つのリストを比較して
//     * 第２引数のリストにしかないものが存在するときtrueを返す
//     *
//     * @param beforeList
//     * @param afterList
//     * @return
//     */
//    public List<String> compareList(List<String> beforeList, List<String> afterList) {
//        List<String> resultList = new ArrayList<>();
//
//        for (String after : afterList) {
//            if (after == null) continue;
//            if (!beforeList.contains(after)) {
//                resultList.add(after);
//            }
//        }
//        return resultList;
//    }

//    private List<String> getTitleList(List<NewsItem> models) {
//        final List<String> list = new ArrayList<>();
//        for (NewsItem model : models) {
//            list.add(model.getTitle());
//        }
//
//        return list;
//    }
}
