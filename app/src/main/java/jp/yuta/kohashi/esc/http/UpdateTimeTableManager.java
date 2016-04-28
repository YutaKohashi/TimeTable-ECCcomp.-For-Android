package jp.yuta.kohashi.esc.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anprosit.android.promise.Callback;
import com.anprosit.android.promise.NextTask;
import com.anprosit.android.promise.Promise;
import com.anprosit.android.promise.Task;
import com.dd.CircularProgressButton;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.activity.MainActivity;
import jp.yuta.kohashi.esc.object.AttendanceRateObject;
import jp.yuta.kohashi.esc.preference.SaveManager;
import jp.yuta.kohashi.esc.object.TimeBlock;
import jp.yuta.kohashi.esc.tools.CreateTimeTableLists;

import jp.yuta.kohashi.esc.tools.CustomProgressDialog;
import jp.yuta.kohashi.esc.tools.GetValuesBase;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import jp.yuta.kohashi.esc.stab;

/**
 * Created by YutaThinkPad on 2016/03/23.
 */
public class UpdateTimeTableManager {



    static final String URL1 = "http://comp2.ecc.ac.jp/sutinfo/login";          //ログイン画面
    static final String URL2 = "http://comp2.ecc.ac.jp/sutinfo/auth/attempt";   //実ログイン
    static final String URL3 ="http://comp2.ecc.ac.jp/sutinfo/logout";

    static final String URL4 = "http://school4.ecc.ac.jp/eccstdweb/st0100/st0100_01.aspx";
    static final String URL5 = "http://school4.ecc.ac.jp/eccstdweb/st0100/st0100_01.aspx";
    static final String URL6 = "http://school4.ecc.ac.jp/EccStdWeb/ST0100/ST0100_02.aspx";
    static final String PREF_NAME ="sample";
    static final String PREF_NAME_ID_PASS = "ip";
//
//    static final String userId = "2140257";
//    static final String password = "455478";
    final String TAG ="error:::";

    //月曜日から金曜日の曜日ごとのリスト
    List<TimeBlock> MondayList;
    List<TimeBlock> TuesdayList;
    List<TimeBlock> WednesdayList;
    List<TimeBlock> ThursdayList;
    List<TimeBlock> FridayList;

    ProgressDialog dialog;
    OkHttpClient client;
    static String mLastResponse;
    int weekCount;



    CookieManager cookieManager;
   CookieJar cookieJar;
    GetValuesBase getValuesBase;

    SaveManager saveManager;
    List<AttendanceRateObject> attendanceRateList;
    ProgressDialog progressDialog;
    CustomProgressDialog customDialog;

