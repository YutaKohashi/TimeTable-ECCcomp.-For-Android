package jp.yuta.kohashi.esc.network.api;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.yuta.kohashi.esc.network.api.model.Token;
import jp.yuta.kohashi.esc.network.api.model.news.NewsArray;
import jp.yuta.kohashi.esc.network.api.model.news.NewsDetail;
import jp.yuta.kohashi.esc.network.api.model.news.NewsDetailRoot;
import jp.yuta.kohashi.esc.network.api.model.news.NewsItem;
import jp.yuta.kohashi.esc.network.api.model.schedule.ScheduleRoot;
import jp.yuta.kohashi.esc.network.api.model.timeTable.RootTimeTable;
import jp.yuta.kohashi.esc.network.api.model.timeTable.TimeTable;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 保存処理も行う
 * Author : yutakohashi
 * Project name : ESC
 * Date : 25 / 04 / 2017
 */
public class EscApiManager {
    /**
     * トークンを保持
     */
    private static String token;
    private static EscJsonApi escJsonApi;

    /**
     * アプリケーションクラスで実行しトークンを取得しておく
     */
    public static void init() {
        String userId = PrefUtil.getUserName();
        String pass = PrefUtil.getPss();
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(pass)) return;
        postToken(userId, pass, tkn -> token = tkn);
    }

    private static EscJsonApi getEscJsonApi() {
        if (escJsonApi == null) escJsonApi = new EscJsonApi();
        return escJsonApi;
    }

    /**
     * @param userId
     * @param password
     * @param callback
     */
    public static void postToken(String userId, String password, EscApiCallback<String> callback) {

        getEscJsonApi().postToken(userId, password, new retrofit2.Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token tkn = response.body();
                if (tkn == null) callback.callback(null);
                else {
                    if (Const.ERROR_AUTH.equals(tkn.getCode())) callback.callback(null);
                    else {
                        token = tkn.getToken();
                        callback.callback(tkn.getToken());
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                callback.callback(null);
            }
        });
    }

    /**
     * 時間割を取得
     * passwordはtokenの取得に必要
     *
     * @param userId
     * @param password
     * @param callback
     */
    public static void getTimeTable(String userId, String password, EscApiCallback<List<TimeTable>> callback) {

        getEscJsonApi().getTimeTable(userId, token, new retrofit2.Callback<RootTimeTable>() {
            @Override
            public void onResponse(Call<RootTimeTable> call, Response<RootTimeTable> response) {
                RootTimeTable rootTimeTable = response.body();
                String code = response.body().getCode();
                if (Const.SUCCESS_AUTH.equals(code)) {
                    callback.callback(rootTimeTable.getTimeTables());

                } else if (Const.ERROR_EXPIRED_TOKEN.equals(code) || Const.ERROR_INVALID_TOKEN.equals(code)) {
                    //　トークンを取得し直す
                    postToken(userId, password, (tkn) -> {
                        if (!TextUtils.isEmpty(token)) {
                            getEscJsonApi().getTimeTable(userId, token, new retrofit2.Callback<RootTimeTable>() {
                                @Override
                                public void onResponse(Call<RootTimeTable> call, Response<RootTimeTable> response) {
                                    RootTimeTable rootTimeTable = response.body();
                                    String code = response.body().getCode();
                                    if (Const.SUCCESS_AUTH.equals(code)) {
                                        callback.callback(rootTimeTable.getTimeTables());
                                    } else {
                                        callback.callback(null);
                                    }
                                }

                                @Override
                                public void onFailure(Call<RootTimeTable> call, Throwable t) {
                                    callback.callback(null);
                                }
                            });
                        } else {
                            //　失敗
                            callback.callback(null);
                        }
                    });
                } else {
                    callback.callback(null);
                }
            }

            @Override
            public void onFailure(Call<RootTimeTable> call, Throwable t) {
                callback.callback(null);
            }
        });
    }


    /**
     * 学校からのお知らせを取得するメソッド
     *
     * @param userId
     * @param password
     * @param callback
     */
    public static void getSchoolNews(String userId, String password, EscApiCallback<List<NewsItem>> callback) {
        getEscJsonApi().getSchoolNews(token, Const.NEWS_LIMIT, new retrofit2.Callback<NewsArray>() {
            @Override
            public void onResponse(Call<NewsArray> call, Response<NewsArray> response) {
                NewsArray newsArray = response.body();
                if (Const.SUCCESS_AUTH.equals(newsArray.getCode())) {
                    callback.callback(newsArray.getNewsArray());
                } else {
                    //　トークン取得し直す
                    postToken(userId, password, (token -> {
                        if (!TextUtils.isEmpty(token)) {
                            getEscJsonApi().getSchoolNews(token, Const.NEWS_LIMIT, new retrofit2.Callback<NewsArray>() {
                                @Override
                                public void onResponse(Call<NewsArray> call, Response<NewsArray> response) {
                                    NewsArray newsArray = response.body();
                                    if (Const.SUCCESS_AUTH.equals(newsArray.getCode()))
                                        callback.callback(newsArray.getNewsArray());
                                    else callback.callback(null);
                                }

                                @Override
                                public void onFailure(Call<NewsArray> call, Throwable t) {
                                    callback.callback(null);
                                }
                            });
                        } else {
                            callback.callback(null);
                        }
                    }));
                }
            }

            @Override
            public void onFailure(Call<NewsArray> call, Throwable t) {
                callback.callback(null);
            }
        });
    }

    /**
     * 担任からのお知らせを取得するメソッド
     *
     * @param userId
     * @param password
     * @param callback
     */
    public static void getTaninNews(String userId, String password, EscApiCallback<List<NewsItem>> callback) {
        getEscJsonApi().getTaninNews(token, Const.NEWS_LIMIT, new retrofit2.Callback<NewsArray>() {
            @Override
            public void onResponse(Call<NewsArray> call, Response<NewsArray> response) {
                NewsArray newsArray = response.body();
                if (Const.SUCCESS_AUTH.equals(newsArray.getCode())) {
                    callback.callback(newsArray.getNewsArray());
                } else {
                    //　トークン取得し直す
                    postToken(userId, password, (token -> {
                        if (!TextUtils.isEmpty(token)) {
                            getEscJsonApi().getTaninNews(token, Const.NEWS_LIMIT, new retrofit2.Callback<NewsArray>() {
                                @Override
                                public void onResponse(Call<NewsArray> call, Response<NewsArray> response) {
                                    NewsArray newsArray = response.body();
                                    if (Const.SUCCESS_AUTH.equals(newsArray.getCode()))
                                        callback.callback(newsArray.getNewsArray());
                                    else callback.callback(null);
                                }

                                @Override
                                public void onFailure(Call<NewsArray> call, Throwable t) {
                                    callback.callback(null);
                                }
                            });
                        } else {
                            callback.callback(null);
                        }
                    }));
                }
            }

            @Override
            public void onFailure(Call<NewsArray> call, Throwable t) {
                callback.callback(null);
            }
        });
    }


    /**
     * お知らせ詳細を取得するメソッド
     *
     * @param newsId
     * @param userId
     * @param password
     * @param callback
     */
    public static void getNewsDetail(Integer newsId, String userId, String password, EscApiCallback<NewsDetail> callback) {
        getEscJsonApi().getNewsDetail(token, newsId, new retrofit2.Callback<NewsDetailRoot>() {
            @Override
            public void onResponse(Call<NewsDetailRoot> call, Response<NewsDetailRoot> response) {
                NewsDetailRoot newsDetailRoot = response.body();
                if (Const.SUCCESS_AUTH.equals(newsDetailRoot.getCode())) {
                    callback.callback(newsDetailRoot.getNewsDetail());
                } else {
                    postToken(userId, password, tkn -> {
                        if (!TextUtils.isEmpty(token)) {
                            getEscJsonApi().getNewsDetail(token, newsId, new retrofit2.Callback<NewsDetailRoot>() {
                                @Override
                                public void onResponse(Call<NewsDetailRoot> call, Response<NewsDetailRoot> response) {
                                    NewsDetailRoot newsDetailRoot = response.body();
                                    if (Const.SUCCESS_AUTH.equals(newsDetailRoot.getCode())) {
                                        callback.callback(newsDetailRoot.getNewsDetail());
                                    } else {
                                        callback.callback(null);
                                    }
                                }

                                @Override
                                public void onFailure(Call<NewsDetailRoot> call, Throwable t) {
                                    callback.callback(null);
                                }
                            });
                        } else {
                            callback.callback(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<NewsDetailRoot> call, Throwable t) {
                callback.callback(null);
            }
        });
    }

    /**
     * スケジュールを取得する
     *
     * @param userId
     * @param password
     * @param callback
     */
    private static int month = 0;
    private static int year = 0;
    private static List<ScheduleRoot> scheduleRootList;

    public static void getSchedule(String userId, String password, EscApiCallback<List<ScheduleRoot>> callback) {
        if (month == 0 && year == 0) {
            month = 4;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            year = calendar.get(Calendar.YEAR);
            scheduleRootList = new ArrayList<>();
        }

        getEscJsonApi().getSchedule(token, year, month, new Callback<ScheduleRoot>() {
            @Override
            public void onResponse(Call<ScheduleRoot> call, Response<ScheduleRoot> response) {
                ScheduleRoot scheduleRoot = response.body();
                if (scheduleRoot != null && !Const.ERROR_INVALID_TOKEN.equals(scheduleRoot.getCode())) {
                    scheduleRootList.add(scheduleRoot);
                    month++;
                    if (month == 13) {
                        month = 1;
                        year += 1;
                    }
                    if (month == 4) {
                        callback.callback(scheduleRootList);
                        resetYearMonth();
                    } else {
                        getSchedule(userId, password, callback);
                    }
                } else {
                    postToken(userId, password, tkn -> {
                        if (!TextUtils.isEmpty(token)) {
                            getEscJsonApi().getSchedule(token, year, month, new Callback<ScheduleRoot>() {
                                @Override
                                public void onResponse(Call<ScheduleRoot> call, Response<ScheduleRoot> response) {
                                    ScheduleRoot scheduleRoot = response.body();
                                    if (scheduleRoot != null && !Const.ERROR_INVALID_TOKEN.equals(scheduleRoot.getCode())) {
                                        scheduleRootList.add(scheduleRoot);
                                        month++;
                                        if (month == 13) {
                                            month = 1;
                                            year += 1;
                                        }
                                        if (month == 4) {
                                            callback.callback(scheduleRootList);
                                            resetYearMonth();
                                        } else {
                                            getSchedule(userId, password, callback);
                                        }
                                    } else {
                                        callback.callback(null);
                                        resetYearMonth();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ScheduleRoot> call, Throwable t) {
                                    callback.callback(null);
                                    resetYearMonth();
                                }
                            });

                        } else {
                            callback.callback(null);
                            resetYearMonth();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ScheduleRoot> call, Throwable t) {
                callback.callback(null);
                resetYearMonth();
            }
        });
    }

    private static void resetYearMonth() {
        year = 0;
        month = 0;
        scheduleRootList = null;
    }
}


