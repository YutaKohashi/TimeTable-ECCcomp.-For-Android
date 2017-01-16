package jp.yuta.kohashi.esc.http2;

import android.content.Context;

/**
 * Created by yutakohashi on 2016/11/15.
 */

public class HttpConnector {

    private HttpHelper httpHelper;

    HttpConnector(Context context) {
        httpHelper = new HttpHelper(context);
    }

    public interface Callback{
        void callback(boolean bool);
    };

    public void request(Type type, String userId, String password, final Callback callback){
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
        }
    }

    public enum Type {
        TIME_TABLE,
        ATTENDANCE_RATE,
        TIME_ATTEND;
    }
}
