package jp.yuta.kohashi.esc.util.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import jp.yuta.kohashi.esc.App;
import jp.yuta.kohashi.esc.Const;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.AttendanceRate;
import jp.yuta.kohashi.esc.network.api.model.news.NewsItem;
import jp.yuta.kohashi.esc.network.api.model.schedule.ScheduleRoot;
import jp.yuta.kohashi.esc.network.api.model.timeTable.TimeTable;
import jp.yuta.kohashi.esc.util.RegexUtil;
import jp.yuta.kohashi.esc.util.Util;


/**
 * Created by yutakohashi on 2017/01/06.
 */

public class PrefUtil {
    private static final String TAG = PrefUtil.class.getSimpleName();

    /************************************* public  *******************************************************/

    //**
    //region Load
    //**

    /**
     * 時間割をろーど
     * @return
     */
    public static List<List<TimeTable>> loadTimeBlockList(){
        return loadTimeBlockList(App.getAppContext());
    }

    /**
     * 外部からcontextを受け付ける
     *
     * @param context
     * @return
     */
    public static List<List<TimeTable>> loadTimeBlockList(Context context) {
        List<List<TimeTable>> lists = new ArrayList<>();
        lists.add((loadTimeBlockList(PrefConst.KEY_SUN_LIST,context)));
        lists.add((loadTimeBlockList(PrefConst.KEY_MON_LIST,context)));
        lists.add((loadTimeBlockList(PrefConst.KEY_TUE_LIST,context)));
        lists.add((loadTimeBlockList(PrefConst.KEY_WED_LIST,context)));
        lists.add((loadTimeBlockList(PrefConst.KEY_THUR_LIST,context)));
        lists.add((loadTimeBlockList(PrefConst.KEY_FRI_LIST,context)));
        lists.add((loadTimeBlockList(PrefConst.KEY_SAT_LIST,context)));

        return lists;
    }

    /**
     * オリジナルをloadする
     * @return
     */
    public static List<List<TimeTable>> loadOriginalTimeBlockList() {
        List<List<TimeTable>> lists = new ArrayList<>();
        lists.add((loadTimeBlockList(PrefConst.KEY_SUN_LIST_ORIGINAL)));
        lists.add((loadTimeBlockList(PrefConst.KEY_MON_LIST_ORIGINAL)));
        lists.add((loadTimeBlockList(PrefConst.KEY_TUE_LIST_ORIGINAL)));
        lists.add((loadTimeBlockList(PrefConst.KEY_WED_LIST_ORIGINAL)));
        lists.add((loadTimeBlockList(PrefConst.KEY_THUR_LIST_ORIGINAL)));
        lists.add((loadTimeBlockList(PrefConst.KEY_FRI_LIST_ORIGINAL)));
        lists.add((loadTimeBlockList(PrefConst.KEY_SAT_LIST_ORIGINAL)));
        return lists;
    }

    public static List<TimeTable> loadTimeBlockList(String key) {
        return loadTimeBlockList(key, App.getAppContext());
    }

    /**
     * 外部からcontextを受け付ける（ウィジェットで使用）
     *
     * @param key
     * @param context
     * @return
     */
    public static List<TimeTable> loadTimeBlockList(String key, Context context) {
        SharedPreferences pref = context.getSharedPreferences(PrefConst.FILE_TIME_TABLE, Context.MODE_PRIVATE);
        List<TimeTable> list = new Gson().fromJson(pref.getString(key,""),new TypeToken<List<TimeTable>>(){}.getType());

        try{
            if(list.size()== 0) list= new ArrayList<>();
        } catch (Exception e){
            list = new ArrayList<>();
        }
        return list;
    }

    /***
     * 出席率のリストをロードするメソッド
     *
     * @return
     */
    public static List<AttendanceRate> loadAttendanceRateModelList() {
        SharedPreferences sharedPreferences;
        List<AttendanceRate> arrayList;
        sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_ATTEND, Context.MODE_PRIVATE);

        try {
            arrayList = new Gson().fromJson(sharedPreferences.getString(PrefConst.KEY_ATTEND, ""), new TypeToken<List<AttendanceRate>>() {
            }.getType());
            if (arrayList.size() == 0) {
                arrayList = new ArrayList<>();
            }
        } catch (Exception e) {
            arrayList = new ArrayList<>();
        }

