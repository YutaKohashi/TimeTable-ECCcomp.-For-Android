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

//    /***
//     * 時間割を取得,保存(ユーザ名も)
//     *
//     * @param userId
//     * @param password
//     * @param successCallbacks
//     */
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

//    public static void getTImeTable(String userId, String password, final SuccessCallbacks successCallbacks){
//        new Esc
//    }


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
//        requestNewsDetail(userId, password, url, new AccessCallbacks() {
//            @Override
//            public void callback(String html, boolean bool) {
//                accessCallbacks.callback(html, bool);
//            }
//        });

        EscApiManager.getNewsDetail(newsId, userId, password, newsDetail -> {
            callback.callback(newsDetail);
        });
    }

    public static void getScheduleList(String userId, String password, SuccessCallbacks callbacks){
        EscApiManager.getSchedule(userId, password, response -> {
            List<ScheduleRoot> scheduleRootList = response;
            if(scheduleRootList != null){
                PrefUtil.saveSchedule(scheduleRootList);
                callbacks.callback(true);
            } else {
                callbacks.callback(false);
            }
        });
    }

    /************************************   private   ********************************************************/

//    //**
//    //region 時間割
//    //**
//
//    /***
//     * 時間割のリクエストメソッド
//     *
//     * @param userId
//     * @param password
//     * @param listCallbacks
//     */
////    private static void requestTimeTable(final String userId,
////                                         final String password,
////                                         final AccessListCallbacks listCallbacks) {
////
////        if (teacherHtmls != null) teacherHtmls.clear();
////
////
////
////        Promise.with(App.getAppContext(), Void.class).thenOnAsyncThread(new Task<Void, Void>() {
////            @Override
////            public void run(Void aVoid, NextTask<Void> nextTask) {
////                try {
////                    HttpResultClass result = loginToESC(userId, password);
////
////                    //failure
////                    if (!result.getBool()) throw new Exception("ログインに失敗しました");
////
////                    mLastResponseHtml = result.getString();
////                    List<String> urls = getTeacherNameUrls(mLastResponseHtml);
////
////                    teacherHtmls = requestTeacherName(urls);
////
////                    //先生名取得失敗
////                    if (teacherHtmls == null) throw new Exception("先生名の取得に失敗しました");
////
//////                    Log.d(TAG, mLastResponseHtml);
////                    nextTask.run(null);
////                } catch (Exception e) {
////                    nextTask.fail(null, e);
////                }
////            }
////        }).setCallback(new Callback<Void>() {
////            @Override
////            public void onSuccess(Void aVoid) {
////                listCallbacks.callback(mLastResponseHtml, teacherHtmls, true);
////            }
////
////            @Override
////            public void onFailure(Bundle bundle, Exception e) {
//////                log(e);
////                listCallbacks.callback(mLastResponseHtml, teacherHtmls, false);
////            }
////        }).create().execute(null);
////    }
//
//    /**
//     * 引数のhtmlソースから先生名のurlを取り出すメソッド
//     *
//     * @param html
//     * @return
//     */
////    private static List<String> getTeacherNameUrls(String html) {
////        List<String> urls = new ArrayList<>();
////
////        html = RegexUtil.replaceCRLF(html, true);
////        Matcher m = RegexUtil.getGroupValues("<li class=\"letter\"><a href=\"(.+?)\">投書</a>", html);
////        while (m.find()) {
////            String url = m.group(1);
////            urls.add(url);
////        }
////        return urls;
////    }
//
//
//    /***
//     * 先生名のhtmlソースを取得するメソッド
//     *
//     * @param urls
//     * @return
//     */
////    private static List<String> requestTeacherName(final List<String> urls) {
////
////        final List<String> htmls = new ArrayList<>();
////
////        for (String url : urls) {
////            HttpResultClass result = HttpBase.httpGet(url, RequestURL.ESC_TO_LOGIN_PAGE);
////            if (!result.getBool()) return null;
////
////            htmls.add(result.getString());
////        }
////        return htmls;
////    }
//
//    //**
//    //endregion
//    //**


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
//                    Log.d(TAG, mLastResponseHtml);
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
//                log(e);
                accessCallbacks.callback(mLastResponseHtml, false);
            }
        }).create().execute(null);
    }


    //**
    //endregion
    //**


    //**
    //region お知らせ
    //**

