package jp.yuta.kohashi.esc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dd.CircularProgressButton;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.NewsModel;
import jp.yuta.kohashi.esc.network.HttpConnector;
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

        mIdTextView = (TextView) findViewById(R.id.text_view_id);
        mPasswordTextView = (TextView) findViewById(R.id.text_view_password);
        mIdTextView.setOnClickListener(this);
        mPasswordTextView.setOnClickListener(this);

        connector = new HttpConnector();
    }


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
            mLoginButton.setProgress(0);
        }
    }

    private void login(){
        if(connector != null){
            connector = new HttpConnector();
        }

        mLoginButton.setProgress(10);
        //　時間割を取得
        connector.request(HttpConnector.Type.TIME_TABLE, userId, password, new HttpConnector.Callback() {
            @Override
            public void callback(boolean bool) {
                if (bool) {
                    mLoginButton.setProgress(40);
                    // お知らせ
                    connector.request(HttpConnector.Type.NEWS_SCHOOL_TEACHER, userId, password, new HttpConnector.Callback() {
                        @Override
                        public void callback(boolean bool) {
                            if(bool){
                                mLoginButton.setProgress(70);
                                // 出席照会
                                connector.request(HttpConnector.Type.ATTENDANCE_RATE, userId, password, new HttpConnector.Callback() {
                                    @Override
                                    public void callback(boolean bool) {
                                        // ログイン状態を保存
                                        PrefManager.saveLoginState(true);
                                        PrefManager.saveIdPass(userId,password);
                                        mLoginButton.setProgress(90);
                                        mLoginButton.setProgress(100);
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        intent.putExtra(MainActivity.GET_ATTENDANCE_RATE,bool);  //出席照会を取得できたか
                                        startActivity(intent);

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
    private void failureLogin() {
        Snackbar.make(findViewById(R.id.login_activity),"ログインに失敗しました",Snackbar.LENGTH_SHORT).show();
        mLoginButton.setProgress(-1);
    }

    /**
     * loginbtnのclickableセットメソッド
     */
    private void setClickableLoginBtn(boolean bool) {
        mLoginButton.setClickable(bool);
    }


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

}
