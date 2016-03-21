package com.example.yutathinkpad.esc.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.InetSocketAddress;
import java.net.Proxy;
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

    static final String URL1 = "http://comp2.ecc.ac.jp/sutinfo/login";          //ログイン画面
    static final String URL2 = "http://comp2.ecc.ac.jp/sutinfo/auth/attempt";   //実ログイン
    static final String URL3 ="http://comp2.ecc.ac.jp/sutinfo/logout";

    static final String userId = "2140257";
    static final String password = "455478";
    final String TAG ="error:::";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Proxy p=new Proxy(Proxy.Type.HTTP,new InetSocketAddress("192.168.10.8", 8888));
        mBtnStart =(Button)findViewById(R.id.btn_start);
        mBtnEnd = (Button)findViewById(R.id.btn_end);
        textView =(TextView)findViewById(R.id.text_view);
        dialog = new ProgressDialog(this);

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieJar cookieJar = new JavaNetCookieJar(cookieManager);

        client = new OkHttpClient.Builder()
                .proxy(p)
                .cookieJar(cookieJar)
                .build();

        //リセットボタン
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Promise.with(this,String.class).then(new Task<String,String>(){
                    @Override
                    public void run(String s, NextTask<String> nextTask) {
                        //ダイアログを表示
                        dialog.setMessage("メッセージ");
                        dialog.setTitle("タイトル");
                        dialog.show();
                        nextTask.run(null);

                    }

                }).thenOnAsyncThread(new Task<String, String>() {
                    @Override
                    public void run(String result, NextTask<String> nextTask) {
                        //ログイン画面へGET通信
                        Request request = new Request.Builder()
                                .url(URL1)
                                .build();
                        Response response;
                        try {
                            //ログインページヘ
                            response = client.newCall(request).execute();
                            Thread.sleep(1500);
                            mLastResponse = response.body().string();
                        } catch (IOException|InterruptedException e) {
                            e.printStackTrace();
                        }

                        /********************************************************/
                        //トークンの抽出処理
                        String token = GetToken(mLastResponse);
                        Log.d(TAG,token);
                        if("ERROR".equals(token)){
                            //例外処理
                            Log.d(TAG,"トークン取得失敗");
                        }

                        /********************************************************/
                        //ESCへPOST
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
                            //ログイン
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
                        textView.setText(result);
                        if(result.indexOf("新着情報")!= -1){
                            Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(MainActivity.this,"Failure",Toast.LENGTH_LONG).show();
                        }
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
                        //ログアウト処理
                        Request request = new Request.Builder()
                                .url(URL3)
                                .build();
                        Response response;
                        try {
                            //ログインページヘ
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
