package com.example.yutathinkpad.esc.fragment;


import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.preference.PreferenceFragment;

import com.example.yutathinkpad.esc.R;
import com.example.yutathinkpad.esc.activity.LoginActivity;
import com.example.yutathinkpad.esc.activity.MainActivity;
import com.example.yutathinkpad.esc.tools.GetValuesBase;

import me.drakeet.materialdialog.MaterialDialog;

public class PreferenceRelationFragment extends PreferenceFragment{
    PreferenceScreen preferenceScreen;


    public PreferenceRelationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.settings_item);

        preferenceScreen = (PreferenceScreen)findPreference("logout_button");

        preferenceScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDialog(LOGOUT_DIALOG, getActivity());

                return false;
            }
        });
    }

    MaterialDialog mMaterialDialog;
    final int LOGOUT_DIALOG = 0;
    public void showDialog(int id, Context context){
        switch(id){
            case LOGOUT_DIALOG:
                mMaterialDialog = new MaterialDialog(context)
                        .setTitle("ログアウト")
                        .setMessage("アプリケーションからアカウントを消去します")
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GetValuesBase getValuesBase = new GetValuesBase();
                                getValuesBase.SetLoginState(getActivity(),false);
                                mMaterialDialog.dismiss();

                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);


                            }
                        })
                        .setNegativeButton("CANCEL", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
//
//                                Snackbar.make(v,"キャンセルしました",)
                            }
                        });

                mMaterialDialog.show();
                break;

        }
    }



}
