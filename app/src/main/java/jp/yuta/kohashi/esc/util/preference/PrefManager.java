package jp.yuta.kohashi.esc.util.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import jp.yuta.kohashi.esc.Const;
import jp.yuta.kohashi.esc.model.AttendanceRateModel;
import jp.yuta.kohashi.esc.model.NewsModel;
import jp.yuta.kohashi.esc.model.TimeBlockModel;
import jp.yuta.kohashi.esc.util.RegexManager;


/**
 * Created by yutakohashi on 2017/01/06.
 */

public class PrefManager {
    private static final String TAG = PrefManager.class.getSimpleName();

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    /************************************   public   ********************************************************/

    //**
    //region Load
    //**

    public static List<List<TimeBlockModel>> loadTimeBlockList(){
        List<List<TimeBlockModel>> lists = new ArrayList<>();

        lists.add(loadTimeBlockList(PrefConst.KEY_MON_LIST));
        lists.add(loadTimeBlockList(PrefConst.KEY_TUE_LIST));
        lists.add(loadTimeBlockList(PrefConst.KEY_WED_LIST));
        lists.add(loadTimeBlockList(PrefConst.KEY_THUR_LIST));
        lists.add(loadTimeBlockList(PrefConst.KEY_FRI_LIST));

        return lists;
    }

    /***
     * 曜日ごとの時間割をロードするメソッド
     *
     * @param key
     * @return
     */
    public static List<TimeBlockModel> loadTimeBlockList(String key) {
        SharedPreferences sharedPreferences;
        List<TimeBlockModel> arrayList;
        sharedPreferences = mContext.getSharedPreferences(PrefConst.FILE_TIME_TABLE, mContext.MODE_PRIVATE);
        arrayList = new Gson().fromJson(sharedPreferences.getString(key, ""), new TypeToken<List<TimeBlockModel>>() {
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
    public static List<AttendanceRateModel> loadAttendanceRateModelList() {
        SharedPreferences sharedPreferences;
        List<AttendanceRateModel> arrayList;
        sharedPreferences = mContext.getSharedPreferences(PrefConst.FILE_ATTEND, mContext.MODE_PRIVATE);

        try {
            arrayList = new Gson().fromJson(sharedPreferences.getString(PrefConst.KEY_ATTEND, ""), new TypeToken<List<AttendanceRateModel>>() {
            }.getType());
            if (arrayList.size() == 0) {
                arrayList = new ArrayList<>();
            }
        } catch (Exception e) {
            arrayList = new ArrayList<>();
        }

        return arrayList;
    }

    public static AttendanceRateModel loadAttendanceTotalData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PrefConst.FILE_ATTEND, mContext.MODE_PRIVATE);
        AttendanceRateModel model = null;
        try {
            model = new Gson().fromJson(sharedPreferences.getString(PrefConst.KEY_ATTEND_ALL_RATE, ""), new TypeToken<AttendanceRateModel>() {
            }.getType());
            if (model == null) {
                model = new AttendanceRateModel();
            }
        }catch(Exception e){
            model = new AttendanceRateModel();
            Log.d(TAG,e.toString());
        }

        return model;
    }

    /***
     * 学校からのお知らせを取得するメソッド
     *
     * @return
     */
    public static List<NewsModel> loadSchoolNewsList() {
        SharedPreferences sharedPreferences;
        List<NewsModel> arrayList;
        sharedPreferences = mContext.getSharedPreferences(PrefConst.FILE_NEWS, mContext.MODE_PRIVATE);

        try {
            arrayList = new Gson().fromJson(sharedPreferences.getString(PrefConst.KEY_SCHOOL_NEWS, ""), new TypeToken<List<NewsModel>>() {
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
    public static List<NewsModel> loadTanninNewsList() {
        SharedPreferences sharedPreferences;
        List<NewsModel> arrayList;
        sharedPreferences = mContext.getSharedPreferences(PrefConst.FILE_NEWS, mContext.MODE_PRIVATE);
        arrayList = new Gson().fromJson(sharedPreferences.getString(PrefConst.KEY_TANNIN_NEWS, ""), new TypeToken<List<NewsModel>>() {
        }.getType());

        try {
            arrayList = new Gson().fromJson(sharedPreferences.getString(PrefConst.KEY_TANNIN_NEWS, ""), new TypeToken<List<NewsModel>>() {
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
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PrefConst.FILE_ID_PASS, mContext.MODE_PRIVATE);
        return sharedPreferences.getString(PrefConst.KEY_ID, null);
    }

    /**
     * ログインpassをロードするメソッド
     *
     * @return
     */
    public static String getPss() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PrefConst.FILE_ID_PASS, mContext.MODE_PRIVATE);
        return sharedPreferences.getString(PrefConst.KEY_PASS, null);
    }

    /**
     * ログイン状態を取得するメソッド
     *
     * @return
     */
    public static boolean getLoginState() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PrefConst.FILE_STATE, mContext.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_LOGIN_STATE, false); //default:false
    }

    /**
     * 初回起動の状態を取得するメソッド
     *
     * @return
     */
    public static boolean getFirstTime() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PrefConst.FILE_STATE, mContext.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_FIRST_TIME, false);
    }

    public static String getAppVersion() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PrefConst.FILE_UTIL, mContext.MODE_PRIVATE);
        return sharedPreferences.getString(PrefConst.KEY_APP_VERSION, "-1");
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
        List<TimeBlockModel> tempList = createTempList(html, names);

