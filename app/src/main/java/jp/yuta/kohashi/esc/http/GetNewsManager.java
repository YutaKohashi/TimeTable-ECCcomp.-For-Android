package jp.yuta.kohashi.esc.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anprosit.android.promise.Callback;
import com.anprosit.android.promise.NextTask;
import com.anprosit.android.promise.Promise;
import com.anprosit.android.promise.Task;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.adapter.NewsRecyclerViewAdapter;
import jp.yuta.kohashi.esc.object.NewsChildListItem;
import jp.yuta.kohashi.esc.object.NewsParentListItem;
import jp.yuta.kohashi.esc.preference.LoadManager;
import jp.yuta.kohashi.esc.preference.SaveManager;
import jp.yuta.kohashi.esc.tools.CustomProgressDialog;
import jp.yuta.kohashi.esc.tools.GetValuesBase;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Yuta on 2016/06/15.
 */
public class GetNewsManager {
    Request request;
    Response response;

    static final String URL1 = "http://comp2.ecc.ac.jp/sutinfo/login";          //ログイン画面
    static final String URL2 = "http://comp2.ecc.ac.jp/sutinfo/auth/attempt";   //実ログイン
    static final String URL3 ="http://comp2.ecc.ac.jp/sutinfo/logout";

    static final String PREF_NAME ="sample";
    //
    final String TAG ="error:::";


    ProgressDialog dialog;
    OkHttpClient client;
    static String mLastResponse;

    CookieManager cookieManager;
    CookieJar cookieJar;
    GetValuesBase getValuesBase;

    SaveManager saveManager;

    SwipeRefreshLayout mSwipeRefreshLayout;
    private List<NewsChildListItem> expListItems;

    RecyclerView recyclerView;
    NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;

    public void getNews(final Context context, final View v, final String userId, final String password){

        final String userId2 = userId;
        final String password2 = password;

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
                //  ダイアログを表示

                if(!getValuesBase.ConnectionCheck(context)){
                    //リフレッシュを終了
                    mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_news);
                    mSwipeRefreshLayout.setRefreshing(false);
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
                    mLastResponse = response.body().string();
                } catch (UnknownHostException e) {
                    Toast.makeText(context, "通信に失敗しました", Toast.LENGTH_LONG).show();
                    Log.d("okhttp::::", "通信に失敗しました");
                } catch (IOException  e) {
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
                    mLastResponse = response.body().string();
                    response.body().close();

                } catch (IOException  e) {
                    Log.d(TAG, "POST失敗");
                    e.printStackTrace();
                    Toast.makeText(context,"通信に失敗しました",Toast.LENGTH_LONG).show();
                }

                nextTask.run(mLastResponse);
            }

        }).thenOnAsyncThread(new Task<String, String>() {

            @Override
            public void run(String result, NextTask<String> nextTask) {
                mLastResponse = result;
                //各時間割の先生名の取得処理
                expListItems =   newsSaveManager(mLastResponse);


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

                try {
                    client.newCall(request).execute();
                    //mLastResponse = response.body().string();
                } catch (IOException  e) {
                    e.printStackTrace();
                }
                nextTask.run(mLastResponse);
            }
        }).setCallback(new Callback<String>() {
            @Override

            public void onSuccess(String result){
                mLastResponse = result;


                /********************** 以上でリスト完成 **********************/
                /******************* 以下データベース登録処理 ******************/
               saveManager = new SaveManager();
                saveManager.saveMangerWithPreference(context,PREF_NAME,expListItems,"newsList");


                //リフレッシュを終了
                mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_news);
                mSwipeRefreshLayout.setRefreshing(false);

                recyclerView = (RecyclerView) v.findViewById(R.id.news_recycler_view);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);

                newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(expListItems, context);
                recyclerView.setAdapter(newsRecyclerViewAdapter);


                Snackbar.make(v,"更新しました",Snackbar.LENGTH_SHORT).show();


                //dialog.dismiss();
            }

            @Override
            public void onFailure(Bundle bundle, Exception e) {
                //リフレッシュを終了
                mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_news);
                mSwipeRefreshLayout.setRefreshing(false);
                Snackbar.make(v,"更新に失敗しました",Snackbar.LENGTH_SHORT).show();
            }
        }).create().execute(null);
    }


    private List<NewsChildListItem> newsSaveManager(String html){
        getValuesBase = new GetValuesBase();
        //担任からのお知らせを取得
        String tanninHtml = getValuesBase.NarrowingLastValues("<divclass=\"col\"><h2>担任からのお知らせ</h2>","</div><divid=\"school_news_col\"class=\"col\">",html,true);



        //What's New 学校からのお知らせ
        String schoolNews = getValuesBase.NarrowingValues("</div><divid=\"school_news_col\"class=\"col\">","<divid=\"shcool_event_col\"class=\"col\">",html,true);

        Matcher match2 = getValuesBase.GetGropValues("<h3>(.*?)</h3>",html);
        //Matcher match2 = getValuesBase.GetGropValues("<td>(.+?)</td>",html3);

        List<NewsChildListItem> list = new ArrayList<>();

        while(match2.find()){
            String str = match2.group(1);
            String containtsHtml = getValuesBase.NarrowingValues(str,"</div>",schoolNews);

            //タイトル
            NewsChildListItem parentItem = new NewsChildListItem();
//            parentItem.setChildItems(childList);
            parentItem.setTitle(str);
            list.add(parentItem);
//            List<NewsChildListItem> childList = new ArrayList<>();

            Matcher match3 = getValuesBase.GetGropValues("<li>(.*?)</li>",containtsHtml);

            String str1 = "";
            while(match3.find()){
                str1 = match3.group(1);
                String date = getValuesBase.GetValues("<pclass=\"date\">(.*?)</p>",str1);

                String uri = getValuesBase.GetValues("<ahref=\"(.*?)\">",str1);
                String title = getValuesBase.GetValues("</p><pclass=\"title\"><ahref=\""+ uri +"\">(.*?)</a></p>",str1);


                NewsChildListItem item = new NewsChildListItem();
                item.setTitle(title);
                item.setTime(date);
                item.setUri(uri);

                list.add(item);
            }




        }

        return list;
    }

}
