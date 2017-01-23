package jp.yuta.kohashi.esc.ui.service;

import android.content.Context;

import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * Created by yutakohashi on 2017/01/22.
 */

public class OnBootReceiver extends BaseOnBootReceiver {
    @Override
    protected void onDeviceBoot(Context context) {
        if (PrefUtil.isNotifyNews()) {
            new EccNewsManageService().startResident(context);
        }
    }
}
