package com.example.yutathinkpad.esc;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anprosit.android.promise.Callback;
import com.anprosit.android.promise.NextTask;
import com.anprosit.android.promise.Promise;
import com.anprosit.android.promise.Task;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.MediaType;
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
    String mLastResponse;

    static final String URL1 = "http://comp2.ecc.ac.jp/sutinfo/login";          //ログイン画面
    static final String URL2 = "http://comp2.ecc.ac.jp/sutinfo/auth/attempt";   //実ログイン
    static final String URL3 ="http://comp2.ecc.ac.jp/sutinfo";
    static final String userId = "2140257";
    static final String password = "455478";
    final String TAG ="error:::";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnStart =(Button)findViewById(R.id.btn_start);
        mBtnEnd = (Button)findViewById(R.id.btn_end);
        textView =(TextView)findViewById(R.id.text_view);
        dialog = new ProgressDialog(this);
        client = new OkHttpClient();

        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        //リセットボタン
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Promise.with(this,String.class).then(new Task<String,String>(){
                    @Override
                    public void run(String s, NextTask<String> nextTask) {
                        //ダイアログ
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

                            mLastResponse = response.body().string();
                        } catch (IOException e) {
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
                                .build();

                        try {
                            //ログイン
                            Log.d(TAG,"POST");
                            response = client.newCall(request).execute();
                            mLastResponse = response.body().string();
                        } catch (IOException e) {
                            Log.d(TAG,"POST失敗");
                            e.printStackTrace();
                        }

                        /********************************************************/
                        //ログイン画面へGET通信
                        request = new Request.Builder()
                                .url(URL3)
                                .build();
//                        Response response;
                        try {
                            //ログインページヘ
                            Log.d(TAG,"最終GET");
                            response = client.newCall(request).execute();

                            mLastResponse = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d(TAG,"最終GET失敗");
                        }

                        nextTask.run(mLastResponse);
                    }

                }).setCallback(new Callback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        textView.setText(result);
                        if(result.indexOf("新着情報")!= -1){
                            Toast.makeText(MainActivity.this,"成功",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(MainActivity.this,"失敗",Toast.LENGTH_LONG).show();
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
            }
        });

    }

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
