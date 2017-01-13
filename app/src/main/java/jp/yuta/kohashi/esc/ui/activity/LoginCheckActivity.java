package jp.yuta.kohashi.esc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import jp.yuta.kohashi.esc.network.service.HttpHelper;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefManager;


/***
 * このActivityを経由してログイン済みかどうかの判定を行う
 */
public class LoginCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        if (PrefManager.isLogin()) {
            // ログインしていない
            Intent intent = new Intent(LoginCheckActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            //ログインしている
            Intent intent = new Intent(LoginCheckActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void init() {
        HttpHelper.init(this);
        PrefManager.init(this);
        Util.init(getApplicationContext());
    }
}
