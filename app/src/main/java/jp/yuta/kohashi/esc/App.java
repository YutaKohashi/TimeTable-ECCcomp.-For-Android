package jp.yuta.kohashi.esc;


import android.app.Application;
import android.content.Context;

import jp.yuta.kohashi.esc.network.HttpBase;
import jp.yuta.kohashi.esc.network.api.EscApiManager;

/**
 * Created by yutakohashi on 2017/01/17.
 */

public class App extends Application {
    private static Context sContext;

    public static Context getAppContext(){
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        HttpBase.init();
        EscApiManager.init();
    }
}