    public void upDateTimeTable(final Context context, final View v, final String userId, final String password){

        final String userId2 = userId;
        final String password2 = password;

        MondayList = new ArrayList<>();
        TuesdayList = new ArrayList<>();
        WednesdayList = new ArrayList<>();
        ThursdayList = new ArrayList<>();
        FridayList = new ArrayList<>();


        dialog = new ProgressDialog(context);

        cookieManager = new CookieManager();
        cookieJar = new JavaNetCookieJar(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        getValuesBase = new GetValuesBase();

        client = new OkHttpClient.Builder()
                //.proxy(p)
                .cookieJar(cookieJar)
                .build();

       customDialog = new CustomProgressDialog();
        Promise.with(this,String.class).then(new Task<String,String>(){
            @Override
            public void run(String s, NextTask<String> nextTask) {
               //  ダイアログを表示
                if (progressDialog == null) {
                    progressDialog = customDialog.createProgressDialogForTimeTableUpdate(context);
                    progressDialog.show();
                } else {
                    progressDialog.show();
                }

                if(!getValuesBase.ConnectionCheck(context)){
                    progressDialog.dismiss();
                    Snackbar.make(v,"インターネットに接続されていません",Snackbar.LENGTH_LONG).show();
                    return;
                }
                nextTask.run(null);



            }

        }).thenOnAsyncThread(new Task<String, String>() {
            @Override
            public void run(String result, NextTask<String> nextTask) {
                // ログインページヘ
                try {
                    request = new Request.Builder()
                            .url(URL1)
                            .build();

                    response = client.newCall(request).execute();
                    Thread.sleep(500);
                    mLastResponse = response.body().string();
                } catch (UnknownHostException e) {
                    Toast.makeText(context, "通信に失敗しました", Toast.LENGTH_LONG).show();
                    Log.d("okhttp::::", "通信に失敗しました");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();

                }
                nextTask.run(mLastResponse);
            }

        }).thenOnAsyncThread( new Task<String, String>() {


            @Override
            public void run(String s, NextTask<String> nextTask) {
                // トークンの抽出処理
                String token = getValuesBase.GetToken(mLastResponse);
                Log.d(TAG,token);
                if("ERROR".equals(token)){
                    //例外処理
                    Log.d(TAG,"トークン取得失敗");
                }
                nextTask.run(token);
            }

        }).thenOnAsyncThread(new Task<String, String>() {

            @Override
            public void run(String token, NextTask<String> nextTask) {

                // ESCへPOST
                // パラメータ
                RequestBody requestBody = new FormBody.Builder()
                        .add("_token", token)
                        .add("userid", userId2)
                        .add("password", password2)
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

                    //名前を保存
                    String name = getValuesBase.GetValues(" id=\"user_name\" class=\".*?\">(.+?)さん</li>",mLastResponse);
                    name = name.replaceAll("&nbsp;","");
                    Log.d("名前:::",name);
                    SharedPreferences pref = context.getSharedPreferences("username", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username",name);
                    editor.apply();

                } catch (IOException |InterruptedException e) {
                    Log.d(TAG, "POST失敗");
                    e.printStackTrace();
                    Toast.makeText(context,"通信に失敗しました",Toast.LENGTH_LONG).show();
                }

                nextTask.run(mLastResponse);
            }

        }).thenOnAsyncThread(new Task<String, String>() {

            @Override
            public void run(String s, NextTask<String> nextTask) {
                // トークンの抽出処理
                String token = getValuesBase.GetToken(mLastResponse);
                Log.d(TAG,token);
                if("ERROR".equals(token)){
                    //例外処理
                    Log.d(TAG,"トークン取得失敗");
                }
                //btn.setProgress(70);
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
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                nextTask.run(mLastResponse);
            }
        }).setCallback(new Callback<String>() {
            @Override

            public void onSuccess(String result){
                mLastResponse = result;
                Pattern pattern1 = Pattern.compile("<td>(.+?)</td>");
                Matcher matcher1;

                //1時限目のHTMLソース
                weekCount = 0;
                result= getValuesBase.NarrowingValues("<tr><thclass=\"term\">1","<thclass=\"term\">2</th>",mLastResponse,true);
                CreateTimeTableLists createList = new CreateTimeTableLists();
                matcher1 = pattern1.matcher(result);
                while(matcher1.find()) {
                    TimeBlock timeBlock =createList.CreateTimeTableList(matcher1.group());
                    switch (weekCount) {
                        case 0:
                            MondayList.add(timeBlock);
                            break;
                        case 1:
                            TuesdayList.add(timeBlock);
                            break;
                        case 2:
                            WednesdayList.add(timeBlock);
                            break;
                        case 3:
                            ThursdayList.add(timeBlock);
                            break;
                        case 4:
                            FridayList.add(timeBlock);
                            break;
                    }
                    weekCount++;
                }

                //2時限目のHTMLソース
                weekCount = 0;
                result= getValuesBase.NarrowingValues("<thclass=\"term\">2</th>","<thclass=\"term\">3</th>",mLastResponse,true);
                matcher1 = pattern1.matcher(result);
                while(matcher1.find()) {
                    TimeBlock timeBlock =createList.CreateTimeTableList(matcher1.group());
                    switch (weekCount) {
                        case 0:
                            MondayList.add(timeBlock);
                            break;
                        case 1:
                            TuesdayList.add(timeBlock);
                            break;
                        case 2:
                            WednesdayList.add(timeBlock);
                            break;
                        case 3:
                            ThursdayList.add(timeBlock);
                            break;
                        case 4:
                            FridayList.add(timeBlock);
                            break;
                    }
                    weekCount++;
                }

                //3時限目のHTMLソース
                weekCount = 0;
                result= getValuesBase.NarrowingValues("<thclass=\"term\">3</th>","<thclass=\"term\">4</th>",mLastResponse,true);
                matcher1 = pattern1.matcher(result);
                while(matcher1.find()) {
                    TimeBlock timeBlock =createList.CreateTimeTableList(matcher1.group());
                    switch (weekCount) {
                        case 0:
                            MondayList.add(timeBlock);
                            break;
                        case 1:
                            TuesdayList.add(timeBlock);
                            break;
                        case 2:
                            WednesdayList.add(timeBlock);
                            break;
                        case 3:
                            ThursdayList.add(timeBlock);
                            break;
                        case 4:
                            FridayList.add(timeBlock);
                            break;
                    }
                    weekCount++;
                }

                //4時限目のHTMLソース
                weekCount = 0;
                result= getValuesBase.NarrowingValues("<thclass=\"term\">4</th>","<h2>担任からのお知らせ</h2>",mLastResponse,true);
                matcher1 = pattern1.matcher(result);
                while(matcher1.find()) {
                    TimeBlock timeBlock =createList.CreateTimeTableList(matcher1.group());
                    switch (weekCount) {
                        case 0:
                            MondayList.add(timeBlock);
                            break;
                        case 1:
                            TuesdayList.add(timeBlock);
                            break;
                        case 2:
                            WednesdayList.add(timeBlock);
                            break;
                        case 3:
                            ThursdayList.add(timeBlock);
                            break;
                        case 4:
                            FridayList.add(timeBlock);
                            break;
                    }
                    weekCount++;
                }

                /********************** 以上でリスト完成 **********************/
                /******************* 以下データベース登録処理 ******************/

                saveManager = new SaveManager();
                saveManager.saveMangerWithPreference(context, PREF_NAME,MondayList,"monList");
                saveManager.saveMangerWithPreference(context, PREF_NAME,TuesdayList,"tueList");
                saveManager.saveMangerWithPreference(context, PREF_NAME,WednesdayList,"wedList");
                saveManager.saveMangerWithPreference(context, PREF_NAME,ThursdayList,"thurList");
                saveManager.saveMangerWithPreference(context, PREF_NAME,FridayList,"friList");

                getTimeTable = true;
                progressDialog.dismiss();
                Snackbar.make(v,"更新しました",Snackbar.LENGTH_SHORT).show();


                //dialog.dismiss();
            }

            @Override
            public void onFailure(Bundle bundle, Exception e) {
                progressDialog.dismiss();

                Snackbar.make(v,"更新に失敗しました",Snackbar.LENGTH_SHORT).show();
                //dialog.dismiss();
            }

        }).create().execute(null);

    }

    /******************************** ログイン時に使用   *********************************************************/

    // ログイン画面へGET通信
    Request request;
    Response response;
    CircularProgressButton btn;
    View viewStart;
    boolean getTimeTable = false;
    String name ="no_name";
    /**
     *
     * @param context  コンテキスト
     * @param v        layout
     * @param userId   学籍番号
     * @param password パスワード
     */
    public void upDateTimeTableForLogin(final Context context, final View v, final String userId, final String password){

        final String userId2 = userId;
        final String password2 = password;
        MondayList = new ArrayList<>();
        TuesdayList = new ArrayList<>();
        WednesdayList = new ArrayList<>();
        ThursdayList = new ArrayList<>();
        FridayList = new ArrayList<>();

        dialog = new ProgressDialog(context);

        cookieManager = new CookieManager();
        cookieJar = new jp.yuta.kohashi.esc.http.JavaNetCookieJar(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        getValuesBase = new GetValuesBase();


        btn =  (CircularProgressButton)v.findViewById(R.id.login_btn);

        try{
            client = new OkHttpClient.Builder()
                    //.proxy(p)
                    .cookieJar(cookieJar)
                    .build();

        }catch(Exception e){
            Log.d("okhttp::::","通信に失敗しました");
        }



        Promise.with(this,String.class).then(new Task<String,String>(){
            @Override
            public void run(String s, NextTask<String> nextTask) {
                // ダイアログを表示
//                dialog.setMessage("メッセージ");
//                dialog.setTitle("タイトル");.
//                dialog.show();
                if(!getValuesBase.ConnectionCheck(context)){
                    Snackbar.make(v,"インターネットに接続されていません",Snackbar.LENGTH_LONG).show();
                    return;
                }
                nextTask.run(null);



            }

        }).thenOnAsyncThread(new Task<String, String>() {
            @Override
            public void run(String result, NextTask<String> nextTask) {
                // ログインページヘ
                try {
                    request = new Request.Builder()
                            .url(URL1)
                            .build();

                    response = client.newCall(request).execute();
                    Thread.sleep(500);
                    mLastResponse = response.body().string();
                } catch (UnknownHostException e) {
                    Toast.makeText(context, "通信に失敗しました", Toast.LENGTH_LONG).show();
                    Log.d("okhttp::::", "通信に失敗しました");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();

                }
                nextTask.run(mLastResponse);
            }

        }).thenOnAsyncThread( new Task<String, String>() {


            @Override
            public void run(String s, NextTask<String> nextTask) {
            // トークンの抽出処理
                String token = getValuesBase.GetToken(mLastResponse);
                Log.d(TAG,token);
                if("ERROR".equals(token)){
                    //例外処理
                    Log.d(TAG,"トークン取得失敗");
                }
                nextTask.run(token);
        }

        }).thenOnAsyncThread(new Task<String, String>() {

            @Override
            public void run(String token, NextTask<String> nextTask) {

                // ESCへPOST
                // パラメータ
                RequestBody requestBody = new FormBody.Builder()
                        .add("_token", token)
                        .add("userid", userId2)
                        .add("password", password2)
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

                    //名前を保存
                    name = getValuesBase.GetValues(" id=\"user_name\" class=\".*?\">(.+?)さん</li>",mLastResponse);
                    name = name.replaceAll("&nbsp;","");
                    Log.d("名前:::",name);
                    SharedPreferences pref = context.getSharedPreferences("username", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username",name);
                    editor.apply();

                } catch (IOException |InterruptedException e) {
                    Log.d(TAG, "POST失敗");
                    e.printStackTrace();
                    Toast.makeText(context,"通信に失敗しました",Toast.LENGTH_LONG).show();
                }

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
                    // ログアウト
                    client.newCall(request).execute();
                    Thread.sleep(1500);
                    //mLastResponse = response.body().string();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                nextTask.run(mLastResponse);
            }
        }).thenOnAsyncThread(new Task<String, String>() {
            @Override
            public void run(String result, NextTask<String> nextTask) {
                mLastResponse = result;
                Pattern pattern1 = Pattern.compile("<td>(.+?)</td>");
                Matcher matcher1;

                //1時限目のHTMLソース
                weekCount = 0;
                result= getValuesBase.NarrowingValues("<tr><thclass=\"term\">1","<thclass=\"term\">2</th>",mLastResponse,true);
                CreateTimeTableLists createList = new CreateTimeTableLists();
                matcher1 = pattern1.matcher(result);
                while(matcher1.find()) {
                    TimeBlock timeBlock =createList.CreateTimeTableList(matcher1.group());
                    switch (weekCount) {
                        case 0:
                            MondayList.add(timeBlock);
                            break;
                        case 1:
                            TuesdayList.add(timeBlock);
                            break;
                        case 2:
                            WednesdayList.add(timeBlock);
                            break;
                        case 3:
                            ThursdayList.add(timeBlock);
                            break;
                        case 4:
                            FridayList.add(timeBlock);
                            break;
                    }
                    weekCount++;
                }

                //2時限目のHTMLソース
                weekCount = 0;
                result= getValuesBase.NarrowingValues("<thclass=\"term\">2</th>","<thclass=\"term\">3</th>",mLastResponse,true);
                matcher1 = pattern1.matcher(result);
                while(matcher1.find()) {
                    TimeBlock timeBlock =createList.CreateTimeTableList(matcher1.group());
                    switch (weekCount) {
                        case 0:
                            MondayList.add(timeBlock);
                            break;
                        case 1:
                            TuesdayList.add(timeBlock);
                            break;
                        case 2:
                            WednesdayList.add(timeBlock);
                            break;
                        case 3:
                            ThursdayList.add(timeBlock);
                            break;
                        case 4:
                            FridayList.add(timeBlock);
                            break;
                    }
                    weekCount++;
                }

                //3時限目のHTMLソース
                weekCount = 0;
                result= getValuesBase.NarrowingValues("<thclass=\"term\">3</th>","<thclass=\"term\">4</th>",mLastResponse,true);
                matcher1 = pattern1.matcher(result);
                while(matcher1.find()) {
                    TimeBlock timeBlock =createList.CreateTimeTableList(matcher1.group());
                    switch (weekCount) {
                        case 0:
                            MondayList.add(timeBlock);
                            break;
                        case 1:
                            TuesdayList.add(timeBlock);
                            break;
                        case 2:
                            WednesdayList.add(timeBlock);
                            break;
                        case 3:
                            ThursdayList.add(timeBlock);
                            break;
                        case 4:
                            FridayList.add(timeBlock);
                            break;
                    }
                    weekCount++;
                }

                //4時限目のHTMLソース
                weekCount = 0;
                result= getValuesBase.NarrowingValues("<thclass=\"term\">4</th>","<h2>担任からのお知らせ</h2>",mLastResponse,true);
                matcher1 = pattern1.matcher(result);
                while(matcher1.find()) {
                    TimeBlock timeBlock =createList.CreateTimeTableList(matcher1.group());
                    switch (weekCount) {
                        case 0:
                            MondayList.add(timeBlock);
                            break;
                        case 1:
                            TuesdayList.add(timeBlock);
                            break;
                        case 2:
                            WednesdayList.add(timeBlock);
                            break;
                        case 3:
                            ThursdayList.add(timeBlock);
                            break;
                        case 4:
                            FridayList.add(timeBlock);
                            break;
                    }
                    weekCount++;
                }

                /********************** 以上でリスト完成 **********************/
                /******************* 以下データベース登録処理 ******************/

                saveManager = new SaveManager();
                saveManager.saveMangerWithPreference(context, PREF_NAME,MondayList,"monList");
                saveManager.saveMangerWithPreference(context, PREF_NAME,TuesdayList,"tueList");
                saveManager.saveMangerWithPreference(context, PREF_NAME,WednesdayList,"wedList");
                saveManager.saveMangerWithPreference(context, PREF_NAME,ThursdayList,"thurList");
                saveManager.saveMangerWithPreference(context, PREF_NAME,FridayList,"friList");

                nextTask.run(mLastResponse);

            }
        }).thenOnMainThread(new Task<String, String>() {

            @Override
            public void run(String result, NextTask<String> nextTask) {
                getTimeTable = true;
                nextTask.run(mLastResponse);
            }

        }).thenOnAsyncThread(new Task<String, String>() {
            @Override
            public void run(String s, NextTask<String> nextTask) {
                Request request = new Request.Builder()
                        .url(URL4)
                        .addHeader("Referer","http://school4.ecc.ac.jp/eccstdweb/st0100/st0100_01.aspx")
                        .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87")
                        .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .build();
                Response response;
                try{
                    response = client.newCall(request).execute();
                    Thread.sleep(500);

                    mLastResponse = response.body().string();
                }catch(IOException | InterruptedException e){
                    e.printStackTrace();
                }

                String __LASTFOCUS = getValuesBase.GetValues("input type=\"hidden\" name=\"__LASTFOCUS\" id=\"__LASTFOCUS\" value=\"(.+?)\"",mLastResponse);
                //Log.d("content:;::",__LASTFOCUS);
                String __VIEWSTATE =  getValuesBase.GetValues("input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"(.+?)\"",mLastResponse);
                String __SCROLLPOSITIONX = getValuesBase.GetValues("input type=\"hidden\" name=\"__SCROLLPOSITIONX\" id=\"__SCROLLPOSITIONX\" value=\"(.+?)\"",mLastResponse);
                String __SCROLLPOSITIONY = getValuesBase.GetValues("input type=\"hidden\" name=\"__SCROLLPOSITIONY\" id=\"__SCROLLPOSITIONY\" value=\"(.+?)\"",mLastResponse);
                String __EVENTTARGET = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"(.+?)\"",mLastResponse);
                String __EVENTARGUMENT = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"(.+?)\"",mLastResponse);
                String __EVENTVALIDATION = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"(.+?)\"",mLastResponse);
                String ctl00$ContentPlaceHolder1$txtUserId = userId2;
               String ctl00$ContentPlaceHolder1$txtPassword = password2;
//                String ctl00$ContentPlaceHolder1$txtPassword = "3333";
                String ctl00$ContentPlaceHolder1$btnLogin = "ログイン";

                RequestBody requestBody2 = new FormBody.Builder()
                        .add("__LASTFOCUS",__LASTFOCUS)
                        .add("__VIEWSTATE",__VIEWSTATE)
                        .add("__SCROLLPOSITIONX",__SCROLLPOSITIONX)
                        .add("__SCROLLPOSITIONY",__SCROLLPOSITIONY)
                        .add("__EVENTTARGET",__EVENTTARGET)
                        .add("__EVENTARGUMENT",__EVENTARGUMENT)
                        .add("__EVENTVALIDATION",__EVENTVALIDATION)
                        .add("ctl00$ContentPlaceHolder1$txtUserId",ctl00$ContentPlaceHolder1$txtUserId)
                        .add("ctl00$ContentPlaceHolder1$txtPassword",ctl00$ContentPlaceHolder1$txtPassword)
                        .add("ctl00$ContentPlaceHolder1$btnLogin",ctl00$ContentPlaceHolder1$btnLogin)
                        .build();

                request = new Request.Builder()
                        .url(URL5)
                        .post(requestBody2)
                        .addHeader("Referer","http://school4.ecc.ac.jp/eccstdweb/st0100/st0100_01.aspx")
                        .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87")
                        .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .build();

                try{
                    Log.d(TAG,"POST");
                    response = client.newCall(request).execute();
                    Thread.sleep(500);
                    mLastResponse = response.body().string();
                } catch (IOException |InterruptedException e) {
                    Log.d(TAG, "POST失敗");
                    e.printStackTrace();
                    Toast.makeText(context,"通信に失敗しました",Toast.LENGTH_LONG).show();
                }

//                String __EVENTTARGET2 = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"(.+?)\"",mLastResponse);
                String __EVENTTARGET2 = "ctl00$btnSyuseki";
                String __EVENTARGUMENT2 = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"(.+?)\"",mLastResponse);
                String __VIEWSTATE2 = getValuesBase.GetValues("input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"(.+?)\"",mLastResponse);
                String __SCROLLPOSITIONX2 = getValuesBase.GetValues("input type=\"hidden\" name=\"__SCROLLPOSITIONX\" id=\"__SCROLLPOSITIONX\" value=\"(.+?)\"",mLastResponse);
                String __SCROLLPOSITIONY2 = getValuesBase.GetValues("input type=\"hidden\" name=\"__SCROLLPOSITIONY\" id=\"__SCROLLPOSITIONY\" value=\"(.+?)\"",mLastResponse);
                String __EVENTVALIDATION2 = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"(.+?)\"",mLastResponse);
                String ctl00$txtWindowOpenFlg = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtWindowOpenFlg\" id=\"ctl00_txtWindowOpenFlg\" value=\"(.+?)\"",mLastResponse);
                String ctl00$txtWindowOpenUrl = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtWindowOpenUrl\" id=\"ctl00_txtWindowOpenUrl\" value=\"(.+?)\"",mLastResponse);
                String ctl00$txtWindowOpenName = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtWindowOpenName\" id=\"ctl00_txtWindowOpenName\" value=\"(.+?)\"",mLastResponse);
                String ctl00$txtWindowOpenStyle = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtWindowOpenStyle\" id=\"ctl00_txtWindowOpenStyle\" value=\"(.+?)\"",mLastResponse);
                String ctl00$txtSearchKey = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtSearchKey\" id=\"ctl00_txtSearchKey\" value=\"(.+?)\"",mLastResponse);
                String ctl00$txtParamKey = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtParamKey\" id=\"ctl00_txtParamKey\" value=\"(.+?)\"",mLastResponse);
                String ctl00$txtCssFileName = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtCssFileName\" id=\"ctl00_txtCssFileName\" value=\"(.+?)\"",mLastResponse);
                String ctl00$txtHeadTitle = "";


                RequestBody requestBody3 = new FormBody.Builder()
                        .add("__EVENTTARGET",__EVENTTARGET2)
                        .add("__EVENTARGUMENT",__EVENTARGUMENT2)
                        .add("__VIEWSTATE",__VIEWSTATE2)
                        .add("__SCROLLPOSITIONX",__SCROLLPOSITIONX2)
                        .add("__SCROLLPOSITIONY",__SCROLLPOSITIONY2)
                        .add("__EVENTVALIDATION",__EVENTVALIDATION2)
                        .add("ctl00$txtWindowOpenFlg",ctl00$txtWindowOpenFlg)
                        .add("ctl00$txtWindowOpenUrl",ctl00$txtWindowOpenUrl)
                        .add("ctl00$txtWindowOpenName",ctl00$txtWindowOpenName)
                        .add("ctl00$txtWindowOpenStyle",ctl00$txtWindowOpenStyle)
                        .add("ctl00$txtSearchKey",ctl00$txtSearchKey)
                        .add("ctl00$txtParamKey",ctl00$txtParamKey)
                        .add("ctl00$txtCssFileName",ctl00$txtCssFileName)
                        .add("ctl00$txtHeadTitle",ctl00$txtHeadTitle)
                        .build();

                request = new Request.Builder()
                        .url(URL6)
                        .post(requestBody3)
                        .addHeader("Referer",URL6)
                        .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87")
                        .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .build();

                try{
                    Log.d(TAG,"POST");
                    response = client.newCall(request).execute();
                    Thread.sleep(500);
                    //ここで出席照会のソースがmLastResponseに入っている
                    mLastResponse = response.body().string();
                    //Log.d(TAG,mLastResponse);
                } catch (IOException |InterruptedException e) {
                    Log.d(TAG, "POST失敗");
                    e.printStackTrace();
                    Toast.makeText(context,"通信に失敗しました",Toast.LENGTH_LONG).show();
                }
