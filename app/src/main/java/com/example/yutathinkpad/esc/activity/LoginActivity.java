package com.example.yutathinkpad.esc.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.example.yutathinkpad.esc.R;
import com.example.yutathinkpad.esc.http.LoginManager;
import com.example.yutathinkpad.esc.http.LoginManager2;
import com.example.yutathinkpad.esc.tools.GetValuesBase;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity{
    Button mLoginButton;
    LoginManager loginManager;

    TextView id;
    TextView pss;

    String userId;
    String password;
    EditText editText;
    TextView textView;
    CircularProgressButton circularButton1;
    // キーボード表示を制御するためのオブジェクト
    InputMethodManager inputMethodManager;
    ScrollView mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GetValuesBase getValuesBase = new GetValuesBase();
        if(getValuesBase.IsBooted(LoginActivity.this)){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mainLayout = (ScrollView) findViewById(R.id.login_scroll_view);

        mLoginButton = (Button)findViewById(R.id.login_btn);
        id = (TextView)findViewById(R.id.username);
        pss = (TextView)findViewById(R.id.password);
        editText  = (EditText)findViewById(R.id.username);
        textView = (TextView)findViewById(R.id.num_text);

        circularButton1 = (CircularProgressButton) findViewById(R.id.login_btn);
        circularButton1.setIndeterminateProgressMode(true);

        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId = id.getText().toString();
                password = pss.getText().toString();
                //ログイン処理
                loginManager = new LoginManager();
                loginManager.loginManager(LoginActivity.this, view, userId, password);
                circularButton1.setProgress(1);

            }
        });
//        mLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//
//                userId = id.getText().toString();
//                password = pss.getText().toString();
//                //ログイン処理
//                loginManager = new LoginManager();
//                loginManager.loginManager(LoginActivity.this,view,userId,password);
//
//            }
//        });
    }
    // 画面タップ時の処理
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        // 背景にフォーカスを移す
        mainLayout.requestFocus();

        return true;

    }
}


