package jp.yuta.kohashi.esc.preference;

import android.content.Context;
import android.content.SharedPreferences;

import jp.yuta.kohashi.esc.object.AttendanceRateObject;
import jp.yuta.kohashi.esc.object.TimeBlock;
import jp.yuta.kohashi.esc.tools.CreateTimeTableLists;
import jp.yuta.kohashi.esc.tools.GetValuesBase;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YutaThinkPad on 2016/03/25.
 */
public class SaveManager {
    private Context mContext;

    public SaveManager(Context context){
        mContext  = context;
    }

    public void saveTimeTable(String html){

        // TODO:固定値
        final String PREF_NAME = "";

        List<TimeBlock> MondayList = new ArrayList<>();
        List<TimeBlock> TuesdayList = new ArrayList<>();
        List<TimeBlock>  WednesdayList = new ArrayList<>();
        List<TimeBlock>  ThursdayList = new ArrayList<>();
        List<TimeBlock> FridayList = new ArrayList<>();
        //取得したデータを曜日ごとに格納
        //1限目のHTMLソース
        createTimeTableObject("<tr><thclass=\"term\">1","<thclass=\"term\">2</th>",html,MondayList,TuesdayList,WednesdayList,ThursdayList,FridayList);
        //2限目
        createTimeTableObject("<thclass=\"term\">2</th>","<thclass=\"term\">3</th>",html,MondayList,TuesdayList,WednesdayList,ThursdayList,FridayList);
        //３限目
        createTimeTableObject("<thclass=\"term\">3</th>","<thclass=\"term\">4</th>",html,MondayList,TuesdayList,WednesdayList,ThursdayList,FridayList);
        //4限目
        createTimeTableObject("<thclass=\"term\">4</th>","<h2>担任からのお知らせ</h2>",html,MondayList,TuesdayList,WednesdayList,ThursdayList,FridayList);

        /******************* 以下データベース登録処理 ******************/
        saveMangerWithPreference(mContext, PREF_NAME,MondayList,"monList");
        saveMangerWithPreference(mContext, PREF_NAME,TuesdayList,"tueList");
        saveMangerWithPreference(mContext, PREF_NAME,WednesdayList,"wedList");
        saveMangerWithPreference(mContext, PREF_NAME,ThursdayList,"thurList");
        saveMangerWithPreference(mContext, PREF_NAME,FridayList,"friList");
    }

    public void saveAttendanceRate(String html){

    }

    public void saveMangerWithPreference(Context context, String prefName, List arrayList, String key){
        SharedPreferences sharedPreferences;
        Gson gson;

        sharedPreferences = context.getSharedPreferences(prefName,context.MODE_PRIVATE);
        gson = new Gson();
        sharedPreferences.edit().putString(key,gson.toJson(arrayList)).commit();
    }

    /**
     * 取得したデータを曜日ごとのLISTに格納
     * @param start
     * @param end
     * @param mLastResponse
     * @param MondayList
     * @param TuesdayList
     * @param WednesdayList
     * @param ThursdayList
     * @param FridayList
     */
    private static void createTimeTableObject(String start,String end,String mLastResponse,List MondayList, List TuesdayList,List WednesdayList,List ThursdayList, List FridayList){
        int weekCount = 0;
        Matcher matcher1;

        //使用するクラスをインスタンス化
        GetValuesBase getValuesBase = new GetValuesBase();
        CreateTimeTableLists createList = new CreateTimeTableLists();
        Pattern pattern1 = Pattern.compile("<td>(.+?)</td>");

        String result= getValuesBase.NarrowingValues(start,end ,mLastResponse,true);
        matcher1 = pattern1.matcher(result);
        while(matcher1.find()) {
            TimeBlock timeBlock =createList.CreateTimeTableList(matcher1.group());
            switch (weekCount) {
                case 0:
                    MondayList.add(timeBlock);
                    break;
                case 1:
                    TuesdayList.add(timeBlock);
                    break;
                case 2:
                    WednesdayList.add(timeBlock);
                    break;
                case 3:
                    ThursdayList.add(timeBlock);
                    break;
                case 4:
                    FridayList.add(timeBlock);
                    break;
            }
            weekCount++;
        }
    }

    private  void saveBaseManger(Context context, String prefName, List arrayList, String key){
        SharedPreferences sharedPreferences;
        Gson gson;

        sharedPreferences = context.getSharedPreferences(prefName,context.MODE_PRIVATE);
        gson = new Gson();
        sharedPreferences.edit().putString(key,gson.toJson(arrayList)).commit();
    }


}
