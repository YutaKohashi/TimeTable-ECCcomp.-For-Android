package jp.yuta.kohashi.esc.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.chrome.CustomTabsHelper;
import jp.yuta.kohashi.esc.fragment.AttendanceRateFragment;
import jp.yuta.kohashi.esc.fragment.TimeTableFragment;
import jp.yuta.kohashi.esc.http.UpdateTimeTableManager;
import jp.yuta.kohashi.esc.preference.LoadManager;
import jp.yuta.kohashi.esc.tools.CustomProgressDialog;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    static final String PREF_NAME_ID_PASS = "ip";
    static final String PDF_FILE_NAME = "handbook2015.pdf";

    Toolbar toolbar;
    TextView textView;
    TextView textViewUserName;
    private NavigationView drawer;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedId;

    static int selectedItem = 0;

    TextView appTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ツールバーをActionBarとして扱う
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        initView();

        Intent intent = getIntent();
        boolean notGetAttendanceRate = intent.getBooleanExtra("notGetAttendanceRate", false);

        if(notGetAttendanceRate){
            View view = findViewById(R.id.drawer_layout);
            Snackbar.make(view,getString(R.string.failed_get_attendancerate),Snackbar.LENGTH_LONG).show();
        }

        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        //setDrawerListener ：   非推奨
        //addDrawerListener ：   推奨
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        selectedId=savedInstanceState ==null ? R.id.navigation_item_time_table: savedInstanceState.getInt("SELECTED_ID");
        itemSelection(selectedId);

        appTitle = (TextView)findViewById(R.id.main_title);
        Typeface font = Typeface.createFromAsset(getAssets(), "mplus-1p-medium.ttf");
        appTitle.setTypeface(font);

        //TODO  buildを23にするとエラーが出る

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        // addを呼んでいるので、重なる
        transaction.add(R.id.fragment_container, new TimeTableFragment(),"ddd");
        transaction.addToBackStack(null);
        transaction.commit();

        //初回アップデート時
        SharedPreferences pref = getSharedPreferences("sample", Context.MODE_PRIVATE);
        //初回時はtrueを設定
        String isUpdateFirst = pref.getString("teachers","empty");
        if(isUpdateFirst == "empty"){

            showDialog(0,MainActivity.this);
        }
    }

    //NavigationDrawerに名前、学籍番号をセットする
    //onWindowFocusChangedで行わないとbuild23以上でエラー
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //レイアウトへid,名前の設定
        LoadManager loadManager = new LoadManager();
        List<String> ipList = new ArrayList<>();
        ipList = loadManager.loadManagerWithPreferenceForString(MainActivity.this,PREF_NAME_ID_PASS,"ip");
        textView = (TextView)findViewById(R.id.num_text);
        try{
            textView.setText(ipList.get(0));
        }catch(NullPointerException e){
            textView.setText("NO_NUM");
        }

        SharedPreferences data = getSharedPreferences("username", Context.MODE_PRIVATE);
        String userName = data.getString("username",getString(R.string.no_name));
        textViewUserName = (TextView)findViewById(R.id.name_text);
        textViewUserName.setText(userName);
    }

    private void initView() {
        drawer= (NavigationView) findViewById(R.id.navigation_drawer);
        drawer.setNavigationItemSelectedListener(this);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    Fragment fragment1;
    private void itemSelection(int mSelectedId) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        switch(mSelectedId){
            //時間割
            case R.id.navigation_item_time_table:

                if(selectedItem == 0){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return;
                }
                fragment1 = new TimeTableFragment();
                selectedItem = 0;
                break;

            //出席照会
            case R.id.navigation_item_attendance_rate:
                // addを呼んでいるので、重なる
                if(selectedItem == 1){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return;
                }

                selectedItem = 1;
                fragment1 = new AttendanceRateFragment();
                break;

            //年間スケジュール
            case R.id.navigation_item_schedule:
                Intent intent2 = new Intent(MainActivity.this,SyllabusActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.pull_in_up , R.anim.none_anim);
                drawerLayout.closeDrawer(GravityCompat.START);
                return;

            //設定
            case R.id.navigation_item_settings:
                Intent intent5 =new Intent(MainActivity.this,PreferenceRelationActivity.class);
                startActivityForResult(intent5,999);

                overridePendingTransition(R.anim.pull_in_up , R.anim.none_anim);
                drawerLayout.closeDrawer(GravityCompat.START);
                return;

            //Web版
            case R.id.navigation_item_web:
                drawerLayout.closeDrawer(GravityCompat.START);

                Uri uri = Uri.parse("http://comp2.ecc.ac.jp/sutinfo/login");

                //SelectAppを表示させないためパッケージ名を指定
                String PACKAGE_NAME = "com.android.chrome";

                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                        .setShowTitle(true)
                        .setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary))
                        .setStartAnimations(MainActivity.this, R.anim.pull_in_up , R.anim.none_anim)
                        .setExitAnimations(MainActivity.this, R.anim.none_anim, R.anim.push_out_up)
                        .build();

                customTabsIntent.intent.setData(uri);

                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolveInfo : resolveInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    if (TextUtils.equals(packageName, PACKAGE_NAME))
                        customTabsIntent.intent.setPackage(PACKAGE_NAME);
                }
                customTabsIntent.launchUrl(this, uri);

                return;
            default:
                break;
        }

        //デバッグ時にPCからインストールする際NullpointerExceptionがここで発生する
        try{
            manager.beginTransaction()
                    .replace(R.id.fragment_container, fragment1, "dd")
                    .addToBackStack(null)
                    .commit();
            drawerLayout.closeDrawer(GravityCompat.START);

        }catch(NullPointerException e) {
            Log.d("上書きインストールの際Exception::",e.toString());
        }

    }

    //設定画面起動時、ログアウトをした場合MainActivityもfinishする
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 999 && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            if (extras != null)
            {
                String text = extras.getString("text");
                if ("終了".equals(text))
                {
                    MainActivity.this.finish();
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        selectedId=menuItem.getItemId();
        itemSelection(selectedId);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("SELECTED_ID",selectedId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){

            // ボタンが押されたとき
            final MaterialDialog dialog = new MaterialDialog(this);
            dialog
                    .setTitle("アプリケーション終了")
                    .setMessage("アプリケーションを終了してよろしいですか？")
                    .setPositiveButton("YES", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            finish();
                            moveTaskToBack(true);

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
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        super.onKeyDown(keyCode, event);
//        // 戻るボタンが押されたとき
////        if(keyCode == KeyEvent.KEYCODE_BACK){
//            // ボタンが押されたとき
////            mMaterialDialog = new MaterialDialog(this)
////                    .setTitle("アプリケーションの終了")
////                    .setMessage("アプリケーションを終了してよろしいですか？")
////                    .setPositiveButton("OK", new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            mMaterialDialog.dismiss();
////                            finish();
////                        }
////                    })
////                    .setNegativeButton("CANCEL", new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            mMaterialDialog.dismiss();
////
//////
////                            Snackbar.make(v,"キャンセルしました",Snackbar.LENGTH_SHORT).show();
////                        }
////                    });
//
//        //    mMaterialDialog.show();
//            if (keyCode == KeyEvent.KEYCODE_BACK){
//                new AlertDialog.Builder(this)
//                        .setTitle("アプリケーションの終了")
//                        .setMessage("アプリケーションを終了してよろしいですか？")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO 自動生成されたメソッド・スタブ
//                                MainActivity.this.finish();
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO 自動生成されたメソッド・スタブ
//
//                            }
//                        })
//                        .show();
//
//                return true;
//            }
//            return false;
////        }
////
////        return false;
//    }

    @Override
    public void onResume(){
        super.onResume();
        //時間割のデータを更新した場合
        SharedPreferences sharedPreferences = getSharedPreferences("restart_fragment",MODE_PRIVATE);
        Boolean flag = sharedPreferences.getBoolean("RESTART_FRAGMENT", false);

        if(flag){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            // addを呼んでいるので、重なる
            transaction.add(R.id.fragment_container, new TimeTableFragment(),"ddd");
            transaction.addToBackStack(null);
            transaction.commit();

            //プリファレンスのデータにfalseを設定
            SharedPreferences preferences = getSharedPreferences("restart_fragment",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RESTART_FRAGMENT",false);
            editor.apply();
            itemSelection(R.id.navigation_item_time_table);
        }

        Log.d("MainActivity::::","onResume");
    }

    CustomProgressDialog cProg;
    private void CopyAssets(final Context context) {
        cProg = new CustomProgressDialog();
        //  ダイアログを表示
        prog = cProg.createProgressDialogForFileRead(context);
        prog.show();
;
        new AsyncTask<String,String,String>(){

            @Override
            protected String doInBackground(String... params) {

                AssetManager assetManager = getAssets();

                InputStream in = null;
                OutputStream out = null;
                File file = new File(getFilesDir(), PDF_FILE_NAME);
                try {
                    in = assetManager.open(PDF_FILE_NAME);
                    out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                prog.dismiss();
            }
        }.execute("");
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    //アプリケーションアップデート時にダイアログを表示したい時に使用する
    MaterialDialog mMaterialDialog;
    ProgressDialog prog;
    final int LOGOUT_DIALOG = 0;
    public void showDialog(int id, final Context context){
        prog = new ProgressDialog(context);
        switch(id){
            case LOGOUT_DIALOG:
                mMaterialDialog = new MaterialDialog(context)
                        .setTitle("UPDATE")
                        .setMessage("アプリケーションがアップデートされました。\n" +
                                "時間割の詳細を表示できるようになりました。\n" +
                                "新しい機能を利用するために時間割をアップデートしてください。")
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AsyncTask<String,Void,String>(){
                                    @Override
                                    protected  void onPreExecute(){
                                        super.onPreExecute();

                                    }
                                    @Override
                                    protected String doInBackground(String... strings) {
                                        return "";
                                    }

                                    @Override
                                    protected void onPostExecute(String result){
                                        List<String> list = new ArrayList<String>();
                                        LoadManager loadManager = new LoadManager();
                                        list = loadManager.loadManagerWithPreferenceForString(MainActivity.this,PREF_NAME_ID_PASS,"ip");

                                        String userId = list.get(0);
                                        String pass = list.get(1);
                                        //View view = getActivity().findViewById(R.id.time_table_root);
                                        UpdateTimeTableManager utt = new UpdateTimeTableManager();
                                        //時間割情報のアップデート
                                        utt.upDateTimeTable(MainActivity.this,findViewById(R.id.drawer_layout),userId,pass);

                                        SharedPreferences preferences = MainActivity.this.getSharedPreferences("restart_fragment",MainActivity.this.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putBoolean("RESTART_FRAGMENT",true);
                                        editor.apply();

                                        mMaterialDialog.dismiss();

                                    }
                                }.execute();
                            }
                        });

                mMaterialDialog.show();
                break;

        }
    }


}
