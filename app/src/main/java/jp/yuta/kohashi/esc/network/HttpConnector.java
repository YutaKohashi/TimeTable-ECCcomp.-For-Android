package jp.yuta.kohashi.esc.network;

import jp.yuta.kohashi.esc.network.service.HttpHelper;

/**
 * Created by yutakohashi on 2016/11/15.
 */

/***
 * 通信したい場合はこのクラスからリクエストする
 */
public class HttpConnector {

    /**
     * コールバック
     */
    public interface Callback {
        void callback(boolean bool);
    }

    /**
     * データタイプ
     */
    public enum Type {
        TIME_TABLE,          // 時間割
        ATTENDANCE_RATE,     // 出席率
        TIME_ATTEND,         // 時間割・出席率
        NEWS_SCHOOL,         // 学校からのお知らせ
        NEWS_TEACHER,        // 担任からのお知らせ
        NEWS_SCHOOL_TEACHER; // 学校・担任からのお知らせ
    }

    /***
     * リクエストメソッド
     *
     * @param type     　取得、保存したいデータタイプ
     * @param userId   　学籍番号
     * @param password 　パスワード
     * @param callback 　コールバック
     */
    public static void request(Type type, final String userId, final String password, final Callback callback) {
        switch (type) {
            case TIME_TABLE:
                HttpHelper.getTimeTable(userId,password,(bool -> {
                    callback.callback(bool);
                }));
                break;
            case ATTENDANCE_RATE:
                HttpHelper.getAttendanceRate(userId,password,(bool -> {
                    callback.callback(bool);
                }));
                break;
            case TIME_ATTEND:
                HttpHelper.getTimeAttend(userId,password,(bool -> {
                    callback.callback(bool);
                }));
                break;
            case NEWS_SCHOOL:
                HttpHelper.getSchoolNews(userId,password,(bool -> {
                    callback.callback(bool);
                }));
                break;
            case NEWS_TEACHER:
                HttpHelper.getTanninNews(userId,password,(bool -> {
                    callback.callback(bool);
                }));
                break;
            case NEWS_SCHOOL_TEACHER:
                HttpHelper.getSchoolTanninNews(userId,password,(bool -> {
                    callback.callback(bool);
                }));
        }
    }

    public static void requestNewsDetail(String userId, String password, String url , final HttpHelper.AccessCallbacks accessCallbacks){
        HttpHelper.getNewsDetail(userId,password,url,((html, bool) -> {
            accessCallbacks.callback(html,bool);
        }));
    }
}
