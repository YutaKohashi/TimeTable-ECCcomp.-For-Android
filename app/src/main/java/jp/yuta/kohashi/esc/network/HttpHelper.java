package jp.yuta.kohashi.esc.network;

import android.os.Bundle;
import android.util.Log;

import com.anprosit.android.promise.Callback;
import com.anprosit.android.promise.Promise;
import com.anprosit.android.promise.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.yuta.kohashi.esc.App;
import jp.yuta.kohashi.esc.model.AttendanceRate;
import jp.yuta.kohashi.esc.network.api.EscApiCallback;
import jp.yuta.kohashi.esc.network.api.EscApiManager;
import jp.yuta.kohashi.esc.network.api.model.news.NewsDetail;
import jp.yuta.kohashi.esc.network.api.model.schedule.ScheduleRoot;
import jp.yuta.kohashi.esc.network.api.model.timeTable.TimeTable;
import jp.yuta.kohashi.esc.util.RegexUtil;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * Created by yutakohashi on 2016/11/15.
 */

/**
 * ヘルパ内で保存処理を行う
 */
public class HttpHelper {
    private static final String TAG = HttpHelper.class.getSimpleName();

    private static String mLastResponseHtml = "";

    /************************************   public   ********************************************************/


    /**
     * 時間割を取得,保存
     * @param userId
     * @param password
     * @param successCallbacks
     */
    public static void  getTimeTable(String userId,String password ,final SuccessCallbacks successCallbacks) {

        EscApiManager.getTimeTable(userId, password, (List<TimeTable> timeTableList) ->{
            // nullの時失敗
            if(timeTableList != null){
                PrefUtil.saveTimeTable(timeTableList);
                PrefUtil.saveTimeTableOriginal(timeTableList);
                successCallbacks.callback(true);
            } else {
                successCallbacks.callback(false);
            }
        });
    }


    /***
     * 出席率を取得、保存
     *
     * @param userId
     * @param password
     * @param successCallbacks
     */
    public static void getAttendanceRate(String userId, String password, final SuccessCallbacks successCallbacks) {
        requestAttendanceRate(userId, password, (html, bool) -> {
            if (bool) {
                if (PrefUtil.loadAttendanceRateModelList().size() > 0) {
                    saveAttandanceRate(html);
                } else {
                    PrefUtil.saveAttendanceRate(html);
                }
                PrefUtil.saveAttendanceAllRateData(html);
                PrefUtil.saveStudentInfo(html);
            }
            successCallbacks.callback(bool);
        });
    }

    private static void saveAttandanceRate(String html) {
        List<AttendanceRate> beforeList = PrefUtil.loadAttendanceRateModelList();
        List<AttendanceRate> afterList = PrefUtil.createAttendanceList(html);
        List<AttendanceRate> temp = new ArrayList<>();
        for (AttendanceRate after : afterList) {
            for (AttendanceRate before : beforeList) {
                if (before.getSubjectName().equals(after.getSubjectName())) {
                    after.setType(before.getType());
                    break;
                }
            }
            temp.add(after);
        }
        PrefUtil.saveAttendanceRate(temp);
    }


    /**
     * 時間割と出席率を取得
     *
     * @param userId
     * @param password
     * @param successCallBacks
     */
    public static void getTimeAttend(final String userId, final String password, final SuccessCallbacks successCallBacks) {
        getTimeTable(userId, password, bool -> {
            if (bool) {
                getAttendanceRate(userId, password, bool1 -> successCallBacks.callback(bool1));
            } else {
                successCallBacks.callback(false);
            }
        });
    }


    /**
     * 学校からのお知らせ
     *
     * @param userId
     * @param password
     * @param successCallbacks
     */
    public static void getSchoolNews(final String userId, final String password, final SuccessCallbacks successCallbacks) {
        EscApiManager.getSchoolNews(userId,password,newsItems -> {
            if(newsItems != null){
                PrefUtil.saveSchoolNews(newsItems);
                successCallbacks.callback(true);
            }
            else{
                successCallbacks.callback(false);
            }
        });
    }

    /**
     * 担任からのお知らせ
     *
     * @param userId
     * @param password
     * @param successCallbacks
     */
    public static void getTanninNews(final String userId, final String password, final SuccessCallbacks successCallbacks) {
        EscApiManager.getTaninNews(userId,password,newsItems -> {
            if(newsItems != null){
                PrefUtil.saveTanninNews(newsItems);
                successCallbacks.callback(true);
            }
            else{
                successCallbacks.callback(false);
            }
        });
    }

