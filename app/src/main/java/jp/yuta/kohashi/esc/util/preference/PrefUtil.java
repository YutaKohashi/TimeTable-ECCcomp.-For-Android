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
import jp.yuta.kohashi.esc.model.AttendanceRate;
import jp.yuta.kohashi.esc.model.NewsItem;
import jp.yuta.kohashi.esc.model.TimeBlockItem;
import jp.yuta.kohashi.esc.util.RegexUtil;


/**
 * Created by yutakohashi on 2017/01/06.
 */

public class PrefUtil {
    private static final String TAG = PrefUtil.class.getSimpleName();

    /************************************
     * public
     ********************************************************/

    //**
    //region Load
    //**
    public static List<List<TimeBlockItem>> loadTimeBlockList() {
        List<List<TimeBlockItem>> lists = new ArrayList<>();

        lists.add(loadTimeBlockList(PrefConst.KEY_MON_LIST));
        lists.add(loadTimeBlockList(PrefConst.KEY_TUE_LIST));
        lists.add(loadTimeBlockList(PrefConst.KEY_WED_LIST));
        lists.add(loadTimeBlockList(PrefConst.KEY_THUR_LIST));
        lists.add(loadTimeBlockList(PrefConst.KEY_FRI_LIST));

        return lists;
    }

    /**
     * 外部からcontextを受け付ける
     *
     * @param context
     * @return
     */
    public static List<List<TimeBlockItem>> loadTimeBlockList(Context context) {
        List<List<TimeBlockItem>> lists = new ArrayList<>();

        lists.add(loadTimeBlockList(PrefConst.KEY_MON_LIST, context));
        lists.add(loadTimeBlockList(PrefConst.KEY_TUE_LIST, context));
        lists.add(loadTimeBlockList(PrefConst.KEY_WED_LIST, context));
        lists.add(loadTimeBlockList(PrefConst.KEY_THUR_LIST, context));
        lists.add(loadTimeBlockList(PrefConst.KEY_FRI_LIST, context));

        return lists;
    }

    public static List<List<TimeBlockItem>> loadOriginalTimeBlockList() {
        List<List<TimeBlockItem>> lists = new ArrayList<>();

        lists.add(loadTimeBlockList(PrefConst.KEY_MON_LIST_ORIGINAL));
        lists.add(loadTimeBlockList(PrefConst.KEY_TUE_LIST_ORIGINAL));
        lists.add(loadTimeBlockList(PrefConst.KEY_WED_LIST_ORIGINAL));
        lists.add(loadTimeBlockList(PrefConst.KEY_THUR_LIST_ORIGINAL));
        lists.add(loadTimeBlockList(PrefConst.KEY_FRI_LIST_ORIGINAL));

        return lists;
    }

    /***
     * 曜日ごとの時間割をロードするメソッド
     *
     * @param key
     * @return
     */
    public static List<TimeBlockItem> loadTimeBlockList(String key) {
        SharedPreferences sharedPreferences;
        List<TimeBlockItem> arrayList;
        sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_TIME_TABLE, Context.MODE_PRIVATE);
        arrayList = new Gson().fromJson(sharedPreferences.getString(key, ""), new TypeToken<List<TimeBlockItem>>() {
        }.getType());

        try {
            if (arrayList.size() == 0) {
                arrayList = new ArrayList<>();
            }
        } catch (Exception e) {
            arrayList = new ArrayList<>();
        }

