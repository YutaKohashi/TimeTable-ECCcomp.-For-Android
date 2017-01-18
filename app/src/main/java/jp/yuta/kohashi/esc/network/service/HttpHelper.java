package jp.yuta.kohashi.esc.network.service;

import android.os.Bundle;
import android.util.Log;

import com.anprosit.android.promise.Callback;
import com.anprosit.android.promise.NextTask;
import com.anprosit.android.promise.Promise;
import com.anprosit.android.promise.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import jp.yuta.kohashi.esc.App;
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
    private static List<String> teacherHtmls;

    /************************************   public   ********************************************************/

    /***
     * 時間割を取得,保存(ユーザ名も)
     *
     * @param userId
     * @param password
     * @param successCallbacks
     */
    public static void getTImeTable(String userId, String password, final SuccessCallbacks successCallbacks) {
        requestTimeTable(userId, password, new AccessListCallbacks() {
            @Override
            public void callback(String html, List<String> htmls, boolean bool) {
                if (bool) {
                    PrefUtil.saveTimeTable(html, htmls);
                    PrefUtil.saveUserName(html);
                }
                successCallbacks.callback(bool);
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
        requestAttendanceRate(userId, password, new AccessCallbacks() {
            @Override
            public void callback(String html, boolean bool) {
                if (bool) {
                    PrefUtil.saveAttendanceRate(html);
                    PrefUtil.saveAttendanceAllRateData(html);
                }
                successCallbacks.callback(bool);
            }
        });
    }


    /**
     * 時間割と出席率を取得
     *
     * @param userId
     * @param password
     * @param successCallBacks
     */
    public static void getTimeAttend(final String userId, final String password, final SuccessCallbacks successCallBacks) {
        getTImeTable(userId, password, new SuccessCallbacks() {
            @Override
            public void callback(boolean bool) {
                if (bool) {
                    getAttendanceRate(userId, password, new SuccessCallbacks() {
                        @Override
                        public void callback(boolean bool) {
                            successCallBacks.callback(bool);
                        }
                    });
                } else {
                    successCallBacks.callback(false);
                }
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
        requestNews(userId, password, new AccessCallbacks() {
            @Override
            public void callback(String html, boolean bool) {
                if (bool) {
                    PrefUtil.saveSchoolNews(html);
                }
                successCallbacks.callback(bool);
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
        requestNews(userId, password, new AccessCallbacks() {
            @Override
            public void callback(String html, boolean bool) {
                if (bool) {
                    PrefUtil.saveTanninNews(html);
                }
                successCallbacks.callback(bool);
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
        getSchoolNews(userId, password, new SuccessCallbacks() {
            @Override
            public void callback(boolean bool) {
                if (bool) {
                    getTanninNews(userId, password, new SuccessCallbacks() {
                        @Override
                        public void callback(boolean bool) {
                            successCallbacks.callback(bool);
                        }
                    });
                } else {
                    successCallbacks.callback(false);
                }
            }
        });
    }

    /**
     * ニュース詳細を取得
     *
     * @param userId
     * @param password
     * @param url
     * @param accessCallbacks
     */
    public static void getNewsDetail(final String userId, final String password, String url, final AccessCallbacks accessCallbacks) {
        requestNewsDetail(userId, password, url, new AccessCallbacks() {
            @Override
            public void callback(String html, boolean bool) {
                accessCallbacks.callback(html, bool);
            }
        });
    }

    /************************************   private   ********************************************************/

    //**
    //region 時間割
    //**

    /***
     * 時間割のリクエストメソッド
     *
     * @param userId
     * @param password
     * @param listCallbacks
     */
    private static void requestTimeTable(final String userId,
                                         final String password,
                                         final AccessListCallbacks listCallbacks) {

        if (teacherHtmls != null) teacherHtmls.clear();

        Promise.with(App.getAppContext(), Void.class).thenOnAsyncThread(new Task<Void, Void>() {
            @Override
            public void run(Void aVoid, NextTask<Void> nextTask) {
                try {
                    HttpResultClass result = loginToESC(userId, password);

                    //failure
                    if (!result.getBool()) throw new Exception("ログインに失敗しました");

                    mLastResponseHtml = result.getString();
                    List<String> urls = getTeacherNameUrls(mLastResponseHtml);

                    teacherHtmls = requestTeacherName(urls);

                    //先生名取得失敗
                    if (teacherHtmls == null) throw new Exception("先生名の取得に失敗しました");

                    Log.d(TAG, mLastResponseHtml);
                    nextTask.run(null);
                } catch (Exception e) {
                    nextTask.fail(null, e);
                }
            }
        }).setCallback(new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listCallbacks.callback(mLastResponseHtml, teacherHtmls, true);
            }

            @Override
            public void onFailure(Bundle bundle, Exception e) {
                log(e);
                listCallbacks.callback(mLastResponseHtml, teacherHtmls, false);
            }
        }).create().execute(null);
    }

    /**
     * 引数のhtmlソースから先生名のurlを取り出すメソッド
     *
     * @param html
     * @return
     */
    private static List<String> getTeacherNameUrls(String html) {
        List<String> urls = new ArrayList<>();

        html = RegexUtil.replaceCRLF(html, true);
        Matcher m = RegexUtil.getGroupValues("<li class=\"letter\"><a href=\"(.+?)\">投書</a>", html);
        while (m.find()) {
            String url = m.group(1);
            urls.add(url);
        }
        return urls;
    }


    /***
     * 先生名のhtmlソースを取得するメソッド
     *
     * @param urls
     * @return
     */
    private static List<String> requestTeacherName(final List<String> urls) {

        final List<String> htmls = new ArrayList<>();

        for (String url : urls) {
            HttpResultClass result = HttpBase.httpGet(url, RequestURL.ESC_TO_PAGE);
            if (!result.getBool()) return null;

            htmls.add(result.getString());
        }
        return htmls;
    }

    //**
    //endregion
    //**


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

        Promise.with(App.getAppContext(), Void.class).thenOnAsyncThread(new Task<Void, Void>() {
            @Override
            public void run(Void aVoid, NextTask<Void> nextTask) {
                try {
                    //ログイン
                    HttpResultClass result = loginToYS(userId, password);

                    //failure
                    if (!result.getBool()) throw new Exception("ログインに失敗しました");

                    mLastResponseHtml = result.getString();
                    //出席率ページへリクエスト
                    Map<String, String> body = CreateRequestBody.createPostDataForRatePage(mLastResponseHtml);
                    result = HttpBase.httpPost(RequestURL.YS_TO_RATE_PAGE, body, RequestURL.ESC_LOGIN);

                    //failure
                    if (!result.getBool()) throw new Exception("出席率ページへの遷移に失敗しました");

                    mLastResponseHtml = result.getString();
                    Log.d(TAG, mLastResponseHtml);
                    if (!RegexUtil.containsCheck(">個人別出席率表<", mLastResponseHtml)) {
                        throw new Exception("出席率ページへの遷移に失敗しました");
                    }

                    nextTask.run(null);
                }catch(Exception e){
                    nextTask.fail(null,e);
                }

            }
        }).setCallback(new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                accessCallbacks.callback(mLastResponseHtml, true);
            }

            @Override
            public void onFailure(Bundle bundle, Exception e) {
                log(e);
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

    private static void requestNews(final String userId, final String password, final AccessCallbacks accessCallbacks) {

        Promise.with(App.getAppContext(), Void.class).thenOnAsyncThread(new Task<Void, Void>() {
            @Override
            public void run(Void aVoid, NextTask<Void> nextTask) {
                try{
                    //ログイン
                    HttpResultClass result = loginToESC(userId, password);

                    //failure
                    if (!result.getBool()) throw new Exception("ログインに失敗しました");

                    mLastResponseHtml = result.getString();
                    Log.d(TAG, mLastResponseHtml);
                    nextTask.run(null);
                }catch(Exception e){
                    nextTask.fail(null, e);
                }
            }
        }).setCallback(new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                accessCallbacks.callback(mLastResponseHtml, true);
            }

            @Override
            public void onFailure(Bundle bundle, Exception e) {
                log(e);
                accessCallbacks.callback(mLastResponseHtml, false);
            }
        }).create().execute(null);
    }

    private static void requestNewsDetail(final String userId, final String password, final String url, final AccessCallbacks accessCallbacks) {
        Promise.with(App.getAppContext(), Void.class).thenOnAsyncThread(new Task<Void, Void>() {
            @Override
            public void run(Void aVoid, NextTask<Void> nextTask) {
                try{
                    //ログイン
                    HttpResultClass result = loginToESC(userId, password);
                    if (!result.getBool()) throw new Exception("ログインに失敗しました");

                    mLastResponseHtml = result.getString();

                    result = HttpBase.httpGet(url, RequestURL.ESC_TIME_TABLE_PAGE);
                    if (!result.getBool()) throw new Exception("ニュース詳細の取得に失敗しました");

                    mLastResponseHtml = result.getString();
                    Log.d(TAG, mLastResponseHtml);
                    nextTask.run(null);
                }catch (Exception e){
                    nextTask.fail(null, e);
                }

            }
        }).setCallback(new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                accessCallbacks.callback(mLastResponseHtml, true);
            }

            @Override
            public void onFailure(Bundle bundle, Exception e) {
                log(e);
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
     * EccStudentCommunication にログインするメソッド
     *
     * @param userId
     * @param password
     * @return
     */
    private static HttpResultClass loginToESC(String userId, String password) {

        HttpResultClass result = HttpBase.httpGet(RequestURL.ESC_TO_PAGE,
                RequestURL.DEFAULT_REFERRER);

        if (!result.getBool()) return result;

        mLastResponseHtml = result.getString();
        //create requestBody with map
        Map<String, String> body = CreateRequestBody.createPostDataForEscLogin(userId, password, mLastResponseHtml);
        //login
        result = HttpBase.httpPost(RequestURL.ESC_LOGIN, body, RequestURL.ESC_TO_PAGE);

        if (!result.getBool()) return result;
        mLastResponseHtml = result.getString();
        if (!RegexUtil.containsCheck(">ログアウト<", mLastResponseHtml)) {
            result.setBool(false);
        }

        return result;
    }

    /***
     * 山口学園学生サービスにログインするメソッド
     *
     * @param userId
     * @param password
     * @return
     */
    private static HttpResultClass loginToYS(String userId, String password) {
        HttpResultClass result = HttpBase.httpGet(RequestURL.YS_TO_PAGE, RequestURL.DEFAULT_REFERRER);
        try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}

        //failure
        if (!result.getBool()) return result;

        mLastResponseHtml = result.getString();
        Map<String, String> body = CreateRequestBody.createPostDataForYSLogin(userId, password, mLastResponseHtml);
        result = HttpBase.httpPost(RequestURL.YS_LOGIN, body, RequestURL.YS_TO_PAGE);
        try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}

        //failure
        if (!result.getBool()) return result;

        mLastResponseHtml = result.getString();
        if (!RegexUtil.containsCheck("ログオフ", mLastResponseHtml)) {
            result.setBool(false);
        }

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

    public interface AccessListCallbacks {
        void callback(String html, List<String> htmls, boolean bool);
    }

    public interface SuccessCallbacks {
        void callback(boolean bool);
    }

    //**
    //endregion
    //**
}
