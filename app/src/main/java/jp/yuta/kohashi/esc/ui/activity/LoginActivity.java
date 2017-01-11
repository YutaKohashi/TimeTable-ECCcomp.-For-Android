package jp.yuta.kohashi.esc.ui.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.NewsModel;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button mLoginButton;
    TextView mIdTextView;
    TextView mPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = (Button) findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(this);

        mIdTextView = (TextView) findViewById(R.id.text_view_id);
        mPasswordTextView = (TextView) findViewById(R.id.text_view_password);
    }

    @Override
    public void onClick(View view) {
        //テキストフィールドとネットワークのチェック
        if (!Util.checkTextField(mIdTextView) & !Util.checkTextField(mPasswordTextView))  return;
        if(!Util.netWorkCheck()) return;

        String id = mIdTextView.getText().toString();
        String pass = mPasswordTextView.getText().toString();

        id = "2140257";
        pass = "455478";
        new HttpConnector().request(HttpConnector.Type.NEWS_SCHOOL, id, pass, new HttpConnector.Callback() {
            @Override
            public void callback(boolean bool) {
                if (bool) {
                    //success
                    List<NewsModel> list = PrefManager.loadSchoolNewsList();

                    list.size();
                } else {

                }
            }
        });
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