//    private static void requestNews(final String userId, final String password, final AccessCallbacks accessCallbacks) {
//
//        Promise.with(App.getAppContext(), Void.class).thenOnAsyncThread(new Task<Void, Void>() {
//            @Override
//            public void run(Void aVoid, NextTask<Void> nextTask) {
//                try {
//                    //ログイン
//                    HttpResultClass result = loginToESC(userId, password);
//
//                    //failure
//                    if (!result.getBool()) throw new Exception("ログインに失敗しました");
//
//                    mLastResponseHtml = result.getString();
////                    Log.d(TAG, mLastResponseHtml);
//                    nextTask.run(null);
//                } catch (Exception e) {
//                    nextTask.fail(null, e);
//                }
//            }
//        }).setCallback(new Callback<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                accessCallbacks.callback(mLastResponseHtml, true);
//            }
//
//            @Override
//            public void onFailure(Bundle bundle, Exception e) {
//                log(e);
//                accessCallbacks.callback(mLastResponseHtml, false);
//            }
//        }).create().execute(null);
//    }
//
//    private static void requestNewsDetail(final String userId, final String password, final String url, final AccessCallbacks accessCallbacks) {
//        Promise.with(App.getAppContext(), Void.class).thenOnAsyncThread(new Task<Void, Void>() {
//            @Override
//            public void run(Void aVoid, NextTask<Void> nextTask) {
//                try {
//                    //ログイン
//                    HttpResultClass result = loginToESC(userId, password);
//                    if (!result.getBool()) throw new Exception("ログインに失敗しました");
//
//                    mLastResponseHtml = result.getString();
//
//                    result = HttpBase.httpGet(url, RequestURL.ESC_TIME_TABLE_PAGE);
//                    if (!result.getBool()) throw new Exception("ニュース詳細の取得に失敗しました");
//
//                    mLastResponseHtml = result.getString();
////                    Log.d(TAG, mLastResponseHtml);
//                    nextTask.run(null);
//                } catch (Exception e) {
//                    nextTask.fail(null, e);
//                }
//
//            }
//        }).setCallback(new Callback<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                accessCallbacks.callback(mLastResponseHtml, true);
//            }
//
//            @Override
//            public void onFailure(Bundle bundle, Exception e) {
//                log(e);
//                accessCallbacks.callback(mLastResponseHtml, false);
//            }
//        }).create().execute(null);
//    }

    //**
    //endregion
    //**


    //**
    //region ログイン
    //**

    /***
     * EccStudentCommunication にログインするメソッド
     *
     * @param userId
     * @param password
     * @return
     */
//    private static HttpResultClass loginToESC(String userId, String password) {
//
//        HttpResultClass result = HttpBase.httpGet(RequestURL.ESC_TO_LOGIN_PAGE,
//                RequestURL.DEFAULT_REFERRER);
//
//        if (!result.getBool()) return result;
//
//        mLastResponseHtml = result.getString();
//        //create requestBody with map
//        Map<String, String> body = CreateRequestBody.createPostDataForEscLogin(userId, password, mLastResponseHtml);
//        //login
//        result = HttpBase.httpPost(RequestURL.ESC_LOGIN, body, RequestURL.ESC_TO_LOGIN_PAGE);
//
//        if (!result.getBool()) return result;
//        mLastResponseHtml = result.getString();
//        if (!RegexUtil.containsCheck(">ログアウト<", mLastResponseHtml)) {
//            result.setBool(false);
//        }
//
//        return result;
//    }

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
