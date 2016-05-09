package jp.yuta.kohashi.esc.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.WindowCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.fragment.AttendanceRateFragment;
import jp.yuta.kohashi.esc.fragment.TimeTableFragment;
import jp.yuta.kohashi.esc.http.UpdateTimeTableManager;
import jp.yuta.kohashi.esc.preference.LoadManager;
import jp.yuta.kohashi.esc.tools.CustomProgressDialog;
import jp.yuta.kohashi.esc.tools.GetValuesBase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);


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
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        //default it set first item as selected
        selectedId=savedInstanceState ==null ? R.id.navigation_item_1: savedInstanceState.getInt("SELECTED_ID");
        itemSelection(selectedId);

        //レイアウトへid,名前の設定
        LoadManager loadManager = new LoadManager();
        List<String> ipList = new ArrayList<>();
        ipList = loadManager.loadManagerWithPreferenceForString(MainActivity.this,PREF_NAME_ID_PASS,"ip");
        textView = (TextView)findViewById(R.id.num_text);
        textView.setText(ipList.get(0));

        SharedPreferences data = getSharedPreferences("username", Context.MODE_PRIVATE);
        String userName = data.getString("username","NO_NAME");
        textViewUserName = (TextView)findViewById(R.id.name_text);
        textViewUserName.setText(userName);
        //通信
        //UpdateTimeTableManager utt = new UpdateTimeTableManager();
        //utt.upDateTimeTable(MainActivity.this);

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

//    private void setToolbar() {
//        toolbar= (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//        }

    //  }

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
            case R.id.navigation_item_1:

                if(selectedItem == 0){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return;
                }
                fragment1 = new TimeTableFragment();
                selectedItem = 0;
                break;

            //出席照会
            case R.id.navigation_item_2:
                // addを呼んでいるので、重なる
                if(selectedItem == 1){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return;
                }

                selectedItem = 1;
                fragment1 = new AttendanceRateFragment();
                break;

//            case R.id.navigation_item_3:
//                Intent intent1 = new Intent(MainActivity.this,NewsActivity.class);
//                startActivity(intent1);
//                overridePendingTransition(R.anim.pull_in_up , R.anim.none_anim);
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return;

            case R.id.navigation_item_7:
                Intent intent2 = new Intent(MainActivity.this,SyllabusActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.pull_in_up , R.anim.none_anim);
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
//
         //   PDF
//            case R.id.handbook_item4:
//                CopyAssets(this);
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return;

//            //お知らせ(Activity)
//            case R.id.navigation_news:
//                Intent intent9 = new Intent(MainActivity.this,NewsActivity.class);
//                startActivity(intent9);
//                overridePendingTransition(R.anim.pull_in_up , R.anim.none_anim);
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return;

            //設定
            case R.id.navigation_item_5:
                Intent intent5 =new Intent(MainActivity.this,PreferenceRelationActivity.class);
                startActivity(intent5);

                overridePendingTransition(R.anim.pull_in_up , R.anim.none_anim);
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            //このアプリについて
            case R.id.navigation_item_6:
                Intent intent3 =new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent3);

                overridePendingTransition(R.anim.pull_in_up , R.anim.none_anim);
                drawerLayout.closeDrawer(GravityCompat.START);
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
//        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("SELECTED_ID",selectedId);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        // 戻るボタンが押されたとき
//        if(e.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            // ボタンが押されたとき
//            mMaterialDialog = new MaterialDialog(this)
//                    .setTitle("アプリケーションの終了")
//                    .setMessage("アプリケーションを終了してよろしいですか？")
//                    .setPositiveButton("OK", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mMaterialDialog.dismiss();
//                            finish();
//
//                        }
//                    })
//                    .setNegativeButton("CANCEL", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mMaterialDialog.dismiss();
////
//                            Snackbar.make(v,"キャンセルしました",Snackbar.LENGTH_SHORT).show();
//                        }
//                    });
//
//            mMaterialDialog.show();
//            return true;
//
//        }
        finish();
        return super.dispatchKeyEvent(e);
    }

    @Override
    public void onResume(){
        super.onResume();
        //時間割のデータを行使した場合
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
            itemSelection(R.id.navigation_item_1);


        }

        Log.d("MainActivity::::","onResume");
    }



    CustomProgressDialog cProg;
    private void CopyAssets(final Context context) {
        cProg = new CustomProgressDialog();
        //  ダイアログを表示
        prog = cProg.createProgressDialogForFileRead(context);
        prog.show();
//        if (prog == null) {
//
//            prog.show();
//        } else {
//            prog.show();
//        }
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
//                Intent intent = new Intent(context, MuPDFActivity.class);
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setDataAndType(
//                        Uri.parse("file://" + getFilesDir() + "/" + PDF_FILE_NAME),
//                        "application/pdf");
//                startActivity(intent);
//
//
//                overridePendingTransition(R.anim.pull_in_up , R.anim.none_anim);
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

    MaterialDialog mMaterialDialog;
    ProgressDialog prog;
    final int LOGOUT_DIALOG = 0;
    public void showDialog(int id, final Context context){
        prog = new ProgressDialog(context);
        switch(id){
            case LOGOUT_DIALOG:
                mMaterialDialog = new MaterialDialog(context)
                        .setTitle("ECCAPP")
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
