package jp.yuta.kohashi.esc.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.anprosit.android.promise.Callback;
import com.anprosit.android.promise.NextTask;
import com.anprosit.android.promise.Promise;
import com.anprosit.android.promise.Task;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import jp.yuta.kohashi.esc.preference.SaveManager;
import jp.yuta.kohashi.esc.tools.GetValuesBase;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yuta on 2016/05/06.
 */
public class GetTeacherNameManager {
    final String PREF_NAME ="sample";
    final String PREF_TEACHERS_KEY = "teachers";

    String mLastResponse;

    SharedPreferences pref;

    List<String> teacherNames;
    SaveManager saveManager;
    String subjectHtml;

    CookieManager cookieManager;
    CookieJar cookieJar;
    GetValuesBase getValuesBase;
    OkHttpClient client;

//    OkHttpClient

    /**
     *
     * @param html      時間割ページのhtmlソース
     * @param context   コンテキスト
     */
    public void getTeacherNames(String html, Context context){

        //htmlに時間割ページのhtmlソースを入れる


        //教科個別のページにアクセスし先生の名前を取得
        //プリファレンスをインスタンス化
//        pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);

        teacherNames = new ArrayList<>();
        cookieManager = new CookieManager();
        cookieJar = new JavaNetCookieJar(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        getValuesBase = new GetValuesBase();
        client = new OkHttpClient.Builder()
                //.proxy(p)
                .cookieJar(cookieJar)
                .build();
        //************ 解析処理 ***************************************
        getValuesBase = new GetValuesBase();
        //教科個別のページへアクセスするURLをMatcher型で取得
        Matcher match = getValuesBase.GetGropValues("<li class=\"letter\"><a href=\"(.*?)\">",html);

        while(match.find()){
            subjectHtml = "";
            subjectHtml = match.group(1);
//
//            new AsyncTask<String,Void,String>(){
//                @Override
//                protected String doInBackground(String... strings) {
//                    Request request = new Request.Builder()
//                            .url(subjectHtml)
//                            .build();
//                    Response response;
//                    try{
//                        response = client.newCall(request).execute();
//                        Thread.sleep(500);
//
//                        mLastResponse = response.body().string();
//                    }catch(IOException | InterruptedException e){
//                        e.printStackTrace();
//                    }
//                    return mLastResponse;
//                }
//
//                @Override
//                protected void onPostExecute(String result) {
//                    super.onPostExecute(result);
//                    //先生の名前を取り出す
//                    String teacherName = getTeacherName(result);
//                    teacherNames.add(teacherName);
//
//                }
//            }.execute();
            Promise.with(this, String.class).then(new Task<String, String>() {
                @Override
                public void run(String s, NextTask<String> nextTask) {
                    nextTask.run(null);
                }
            }).thenOnAsyncThread(new Task<String, String>() {
                @Override
                public void run(String s, NextTask<String> nextTask) {
                    Request request = new Request.Builder()
                            .url(subjectHtml)
                            .build();
                    Response response;
                    try {
                        response = client.newCall(request).execute();
                        Thread.sleep(500);

                        mLastResponse = response.body().string();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    nextTask.run(mLastResponse);
                }

            }).setCallback(new Callback<String>() {
                @Override
                public void onSuccess(String result) {
                    String teacherName = getTeacherName(result);
                    teacherNames.add(teacherName);
                }

                @Override
                public void onFailure(Bundle bundle, Exception e) {

                }
            }).create().execute(null);
        }

        //作成された先生の名前のコレクションをプリファレンスに保存
        saveManager = new SaveManager();
        saveManager.saveMangerWithPreference(context,PREF_NAME,teacherNames,PREF_TEACHERS_KEY);
    }

    /**
     *
     * @param html 教科個別のページ
     * @return 先生名
     */
    private String getTeacherName(String html){
        //先生の名前を格納する変数
        String teacherName = "";

        getValuesBase = new GetValuesBase();

        teacherName = getValuesBase.GetValues("<h3>受信者</h3><p>(.*?)</p>",html,true);

        return teacherName;
    }
}
