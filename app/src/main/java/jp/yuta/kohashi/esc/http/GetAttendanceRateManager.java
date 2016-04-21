package jp.yuta.kohashi.esc.http;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anprosit.android.promise.Callback;
import com.anprosit.android.promise.NextTask;
import com.anprosit.android.promise.Promise;
import com.anprosit.android.promise.Task;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.activity.MainActivity;
import jp.yuta.kohashi.esc.adapter.RecyclerViewAdapter;
import jp.yuta.kohashi.esc.object.AttendanceRateObject;
import jp.yuta.kohashi.esc.preference.LoadManager;
import jp.yuta.kohashi.esc.preference.SaveManager;
import jp.yuta.kohashi.esc.stab;
import jp.yuta.kohashi.esc.tools.GetValuesBase;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
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

    static final String PREF_NAME ="sample";
    static final String PREF_NAME_ID_PASS = "ip";  //IDPAssが保存されているプリファレンス
    ProgressDialog prg;
    OkHttpClient client;
    String mLastResponse;

    CookieManager cookieManager;
    CookieJar cookieJar;
    GetValuesBase getValuesBase;

    SaveManager saveManager;
    List<AttendanceRateObject> attendanceRateList;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;



    public void getAttendanceRate(final Context context, final View view, String userId, String password){
        final String userId2 = userId;
        final String password2 = password;
        prg = new ProgressDialog(context);

        cookieManager = new CookieManager();
        cookieJar = new JavaNetCookieJar(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        getValuesBase = new GetValuesBase();
        saveManager = new SaveManager();
        attendanceRateList = new ArrayList();

        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        Promise.with(this,String.class).then(new Task<String, String>() {
            @Override
            public void run(String s, NextTask<String> nextTask) {
//                // ダイアログを表示
//                prg.setMessage("メッセージ");
//                prg.setTitle("タイトル");
//                prg.show();
                if(!getValuesBase.ConnectionCheck(context)){
                    mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
                    mSwipeRefreshLayout.setRefreshing(false);
                    Snackbar.make(view,"インターネットに接続されていません",Snackbar.LENGTH_LONG).show();
                    return;
                }
                recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
                List<AttendanceRateObject> rateObjectList = new ArrayList<>();
                adapter = new RecyclerViewAdapter(rateObjectList,context);
                recyclerView.setAdapter(adapter);
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
                        .url(URL2)
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
                        .url(URL3)
                        .post(requestBody3)
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
//
                nextTask.run(mLastResponse);
            }

        }).thenOnAsyncThread(new Task<String, String>() {
            @Override
            public void run(String result, NextTask<String> nextTask) {

                //**************スタブ****************************
                //              stab stub = new stab();
//                mLastResponse = stub.FireLoad(context);
                //**************スタブ****************************

                String html ="";
                html = getValuesBase.NarrowingValues("<tableclass=\"GridVeiwTable\"","<tablecellspacing=\"0\"border=\"0\"id=\"ctl00_ContentPlaceHolder1_fmvSyuseki\"",mLastResponse,true);

                if (html == "") {
                    // throw new Exception();
                    Toast.makeText(context,"解析に失敗しました",Toast.LENGTH_LONG).show();
                }
                attendanceRateList = new ArrayList();

//                html = html.replaceAll("align=\"right\"style=\"border-color:Black;border-width:1px;border-style:Solid;font-size:9pt;width:8%;\"","");
                Log.d(TAG,html);
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
//                List<String> ipList = new ArrayList<String>();
//                ipList.add(userId2);
//                ipList.add(password2);
//                saveManager.saveMangerWithPreference(context,PREF_NAME_ID_PASS,ipList,"ip");

//                viewStart =v.findViewById(R.id.ecclogo_0001);
//                Intent intent = new Intent(context, MainActivity.class);
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, viewStart, "ecclogo");
//                context.startActivity(intent,options.toBundle());

//                Intent intent = new Intent(context, MainActivity.class);
//                context.startActivity(intent);
//                ((Activity)context).finish();

                //ログインしたことを記憶
//                getValuesBase.SetLoginState(context, true);
                //リフレッシュを終了
                mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
                mSwipeRefreshLayout.setRefreshing(false);


                recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
                List<AttendanceRateObject> rateObjectList = new ArrayList<>();
//                adapter = new RecyclerViewAdapter(rateObjectList);
//                recyclerView.setAdapter(adapter);
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//
//
//                }
                LoadManager loadManager = new LoadManager();
                rateObjectList = loadManager.loadManagerWithPreferenceForAttendance(context,PREF_NAME,"attendanceList");

                recyclerView.setHasFixedSize(true);

                layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);

                adapter = new RecyclerViewAdapter(rateObjectList,context);
                recyclerView.setAdapter(adapter);


                Snackbar.make(view,"更新しました",Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Bundle bundle, Exception e) {
                Snackbar.make(view,"更新に失敗しました",Snackbar.LENGTH_SHORT).show();
                //btn.setProgress(0);
                //dialog.dismiss();
                //リフレッシュを終了
                mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
                mSwipeRefreshLayout.setRefreshing(false);

                recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
                List<AttendanceRateObject> rateObjectList = new ArrayList<>();
                LoadManager loadManager = new LoadManager();
                //データが取得できなかった場合null値が代入される
                rateObjectList = loadManager.loadManagerWithPreferenceForAttendance(context,PREF_NAME,"attendanceList");

                if(rateObjectList == null){
                    rateObjectList = new ArrayList<>();
                    rateObjectList.add(new AttendanceRateObject(context.getString(R.string.get_rate_error)));

                }
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);

                adapter = new RecyclerViewAdapter(rateObjectList,context);
                recyclerView.setAdapter(adapter);



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
