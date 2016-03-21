package com.example.yutathinkpad.esc.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anprosit.android.promise.Callback;
import com.anprosit.android.promise.NextTask;
import com.anprosit.android.promise.Promise;
import com.anprosit.android.promise.Task;

import com.example.yutathinkpad.esc.R;
import com.example.yutathinkpad.esc.http.JavaNetCookieJar;
import com.example.yutathinkpad.esc.object.TimeBlock;
import com.example.yutathinkpad.esc.tools.CreateTimeTableLists;
import com.example.yutathinkpad.esc.tools.GetValuesBase;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.InetSocketAddress;
import java.net.Proxy;
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

public class MainActivity extends AppCompatActivity {

    Button mBtnStart;
    Button mBtnEnd;
    TextView textView;
    ProgressDialog dialog;
    OkHttpClient client;
    static String mLastResponse;
    int weekCount;

    static final String URL1 = "http://comp2.ecc.ac.jp/sutinfo/login";          //ログイン画面
    static final String URL2 = "http://comp2.ecc.ac.jp/sutinfo/auth/attempt";   //実ログイン
    static final String URL3 ="http://comp2.ecc.ac.jp/sutinfo/logout";

    static final String userId = "2140257";
    static final String password = "455478";
    final String TAG ="error:::";

    //月曜日から金曜日の曜日ごとのリスト
    static List<TimeBlock> MondayList;
    static List<TimeBlock> TuesdayList;
    static List<TimeBlock> WednesdayList;
    static List<TimeBlock> ThursdayList;
    static List<TimeBlock> FridayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Proxy p=new Proxy(Proxy.Type.HTTP,new InetSocketAddress("192.168.10.8", 8888));
        mBtnStart =(Button)findViewById(R.id.btn_start);
        mBtnEnd = (Button)findViewById(R.id.btn_end);
        textView =(TextView)findViewById(R.id.text_view);
        dialog = new ProgressDialog(this);

        MondayList = new ArrayList<>();
        TuesdayList = new ArrayList<>();
        WednesdayList = new ArrayList<>();
        ThursdayList = new ArrayList<>();
        FridayList = new ArrayList<>();

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieJar cookieJar = new JavaNetCookieJar(cookieManager);
        final GetValuesBase getValuesBase = new GetValuesBase();

        client = new OkHttpClient.Builder()
                //.proxy(p)
                .cookieJar(cookieJar)
                .build();

        //リセットボタン
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Response response;
                        try {
                            // ログインページヘ
                            response = client.newCall(request).execute();
                            Thread.sleep(1500);
                            mLastResponse = response.body().string();
                        } catch (IOException|InterruptedException e) {
                            e.printStackTrace();
                        }

                        // トークンの抽出処理
                        String token = GetToken(mLastResponse);
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
                            // ログイン
                            Log.d(TAG,"POST");
                            response = client.newCall(request).execute();
                            Thread.sleep(1500);

                            mLastResponse = response.body().string();
                        } catch (IOException |InterruptedException e) {
                            Log.d(TAG, "POST失敗");
                            e.printStackTrace();
                        }

                        nextTask.run(mLastResponse);
                    }

                }).setCallback(new Callback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        mLastResponse = result;
                        Pattern pattern1 = Pattern.compile("<td>(.+?)</td>");
                        Matcher matcher1;

                        //1時限目のHTMLソース
                        weekCount = 0;
                        result= getValuesBase.NarrowingValues("<tr><thclass=\"term\">1","投書</a></li></ul></td></tr>",mLastResponse,true);
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

                        /********************** リスト完成 **********************/

                        String LastResult = "";
                        for(TimeBlock tb:MondayList){

                            LastResult +=tb.getClassRoom() + "<br>";
                            LastResult +=tb.getSubject() + "<br>";
                        }
                        for(TimeBlock tb:TuesdayList){
                            LastResult +=tb.getClassRoom() + "<br>";
                            LastResult +=tb.getSubject() + "<br>";
                        }
                        for(TimeBlock tb:WednesdayList){
                            LastResult +=tb.getClassRoom() + "<br>";
                            LastResult +=tb.getSubject() + "<br>";
                        }
                        for(TimeBlock tb:ThursdayList){
                            LastResult +=tb.getClassRoom() + "<br>";
                            LastResult +=tb.getSubject() + "<br>";
                        }
                        for(TimeBlock tb:FridayList){
                            LastResult +=tb.getClassRoom() + "<br>";
                            LastResult +=tb.getSubject() + "<br>";
                        }
                        textView.setText(Html.fromHtml(LastResult));

//                        if(getValuesBase.ContainsCheck("ログアウト",result)){
//                            Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_LONG).show();
//                        }else{
//                            Toast.makeText(MainActivity.this,"Failure",Toast.LENGTH_LONG).show();
//                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Bundle bundle, Exception e) {
                        Toast.makeText(MainActivity.this,"onFailure::" + e.toString(),Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }

                }).create().execute(null);

            }
        });

        //クリアボタン
        mBtnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");

                Promise.with(this,String.class).thenOnAsyncThread(new Task<String, String>() {
                    @Override
                    public void run(String s, NextTask<String> nextTask) {
                        // ログアウト処理
                        Request request = new Request.Builder()
                                .url(URL3)
                                .build();
                        Response response;
                        try {
                            // ログインページヘ
                            response = client.newCall(request).execute();
                            Thread.sleep(1500);
                            mLastResponse = response.body().string();
                        } catch (IOException|InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    //トークンの切り出し処理
    private String GetToken(String target){
        //TOKENをここで取得する
        //matchで取得
        //input name="_token" type="hidden" value="Nffzc7TtEFFuuhKC6bL4rg7ViPib1jkVxj2QBqlk"
        String token = "";
        Pattern exp = Pattern.compile("input name=\"_token\" type=\"hidden\" value=\"(.+?)\"");
        Matcher matcher = exp.matcher(target);
        if(matcher.find()){
            token = matcher.group(1);
        }
        if(token.equals("")){
           token = "ERROR";
            Log.d(TAG,"トークンの取得に失敗");
        }
        return token;
    }
}
