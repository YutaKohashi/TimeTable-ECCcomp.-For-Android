package jp.yuta.kohashi.esc.event;

import com.squareup.otto.Bus;

/**
 * Created by yutakohashi on 2017/01/31.
 */

public class BusHolder {

    private static Bus sBus = new Bus();

    public static Bus get() {
        return sBus;
    }

}
