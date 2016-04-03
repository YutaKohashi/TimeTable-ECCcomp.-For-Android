package jp.yuta.kohashi.esc.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.view.View;
import android.preference.PreferenceFragment;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.activity.LoginActivity;
import jp.yuta.kohashi.esc.http.UpdateTimeTableManager;
import jp.yuta.kohashi.esc.preference.LoadManager;
import jp.yuta.kohashi.esc.tools.CustomProgressDialog;
import jp.yuta.kohashi.esc.tools.GetValuesBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class PreferenceRelationFragment extends PreferenceFragment{
    PreferenceScreen logout_item;
    PreferenceScreen update_time_table_item;
    static final String PREF_NAME_ID_PASS = "ip";
    CustomProgressDialog customProgressDialog;
    ProgressDialog dialog;

    public PreferenceRelationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.settings_item);


        logout_item = (PreferenceScreen)findPreference("logout_button");
        update_time_table_item= (PreferenceScreen)findPreference("update_time_table");

        logout_item.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDialog(LOGOUT_DIALOG, getActivity());

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
                editor.apply();

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

                                        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("ip",getActivity().MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.commit();

                                        sharedPreferences =getActivity().getSharedPreferences("sample",getActivity().MODE_PRIVATE);
                                        editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.commit();

                                        sharedPreferences =getActivity().getSharedPreferences("username",getActivity().MODE_PRIVATE);
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
//
                                Snackbar.make(v,"キャンセルしました",Snackbar.LENGTH_SHORT).show();
                            }
                        });

                mMaterialDialog.show();
                break;

        }
    }



}
