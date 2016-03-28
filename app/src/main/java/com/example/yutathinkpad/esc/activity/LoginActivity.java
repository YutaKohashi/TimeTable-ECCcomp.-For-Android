package com.example.yutathinkpad.esc.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yutathinkpad.esc.R;
import com.example.yutathinkpad.esc.http.LoginManager;
import com.example.yutathinkpad.esc.http.LoginManager2;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = (Button)findViewById(R.id.login_btn);
        id = (TextView)findViewById(R.id.username);
        pss = (TextView)findViewById(R.id.password);
        editText  = (EditText)findViewById(R.id.username);
        textView = (TextView)findViewById(R.id.num_text);



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
