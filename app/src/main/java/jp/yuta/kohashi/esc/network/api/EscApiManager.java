package jp.yuta.kohashi.esc.network.api;

import android.text.TextUtils;

import java.util.List;

import jp.yuta.kohashi.esc.network.api.model.Token;
import jp.yuta.kohashi.esc.network.api.model.timeTable.RootTimeTable;
import jp.yuta.kohashi.esc.network.api.model.timeTable.TimeTable;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;
import retrofit2.Call;
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

    /**
     * アプリケーションクラスで実行しトークンを取得しておく
     */
    public static void init(){
        String userId = PrefUtil.getUserName();
        String pass = PrefUtil.getPss();
        if(userId.equals("") || pass.equals("")) return;
        postToken(userId, pass, new TokenCallback() {
            @Override
            public void callback(String tkn) {
                token = tkn;
            }
        });
    }

    /**
     *
     * @param userId
     * @param password
     * @param callback
     */
    public static void postToken(String userId, String password,TokenCallback callback){

        new EscJsonApi().postToken(userId, password, new retrofit2.Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token token = response.body();
                if(token == null) callback.callback(null);
                else {
                    if(token.getCode().equals(Const.ERROR_AUTH)) callback.callback(null);
                    else callback.callback(token.getToken());
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
     * @param userId
     * @param password
     * @param callback
     */
    public static void getTimeTable(String userId, String password, TimeTableCallback callback) {
        new EscJsonApi().getTimeTable(userId, token, new retrofit2.Callback<RootTimeTable>() {
            @Override
            public void onResponse(Call<RootTimeTable> call, Response<RootTimeTable> response) {
                RootTimeTable rootTimeTable = response.body();
                String code = response.body().getCode();
                if (code.equals(Const.SUCCESS_GET_TIMETABLE)) {
                    callback.callback(rootTimeTable.getTimeTables());

                } else if (code.equals(Const.ERROR_EXPIRED_TOKEN) || code.equals(Const.ERROR_INVALID_TOKEN)) {
                    //　トークンを取得し直す
                    postToken(userId,password,(token)->{
                        if (!TextUtils.isEmpty(token)) {
                            new EscJsonApi().getTimeTable(userId, token, new retrofit2.Callback<RootTimeTable>() {
                                @Override
                                public void onResponse(Call<RootTimeTable> call, Response<RootTimeTable> response) {
                                    RootTimeTable rootTimeTable = response.body();
                                    String code = response.body().getCode();
                                    if (code.equals(Const.SUCCESS_GET_TIMETABLE)) {
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



    /*******************************************************************************
    /**
     * 保存処理
     */
    private void saveTimeTable(RootTimeTable rootTimeTable){

    }

    public interface Callback{
        void callback(boolean isSuccess);
    }

    /**
     * 時間割コールバック
     */
    public interface  TimeTableCallback{
        void callback(List<TimeTable> timeTableList);
    }

    interface TokenCallback{
        void callback(String tkn);
    }

}
