package jp.yuta.kohashi.esc.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import jp.yuta.kohashi.esc.model.AttendanceRateModel;
import jp.yuta.kohashi.esc.model.NewsChildModel;
import jp.yuta.kohashi.esc.model.TimeBlockModel;
import jp.yuta.kohashi.esc.util.RegexManager;

/**
 * Created by YutaThinkPad on 2016/03/25.
 */
public class SaveManager {
//    private static Context mContext;
//
//    public SaveManager(Context context) {
//        mContext = context;
//    }

    public static void saveTimeTable(Context context, String html) {
        List<TimeBlockModel> mondayList = new ArrayList<>();
        List<TimeBlockModel> tuesdayList = new ArrayList<>();
        List<TimeBlockModel> wednesdayList = new ArrayList<>();
        List<TimeBlockModel> thursdayList = new ArrayList<>();
        List<TimeBlockModel> fridayList = new ArrayList<>();
        //取得したデータを曜日ごとに格納
        //1限目のHTMLソース
        createTimeTableObject("<tr><th class=\"term\">1", "<thclass=\"term\">2</th>", html, mondayList, tuesdayList, wednesdayList, thursdayList, fridayList);
        //2限目
        createTimeTableObject("<th class=\"term\">2</th>", "<thclass=\"term\">3</th>", html, mondayList, tuesdayList, wednesdayList, thursdayList, fridayList);
        //３限目
        createTimeTableObject("<th class=\"term\">3</th>", "<thclass=\"term\">4</th>", html, mondayList, tuesdayList, wednesdayList, thursdayList, fridayList);
        //4限目
        createTimeTableObject("<th class=\"term\">4</th>", "<h2>担任からのお知らせ</h2>", html, mondayList, tuesdayList, wednesdayList, thursdayList, fridayList);

        /******************* データベース登録処理 ******************/
        save(context, mondayList, PrefConst.KEY_MON_LIST, PrefConst.FILE_TIME_ATTEND);
        save(context, tuesdayList, PrefConst.KEY_TUE_LIST, PrefConst.FILE_TIME_ATTEND);
        save(context, wednesdayList, PrefConst.KEY_WED_LIST, PrefConst.FILE_TIME_ATTEND);
        save(context, thursdayList, PrefConst.KEY_THUR_LIST, PrefConst.FILE_TIME_ATTEND);
        save(context, fridayList, PrefConst.KEY_FRI_LIST, PrefConst.FILE_TIME_ATTEND);
    }

    public static void saveAttendanceRate(Context context, String html) {
        List<AttendanceRateModel> attendanceRateList = createAttendanceList(html);
        save(context, attendanceRateList, PrefConst.KEY_ATTEND, PrefConst.FILE_TIME_ATTEND);
    }

    public static void saveLoginState(Context context, boolean bool) {
        save(context, bool, PrefConst.KEY_LOGIN_STATE, PrefConst.FILE_STATE);
    }

    public static void saveChildNewsList(Context context, String html) {
        List<NewsChildModel> list = createNewsChildList(html);
        save(context, list, PrefConst.KEY_NEWS, PrefConst.FILE_NEWS);
    }


    /********************************************************************************************/
    /********************************************************************************************/
    /********************************************************************************************/


