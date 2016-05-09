package jp.yuta.kohashi.esc.preference;

import android.content.Context;
import android.content.SharedPreferences;

import jp.yuta.kohashi.esc.object.AttendanceRateObject;
import jp.yuta.kohashi.esc.object.TeacherNameObject;
import jp.yuta.kohashi.esc.object.TimeBlock;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YutaThinkPad on 2016/03/25.
 */
public class LoadManager {

    public LoadManager(){

    }

    /**
     *
     * @param context
     * @param prefName
     * @param key
     * @return
     */
    public List<TimeBlock> loadManagerWithPreferenceForTimeBlock(Context context, String prefName, String key){
        SharedPreferences sharedPreferences;
        List<TimeBlock> arrayList;

        sharedPreferences = context.getSharedPreferences(prefName,context.MODE_PRIVATE);
        Gson gson = new Gson();
        arrayList = new ArrayList<>();
        arrayList = gson.fromJson(sharedPreferences.getString(key,""),new TypeToken<List<TimeBlock>>(){}.getType());


        return arrayList;
    }

    public List<String> loadManagerWithPreferenceForString(Context context, String prefName, String key){
        SharedPreferences sharedPreferences;
        List<String> arrayList;

        sharedPreferences = context.getSharedPreferences(prefName,context.MODE_PRIVATE);
        Gson gson = new Gson();
        arrayList = new ArrayList<>();
        arrayList = gson.fromJson(sharedPreferences.getString(key,""),new TypeToken<List<String>>(){}.getType());


        return arrayList;
    }

    public List<AttendanceRateObject> loadManagerWithPreferenceForAttendance(Context context, String prefName, String key){
        SharedPreferences sharedPreferences;
        List<AttendanceRateObject> arrayList;

        sharedPreferences = context.getSharedPreferences(prefName,context.MODE_PRIVATE);
        Gson gson = new Gson();
        arrayList = new ArrayList<>();
        arrayList = gson.fromJson(sharedPreferences.getString(key,""),new TypeToken<List<AttendanceRateObject>>(){}.getType());


        return arrayList;
    }

    public List<TeacherNameObject> loadManagerWithPreferenceForTeacherName(Context context, String prefName, String key){
        SharedPreferences sharedPreferences;
        List<TeacherNameObject> arrayList;

        sharedPreferences = context.getSharedPreferences(prefName,context.MODE_PRIVATE);
        Gson gson = new Gson();
        arrayList = new ArrayList<>();
        arrayList = gson.fromJson(sharedPreferences.getString(key,""),new TypeToken<List<TeacherNameObject>>(){}.getType());


        return arrayList;
    }
}