        return arrayList;
    }

    public static AttendanceRate loadAttendanceTotalData() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_ATTEND, Context.MODE_PRIVATE);
        AttendanceRate model = null;
        try {
            model = new Gson().fromJson(sharedPreferences.getString(PrefConst.KEY_ATTEND_ALL_RATE, ""), new TypeToken<AttendanceRate>() {
            }.getType());
            if (model == null) {
                model = new AttendanceRate();
            }
        } catch (Exception e) {
            model = new AttendanceRate();
            Log.d(TAG, e.toString());
        }

        return model;
    }

    /***
     * 学校からのお知らせを取得するメソッド
     *
     * @return
     */
    public static List<NewsItem> loadSchoolNewsList() {
        SharedPreferences sharedPreferences;
        List<NewsItem> arrayList;
        sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_NEWS, Context.MODE_PRIVATE);

        try {
            arrayList = new Gson().fromJson(sharedPreferences.getString(PrefConst.KEY_SCHOOL_NEWS, ""), new TypeToken<List<NewsItem>>() {
            }.getType());

            if (arrayList.size() == 0) {
                arrayList = new ArrayList<>();
            }
        } catch (Exception e) {
            arrayList = new ArrayList<>();
        }
        return arrayList;
    }

    /**
     * 　担任からのお知らせを取得するメソッド
     *
     * @return
     */
    public static List<NewsItem> loadTanninNewsList() {
        SharedPreferences sharedPreferences;
        List<NewsItem> arrayList;
        sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_NEWS, Context.MODE_PRIVATE);

        try {
            arrayList = new Gson().fromJson(sharedPreferences.getString(PrefConst.KEY_TANNIN_NEWS, ""), new TypeToken<List<NewsItem>>() {
            }.getType());
            if (arrayList.size() == 0) {
                arrayList = new ArrayList<>();
            }
        } catch (Exception e) {
            arrayList = new ArrayList<>();
        }

        return arrayList;
    }

    /**
     * スケジュールを取得するメソッド
     * @return
     */
    public static List<ScheduleRoot> loadSchedule(){
        SharedPreferences preferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_SCHEDULE, Context.MODE_PRIVATE);
        List<ScheduleRoot> scheduleRoots;
        try{
            scheduleRoots = new Gson().fromJson(preferences.getString(PrefConst.KEY_SCHEDULE, ""), new TypeToken<List<ScheduleRoot>>() {
            }.getType());
        } catch( Exception e) {
            Log.d(TAG,e.toString());
            scheduleRoots = new ArrayList<>();
        }

        return scheduleRoots;
    }

    /***
     * ログインIDを取得するメソッド
     *
     * @return
     */
    public static String getId() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_ID_PASS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PrefConst.KEY_ID, null);
    }

    /**
     * ログインpassをロードするメソッド
     *
     * @return
     */
    public static String getPss() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_ID_PASS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PrefConst.KEY_PASS, null);
    }

    /**
     * ログイン状態を取得するメソッド
     *
     * @return
     */
    public static boolean getLoginState() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_STATE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_LOGIN_STATE, false); //default:false
    }

    /**
     * 初回起動の状態を取得するメソッド
     *
     * @return
     */
    public static boolean getFirstTime() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_STATE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_FIRST_TIME, false);
    }

    public static String getAppVersion() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PrefConst.KEY_APP_VERSION, "-1");
    }

    /**
     * ユーザ名を保存
     */
    public static String getUserName() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_ID_PASS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PrefConst.KEY_USER_NAME, "");
    }

    public static String getCourse() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_ID_PASS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PrefConst.KEY_COURSE, "");
    }

    public static String getGakka() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_ID_PASS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PrefConst.KEY_GAKKA, "");
    }

    public static String getClassTxt() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_ID_PASS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PrefConst.KEY_CLASS, "");
    }

    public static String getShusekiNum() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_ID_PASS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PrefConst.KEY_SHUSEKI_NUM, "");
    }


    /**
     * 出席率色分け
     */
    public static int loadColorU90() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PrefConst.KEY_COLOR_U90, Util.getColor(R.color.bg_title));
    }

    public static int loadColorU81() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PrefConst.KEY_COLOR_U81, Util.getColor(R.color.bg_title));
    }

    public static int loadColorU75() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PrefConst.KEY_COLOR_U75, Util.getColor(R.color.bg_title));
    }

    public static int loadRateU90() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PrefConst.KEY_RATE_U90, 90);
    }

    public static int loadRateU81() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PrefConst.KEY_RATE_U81, 81);
    }

    public static int loadRateU75() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PrefConst.KEY_RATE_U75, 75);
    }

    public static int getAttendanceTabPosition(){
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PrefConst.KEY_ATTENDANCE_TAB_POSITION, 0);
    }

    public static long getLatestUpdateDate(){
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL,Context.MODE_PRIVATE);
        return sharedPreferences.getLong(PrefConst.KEY_LATEST_UPDATE_DATA, Util.getCurrentTimeMillis()); // dfault current time
    }


    //**
    //endregion
    //**


    //**
    //region save
    //**

    /**
     * 時間割を保存するメソッド
     * @param timeTables
     */
    public static void saveTimeTable(List<TimeTable> timeTables) {
        saveTimeTableMon(createWeekList(timeTables, 1));
        saveTimeTableTue(createWeekList(timeTables, 2));
        saveTimeTableWed(createWeekList(timeTables, 3));
        saveTimeTableThur(createWeekList(timeTables,4));
        saveTimeTableFri(createWeekList(timeTables, 5));
        saveTimeTableSat(createWeekList(timeTables, 6)); //土
        saveTimeTableSun(createWeekList(timeTables, 0)); //日
    }

    /**
     * オリジナル時間割を保存するメソッド
     * @param timeTables
     */
    public static void saveTimeTableOriginal(List<TimeTable> timeTables){
        save(createWeekList(timeTables, 1), PrefConst.KEY_MON_LIST_ORIGINAL, PrefConst.FILE_TIME_TABLE);
        save(createWeekList(timeTables, 2), PrefConst.KEY_TUE_LIST_ORIGINAL, PrefConst.FILE_TIME_TABLE);
        save(createWeekList(timeTables, 3), PrefConst.KEY_WED_LIST_ORIGINAL, PrefConst.FILE_TIME_TABLE);
        save(createWeekList(timeTables, 4), PrefConst.KEY_THUR_LIST_ORIGINAL, PrefConst.FILE_TIME_TABLE);
        save(createWeekList(timeTables, 5), PrefConst.KEY_FRI_LIST_ORIGINAL, PrefConst.FILE_TIME_TABLE);
        save(createWeekList(timeTables, 6), PrefConst.KEY_SAT_LIST_ORIGINAL, PrefConst.FILE_TIME_TABLE);
        save(createWeekList(timeTables, 0), PrefConst.KEY_SUN_LIST_ORIGINAL, PrefConst.FILE_TIME_TABLE);
    }

    public static void saveTimeTableMon(List<TimeTable> list) {
        save(list, PrefConst.KEY_MON_LIST, PrefConst.FILE_TIME_TABLE);
    }

    public static void saveTimeTableTue(List<TimeTable> list) {
        save(list, PrefConst.KEY_TUE_LIST, PrefConst.FILE_TIME_TABLE);
    }

    public static void saveTimeTableWed(List<TimeTable> list) {
        save(list, PrefConst.KEY_WED_LIST, PrefConst.FILE_TIME_TABLE);
    }

    public static void saveTimeTableThur(List<TimeTable> list) {
        save(list, PrefConst.KEY_THUR_LIST, PrefConst.FILE_TIME_TABLE);
    }

    public static void saveTimeTableFri(List<TimeTable> list) {
        save(list, PrefConst.KEY_FRI_LIST, PrefConst.FILE_TIME_TABLE);
    }

    public static void saveTimeTableSat(List<TimeTable> list) {
        save(list, PrefConst.KEY_SAT_LIST, PrefConst.FILE_TIME_TABLE);
    }

    public static void saveTimeTableSun(List<TimeTable> list) {
        save(list, PrefConst.KEY_SUN_LIST, PrefConst.FILE_TIME_TABLE);
    }

    /**
     * 出席率を保存するメソッド
     *
     * @param html
     */
    public static void saveAttendanceRate(String html) {
        List<AttendanceRate> attendanceRateList = createAttendanceList(html);
        saveAttendanceRate(attendanceRateList);
    }

    /**
     * 出席率を保存するメソッド
     */
    public static void saveAttendanceRate(List<AttendanceRate> attendanceRateList){
        save(attendanceRateList, PrefConst.KEY_ATTEND, PrefConst.FILE_ATTEND);
    }


    /**
     * 合計出席率データを保存するメソッド
     *
     * @param html
     */
    public static void saveAttendanceAllRateData(String html) {
        AttendanceRate attendanceRate = createAllRateData(html);
        save(attendanceRate, PrefConst.KEY_ATTEND_ALL_RATE, PrefConst.FILE_ATTEND);
    }

    public static void saveStudentInfo(String html) {
        String name = RegexUtil.getValues("id=\"ctl00_lblSimei\" style=\"[^\"]*\">(.+?)<",html);
        String classTxt = RegexUtil.getValues("id=\"ctl00_lblClass\" style=\"[^\"]*\">(.+?)</span>", html);
        String gakka = RegexUtil.getValues("id=\"ctl00_lblGakka\" style=\"[^\"]*\">(.+?)</span>", html);
        String course = RegexUtil.getValues("id=\"ctl00_lblCourse\" style=\"[^\"]*\">(.+?)</span>", html);
        String shusekiNum = RegexUtil.getValues("id=\"ctl00_lblSyuseki\" style=\"[^\"]*\">(.+?)</span>", html);
        save(name, PrefConst.KEY_USER_NAME,PrefConst.FILE_ID_PASS);
        save(classTxt, PrefConst.KEY_CLASS, PrefConst.FILE_ID_PASS);
        save(gakka, PrefConst.KEY_GAKKA, PrefConst.FILE_ID_PASS);
        save(course, PrefConst.KEY_COURSE, PrefConst.FILE_ID_PASS);
        save(shusekiNum, PrefConst.KEY_SHUSEKI_NUM, PrefConst.FILE_ID_PASS);
    }

    /**
     * 学校からのお知らせを保存するメソッド
     *
     *
     */
    public static void saveSchoolNews(List<NewsItem> newsItems) {
        save(newsItems,PrefConst.KEY_SCHOOL_NEWS,PrefConst.FILE_NEWS);
    }

    /**
     * 担任からのお知らせを保存するメソッド
     *
     * @param newsItems
     */
    public static void saveTanninNews(List<NewsItem> newsItems) {
        save(newsItems,PrefConst.KEY_TANNIN_NEWS,PrefConst.FILE_NEWS);
    }

    /**
     * 初めて起動したかどうかを保存するメソッド
     *
     * @param bool
     */
    public static void saveFirstTime(boolean bool) {
        save(bool, PrefConst.KEY_FIRST_TIME, PrefConst.FILE_STATE);
    }

    /**
     * ログイン状態を保存するメソッド
     *
     * @param bool
     */
    public static void saveLoginState(boolean bool) {
        save(bool, PrefConst.KEY_LOGIN_STATE, PrefConst.FILE_STATE);
    }

    /**
     * Id Pass を保存するメソッド
     *
     * @param id
     * @param pass
     */
    public static void saveIdPass(String id, String pass) {
        save(id, PrefConst.KEY_ID, PrefConst.FILE_ID_PASS);
        save(pass, PrefConst.KEY_PASS, PrefConst.FILE_ID_PASS);
    }

    /**
     * 現在のアプリケーションババージョンを保存するメソッド
     */
    public static void saveAppVersion() {
        save(Const.APP_VERSION, PrefConst.KEY_APP_VERSION, PrefConst.FILE_UTIL);
    }

    public static void saveUserName(String html) {
        String name = RegexUtil.getValues("<li id=\"user_name\" class=\"floatLeft\">(.+?)さん</li>", html);
        name = name.replace("&nbsp;", " ").trim();

        save(name, PrefConst.KEY_USER_NAME, PrefConst.FILE_ID_PASS);
    }

    public static void saveClassTxt(String string) {
        save(string, PrefConst.KEY_CLASS, PrefConst.FILE_ID_PASS);
    }

    public static void saveGakka(String string) {
        save(string, PrefConst.KEY_GAKKA, PrefConst.FILE_ID_PASS);
    }

    public static void saveCourse(String string) {
        save(string, PrefConst.KEY_COURSE, PrefConst.FILE_ID_PASS);
    }

    public static void saveShusekiNum(String string) {
        save(string, PrefConst.KEY_SHUSEKI_NUM, PrefConst.FILE_ID_PASS);
    }

    public static void saveEnableColorChange(boolean bool) {
        save(bool, PrefConst.KEY_ENABLE_COLOR_CHANGE, PrefConst.FILE_UTIL);
    }

    public static void saveColorU90(int color) {
        save(color, PrefConst.KEY_COLOR_U90, PrefConst.FILE_UTIL);
    }

    public static void saveColorU81(int color) {
        save(color, PrefConst.KEY_COLOR_U81, PrefConst.FILE_UTIL);
    }

    public static void saveColorU75(int color) {
        save(color, PrefConst.KEY_COLOR_U75, PrefConst.FILE_UTIL);
    }

    public static void saveRateU90(int rate) {
        save(rate, PrefConst.KEY_RATE_U90, PrefConst.FILE_UTIL);
    }

    public static void saveRateU81(int rate) {
        save(rate, PrefConst.KEY_RATE_U81, PrefConst.FILE_UTIL);
    }

    public static void saveRateU75(int rate) {
        save(rate, PrefConst.KEY_RATE_U75, PrefConst.FILE_UTIL);
    }

    public static void saveBlackout(boolean bool) {
        save(bool, PrefConst.KEY_BLACKOUT, PrefConst.FILE_UTIL);
    }

    public static void saveNotifyNews(boolean bool) {
        save(bool, PrefConst.KEY_ENABLE_NOTIFY_NEWS, PrefConst.FILE_UTIL);
    }

    public static void saveAttendanceDivide(boolean bool){
        save(bool, PrefConst.KEY_DIVIDE_ATTENDANCE, PrefConst.FILE_UTIL);
    }

    public static void saveAttendanceTabPosition(int position){
        save(position, PrefConst.KEY_ATTENDANCE_TAB_POSITION, PrefConst.FILE_UTIL);
    }

    /**
     * アプリを起動した最新の日時を保存する
     * @param date
     */
    public static void saveLatestUpdateData(long date){
        save(date ,PrefConst.KEY_LATEST_UPDATE_DATA,PrefConst.FILE_UTIL);
    }

    /**
     * 0限5限有効無効
     */
    public static void saveEnableZeroGen(boolean bool){
        save(bool, PrefConst.KEY_ENABLE_ZERO_GEN, PrefConst.FILE_UTIL);
    }

    public static void saveEnableGoGen(boolean bool){
        save(bool, PrefConst.KEY_ENABLE_GO_GEN, PrefConst.FILE_UTIL);
    }

    /**
     * 土日有効無効
     */
    public static void saveEnableSunCol(boolean bool){
        save(bool, PrefConst.KEY_ENABLE_SUN_COL, PrefConst.FILE_UTIL);
    }

    public static void saveEnableSatCol(boolean bool){
        save(bool, PrefConst.KEY_ENABLE_SAT_COL, PrefConst.FILE_UTIL);
    }

    public static void saveSchedule(List<ScheduleRoot> scheduleRoots){
        save(scheduleRoots, PrefConst.KEY_SCHEDULE, PrefConst.FILE_SCHEDULE);
    }

    //**
    //endregion
    //**


    //**
    //region state
    //**

    /**
     * ログインされているかどうか
     *
     * @return
     */
    public static boolean isLogin() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_STATE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_LOGIN_STATE, false);
    }

    /**
     * 初めて起動したかどうか
     *
     * @return
     */
    public static boolean isFirstTime() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_STATE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_FIRST_TIME, false);
    }


    /**
     * アップデートされたかどうか
     *
     * @return
     */
    public static boolean isUpdated() {
        //以前のアプリケーションバージョンを取得
        String beforeVersion = getAppVersion();
        if (beforeVersion.equals(Const.APP_VERSION)) {
            return false; //アップデートされていない
        } else {
            return true;  //アップデートされた　
        }
    }


    public static boolean isChangeColor() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_ENABLE_COLOR_CHANGE, false);
    }

    public static boolean isBlackout() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_BLACKOUT, false);
    }

    /**
     * お知らせを通知するか
     *
     * @return
     */
    public static boolean isNotifyNews() {
        return isNotifyNews(App.getAppContext());
    }

    public static boolean isNotifyNews(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_ENABLE_NOTIFY_NEWS, true); //default true
    }

    public static boolean isDivideAttendance(){
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_DIVIDE_ATTENDANCE, true); //default true
    }


    /**
     * 0限5限有効無効
     */
    public static boolean isEnableZeroGen(){
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_ENABLE_ZERO_GEN, false);
    }

    public static boolean isEnableGoGen(){
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_ENABLE_GO_GEN, false);
    }

    /**
     * 土日有効無効
     */
    public static boolean isEnableSunCol(){
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_ENABLE_SUN_COL, false);
    }

    public static boolean isEnableSatCol(){
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_ENABLE_SAT_COL, false);
    }


    //**
    //endregion
    //**


    //**
    //region delete
    //**

    /**
     * すべてのデータを削除
     */
    public static void deleteAll() {
        App.getAppContext().getSharedPreferences(PrefConst.FILE_ATTEND, Context.MODE_PRIVATE).edit().clear().apply();
        App.getAppContext().getSharedPreferences(PrefConst.FILE_TIME_TABLE, Context.MODE_PRIVATE).edit().clear().apply();
        App.getAppContext().getSharedPreferences(PrefConst.FILE_STATE, Context.MODE_PRIVATE).edit().clear().apply();
        App.getAppContext().getSharedPreferences(PrefConst.FILE_NEWS, Context.MODE_PRIVATE).edit().clear().apply();
        App.getAppContext().getSharedPreferences(PrefConst.FILE_ID_PASS, Context.MODE_PRIVATE).edit().clear().apply();
        App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE).edit().clear().apply();
        App.getAppContext().getSharedPreferences(PrefConst.FILE_SCHEDULE, Context.MODE_PRIVATE).edit().clear().apply();
        PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit().clear().apply();
    }

    /**
     * 　存在するファイルをすべて削除
     */
    public static void deleteSharedPreferencesFiles() throws Throwable{
        try {
            ApplicationInfo info = App.getAppContext().getPackageManager()
                    .getApplicationInfo(App.getAppContext().getPackageName(), 0);
            String dirPath = info.dataDir + File.separator + "shared_prefs"
                    + File.separator;
            File dir = new File(dirPath);
            if (dir.exists() && dir.isDirectory()) {
                String[] list = dir.list();
                int size = list.length;
                for (int i = 0; i < size; i++) {
                    new File(dirPath + list[i]).delete();
                }
            } else {
                Log.d(TAG, "NO FILE or NOT DIR");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    //**
    //endregion
    //**

    /************************************* private *******************************************************/

    //**
    //region private methods
    //**

    //**
    //region 出席率関連
    //**

    /***
     * 出席率モデルのリストを作成するメソッド
     *
     * @param html
     * @return
     */
    public static List<AttendanceRate> createAttendanceList(String html) {
        List<AttendanceRate> attendanceRateList = new ArrayList();

        html = RegexUtil.replaceCRLF(html, true);
        html = RegexUtil.narrowingValues("<table class=\"GridVeiwTable\"", "<table cellspacing=\"0\" border=\"0\" id=\"ctl00_ContentPlaceHolder1_fmvSyuseki\"", html);

        Matcher m = RegexUtil.getGroupValues("<tr>.*?</tr>", html);
        while (m.find()) {
            AttendanceRate attendanceRate = new AttendanceRate();

            String narrowHtml = m.group();
            Log.d(TAG,"narrowHtml : " + narrowHtml);

            String subject = RegexUtil.getValues("<img(?:\\\".*?\\\"|\\'.*?\\'|[^\\'\\\"])*?>(.+?)</a>", narrowHtml);
            attendanceRate.setSubjectName(subject);
            Log.d(TAG,"subject : " + subject);
            int count = 0;
            Matcher m2 = RegexUtil.getGroupValues("<td(?:\\\".*?\\\"|\\'.*?\\'|[^\\'\\\"])*?>(.*?)</td>", narrowHtml);
            while (m2.find()) {
                String str = m2.group(1);
                Log.d(TAG,"count : " + count +" " +str);
                str = RegexUtil.deletePercent(str);
                str = RegexUtil.deleteNBSPTo0(str);
                switch (count) {
                    case 0:
                        break;
                    case 1:
                        attendanceRate.setUnit(str);
                        break;
                    case 2:
                        attendanceRate.setAttendanceNumber(str);
                        break;
                    case 3:
                        attendanceRate.setAbsentNumber(str);
                        break;
                    case 4:
                        attendanceRate.setLateNumber(str);
                        break;
                    case 5:
                        attendanceRate.setPublicAbsentNumber1(str);
                        break;
                    case 6:
                        attendanceRate.setPublicAbsentNumber2(str);
                        break;
                    case 7:
                        // E休
                        break;
                    case 8:
                        attendanceRate.setAttendanceRate(str);
                        break;
                    case 9:
                        attendanceRate.setShortageseNumber(str);
                        break;
                }
                count++;
            }
            attendanceRateList.add(attendanceRate);
        }

        return attendanceRateList;
    }

    /**
     * 　全体の出席照会データのモデルを作成するメソッド
     *
     * @param html
     * @return
     */
    private static AttendanceRate createAllRateData(String html) {
        AttendanceRate model = new AttendanceRate();
        html = RegexUtil.replaceCRLF(html, true);

        model.setUnit(RegexUtil.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalTani\">(.+?)</span>", html));
        model.setAttendanceNumber(RegexUtil.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalShuseki\">(.+?)<", html));
        model.setAbsentNumber(RegexUtil.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalKesski\">(.+?)<", html));
        model.setLateNumber(RegexUtil.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalChikoku\">(.+?)<", html));
        model.setPublicAbsentNumber1(RegexUtil.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalKouketsu1\">(.+?)<", html));
        model.setPublicAbsentNumber2(RegexUtil.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalKouketsu2\">(.+?)<", html));
        model.setAttendanceRate(RegexUtil.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalShutsuritsu\">(.+?)%<", html));
        model.setShortageseNumber(RegexUtil.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalFusoku\">(.+?)<", html));

        return model;
    }

    //**
    //endregion
    //**


    //**
    //region 時間割関連
    //**

    /***
     * 引数のnumを利用して曜日ごとに振り分けるメソッド
     * num 1 = 月曜日
     * @param timeTables
     * @param num
     * @return
     */
    private static List<TimeTable> createWeekList(List<TimeTable> timeTables, int num) {
        List<TimeTable> tmp = new ArrayList<>();
        for(TimeTable timeTable:timeTables){
            // 引数で指定された曜日のリストtmpを作成
            if (timeTable.getWeek() == num) tmp.add(timeTable);
        }

        //　ソートする
        List<TimeTable> list = new ArrayList<>();
        // 0限から5限まで回す
        for(int i = 0; i<= 5; i++){
            boolean flg = false;
            for(int j = 0; j < tmp.size(); j++){
                if(tmp.get(j).getTerm() == i) {
                    list.add(tmp.get(j));
                    flg = true;
                }
            }
            if(!flg) {
                // 空のオブジェクトを入れる
                TimeTable timeTable = new TimeTable();
                timeTable.setTerm(i);
                timeTable.setWeek(num);
                list.add(timeTable);
            }
        }

        return list;
    }

    //**
    //endregion
    //**

    //**
    //region 実際に保存するメソッド
    //**

    /**
     * オブジェクト保存メソッド
     *
     * @param key
     * @param fileName
     */
    private static void save(Object obj, String key, String fileName) {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, new Gson().toJson(obj)).commit();
    }

    /***
     * Boolean保存めおsっど
     *
     * @param bool
     * @param key
     * @param fileName
     */
    private static void save(boolean bool, String key, String fileName) {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, bool).commit();
    }

    /***
     * String保存メソッド
     *
     * @param string
     * @param key
     * @param fileName
     */
    private static void save(String string, String key, String fileName) {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, string).commit();
    }

    private static void save(int num, String key, String fileName) {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, num).commit();
    }

    private static void save(long num ,String key,String fileName){
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(fileName,Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(key,num).commit();
    }

    //**
    //endregion
    //**


    //**
    //endregion
    //**
}