    /***
     * 出席率モデルのリストを作成するメソッド
     *
     * @param html
     * @return
     */
    private static List<AttendanceRateModel> createAttendanceList(String html) {
        List<AttendanceRateModel> attendanceRateList = new ArrayList();

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
     * 取得したデータを曜日ごとのLISTに格納
     *
     * @param start
     * @param end
     * @param html
     * @param mondayList
     * @param tuesdayList
     * @param wednesdayList
     * @param thursdayList
     * @param fridayList
     */
    private static void createTimeTableObject(String start, String end, String html, List mondayList, List tuesdayList, List wednesdayList, List thursdayList, List fridayList) {
        int weekCount = 0;

        String narrowHtml = RegexManager.narrowingValues(start, end, html);
        Matcher m = RegexManager.getGroupValues("<td>(.+?)</td>", narrowHtml);
        while (m.find()) {
            TimeBlockModel timeBlock = createTimeTableList(m.group());
            switch (weekCount) {
                case 0:
                    mondayList.add(timeBlock);
                    break;
                case 1:
                    tuesdayList.add(timeBlock);
                    break;
                case 2:
                    wednesdayList.add(timeBlock);
                    break;
                case 3:
                    thursdayList.add(timeBlock);
                    break;
                case 4:
                    fridayList.add(timeBlock);
                    break;
            }
            weekCount++;
        }

//        Matcher matcher1;
//
//        Pattern pattern1 = Pattern.compile("<td>(.+?)</td>");
//
//        String result= RegexManager.narrowingValues(start,end ,mLastResponse);
//
//        matcher1 = pattern1.matcher(result);
//        while(matcher1.find()) {
//            TimeBlockModel timeBlock = createTimeTableList(matcher1.group());
//            switch (weekCount) {
//                case 0:
//                    mondayList.add(timeBlock);
//                    break;
//                case 1:
//                    tuesdayList.add(timeBlock);
//                    break;
//                case 2:
//                    WednesdayList.add(timeBlock);
//                    break;
//                case 3:
//                    ThursdayList.add(timeBlock);
//                    break;
//                case 4:
//                    fridayList.add(timeBlock);
//                    break;
//            }
//            weekCount++;
//        }
    }

//    private void saveBaseManger(Context context, String prefName, List arrayList, String key) {
//        SharedPreferences sharedPreferences;
//        Gson gson;
//
//        sharedPreferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
//        gson = new Gson();
//        sharedPreferences.edit().putString(key, gson.toJson(arrayList)).commit();
//    }

    private static TimeBlockModel createTimeTableList(String html) {
        TimeBlockModel timeBlock = new TimeBlockModel();
//
//        //曜日ごとに切り出し
//        Pattern pattern1 = Pattern.compile("<td>(.+?)</td>");
//        Matcher matcher1 = pattern1.matcher(result);
//        while (matcher1.find()) {
//            //Log.d("regex::::", matcher1.group());
//
//            int count = 0;
//            Pattern pattern = Pattern.compile("<li>(.+?)</li>");
//            Matcher matcher = pattern.matcher(matcher1.group());
//
//
//            while (matcher.find()) {
//                switch (count) {
//                    case 0:
//                        timeBlock.setSubject(matcher.group().replaceAll("<.+?>", " ").trim());
//                        //Log.d("regex::::", matcher.group().replaceAll("<.+?>", " "));
//                        break;
//                    case 1:
//                        timeBlock.setClassRoom(matcher.group().replaceAll("<.+?>", " ").trim());
//                        //Log.d("regex::::", matcher.group().replaceAll("<.+?>", " "));
//                        break;
//                }
//                count++;
//            }
//        }
        Matcher m = RegexManager.getGroupValues("", html);
        while (m.find()) {
            int count = 0;
            Matcher m2 = RegexManager.getGroupValues("<td>(.+?)</td>", m.group());
            while (m2.find()) {
                switch (count) {
                    case 0:
                        timeBlock.setSubject(m2.group().replaceAll("<.+?>", " ").trim());
                        break;
                    case 1:
                        timeBlock.setClassRoom(m2.group().replaceAll("<.+?>", " ").trim());
                        break;
                }
                count++;
            }
        }
        return timeBlock;
    }

    private static List<NewsChildModel> createNewsChildList(String html) {
        List<NewsChildModel> list = new ArrayList<>();

        //担任からのお知らせを取得
        String tanninHtml = RegexManager.narrowingValues("<divclass=\"col\"><h2>担任からのお知らせ</h2>", "</div><divid=\"school_news_col\"class=\"col\">", html);

        Matcher m = RegexManager.getGroupValues("<li>(.*?)</li>", tanninHtml);
        //Matcher match2 = getValuesBase.GetGropValues("<td>(.+?)</td>",html3);

        while (m.find()) {
            String narrowHtml = m.group(1);
            String date = RegexManager.getValues("<span class=\"date\">(.*?)</span>", narrowHtml);
            String uri = RegexManager.getValues("<a href=\"(.*?)\">", narrowHtml);
            String title = RegexManager.getValues("</span><span class=\"title\"><a href=\"" + uri + "\">(.*?)</a></span>", narrowHtml);

            NewsChildModel model = new NewsChildModel(title, date, uri);

            list.add(model);
        }
        return list;
    }

    private static void save(Context context, List arrayList, String key, String fileName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, new Gson().toJson(arrayList)).commit();
    }

    private static void save(Context context, Boolean bool, String key, String fileName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, bool).commit();
    }

}
