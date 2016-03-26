package com.example.yutathinkpad.esc.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anprosit.android.promise.Callback;
import com.anprosit.android.promise.NextTask;
import com.anprosit.android.promise.Promise;
import com.anprosit.android.promise.Task;
import com.example.yutathinkpad.esc.activity.MainActivity;
import com.example.yutathinkpad.esc.tools.GetValuesBase;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Yuta on 2016/03/26.
 */
public class LoginManager {

    static final String URL1 = "http://comp2.ecc.ac.jp/sutinfo/login";          //ログイン画面
    static final String URL2 = "http://comp2.ecc.ac.jp/sutinfo/auth/attempt";   //実ログイン
    static final String URL3 ="http://comp2.ecc.ac.jp/sutinfo/logout";

    static final String TAG="loginManager::::";

    ProgressDialog dialog;
    OkHttpClient client;
    String mLastResponse;
    int weekCount;
    boolean returnbln;
    String returnstr;
    CookieManager cookieManager;
    CookieJar cookieJar;
    GetValuesBase getValuesBase;


    /**
     *
     * @param context
     * @return
     */
    public void loginManager(final Context context, final View view, String Id, String pass){

        returnstr = "";
        returnbln = false;
        final String userId = Id;
        final String password = pass;
        final Context context1 = context;

        dialog = new ProgressDialog(context);
        cookieManager = new CookieManager();
        cookieJar = new JavaNetCookieJar(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        getValuesBase = new GetValuesBase();


        client = new OkHttpClient.Builder()
                //.proxy(p)
                .cookieJar(cookieJar)
                .build();

        Promise.with(this,String.class).then(new Task<String,String>(){
            @Override
            public void run(String s, NextTask<String> nextTask) {
                // ダイアログを表示
                dialog.setMessage("メッセージ");
                dialog.setTitle("タイトル");
                dialog.show();
                nextTask.run(null);
            }

        }).thenOnAsyncThread(new Task<String, String>() {
            @Override
            public void run(String result, NextTask<String> nextTask) {
                // ログイン画面へGET通信
                Request request = new Request.Builder()
                        .url(URL1)
                        .build();
                Response response = null;
                try {
                    // ログインページヘ
                    response = client.newCall(request).execute();
                    Thread.sleep(500);
                    mLastResponse = response.body().string();
                } catch (IOException |InterruptedException e) {
                    e.printStackTrace();
                }

                int respCode = response.code();
                Log.d("response::::",String.valueOf(respCode));



                // トークンの抽出処理
                String token = getValuesBase.GetToken(mLastResponse);
                Log.d(TAG,token);
                if("ERROR".equals(token)){
                    //例外処理
                    Log.d(TAG,"トークン取得失敗");
                }

                // ESCへPOST
                // パラメータ
                RequestBody requestBody = new FormBody.Builder()
                        .add("_token", token)
                        .add("userid", userId)
                        .add("password", password)
                        .build();

                // リクエストオブジェクトを作成
                request = new Request.Builder()
                        .url(URL2)
                        .post(requestBody)
                        .addHeader("Referer","http://comp2.ecc.ac.jp/sutinfo/login")
                        .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87")
                        .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .build();

                try {
                    Log.d(TAG,"POST");
                    response = client.newCall(request).execute();
                    Thread.sleep(500);

                    mLastResponse = response.body().string();
                } catch (IOException |InterruptedException e) {
                    Log.d(TAG, "POST失敗");
                    e.printStackTrace();
                    Toast.makeText(context1,"通信に失敗しました",Toast.LENGTH_LONG).show();
                }

                returnstr = mLastResponse;
                nextTask.run(mLastResponse);

            }

        }).thenOnAsyncThread(new Task<String, String>() {
            @Override
            public void run(String result, NextTask<String> nextTask) {
                // ログアウト処理
                mLastResponse = result;

                Request request = new Request.Builder()
                        .url(URL3)
                        .build();
                //Response response;
                try {
                    // ログインページヘ
                    client.newCall(request).execute();
                    Thread.sleep(1500);
                    //mLastResponse = response.body().string();
                } catch (IOException|InterruptedException e) {
                    e.printStackTrace();
                }


                nextTask.run(mLastResponse);
            }

        }).setCallback(new Callback<String>() {

            @Override
            public void onSuccess(String s) {
                dialog.dismiss();
                UpdateTimeTableManager utt = new UpdateTimeTableManager();
                utt.upDateTimeTableForLogin(context,userId,password);

            }

            @Override
            public void onFailure(Bundle bundle, Exception e) {
                dialog.dismiss();
                returnstr = "false";
                Snackbar.make(view,"通信に失敗しました",Snackbar.LENGTH_LONG).show();

            }

        }).create().execute(null);


    }
}
