package com.example.yutathinkpad.esc.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.yutathinkpad.esc.object.TimeBlock;
import com.google.gson.Gson;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YutaThinkPad on 2016/03/25.
 */
public class SaveManager {

    public SaveManager(){

    }

    public void saveMangerWithPreference(Context context, String prefName, List<TimeBlock> arrayList, String key){
        SharedPreferences sharedPreferences;
        Gson gson;

        sharedPreferences = context.getSharedPreferences(prefName,context.MODE_PRIVATE);
        gson = new Gson();
        sharedPreferences.edit().putString(key,gson.toJson(arrayList)).commit();
    }
}