        return arrayList;
    }

    /**
     * 外部からcontextを受け付ける（ウィジェットで使用）
     *
     * @param key
     * @param context
     * @return
     */
    public static List<TimeBlockItem> loadTimeBlockList(String key, Context context) {
        SharedPreferences sharedPreferences;
        List<TimeBlockItem> arrayList;
        sharedPreferences = context.getSharedPreferences(PrefConst.FILE_TIME_TABLE, Context.MODE_PRIVATE);
        arrayList = new Gson().fromJson(sharedPreferences.getString(key, ""), new TypeToken<List<TimeBlockItem>>() {
        }.getType());

        try {
            if (arrayList.size() == 0) {
                arrayList = new ArrayList<>();
            }
        } catch (Exception e) {
            arrayList = new ArrayList<>();
        }

        return arrayList;
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
        arrayList = new Gson().fromJson(sharedPreferences.getString(PrefConst.KEY_TANNIN_NEWS, ""), new TypeToken<List<NewsItem>>() {
        }.getType());

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
        return sharedPreferences.getInt(PrefConst.KEY_COLOR_U90, -1);
    }

    public static int loadColorU81() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PrefConst.KEY_COLOR_U81, -1);
    }

    public static int loadColorU75() {
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PrefConst.KEY_COLOR_U75, -1);
    }

    public static int getAttendanceTabPosition(){
        SharedPreferences sharedPreferences = App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PrefConst.KEY_ATTENDANCE_TAB_POSITION, 0);
    }

    //**
    //endregion
    //**


    //**
    //region save
    //**

    /**
     * 時間割（先生名を含めて）保存するメソッド
     *
     * @param html
     * @param htmls
     */
    public static void saveTimeTable(String html, List<String> htmls) {
        List<String> names = getTeacherNames(htmls);
        List<TimeBlockItem> tempList = createTempList(html, names);

        saveTimeTableMon(createWeekList(tempList, 0));
        saveTimeTableTue(createWeekList(tempList, 1));
        saveTimeTableWed(createWeekList(tempList, 2));
        saveTimeTableThur(createWeekList(tempList, 3));
        saveTimeTableFri(createWeekList(tempList, 4));
    }

    /**
     * オリジナルの時間割を保存
     *
     * @param html
     * @param htmls
     */
    public static void saveTimeTableOriginal(String html, List<String> htmls) {
        List<String> names = getTeacherNames(htmls);
        List<TimeBlockItem> tempList = createTempList(html, names);

        save(createWeekList(tempList, 0), PrefConst.KEY_MON_LIST_ORIGINAL, PrefConst.FILE_TIME_TABLE);
        save(createWeekList(tempList, 1), PrefConst.KEY_TUE_LIST_ORIGINAL, PrefConst.FILE_TIME_TABLE);
        save(createWeekList(tempList, 2), PrefConst.KEY_WED_LIST_ORIGINAL, PrefConst.FILE_TIME_TABLE);
        save(createWeekList(tempList, 3), PrefConst.KEY_THUR_LIST_ORIGINAL, PrefConst.FILE_TIME_TABLE);
        save(createWeekList(tempList, 4), PrefConst.KEY_FRI_LIST_ORIGINAL, PrefConst.FILE_TIME_TABLE);
    }

    public static void saveTimeTableMon(List<TimeBlockItem> list) {
        save(list, PrefConst.KEY_MON_LIST, PrefConst.FILE_TIME_TABLE);
    }

    public static void saveTimeTableTue(List<TimeBlockItem> list) {
        save(list, PrefConst.KEY_TUE_LIST, PrefConst.FILE_TIME_TABLE);
    }

    public static void saveTimeTableWed(List<TimeBlockItem> list) {
        save(list, PrefConst.KEY_WED_LIST, PrefConst.FILE_TIME_TABLE);
    }

    public static void saveTimeTableThur(List<TimeBlockItem> list) {
        save(list, PrefConst.KEY_THUR_LIST, PrefConst.FILE_TIME_TABLE);
    }

    public static void saveTimeTableFri(List<TimeBlockItem> list) {
        save(list, PrefConst.KEY_FRI_LIST, PrefConst.FILE_TIME_TABLE);
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
        String classTxt = RegexUtil.getValues("id=\"ctl00_lblClass\" style=\"[^\"]*\">(.+?)</span>", html);
        String gakka = RegexUtil.getValues("id=\"ctl00_lblGakka\" style=\"[^\"]*\">(.+?)</span>", html);
        String course = RegexUtil.getValues("id=\"ctl00_lblCourse\" style=\"[^\"]*\">(.+?)</span>", html);
        String shusekiNum = RegexUtil.getValues("id=\"ctl00_lblSyuseki\" style=\"[^\"]*\">(.+?)</span>", html);
        save(classTxt, PrefConst.KEY_CLASS, PrefConst.FILE_ID_PASS);
        save(gakka, PrefConst.KEY_GAKKA, PrefConst.FILE_ID_PASS);
        save(course, PrefConst.KEY_COURSE, PrefConst.FILE_ID_PASS);
        save(shusekiNum, PrefConst.KEY_SHUSEKI_NUM, PrefConst.FILE_ID_PASS);
    }

    /**
     * 学校からのお知らせを保存するメソッド
     *
     * @param html
     */
    public static void saveSchoolNews(String html) {
        List<NewsItem> list = createSchoolNewsList(html);
        save(list, PrefConst.KEY_SCHOOL_NEWS, PrefConst.FILE_NEWS);
    }

    /**
     * 担任からのお知らせを保存するメソッド
     *
     * @param html
     */
    public static void saveTanninNews(String html) {
        List<NewsItem> list = createTanninNewsList(html);
        save(list, PrefConst.KEY_TANNIN_NEWS, PrefConst.FILE_NEWS);
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
        App.getAppContext().getSharedPreferences(PrefConst.FILE_ATTEND, Context.MODE_PRIVATE).edit().clear().commit();
        App.getAppContext().getSharedPreferences(PrefConst.FILE_TIME_TABLE, Context.MODE_PRIVATE).edit().clear().commit();
        App.getAppContext().getSharedPreferences(PrefConst.FILE_STATE, Context.MODE_PRIVATE).edit().clear().commit();
        App.getAppContext().getSharedPreferences(PrefConst.FILE_NEWS, Context.MODE_PRIVATE).edit().clear().commit();
        App.getAppContext().getSharedPreferences(PrefConst.FILE_ID_PASS, Context.MODE_PRIVATE).edit().clear().commit();
        App.getAppContext().getSharedPreferences(PrefConst.FILE_UTIL, Context.MODE_PRIVATE).edit().clear().commit();
        PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit().clear().commit();
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

            String subject = RegexUtil.getValues("<img(?:\\\".*?\\\"|\\'.*?\\'|[^\\'\\\"])*?>(.+?)</a>", narrowHtml);
            attendanceRate.setSubjectName(subject);

            int count = 0;
            Matcher m2 = RegexUtil.getGroupValues("<td(?:\\\".*?\\\"|\\'.*?\\'|[^\\'\\\"])*?>(.*?)</td>", narrowHtml);
            while (m2.find()) {
                String str = m2.group(1);
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
                        attendanceRate.setAttendanceRate(str);
                        break;
                    case 8:
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

    /**
     * @param subject
     * @param room
     * @param name
     * @param row
     * @param col
     * @return
     */
    private static TimeBlockItem createTimeBlockModel(String subject, String room, String name, int row, int col) {
        TimeBlockItem model = new TimeBlockItem();
        model.setSubject(subject);
        model.setClassRoom(room);
        model.setTeacherName(name);
        model.setRowNum(row);
        model.setColNum(col);
        return model;
    }

    /***
     * 曜日別ではなくhtmlソースをの頭から順に時間割のセルごとのリストを作成するメソッド
     * 曜日別に保存したいのでこのメソッドを実行した後にcreateWeekListを実行
     *
     * @param html
     * @param names
     * @return
     */
    private static List<TimeBlockItem> createTempList(String html, List<String> names) {
        List<TimeBlockItem> temp = new ArrayList<>();

        html = RegexUtil.replaceCRLF(html, true);
        String narrowHtml = RegexUtil.narrowingValues("<div id=\"timetable_col\" class=\"col\">", "<div class=\"col\">", html);

        int rowNum = 1; //1〜
        int teacherIndex = 0;
        boolean flg = true;

        Matcher row = RegexUtil.getGroupValues("<th class=\"term\">.*?</tr>", narrowHtml);
        while (row.find()) {

            //１行目はヘッダのため無視
            if (flg) {
                flg = false;
                continue;
            }

            Matcher col = RegexUtil.getGroupValues("<td>(.+?)</td>", row.group());
            int colNum = 1; //1〜
            while (col.find()) {
                String colHtml = col.group();

                String subject = "";
                String room = "";
                String teacherName = "";
                if (RegexUtil.containsCheck("<li>", colHtml)) {
                    subject = RegexUtil.getValues("<td>\\s*<ul>\\s*<li>(.+?)</li>", colHtml);
                    room = RegexUtil.getValues("</li>\\s*<li>(.+?)</li>", colHtml);
                    teacherName = names.get(teacherIndex++);
                }

                TimeBlockItem model = createTimeBlockModel(subject, room, teacherName, rowNum, colNum++);
                temp.add(model);
            }
            rowNum++;
        }
        return temp;
    }

    /***
     * 引数のnumを利用して曜日ごとに振り分けるメソッド
     *
     * @param list
     * @param num
     * @return
     */
    private static List<TimeBlockItem> createWeekList(List<TimeBlockItem> list, int num) {
        List<TimeBlockItem> weekList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int index = num + i * 5;
            weekList.add(list.get(index));
        }
        return weekList;
    }

    //**
    //endregion
    //**


    //**
    //region 先生名関連
    //**

    /***
     * 先生名リストを作成するメソッド
     *
     * @param htmls
     * @return
     */
    private static List<String> getTeacherNames(List<String> htmls) {
        List<String> names = new ArrayList<>();
        for (String html : htmls) {
            String name = getTeacherName(html);
            names.add(name);
        }
        return names;
    }

    /***
     * htmlソースから先生名を取得するメソッド
     *
     * @param html
     * @return
     */
    private static String getTeacherName(String html) {
        html = RegexUtil.replaceCRLF(html, true);
        String teacherName = RegexUtil.getValues("<h3>受信者</h3>    <p>(.+?)</p>", html);
        teacherName = fixTeacherName(teacherName);
        return teacherName;
    }

    /***
     * 不要な空白を取り除くメソッド
     *
     * @param teacherName
     * @return
     */
    private static String fixTeacherName(String teacherName) {
        teacherName = teacherName.replace("     ", " ");
        teacherName = teacherName.replace("    ", " ");
        teacherName = teacherName.replace("   ", " ");
        teacherName = teacherName.replace("  ", " ");

        return teacherName.trim();
    }

    //**
    //endregion
    //**


    //**
    //region お知らせ関連
    //**

    /***
     * 学校からのお知らせリストを作成するメソッド
     *
     * @param html
     * @return
     */
    private static List<NewsItem> createSchoolNewsList(String html) {
        html = RegexUtil.replaceCRLF(html, true);
        String narrowHtml = RegexUtil.narrowingValues("<div id=\"school_news_col\" class=\"col\">", "<div id=\"shcool_event_col\"", html);
        List<NewsItem> schoolNewsList = new ArrayList<>();

        Matcher m = RegexUtil.getGroupValues("<div class=\"wrapper\">(.+?)</div>", narrowHtml);
        while (m.find()) {
            String groupHtml = m.group(1);
            String groupTitle = RegexUtil.getValues("<h3>(.+?)</h3>", groupHtml);

            schoolNewsList.add(new NewsItem(groupTitle));
            schoolNewsList.addAll(createNewsList(groupHtml));
        }
        return schoolNewsList;
    }

    /**
     * 担任からのお知らせリストを作成するメソッド
     *
     * @param html
     * @return
     */
    private static List<NewsItem> createTanninNewsList(String html) {
        html = RegexUtil.replaceCRLF(html, true);
        String narrowHtml = RegexUtil.narrowingValues("<h2>担任からのお知らせ</h2>", "</div>", html);
        List<NewsItem> tanninNewsList = createNewsList(narrowHtml);
        return tanninNewsList;
    }

    /**
     * 与えられた引数の文字列からニュースリストを作成する
     *
     * @param narrowHtml
     * @return
     */
    private static List<NewsItem> createNewsList(String narrowHtml) {
        List<NewsItem> list = new ArrayList<>();

        Matcher m = RegexUtil.getGroupValues("<li>(.*?)</li>", narrowHtml);
        while (m.find()) {
            String news = m.group();
            String date = RegexUtil.getValues("class=\"date\">(.+?)<", news);
            String uri = RegexUtil.getValues("<a href=\"(.+?)\">", news);
            String title = RegexUtil.getValues("<a href=\"[^\"]*\">(.+?)</a>", news);

            list.add(new NewsItem(title, date, uri));
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

    //**
    //endregion
    //**


    //**
    //endregion
    //**
}
