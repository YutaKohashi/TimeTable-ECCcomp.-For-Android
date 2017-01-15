package jp.yuta.kohashi.esc;

import java.util.Calendar;

/**
 * Created by yutakohashi on 2017/01/10.
 */

public final class Const {
    public static final String APP_VERSION = "2.0.0";
    public static final String SCHEDULE_FILE_NAME = "2016Schedule.json";

    public static final int YEAR = Calendar.getInstance().get(Calendar.YEAR); //　現在の年
    public static final int MONTH = Calendar.getInstance().get(Calendar.MONTH) + 1; //　現在の年


}
