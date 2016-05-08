package jp.yuta.kohashi.esc.http;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import jp.yuta.kohashi.esc.preference.SaveManager;
import jp.yuta.kohashi.esc.tools.GetValuesBase;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yuta on 2016/05/06.
 */
public class GetTeacherNameManager {
    final String PREF_NAME ="sample";
    final String PREF_TEACHERS_KEY = "teachers";

    GetValuesBase getValuesBase;
    String mLastResponse;

    SharedPreferences pref;

    List<String> teacherNames;
    SaveManager saveManager;

    /**
     *
     * @param html      時間割ページのhtmlソース
     * @param context   コンテキスト
     * @param client    OkhttpClientのインスタンス
     */
    public void getTeacherNames(String html, Context context,OkHttpClient client){

        //htmlに時間割ページのhtmlソースを入れる

        //教科個別のページにアクセスし先生の名前を取得
        //プリファレンスをインスタンス化
        pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        teacherNames = new ArrayList<>();

        //************ 解析処理 ***************************************
        getValuesBase = new GetValuesBase();
        //教科個別のページへアクセスするURLをMatcher型で取得
        Matcher match = getValuesBase.GetGropValues("<li class=\"letter\"><a href=\"(.*?)\">",html);

        while(match.find()){
            String subjectHtml = "";
            subjectHtml = match.group(1);


            Request request = new Request.Builder()
                    .url(subjectHtml)
                    .addHeader("Referer","http://school4.ecc.ac.jp/eccstdweb/st0100/st0100_01.aspx")
                    .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (HTML, like Gecko) Chrome/49.0.2623.87")
                    .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .build();
            Response response;
            try{
                response = client.newCall(request).execute();
                Thread.sleep(500);

                mLastResponse = response.body().string();
            }catch(IOException | InterruptedException e){
                e.printStackTrace();
            }

            //先生の名前を取り出す
            String teacherName = getTeacherName(mLastResponse);

            teacherNames.add(teacherName);
        }

        //作成された先生の名前のコレクションをプリファレンスに保存
        saveManager = new SaveManager();
        saveManager.saveMangerWithPreference(context,PREF_NAME,teacherNames,PREF_TEACHERS_KEY);
    }

    /**
     *
     * @param html 教科個別のページ
     * @return 先生名
     */
    private String getTeacherName(String html){
        //先生の名前を格納する変数
        String teacherName = "";

        getValuesBase = new GetValuesBase();

        teacherName = getValuesBase.GetValues("<h3>受信者</h3><p>(.*?)</p>",html,true);

        return teacherName;
    }
}
