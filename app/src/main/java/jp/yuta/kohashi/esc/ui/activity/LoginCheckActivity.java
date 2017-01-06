package jp.yuta.kohashi.esc.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jp.yuta.kohashi.esc.preference.PrefManager;


/***
 * このActivityを経由してログイン済みかどうかの判定を行う
 */
public class LoginCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!PrefManager.isLogin(this)){
            Intent intent = new Intent(LoginCheckActivity.this,LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoginCheckActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
