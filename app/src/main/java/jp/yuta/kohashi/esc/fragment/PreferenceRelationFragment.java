package jp.yuta.kohashi.esc.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.preference.PreferenceFragment;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.activity.CustomTimeTableActivity;
import jp.yuta.kohashi.esc.activity.LoginActivity;
import jp.yuta.kohashi.esc.http.UpdateTimeTableManager;
import jp.yuta.kohashi.esc.preference.LoadManager;
import jp.yuta.kohashi.esc.tools.CustomProgressDialog;
import jp.yuta.kohashi.esc.tools.GetValuesBase;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class PreferenceRelationFragment extends PreferenceFragment{
    PreferenceScreen logout_item;
    PreferenceScreen update_time_table_item;
    PreferenceScreen view_tutorial;
    static final String PREF_NAME_ID_PASS = "ip";
    CustomProgressDialog customProgressDialog;
    ProgressDialog dialog;
    PreferenceScreen sbout_app;
    PreferenceScreen lisence;
    PreferenceScreen aboutApp;
//    static WebView webView;
//    static View view;
    Dialog mBottomSheetDialog;
    Toolbar toolbar;

    PreferenceScreen custom_time_table;

    public PreferenceRelationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.settings_item);

        logout_item = (PreferenceScreen)findPreference("logout_button");
        update_time_table_item= (PreferenceScreen)findPreference("update_time_table");
        view_tutorial = (PreferenceScreen)findPreference("view_tutorial");
        lisence = (PreferenceScreen)findPreference("license_screen");
        aboutApp = (PreferenceScreen)findPreference("about_screen");
        custom_time_table = (PreferenceScreen)findPreference("custom_time_table");


        logout_item.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDialog(LOGOUT_DIALOG, getActivity());

                return false;
            }
        });

        view_tutorial.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //チュートリアルのフラグを保存しているPreferenceをクリアする
                SharedPreferences sharedPreferences =getActivity().getSharedPreferences("material_showcaseview_prefs",getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                //ActivityiyForResultによる返り値を設定
                Intent intent1 = new Intent();
                intent1.putExtra("text2", "チュートリアル");
                getActivity().setResult(Activity.RESULT_OK, intent1);

                getActivity().finish();


                return false;
            }
        });

        update_time_table_item.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                List<String> list = new ArrayList<String>();
                LoadManager loadManager = new LoadManager();
                list = loadManager.loadManagerWithPreferenceForString(getActivity(),PREF_NAME_ID_PASS,"ip");

                String userId = list.get(0);
                String pass = list.get(1);
                //View view = getActivity().findViewById(R.id.time_table_root);
                UpdateTimeTableManager utt = new UpdateTimeTableManager();
                utt.upDateTimeTable(getActivity(),getView(),userId,pass);

                SharedPreferences preferences = getActivity().getSharedPreferences("restart_fragment",getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("RESTART_FRAGMENT",true);
                editor.commit();

                return false;
            }
        });
        lisence.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                View view = getActivity().getLayoutInflater ().inflate (R.layout.activity_lisence, null);

                WebView webView = (WebView)view.findViewById(R.id.webView2);
                webView.loadUrl( "file:///android_asset/lisence.html" );

                mBottomSheetDialog = new Dialog (getActivity(), R.style.MaterialDialogSheet);
                mBottomSheetDialog.setContentView (view);
                mBottomSheetDialog.setCancelable (true);
                mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
                mBottomSheetDialog.show ();

                //ツールバーの戻るボタンをタップした時finishメソッドを呼び出す
                ImageButton imgButton = (ImageButton) view.findViewById(R.id.lisence_back_button);
                imgButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog.dismiss();
                    }
                });

                return false;
            }
        });

        aboutApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                View view = getActivity().getLayoutInflater ().inflate (R.layout.activity_about, null);

                WebView webView = (WebView)view.findViewById(R.id.webView1);
                webView.loadUrl( "file:///android_asset/about_html.html" );

                mBottomSheetDialog = new Dialog (getActivity(), R.style.MaterialDialogSheet);
                mBottomSheetDialog.setContentView (view);
                mBottomSheetDialog.setCancelable (true);
                mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
                mBottomSheetDialog.show ();

                //ツールバーの戻るボタンをタップした時finishメソッドを呼び出す
                ImageButton imgButton = (ImageButton) view.findViewById(R.id.about_back_button);
                imgButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog.dismiss();
                    }
                });

                return false;
            }
        });

        custom_time_table.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                View view = getActivity().getLayoutInflater ().inflate (R.layout.activity_custom_time_table, null);

                mBottomSheetDialog = new Dialog (getActivity(), R.style.MaterialDialogSheet);
                mBottomSheetDialog.setContentView (view);
                mBottomSheetDialog.setCancelable (true);
                mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
                mBottomSheetDialog.show ();

                //ツールバーの戻るボタンをタップした時finishメソッドを呼び出す
                ImageButton imgButton = (ImageButton) view.findViewById(R.id.about_back_button);
                imgButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog.dismiss();
                    }
                });

                return false;
            }
        });

    }

    MaterialDialog mMaterialDialog;
    ProgressDialog prog;
    final int LOGOUT_DIALOG = 0;
    public void showDialog(int id, final Context context){
        prog = new ProgressDialog(context);
        switch(id){
            case LOGOUT_DIALOG:
                mMaterialDialog = new MaterialDialog(context)
                        .setTitle("ログアウト")
                        .setMessage("アプリケーションからアカウントを消去します")
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AsyncTask<String,Void,String>(){
                                    @Override
                                    protected  void onPreExecute(){
                                        super.onPreExecute();
                                        customProgressDialog = new CustomProgressDialog();
                                        dialog= customProgressDialog .createProgressDialogForLogout(context);
                                        dialog.show();
                                    }
                                    @Override
                                    protected String doInBackground(String... strings) {
                                        GetValuesBase getValuesBase = new GetValuesBase();

                                        mMaterialDialog.dismiss();

                                        //学籍番号：パスワード
                                        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("ip",getActivity().MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.commit();

                                        sharedPreferences =getActivity().getSharedPreferences("sample",getActivity().MODE_PRIVATE);
                                        editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.commit();

                                        //ユーザの名前
                                        sharedPreferences =getActivity().getSharedPreferences("username",getActivity().MODE_PRIVATE);
                                        editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.commit();

                                        //チュートリアルのフラグ
                                       sharedPreferences =getActivity().getSharedPreferences("material_showcaseview_prefs",getActivity().MODE_PRIVATE);
                                         editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.commit();

                                        getValuesBase.SetLoginState(getActivity(),false);

                                        try{
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        return "";
                                    }

                                    @Override
                                    protected void onPostExecute(String result){

                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        intent.putExtra("logouted",true);
                                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                        Intent intent1 = new Intent();
                                        intent1.putExtra("text", "終了");
                                        ((Activity)context).setResult(Activity.RESULT_OK, intent1);
                                        ((Activity)context).finish();

                                        dialog.dismiss();
                                    }
                                }.execute();
                            }
                        })
                        .setNegativeButton("CANCEL", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();

                                Snackbar.make(v,"キャンセルしました",Snackbar.LENGTH_SHORT).show();
                            }
                        });

                mMaterialDialog.show();
                break;

        }
    }


}
