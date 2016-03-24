package com.example.yutathinkpad.esc.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.yutathinkpad.esc.object.TimeBlock;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YutaThinkPad on 2016/03/25.
 */
public class LoadManager {

    public LoadManager(){

    }

    public List<TimeBlock> loadManagerWithPreference(Context context, String prefName, String key){
        SharedPreferences sharedPreferences;
        List<TimeBlock> arrayList;

        sharedPreferences = context.getSharedPreferences(prefName,context.MODE_PRIVATE);
        Gson gson = new Gson();
        arrayList = new ArrayList<>();
        arrayList = gson.fromJson(sharedPreferences.getString(key,""),new TypeToken<List<TimeBlock>>(){}.getType());


        return arrayList;
    }
}
