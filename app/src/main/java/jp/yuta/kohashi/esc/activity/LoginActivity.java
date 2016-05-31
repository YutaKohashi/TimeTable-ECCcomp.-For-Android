package jp.yuta.kohashi.esc.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.http.LoginManager;
import jp.yuta.kohashi.esc.http.LoginManager2;
import jp.yuta.kohashi.esc.tools.GetValuesBase;
import me.drakeet.materialdialog.MaterialDialog;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity{
   // Button mLoginButton;
    LoginManager loginManager;

    EditText id;
    EditText pss;

    String userId;
    String password;
  //  EditText editText;
    TextView textView;
    CircularProgressButton circularButton1;
    TextView dummy;
    // キーボード表示を制御するためのオブジェクト
    InputMethodManager inputMethodManager;
    ScrollView mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        boolean logouted = intent.getBooleanExtra("logouted", false);
        View view = findViewById(R.id.root_linear_layout);
        //
        if(logouted){
            Snackbar.make(view,"ログアウトしました",Snackbar.LENGTH_LONG).show();
        }

        final GetValuesBase getValuesBase = new GetValuesBase();
        if(getValuesBase.IsBooted(LoginActivity.this)){


            Intent intent1 = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent1);
            finish();
        }

//        dummy = (TextView)findViewById(R.id.dummy);
//        dummy.clearFocus();
//        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(dummy.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        mainLayout = (ScrollView) findViewById(R.id.login_scroll_view);

     //   mLoginButton = (Button)findViewById(R.id.login_btn);
        id = (EditText) findViewById(R.id.username);
        pss = (EditText)findViewById(R.id.password);

        //editText  = (EditText)findViewById(R.id.username);
        textView = (TextView)findViewById(R.id.num_text);

        circularButton1 = (CircularProgressButton) findViewById(R.id.login_btn);
        circularButton1.setIndeterminateProgressMode(true);

        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetValuesBase getValuesBase1 = new GetValuesBase();

                if(!getValuesBase.ConnectionCheck(LoginActivity.this)){
                    Snackbar.make(view,"インターネットに接続されていません",Snackbar.LENGTH_SHORT).show();
                    circularButton1.setProgress(-1);

                    return;
                }


                String idCheck = id.getText().toString().replaceAll(" ","");
                idCheck = idCheck.replaceAll("　","");
                String passCheck = pss.getText().toString().replaceAll(" ","");
                passCheck = passCheck.replaceAll("　","");
                if(id.getText().toString().length() == 0 && pss.getText().toString().length() == 0){
                    id.setError("学籍番号を入力してください");
                    pss.setError("パスワードを入力してください");
                    return;
                }else if(id.getText().toString().length() == 0){
                    id.setError("学籍番号を入力してください");
                    return;
                }else if(pss.getText().toString().length() == 0){
                    pss.setError("パスワードを入力してください");
                    return;
                }

                circularButton1.setProgress(0);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                userId = id.getText().toString();
                password = pss.getText().toString();
                //ログイン処理
                loginManager = new LoginManager();
                loginManager.loginManager(LoginActivity.this, view, userId, password);
                circularButton1.setProgress(1);
                //クリックイベントを発生させない
                circularButton1.setClickable(false);

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

        id.setNextFocusDownId(R.id.password);
        pss.setNextFocusDownId(R.id.login_btn);
        id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    circularButton1.setProgress(0);
                }
            }
        });

        pss.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    circularButton1.setProgress(0);
                }
            }
        });

        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circularButton1.setProgress(0);
            }
        });

        pss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circularButton1.setProgress(0);
            }
        });

        pss.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    // ソフトキーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
            }
        });

    }
//    // 画面タップ時の処理
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        // キーボードを隠す
//        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
//        // 背景にフォーカスを移す
//        mainLayout.requestFocus();
//
//        return true;
//
//    }
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent e) {
//        // 戻るボタンが押されたとき
//        if(e.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            // ボタンが押されたとき
//            return true;
//
//        }
//        return super.dispatchKeyEvent(e);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){

            // ボタンが押されたとき
            final MaterialDialog dialog = new MaterialDialog(this);
            dialog
                    .setTitle("アプリケーションの終了")
                    .setMessage("アプリケーションを終了してよろしいですか？")
                    .setPositiveButton("YES", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                           LoginActivity.this.finish();

                        }
                    })
                    .setNegativeButton("NO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
//
                        }
                    }).show();
            return true;
        }
        return false;
    }


}