        List<TimeBlockModel> monday = createWeekList(tempList, 0);
        List<TimeBlockModel> tuesday = createWeekList(tempList, 1);
        List<TimeBlockModel> wednesday = createWeekList(tempList, 2);
        List<TimeBlockModel> thursday = createWeekList(tempList, 3);
        List<TimeBlockModel> friday = createWeekList(tempList, 4);

        save(monday, PrefConst.KEY_MON_LIST, PrefConst.FILE_TIME_TABLE);
        save(tuesday, PrefConst.KEY_TUE_LIST, PrefConst.FILE_TIME_TABLE);
        save(wednesday, PrefConst.KEY_WED_LIST, PrefConst.FILE_TIME_TABLE);
        save(thursday, PrefConst.KEY_THUR_LIST, PrefConst.FILE_TIME_TABLE);
        save(friday, PrefConst.KEY_FRI_LIST, PrefConst.FILE_TIME_TABLE);

    }


    /**
     * 出席率を保存するメソッド
     *
     * @param html
     */
    public static void saveAttendanceRate(String html) {
        List<AttendanceRateModel> attendanceRateList = createAttendanceList(html);
        save(attendanceRateList, PrefConst.KEY_ATTEND, PrefConst.FILE_ATTEND);
    }


    /**
     * 合計出席率データを保存するメソッド
     *
     * @param html
     */
    public static void saveAttendanceAllRateData(String html) {
        AttendanceRateModel attendanceRateModel = createAllRateData(html);
        save(attendanceRateModel, PrefConst.KEY_ATTEND_ALL_RATE, PrefConst.FILE_ATTEND);
    }


    /**
     * 学校からのお知らせを保存するメソッド
     *
     * @param html
     */
    public static void saveSchoolNews(String html) {
        List<NewsModel> list = createSchoolNewsList(html);
        save(list, PrefConst.KEY_SCHOOL_NEWS, PrefConst.FILE_NEWS);
    }

    /**
     * 担任からのお知らせを保存するメソッド
     *
     * @param html
     */
    public static void saveTanninNews(String html) {
        List<NewsModel> list = createTanninNewsList(html);
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
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PrefConst.FILE_STATE, mContext.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_LOGIN_STATE, false);
    }

    /**
     * 初めて起動したかどうか
     *
     * @return
     */
    public static boolean isFirstTime() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PrefConst.FILE_STATE, mContext.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PrefConst.KEY_FIRST_TIME, false);
    }

    //TODO

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
    private static List<AttendanceRateModel> createAttendanceList(String html) {
        List<AttendanceRateModel> attendanceRateList = new ArrayList();

        html = RegexManager.replaceCRLF(html, true);
        html = RegexManager.narrowingValues("<table class=\"GridVeiwTable\"", "<table cellspacing=\"0\" border=\"0\" id=\"ctl00_ContentPlaceHolder1_fmvSyuseki\"", html);

        Matcher m = RegexManager.getGroupValues("<tr>.*?</tr>", html);
        while (m.find()) {
            AttendanceRateModel attendanceRateModel = new AttendanceRateModel();

            String narrowHtml = m.group();

            String subject = RegexManager.getValues("<img(?:\\\".*?\\\"|\\'.*?\\'|[^\\'\\\"])*?>(.+?)</a>", narrowHtml);
            attendanceRateModel.setSubjectName(subject);

            int count = 0;
            Matcher m2 = RegexManager.getGroupValues("<td(?:\\\".*?\\\"|\\'.*?\\'|[^\\'\\\"])*?>(.*?)</td>", narrowHtml);
            while (m2.find()) {
                String str = m2.group(1);
                str = RegexManager.deletePercent(str);
                str = RegexManager.deleteNBSPTo0(str);
                switch (count) {
                    case 0:
                        break;
                    case 1:
                        attendanceRateModel.setUnit(str);
                        break;
                    case 2:
                        attendanceRateModel.setAttendanceNumber(str);
                        break;
                    case 3:
                        attendanceRateModel.setAbsentNumber(str);
                        break;
                    case 4:
                        attendanceRateModel.setLateNumber(str);
                        break;
                    case 5:
                        attendanceRateModel.setPublicAbsentNumber1(str);
                        break;
                    case 6:
                        attendanceRateModel.setPublicAbsentNumber2(str);
                        break;
                    case 7:
                        attendanceRateModel.setAttendanceRate(str);
                        break;
                    case 8:
                        attendanceRateModel.setShortageseNumber(str);
                        break;
                }
                count++;
            }
            attendanceRateList.add(attendanceRateModel);
        }

        return attendanceRateList;
    }

    /**
     * 　全体の出席照会データのモデルを作成するメソッド
     *
     * @param html
     * @return
     */
    private static AttendanceRateModel createAllRateData(String html) {
        AttendanceRateModel model = new AttendanceRateModel();
        html = RegexManager.replaceCRLF(html, true);

        model.setUnit(RegexManager.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalTani\">(.+?)</span>", html));
        model.setAttendanceNumber(RegexManager.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalShuseki\">(.+?)<", html));
        model.setAbsentNumber(RegexManager.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalKesski\">(.+?)<", html));
        model.setLateNumber(RegexManager.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalChikoku\">(.+?)<", html));
        model.setPublicAbsentNumber1(RegexManager.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalKouketsu1\">(.+?)<", html));
        model.setPublicAbsentNumber2(RegexManager.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalKouketsu2\">(.+?)<", html));
        model.setAttendanceRate(RegexManager.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalShutsuritsu\">(.+?)%<", html));
        model.setShortageseNumber(RegexManager.getValues("id=\"ctl00_ContentPlaceHolder1_fmvSyuseki_lblTotalFusoku\">(.+?)<", html));

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
    private static TimeBlockModel createTimeBlockModel(String subject, String room, String name, int row, int col) {
        TimeBlockModel model = new TimeBlockModel();
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
    private static List<TimeBlockModel> createTempList(String html, List<String> names) {
        List<TimeBlockModel> temp = new ArrayList<>();

        html = RegexManager.replaceCRLF(html, true);
        String narrowHtml = RegexManager.narrowingValues("<div id=\"timetable_col\" class=\"col\">", "<div class=\"col\">", html);

        int rowNum = 1; //1〜
        int teacherIndex = 0;
        boolean flg = true;

        Matcher row = RegexManager.getGroupValues("<th class=\"term\">.*?</tr>", narrowHtml);
        while (row.find()) {

            //１行目はヘッダのため無視
            if (flg) {
                flg = false;
                continue;
            }

            Matcher col = RegexManager.getGroupValues("<td>(.+?)</td>", row.group());
            int colNum = 1; //1〜
            while (col.find()) {
                String colHtml = col.group();

                String subject = "";
                String room = "";
                String teacherName = "";
                if (RegexManager.containsCheck("<li>", colHtml)) {
                    subject = RegexManager.getValues("<td>\\s*<ul>\\s*<li>(.+?)</li>", colHtml);
                    room = RegexManager.getValues("</li>\\s*<li>(.+?)</li>", colHtml);
                    teacherName = names.get(teacherIndex++);
                }

                TimeBlockModel model = createTimeBlockModel(subject, room, teacherName, rowNum, colNum++);
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
    private static List<TimeBlockModel> createWeekList(List<TimeBlockModel> list, int num) {
        List<TimeBlockModel> weekList = new ArrayList<>();

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
        html = RegexManager.replaceCRLF(html, true);
        String teacherName = RegexManager.getValues("<h3>受信者</h3>    <p>(.+?)</p>", html);
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
    private static List<NewsModel> createSchoolNewsList(String html) {
        html = RegexManager.replaceCRLF(html, true);
        String narrowHtml = RegexManager.narrowingValues("<div id=\"school_news_col\" class=\"col\">", "<div id=\"shcool_event_col\"", html);
        List<NewsModel> schoolNewsList = new ArrayList<>();

        Matcher m = RegexManager.getGroupValues("<div class=\"wrapper\">(.+?)</div>", narrowHtml);
        while (m.find()) {
            String groupHtml = m.group(1);
            String groupTitle = RegexManager.getValues("<h3>(.+?)</h3>", groupHtml);

            schoolNewsList.add(new NewsModel(groupTitle));
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
    private static List<NewsModel> createTanninNewsList(String html) {
        html = RegexManager.replaceCRLF(html, true);
        String narrowHtml = RegexManager.narrowingValues("<h2>担任からのお知らせ</h2>", "</div>", html);
        List<NewsModel> tanninNewsList = createNewsList(narrowHtml);
        return tanninNewsList;
    }

    /**
     * 与えられた引数の文字列からニュースリストを作成する
     *
     * @param narrowHtml
     * @return
     */
    private static List<NewsModel> createNewsList(String narrowHtml) {
        List<NewsModel> list = new ArrayList<>();

        Matcher m = RegexManager.getGroupValues("<li>(.*?)</li>", narrowHtml);
        while (m.find()) {
            String news = m.group();
            String date = RegexManager.getValues("class=\"date\">(.+?)<", news);
            String uri = RegexManager.getValues("<a href=\"(.+?)\">", news);
            String title = RegexManager.getValues("<a href=\"[^\"]*\">(.+?)</a>", news);

            list.add(new NewsModel(title, date, uri));
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
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName, mContext.MODE_PRIVATE);
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
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName, mContext.MODE_PRIVATE);
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
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName, mContext.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, string).commit();
    }

    //**
    //endregion
    //**


    //**
    //endregion
    //**
}
