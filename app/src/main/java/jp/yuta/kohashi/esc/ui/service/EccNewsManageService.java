package jp.yuta.kohashi.esc.ui.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.Const;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.NewsModel;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.ui.activity.LoginCheckActivity;
import jp.yuta.kohashi.esc.ui.activity.MainActivity;
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
////        return  60000;
//        return 180000; //3分
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
        Log.d(TAG,"execute task ");
        final List<String> schoolNewsList = getTitleList(PrefUtil.loadSchoolNewsList());
        final List<String> tanninNewsList = getTitleList(PrefUtil.loadTanninNewsList());

        new HttpConnector().request(
                HttpConnector.Type.NEWS_SCHOOL_TEACHER,
                PrefUtil.getId(),
                PrefUtil.getPss(),
                new HttpConnector.Callback() {
                    @Override
                    public void callback(boolean bool) {
                        if(bool){
                            List<String> newSchoolNewsList = getTitleList(PrefUtil.loadSchoolNewsList());
                            List<String> newTanninNewsList = getTitleList(PrefUtil.loadTanninNewsList());

                            List<String> compareSchoolList = compareList(schoolNewsList, newSchoolNewsList);
                            List<String> compareTanninList = compareList(tanninNewsList, newTanninNewsList);
                            String detailText = createNotifyText(compareSchoolList, compareTanninList);
                            Log.d(TAG, "detailText is empty = " + TextUtils.isEmpty(detailText.trim()));
                            if (!TextUtils.isEmpty(detailText.trim())) {
                                showNotification(detailText);
                            }
                        }
                    }
                }
        );
    }

    /**
     * 通知するためのテキストを作成
     *
     * @param compareSchoolList
     * @param compareTanninList
     * @return
     */
    private String createNotifyText(List<String> compareSchoolList, List<String> compareTanninList) {
        String detailText = "";
        for (String string : compareSchoolList) {
            detailText += string;
            detailText += "\n";
        }

        for (String string : compareTanninList) {
            detailText += string;
            detailText += "\n";
        }

        return detailText;
    }

    /**
     * 通知を表示する
     *
     * @param detailText
     */
    public void showNotification(String detailText) {
        Log.d(TAG, "detailText : " + detailText);
        Log.d(TAG + "isempty", "" + TextUtils.isEmpty(detailText));
        if (!TextUtils.isEmpty(detailText)) {
            Intent intent = new Intent(this, LoginCheckActivity.class);
            intent.putExtra(MainActivity.SELECT_TAB_NEWS,true);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 0, intent,
                            PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(getResources().getString(R.string.notify_new_news))
                    .setContentText(detailText)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.icon))
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_mail_outline)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(detailText))
                    .setAutoCancel(true);

            //通知のカスタマイズ
            Notification notify = mBuilder.build();
            if (PrefUtil.isNotifyNews()) { //必須
                mNotifyMgr.notify(mNotificationId, notify);
            }
        }
    }

    /**
     * ２つのリストを比較して
     * 第２引数のリストにしかないものが存在するときtrueを返す
     *
     * @param beforeList
     * @param afterList
     * @return
     */
    public static List<String> compareList(List<String> beforeList, List<String> afterList) {
        List<String> resultList = new ArrayList<>();

        for (String after : afterList) {
            if (after == null) continue;
            if (!beforeList.contains(after)) {
                resultList.add(after);
            }
        }
        return resultList;
    }

    private List<String> getTitleList(List<NewsModel> models) {
        final List<String> list = new ArrayList<>();
        for (NewsModel model : models) {
            list.add(model.getTitle());
        }

        return list;
    }
}
