package jp.yuta.kohashi.esc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dd.CircularProgressButton;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.util.ToastManager;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    CircularProgressButton mLoginButton;
    TextView mIdTextView;
    TextView mPasswordTextView;
    String userId;
    String password;
    HttpConnector connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = (CircularProgressButton) findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(this);
        mLoginButton.setIndeterminateProgressMode(true);

        mIdTextView = (TextView) findViewById(R.id.text_view_id);
        mPasswordTextView = (TextView) findViewById(R.id.text_view_password);
        mIdTextView.setOnClickListener(this);
        mPasswordTextView.setOnClickListener(this);

        connector = new HttpConnector();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        finish();
//    }



    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_login){
            //テキストフィールドとネットワークのチェック
            if (!Util.checkTextField(mIdTextView) & !Util.checkTextField(mPasswordTextView)) return;
            if (!Util.netWorkCheck()) return;

            userId = mIdTextView.getText().toString();
            password = mPasswordTextView.getText().toString();

            login();
        } else {
            defaultProgBtn();
        }
    }

    private void login(){
        if(connector != null){
            connector = new HttpConnector();
        }
        disableTextViews();
        disableBtn();
        startProgBtn();
        //　時間割を取得
        connector.request(HttpConnector.Type.TIME_TABLE, userId, password, new HttpConnector.Callback() {
            @Override
            public void callback(boolean bool) {
                if (bool) {
                    // お知らせ
                    connector.request(HttpConnector.Type.NEWS_SCHOOL_TEACHER, userId, password, new HttpConnector.Callback() {
                        @Override
                        public void callback(boolean bool) {
                            if(bool){
                                // 出席照会
                                connector.request(HttpConnector.Type.ATTENDANCE_RATE, userId + "d", password, new HttpConnector.Callback() {
                                    @Override
                                    public void callback(boolean bool) {
                                       successLogin(bool);
                                    }
                                });
                            }else{
                                failureLogin();
                            }
                        }
                    });
                } else {
                    failureLogin();
                }
            }
        });
    }

    /**
     * ログイン失敗時
     */
    private void failureLogin() {
        ToastManager.failureLogin();
        failureProgBtn();
        enableTextViews();

        Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                defaultProgBtn();
                enableBtn();
            }
        };
        mHandler.postDelayed(runnable, 1200);
    }

    /**
     * ログイン成功時
     * 引数に出席照会が取得できたかを入れる
     * true : 取得成功　false 取得失敗
     */
    private void successLogin(Boolean bool){
        successProgBtn();
        // ログイン状態を保存
        PrefManager.saveLoginState(true);
        PrefManager.saveIdPass(userId,password);
        // 画面遷移
        final Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra(MainActivity.GET_ATTENDANCE_RATE,bool);  //出席照会を取得できたか
        Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                startActivity(intent);
                enableTextViews();
                enableBtn();
                finish();
            }
        };
        mHandler.postDelayed(runnable, 500);
    }


    //**
    //region view enable disable
    //**

    /***
     * TextViewのenableセットメソッド
     */
    private void enableTextViews() {
        setEnabledTextViews(true);
    }

    private void disableTextViews() {
        setEnabledTextViews(false);
    }

    private void setEnabledTextViews(Boolean bool) {
        mIdTextView.setEnabled(bool);
        mPasswordTextView.setEnabled(bool);
    }

    private void enableBtn(){
        setClikableBtn(true);
    }

    private void disableBtn(){
        setClikableBtn(false);
    }

    private void setClikableBtn(Boolean bool){
        mLoginButton.setClickable(bool);
    }

    //**
    //endregion
    //**

    //**
    //region login btn state
    //**
    private void startProgBtn(){
        mLoginButton.setProgress(1);
    }

    private void successProgBtn(){
        mLoginButton.setProgress(100);
    }

    private void failureProgBtn(){
        mLoginButton.setProgress(-1);
    }

    private void defaultProgBtn(){
        mLoginButton.setProgress(0);
    }

    //**
    //endregion
    //**

}
