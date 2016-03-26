package com.example.yutathinkpad.esc.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.anprosit.android.promise.Callback;
import com.anprosit.android.promise.NextTask;
import com.anprosit.android.promise.Promise;
import com.anprosit.android.promise.Task;
import com.example.yutathinkpad.esc.preference.SaveManager;
import com.example.yutathinkpad.esc.tools.GetValuesBase;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by YutaThinkPad on 2016/03/26.
 */
public class GetAttendanceRateManager {

    static final String URL1 = "http://school4.ecc.ac.jp/eccstdweb/st0100/st0100_01.aspx";
    static final String URL2 = "http://school4.ecc.ac.jp/eccstdweb/st0100/st0100_01.aspx";
    static final String URL3 = "http://school4.ecc.ac.jp/EccStdWeb/ST0100/ST0100_02.aspx";
    static final String TAG ="error:::";
    ProgressDialog prg;
    OkHttpClient client;
    String mLastResponse;

    CookieManager cookieManager;
    CookieJar cookieJar;
    GetValuesBase getValuesBase;

    SaveManager saveManager;

    public void getAttendanceRate(final Context context, final String userId, final String password){
        prg = new ProgressDialog(context);

        cookieManager = new CookieManager();
        cookieJar = new JavaNetCookieJar(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        getValuesBase = new GetValuesBase();
        saveManager = new SaveManager();

        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        Promise.with(this,String.class).then(new Task<String, String>() {
            @Override
            public void run(String s, NextTask<String> nextTask) {
                // ダイアログを表示
                prg.setMessage("メッセージ");
                prg.setTitle("タイトル");
                prg.show();
                nextTask.run(null);

            }
        }).thenOnAsyncThread(new Task<String, String>() {
            @Override
            public void run(String s, NextTask<String> nextTask) {
                Request request = new Request.Builder()
                        .url(URL1)
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
               Log.d("content:;::",__LASTFOCUS);
                String __VIEWSTATE =  getValuesBase.GetValues("input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"(.+?)\"",mLastResponse);
                String __SCROLLPOSITIONX = getValuesBase.GetValues("input type=\"hidden\" name=\"__SCROLLPOSITIONX\" id=\"__SCROLLPOSITIONX\" value=\"(.+?)\"",mLastResponse);
                String __SCROLLPOSITIONY = getValuesBase.GetValues("input type=\"hidden\" name=\"__SCROLLPOSITIONY\" id=\"__SCROLLPOSITIONY\" value=\"(.+?)\"",mLastResponse);
                String __EVENTTARGET = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"(.+?)\"",mLastResponse);
                String __EVENTARGUMENT = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"(.+?)\"",mLastResponse);
                String __EVENTVALIDATION = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"(.+?)\"",mLastResponse);
                String ctl00$ContentPlaceHolder1$txtUserId = userId;
                String ctl00$ContentPlaceHolder1$txtPassword = password;
                String ctl00$ContentPlaceHolder1$btnLogin = "ログイン";

                RequestBody requestBody = new FormBody.Builder()
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
                        .url(URL2)
                        .post(requestBody)
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


                RequestBody requestBody2 = new FormBody.Builder()
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
                        .url(URL3)
                        .post(requestBody2)
                        .addHeader("Referer",URL3)
                        .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87")
                        .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .build();

                try{
                    Log.d(TAG,"POST");
                    response = client.newCall(request).execute();
                    Thread.sleep(500);
                    //ここで出席照会のソースがmLastResponseに入っている
                    mLastResponse = response.body().string();
                    Log.d(TAG,mLastResponse);
                } catch (IOException |InterruptedException e) {
                    Log.d(TAG, "POST失敗");
                    e.printStackTrace();
                    Toast.makeText(context,"通信に失敗しました",Toast.LENGTH_LONG).show();
                }

                String html = getValuesBase.NarrowingValues("<tableid=\"Table1\"class=\"ListTable\"","<tablecellspacing=\"0\"border=\"0\"id=\"ctl00_ContentPlaceHolder1_fmvSyuseki\"",mLastResponse,true);
                html = html.replaceAll("align=\"right\"style=\"border-color:Black;border-width:1px;border-style:Solid;font-size:9pt;width:8%;\"","");
                Log.d(TAG,html);

                
                nextTask.run(mLastResponse);
            }
        }).setCallback(new Callback<String>() {
            @Override
            public void onSuccess(String s) {

                prg.dismiss();
            }

            @Override
            public void onFailure(Bundle bundle, Exception e) {
                prg.dismiss();
            }
        }).create().execute(null);
    }


}
