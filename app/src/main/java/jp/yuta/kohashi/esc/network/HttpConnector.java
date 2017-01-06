package jp.yuta.kohashi.esc.network;

import android.content.Context;

/**
 * Created by yutakohashi on 2016/11/15.
 */

/***
 * ボタン押下などで通信したい場合はこのクラスからリクエストする
 */
public class HttpConnector {

    private  static HttpHelper httpHelper;

    public static void init(Context context){
        if(httpHelper == null){
            httpHelper = new HttpHelper(context);
        }
    }

    public interface Callback{
        void callback(boolean bool);
    };

    public static void request(Type type, String userId, String password, final Callback callback){
        switch (type){
            case TIME_TABLE:
                httpHelper.getTImetable(userId, password, new HttpHelper.SuccessCallbacks() {
                    @Override
                    public void callback(boolean bool) {
                        callback.callback(bool);
                    }
                });
                break;
            case ATTENDANCE_RATE:
                httpHelper.getAttendanceRate(userId, password, new HttpHelper.SuccessCallbacks() {
                    @Override
                    public void callback(boolean bool) {
                        callback.callback(bool);
                    }
                });
                break;
            case TIME_ATTEND:
                httpHelper.getTimeAttend(userId, password, new HttpHelper.SuccessCallbacks() {
                    @Override
                    public void callback(boolean bool) {
                        callback.callback(bool);
                    }
                });
                break;
            case NEWS_SCHOOL:
                break;
            case NEWS_TEACHER:
                break;
        }
    }

    public enum Type {
        TIME_TABLE,
        ATTENDANCE_RATE,
        TIME_ATTEND,
        NEWS_SCHOOL,
        NEWS_TEACHER;
    }
}
