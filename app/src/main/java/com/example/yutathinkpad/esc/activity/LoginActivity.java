package com.example.yutathinkpad.esc.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anprosit.android.promise.Callback;
import com.anprosit.android.promise.NextTask;
import com.anprosit.android.promise.Promise;
import com.anprosit.android.promise.Task;
import com.example.yutathinkpad.esc.R;
import com.example.yutathinkpad.esc.http.LoginManager;
import com.example.yutathinkpad.esc.http.UpdateTimeTableManager;

public class LoginActivity extends AppCompatActivity{
    Button mLoginButton;
    LoginManager loginManager;

    TextView id;
    TextView pss;

    String userId;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = (Button)findViewById(R.id.login_btn);
        id = (TextView)findViewById(R.id.username);
        pss = (TextView)findViewById(R.id.password);


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                userId = id.getText().toString();
                password = pss.getText().toString();

                //ログイン処理
                loginManager = new LoginManager();

                loginManager.loginManager(LoginActivity.this,view,userId,password);
            }
        });
    }

}
