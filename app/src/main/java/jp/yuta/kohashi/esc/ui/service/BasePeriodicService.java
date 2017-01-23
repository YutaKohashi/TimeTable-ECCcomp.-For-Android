package jp.yuta.kohashi.esc.ui.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import jp.yuta.kohashi.esc.util.Util;

/**
 * Created by yutakohashi on 2017/01/22.
 */

public abstract class BasePeriodicService extends Service {

    /**
     * 定期処理を実行する間隔を指定
     *
     * @return
     */
    protected abstract long getInterval();

    /**
     * 定期処理を実行したいタスク
     */
    protected abstract void execTask();

    /**
     * 実行計画
     */
    protected abstract void makeNextPlan();

    protected final IBinder binder = new Binder() {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * 常駐開始
     *
     * @param context
     * @return
     */
    public BasePeriodicService startResident(Context context) {
        Intent intent = new Intent(context, this.getClass());
        intent.putExtra("type", "start");
        context.startService(intent);
        // show toast
        return this;
    }

    /**
     * @param intent
     * @param startId
     */
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        execTask();
    }

    /**
     * 次回のサービス予約
     */
    public void scheduleNextTime() {
        long now = System.currentTimeMillis();
        PendingIntent alarmSender = PendingIntent.getService(this, 0, new Intent(this, this.getClass()), 0);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC, now + getInterval(), alarmSender);
    }

    /**
     * サービスの定期実行を解除
     *
     * @param context
     */
    public void stopResident(Context context) {
        // サービス名を指定
        Intent intent = new Intent(context, this.getClass());

        //アラームを解除
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
        stopSelf();
    }
}
