package jp.yuta.kohashi.esc.ui.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by yutakohashi on 2017/01/22.
 */

public abstract class BaseOnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        // 端末起動時
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            new Thread(() -> {
                onDeviceBoot(context);
            }).start();
        }
    }

    protected abstract void onDeviceBoot(Context context);
}
