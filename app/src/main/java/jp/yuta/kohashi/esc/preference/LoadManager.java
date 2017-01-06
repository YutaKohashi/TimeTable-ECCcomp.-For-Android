package jp.yuta.kohashi.esc.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.model.AttendanceRateModel;
import jp.yuta.kohashi.esc.model.CustomTimeTableCell;
import jp.yuta.kohashi.esc.model.NewsChildModel;
import jp.yuta.kohashi.esc.model.TimeBlockModel;

/**
 * Created by YutaThinkPad on 2016/03/25.
 */
public class LoadManager {

    public static List<TimeBlockModel> loadTimeBlockList(String key,Context context){
        SharedPreferences sharedPreferences;
        List<TimeBlockModel> arrayList;
        sharedPreferences = context.getSharedPreferences(PrefConst.FILE_TIME_ATTEND,context.MODE_PRIVATE);
        arrayList = new Gson().fromJson(sharedPreferences.getString(key,""),new TypeToken<List<TimeBlockModel>>(){}.getType());
        return arrayList;
    }

    public static List<AttendanceRateModel> loadAttendanceRateModelList(Context context, String prefName, String key){
        SharedPreferences sharedPreferences;
        List<AttendanceRateModel> arrayList;
        sharedPreferences = context.getSharedPreferences(prefName,context.MODE_PRIVATE);
        arrayList = new Gson().fromJson(sharedPreferences.getString(key,""),new TypeToken<List<AttendanceRateModel>>(){}.getType());
        return arrayList;
    }

//    public List<TeacherNameObject> loadManagerWithPreferenceForTeacherName(Context context, String prefName, String key){
//        SharedPreferences sharedPreferences;
//        List<TeacherNameObject> arrayList;
//
//        sharedPreferences = context.getSharedPreferences(prefName,context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        arrayList = new ArrayList<>();
//        arrayList = gson.fromJson(sharedPreferences.getString(key,""),new TypeToken<List<TeacherNameObject>>(){}.getType());
//
//
//        return arrayList;
//    }

    public static List<NewsChildModel> loadNewsChildModelList(Context context, String key){
        SharedPreferences sharedPreferences;
        List<NewsChildModel> arrayList;
        sharedPreferences = context.getSharedPreferences(PrefConst.FILE_NEWS,context.MODE_PRIVATE);
        arrayList = new Gson().fromJson(sharedPreferences.getString(key,""),new TypeToken<List<NewsChildModel>>(){}.getType());
        return arrayList;
    }

    public static List<CustomTimeTableCell> loadCustomTimeTableCellList(Context context, String key){
        SharedPreferences sharedPreferences;
        List<CustomTimeTableCell> arrayList;
        sharedPreferences = context.getSharedPreferences(PrefConst.FILE_TIME_ATTEND,context.MODE_PRIVATE);
        Gson gson = new Gson();
        arrayList = gson.fromJson(sharedPreferences.getString(key,""),new TypeToken<List<CustomTimeTableCell>>(){}.getType());
        return arrayList;
    }
}
