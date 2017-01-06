package jp.yuta.kohashi.esc.network;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.anprosit.android.promise.Callback;
import com.anprosit.android.promise.NextTask;
import com.anprosit.android.promise.Promise;
import com.anprosit.android.promise.Task;

import java.util.Map;

import jp.yuta.kohashi.esc.preference.SaveManager;

/**
 * Created by yutakohashi on 2016/11/15.
 */

//ヘルパ内で保存処理を行う
public class HttpHelper {
    private static final String TAG = HttpHelper.class.getSimpleName();

    private CreateRequestBody requestBody;
    private String mLastResponseHtml;
    private HttpBase httpBase;
    private Context mContext;

    HttpHelper(Context context) {
        requestBody = new CreateRequestBody();
        httpBase = new HttpBase();
        mLastResponseHtml = "";
        mContext = context;
    }

    /***
     * 時間割を取得,保存
     *
     * @param userId
     * @param password
     * @param successCallbacks
     */
    public void getTImetable(String userId, String password, final SuccessCallbacks successCallbacks) {
        requestTimeTable(userId, password, new AccessCallbacks() {
            @Override
            public void callback(String html, boolean bool) {
                if (bool) {
                    SaveManager.saveTimeTable(mContext, html);
                    successCallbacks.callback(true);
                } else {
                    successCallbacks.callback(false);
                }
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
    public void getAttendanceRate(String userId, String password, final SuccessCallbacks successCallbacks) {
        requestAttendanceRate(userId, password, new AccessCallbacks() {
            @Override
            public void callback(String html, boolean bool) {
                if (bool) {
                    SaveManager.saveAttendanceRate(mContext, html);
                    successCallbacks.callback(true);
                } else {
                    successCallbacks.callback(false);
                }
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
    public void getTimeAttend(final String userId, final String password, final SuccessCallbacks successCallBacks) {
        getTImetable(userId, password, new SuccessCallbacks() {
            @Override
            public void callback(boolean bool) {
                if (bool) {
                    getAttendanceRate(userId, password, new SuccessCallbacks() {
                        @Override
                        public void callback(boolean bool) {
                            successCallBacks.callback(bool);
                        }
                    });
                }
            }
        });
    }

    /***
     * 時間割のリクエストメソッド
     *
     * @param userId
     * @param password
     * @param accessCallbacks
     */
    private void requestTimeTable(final String userId,
                                  final String password,
                                  final AccessCallbacks accessCallbacks) {

        Promise.with(this, Void.class).thenOnAsyncThread(new Task<Void, Void>() {
            @Override
            public void run(Void aVoid, NextTask<Void> nextTask) {
                //to login page
                HttpResultClass result = httpBase.httpGet(RequestURL.ESC_TO_PAGE,
                        RequestURL.DEFAULT_REFERRER);

                mLastResponseHtml = result.getString();
                //create RequstBody with map
                Map<String, String> body = requestBody.createPostDataForEscLogin(userId, password, mLastResponseHtml);
                //login
                result = httpBase.httpPost(RequestURL.ESC_LOGIN, body, RequestURL.ESC_TO_PAGE);
                if (!result.getBool()) failureCallback(accessCallbacks);

                mLastResponseHtml = result.getString();
                Log.d(TAG,mLastResponseHtml);
            }
        }).setCallback(new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                successCallback(accessCallbacks);
            }

            @Override
            public void onFailure(Bundle bundle, Exception e) {
                log(e);
                failureCallback(accessCallbacks);
            }
        }).create().execute(null);
    }


    /***
     * 出席率リクエスト　メソッド
     *
     * @param userId
     * @param password
     * @param accessCallbacks
     */
    private void requestAttendanceRate(final String userId, final String password, final AccessCallbacks accessCallbacks) {

        Promise.with(this, Void.class).thenOnAsyncThread(new Task<Void, Void>() {
            @Override
            public void run(Void aVoid, NextTask<Void> nextTask) {
                HttpResultClass result = httpBase.httpGet(RequestURL.YS_TO_PAGE, RequestURL.DEFAULT_REFERRER);
                mLastResponseHtml = result.getString();
                if (!result.getBool()) {
                    failureCallback(accessCallbacks);
                }
                Map<String, String> body = requestBody.createPostDataForYSLogin(userId, password, mLastResponseHtml);
                result = httpBase.httpPost(RequestURL.YS_LOGIN, body, RequestURL.YS_TO_PAGE);
                mLastResponseHtml = result.getString();
                if (!result.getBool()) {
                    failureCallback(accessCallbacks);
                }

                body = requestBody.createPostDataForRatePage(mLastResponseHtml);
                result = httpBase.httpPost(RequestURL.YS_TO_RATE_PAGE, body, RequestURL.ESC_LOGIN);
                mLastResponseHtml = result.getString();
                Log.d(TAG,mLastResponseHtml);
                if (result.getBool()) failureCallback(accessCallbacks);
            }
        }).setCallback(new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                successCallback(accessCallbacks);
            }

            @Override
            public void onFailure(Bundle bundle, Exception e) {
                log(e);
                failureCallback(accessCallbacks);
            }
        }).create().execute(null);
    }

    // 失敗時コールバック
    private void failureCallback(AccessCallbacks accessCallbacks) {
        accessCallbacks.callback(mLastResponseHtml, false);
    }

    // 成功時のコールバック
    private void successCallback(AccessCallbacks accessCallbacks) {
        accessCallbacks.callback(mLastResponseHtml, true);
    }

    // print LOG
    private void log(Exception e) {
        Log.d("ログ", e.toString());
    }

    /**
     * インターフェース
     */
    public interface AccessCallbacks {
        void callback(String html, boolean bool);
    }

    public interface SuccessCallbacks {
        void callback(boolean bool);
    }
}