//
                nextTask.run(mLastResponse);
            }

        }).thenOnAsyncThread(new Task<String, String>() {
            @Override
            public void run(String result, NextTask<String> nextTask) {

                //**************スタブ****************************
//                stab stub = new stab();
  //              mLastResponse = stub.FireLoad(context);
                //**************スタブ****************************

                String html ="";
                html = getValuesBase.NarrowingValues("<tableclass=\"GridVeiwTable\"","<tablecellspacing=\"0\"border=\"0\"id=\"ctl00_ContentPlaceHolder1_fmvSyuseki\"",mLastResponse,true);

                if (html == "") {
                   // throw new Exception();
                    Toast.makeText(context,"解析に失敗しました",Toast.LENGTH_LONG).show();
                }
                attendanceRateList = new ArrayList();

//                html = html.replaceAll("align=\"right\"style=\"border-color:Black;border-width:1px;border-style:Solid;font-size:9pt;width:8%;\"","");
                //値が一致する間ループ
                //科目ごとにソースを抽出
                Matcher match = getValuesBase.GetGropValues("<tr>.*?</tr>",html);
                while(match.find()){
                    AttendanceRateObject rateObject = new AttendanceRateObject();
                    //教科ごとのhtmlソース
                    String html2 = match.group();
                    String subject = getValuesBase.GetValues("<img(?:\\\".*?\\\"|\\'.*?\\'|[^\\'\\\"])*?>(.+?)</a>",html2);
                    rateObject.setSubjectName(subject);
                    //<td>6</td><td>26</td><td>9</td><td>2</td><td>&nbsp;</td><td>&nbsp;</td><td>75%</td><td>&nbsp;</td>

                    //String html3 = getValuesBase.NarrowingValues("</a>","</tr>",html2,false);
                    int count = 0;
                    Matcher match2 = getValuesBase.GetGropValues("<td(?:\\\".*?\\\"|\\'.*?\\'|[^\\'\\\"])*?>(.*?)</td>",html2);
                    //Matcher match2 = getValuesBase.GetGropValues("<td>(.+?)</td>",html3);
                    while(match2.find()){
                        String str = "";
                        str = match2.group(1);
                        str = getValuesBase.DeletePercent(str);
                        str = getValuesBase.DeleteNBSPTo0(str);
                        switch(count){
                            case 0:
                                break;
                            case 1:
                                rateObject.setUnit(str);
                                break;
                            case 2:
                                rateObject.setAttendanceNumber(str);
                                break;
                            case 3:
                                rateObject.setAbsentNumber(str);
                                break;
                            case 4:
                                rateObject.setLateNumber(str);
                                break;
                            case 5:
                                rateObject.setPublicAbsentNumber1(str);
                                break;
                            case 6:
                                rateObject.setPublicAbsentNumber2(str);
                                break;
                            case 7:
                                rateObject.setAttendanceRate(str);
                                break;
                            case 8:
                                rateObject.setShortageNumber(str);
                                break;
                        }
                        count++;
                    }
                    attendanceRateList.add(rateObject);
                }

                //出席率画面のリスト完成
                /******************* 以下データベース登録処理 ******************/
                saveManager = new SaveManager();
                saveManager.saveMangerWithPreference(context,PREF_NAME,attendanceRateList,"attendanceList");

                nextTask.run(mLastResponse);
            }

        }). setCallback(new Callback<String>() {
            @Override
            public void onSuccess(String result){

                //dialog.dismiss();

                //ユーザID：パスワードの保存
                List<String> ipList = new ArrayList<String>();
                ipList.add(userId2);
                ipList.add(password2);
                saveManager.saveMangerWithPreference(context,PREF_NAME_ID_PASS,ipList,"ip");

                btn.setProgress(100);
//
//                viewStart =v.findViewById(R.id.ecclogo_0001);
//                Intent intent = new Intent(context, MainActivity.class);
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, viewStart, "ecclogo");
//                context.startActivity(intent,options.toBundle());

                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.pull_in_up , R.anim.none_anim);
                ((Activity)context).finish();
                btn.setClickable(true);

                //ログインしたことを記憶
                getValuesBase.SetLoginState(context, true);
            }

            @Override
            public void onFailure(Bundle bundle, Exception e) {
                btn.setProgress(-1);
                btn.setClickable(true);
                //Snackbar.make(v.getRootView(),"通信エラーが発生しました",Snackbar.LENGTH_SHORT).show();

                //時間割のみ取得できている場合
                if(getTimeTable == true){
                    name = name.replaceAll("&nbsp;","");
                    Log.d("名前:::",name);
                    SharedPreferences pref = context.getSharedPreferences("username", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username",name);
                    editor.apply();

                    //ユーザID：パスワードの保存
                    List<String> ipList = new ArrayList<String>();
                    ipList.add(userId2);
                    ipList.add(password2);
                    saveManager = new SaveManager();
                    saveManager.saveMangerWithPreference(context,PREF_NAME_ID_PASS,ipList,"ip");


                    btn.setProgress(100);
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("notGetAttendanceRate",true);
                    context.startActivity(intent);
                    ((Activity)context).overridePendingTransition(R.anim.pull_in_up , R.anim.none_anim);
                    ((Activity)context).finish();
                    btn.setClickable(true);
                    //ログインしたことを記憶
                    getValuesBase.SetLoginState(context, true);
                }

                //btn.setProgress(0);
                //dialog.dismiss();

//                try {
//                    Thread.sleep(1500);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
//                btn.setProgress(0);

            }

        }).create().execute(null);

    }






}