    /**
     * 担任・学校からのお知らせを取得するメソッド
     *
     * @param userId
     * @param password
     * @param successCallbacks
     */
    public static void getSchoolTanninNews(final String userId, final String password, final SuccessCallbacks successCallbacks) {
        getSchoolNews(userId, password, bool -> {
            if (bool) {
                getTanninNews(userId, password, bool1 -> successCallbacks.callback(bool1));
            } else {
                successCallbacks.callback(false);
            }
        });
    }

    /**
     * ニュース詳細を取得
     * @param newsId
     * @param userId
     * @param password
     * @param callback
     */
    public static void getNewsDetail(Integer newsId, String userId, String password, EscApiCallback<NewsDetail> callback) {
        EscApiManager.getNewsDetail(newsId, userId, password, newsDetail -> {
            callback.callback(newsDetail);
        });
    }

    public static void getScheduleList(String userId, String password, SuccessCallbacks callbacks){
        EscApiManager.getSchedule(userId, password, response -> {
            List<ScheduleRoot> scheduleRootList = response;
            if(scheduleRootList != null){
                /**
                 * 保存する段階でnullが内容にリストを整形する
                 */
//                scheduleRootList = fixScheduleList(scheduleRootList);
                PrefUtil.saveSchedule(scheduleRootList);
                callbacks.callback(true);
            } else {
                callbacks.callback(false);
            }
        });
    }

    /************************************   private   ********************************************************/

    //**
    //region 出席率
    //**

    /***
     * 出席率リクエスト　メソッド
     *
     * @param userId
     * @param password
     * @param accessCallbacks
     */
    private static void requestAttendanceRate(final String userId, final String password, final AccessCallbacks accessCallbacks) {

        Promise.with(App.getAppContext(), Void.class).thenOnAsyncThread((Task<Void, Void>) (aVoid, nextTask) -> {
            try {
                //ログイン
                HttpResultClass result = loginToYS(userId, password);

                //failure
                if (!result.getBool()) throw new Exception("ログインに失敗しました");

                mLastResponseHtml = result.getString();
                //出席率ページへリクエスト
                Map<String, String> body = CreateRequestBody.createPostDataForRatePage(mLastResponseHtml);
                result = HttpBase.httpPost(RequestURL.YS_TO_RATE_PAGE, body, RequestURL.DEFAULT_REFERRER);

                //failure
                if (!result.getBool()) throw new Exception("出席率ページへの遷移に失敗しました");

                mLastResponseHtml = result.getString();
                if (!RegexUtil.containsCheck(">個人別出席率表<", mLastResponseHtml)) {
                    throw new Exception("出席率ページへの遷移に失敗しました");
                }

                nextTask.run(null);
            } catch (Exception e) {
                nextTask.fail(null, e);
            }

        }).setCallback(new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                accessCallbacks.callback(mLastResponseHtml, true);
            }

            @Override
            public void onFailure(Bundle bundle, Exception e) {
                accessCallbacks.callback(mLastResponseHtml, false);
            }
        }).create().execute(null);
    }


    //**
    //endregion
    //**


    //**
    //region ログイン
    //**

    /***
     * 山口学園学生サービスにログインするメソッド
     *
     * @param userId
     * @param password
     * @return
     */
    private static HttpResultClass loginToYS(String userId, String password) {
        HttpResultClass result = HttpBase.httpGet(RequestURL.YS_TO_LOGIN_PAGE, RequestURL.DEFAULT_REFERRER);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //failure
        if (!result.getBool()) return result;

        mLastResponseHtml = result.getString();
        Map<String, String> body = CreateRequestBody.createPostDataForYSLogin(userId, password, mLastResponseHtml);
        result = HttpBase.httpPost(RequestURL.YS_LOGIN, body, RequestURL.YS_TO_LOGIN_PAGE);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //failure
        if (!result.getBool()) return result;

        mLastResponseHtml = result.getString();
        if (!RegexUtil.containsCheck("ログオフ", mLastResponseHtml))
            result.setBool(false);


        return result;
    }


    //**
    //endregion
    //**


    /***
     * print log
     *
     * @param e
     */
    private static void log(Exception e) {
        Log.d(TAG, e.toString());
    }


    //**
    //region インターフェース
    //**

    public interface AccessCallbacks {
        void callback(String html, boolean bool);
    }

    public interface SuccessCallbacks {
        void callback(boolean bool);
    }

    //**
    //endregion
    //**
}
