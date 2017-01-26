package jp.yuta.kohashi.esc.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import jp.yuta.kohashi.esc.util.preference.PrefUtil;


/***
 * このActivityを経由してログイン済みかどうかの判定を行う
 */
public class LoginCheckActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrefUtil.saveAppVersion();

        if (!PrefUtil.isLogin()) {
            // ログインしていない
            Intent intent = new Intent(LoginCheckActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            //ログインしている
            Intent intent = new Intent(LoginCheckActivity.this, MainActivity.class);
            startActivity(intent);
        }
        ActivityCompat.finishAffinity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
