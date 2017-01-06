package jp.yuta.kohashi.esc.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.network.HttpHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = (Button)findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(this);

        HttpConnector.init(this);
    }

    @Override
    public void onClick(View view) {

        HttpConnector.request(HttpConnector.Type.ATTENDANCE_RATE, "2140257", "455478", new HttpConnector.Callback() {
            @Override
            public void callback(boolean bool) {

            }
        });

    }
}
