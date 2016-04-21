package jp.yuta.kohashi.esc.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.yuta.kohashi.esc.object.ScheduleJJsonObject;

/**
 * Created by Yuta on 2016/03/21.
 */
public class GetValuesBase {
    final String TAG ="error:::";

    /**
     *
     * @param start     切り出しはじめの文字列
     * @param end       切り出し終わりの文字列
     * @param target    ターゲット文字列
     * @return
     */
    public String NarrowingValues(String start, String end,String target,Boolean bln){
        int startIndex;
        int endIndex;
        String returnStr = "";

        if(bln){
            target = ReplaceCRLF(target);
        }

        startIndex = target.indexOf(start);
        endIndex = target.indexOf(end);

        returnStr = target.substring(startIndex,endIndex);

        return returnStr;
    }

    public String NarrowingValuesforJson(String start, String end,String target,Boolean bln){
        int startIndex;
        int endIndex;
        String returnStr = "";

        if(bln){
            target = ReplaceCRLF(target);
        }

        startIndex = target.indexOf(start);
        endIndex = target.indexOf(end);

        returnStr = target.substring(startIndex,endIndex);

        returnStr = returnStr.replace(start,"");
        returnStr = "[" + returnStr+ "]";
        return returnStr;
    }

    public String NarrowingValues(String start,String end,String target){
        String keyStr = "";
        String key = start + ".*?" + end;
        Pattern exp = Pattern.compile(key);
        Matcher matcher = exp.matcher(target);
        if(matcher.find()){
            keyStr = matcher.group();
        }
        return  keyStr;
    }

    /**
     *
     * @param start
     * @param end
     * @param target
     * @param bln       タブ改行を削除するか
     * @return
     */
    public String NarrowingLastValues(String start, String end,String target,Boolean bln) {
        int startIndex;
        int endIndex;
        String returnStr = "";

        if(bln){
            target = ReplaceCRLF(target);
        }

        startIndex = target.lastIndexOf(start);
        endIndex = target.lastIndexOf(end);

        returnStr = target.substring(startIndex,endIndex);

        return returnStr;
    }


    /**
     *
     * @param regex
     * @param target
     * @param bln       タブ,改行を削除するか
     * @return
     */
    public String GetValues(String regex,String target,Boolean bln){
        String keyStr = "";

        Pattern exp = Pattern.compile(regex);

        if(bln){
            target = ReplaceCRLF(target);
        }

        Matcher matcher = exp.matcher(target);
        if(matcher.find()){
            keyStr = matcher.group(1);
        }

        return keyStr;
    }

//    public String GetLastValues(String regex,String target){
//
//    }

    /**
     *
     * @param key
     * @param target
     * @return
     */
    public boolean ContainsCheck(String key,String target){
        Boolean bln =false;


        if(target.indexOf(key)!= -1){
            bln = true;
        }

        return bln;
    }


    public String ReplaceCRLF(String target){
        String afterStr;
        try{
            afterStr = target.replaceAll("[\r\n]", "");
            afterStr = afterStr.replaceAll("[\n\r]", "");
            afterStr= afterStr.replaceAll("[\n]", "");

            //タブを消去
            afterStr = afterStr.replaceAll("\t", "");

            //スペースを消去
            afterStr = afterStr.replaceAll(" ","");
        }catch(NullPointerException e){
            afterStr = "";
        }


        return afterStr;
    }

    //トークンの切り出し処理
    public String GetToken(String target){
        //TOKENをここで取得する
        //matchで取得
        //input name="_token" type="hidden" value="Nffzc7TtEFFuuhKC6bL4rg7ViPib1jkVxj2QBqlk"
        String token = "";
        Pattern exp = Pattern.compile("input name=\"_token\" type=\"hidden\" value=\"(.+?)\"");
        Matcher matcher = exp.matcher(target);
        if(matcher.find()){
            token = matcher.group(1);
        }
        return token;
    }

    public String GetValues(String key,String target){
        String returnStr = "";
        Pattern exp = Pattern.compile(key);
        Matcher matcher = exp.matcher(target);

        if(matcher.find()){
            returnStr = matcher.group(1);
        }

        if(key.equals("")){
            returnStr= "ERROR";
            Log.d(TAG,"失敗");
        }

        return returnStr;
    }

    /**
     *
     * @param key regex
     * @param target target
     * @return Matcher型で返り値を返す
     */
    public Matcher GetGropValues(String key,String target){

        Pattern exp = Pattern.compile(key);

        Matcher matcher = exp.matcher(target);

        return matcher;
    }

    public String DeletePercent(String str){
        str = str.replaceAll("%","");
        return str;
    }

    public String DeleteNBSPTo0(String str){
        str = str.replaceAll("&nbsp;","0");
        return str;
    }

    public static final int PREFERENCE_INIT = 0;
    public static final int PREFERENCE_BOOTED = 1;

    public int GetLoginState(Context context){
        int state;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        state = pref.getInt("InitState",PREFERENCE_INIT);

        return state;
    }

    public void SetLoginState(Context context,boolean state){
        int state2 = 0;

        if(state){
            state2 = 1;
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putInt("InitState",state2).apply();

    }

    public boolean IsBooted(Context context){
        int state = GetLoginState(context);

        boolean rtn = false;
        if(state == PREFERENCE_INIT){
            rtn =  false;
        }else if(state == PREFERENCE_BOOTED){
            rtn = true;
        }

        return rtn;
    }



    ConnectivityManager cm;
    public boolean ConnectionCheck(Context context){

        boolean rtnbln = true;
        cm = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =cm.getActiveNetworkInfo();

        if (networkInfo == null) {
            //Toast.makeText(this, "No Network Connection!", Toast.LENGTH_LONG)
            //.show();
            rtnbln = false;
        }

        return rtnbln;

    }

    String jsonText = "";
    InputStream inputStream;
    BufferedReader bufferedReader;
    public String returnJsonStr(String assetsFimeName, Context context){
        //返り値の変数を初期化
        jsonText = "";

        try {
            try {
                // JSON データを assets から取得
                inputStream = context.getAssets().open(assetsFimeName);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                // １行ずつ読み込み、改行を付加する
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    jsonText += str + "\n";
                }
            } finally {
                if (inputStream != null) inputStream.close();
                if (bufferedReader != null) bufferedReader.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonText;
    }



    //assets フォルダから、テキストファイルを読み込む(Android 用)
    private static final String DEFAULT_ENCORDING = "UTF-8";//デフォルトのエンコード

    public static final String loadTextAsset(String fileName, Context context) throws IOException {
        final AssetManager assetManager = context.getAssets();
        InputStream is = assetManager.open(fileName);
        return  loadText(is, DEFAULT_ENCORDING);
    }

    //ストリームから読み込み、バイト配列で返す
    public static final byte[] readStream(InputStream inputStream, int readLength) throws IOException {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream(readLength);  //一時バッファのように使う
        final byte[] bytes = new byte[readLength];    //read() 毎に読み込むバッファ
        final BufferedInputStream bis = new BufferedInputStream(inputStream, readLength);

        try {
            int len = 0;
            while ((len = bis.read(bytes, 0, readLength)) > 0) {
                byteStream.write(bytes, 0, len);    //ストリームバッファに溜め込む
            }
            return byteStream.toByteArray();    //byte[] に変換

        } finally {
            try {
                byteStream.reset();     //すべてのデータを破棄
                bis.close();            //ストリームを閉じる
            } catch (Exception e) {
                //IOException
            }
        }
    }

    //設定値
    private static final int DEFAULT_READ_LENGTH = 8192;      //一度に読み込むバッファサイズ
    //ストリームから読み込み、テキストエンコードして返す
    public static final String loadText(InputStream inputStream, String charsetName)
            throws IOException, UnsupportedEncodingException {
        return new String(readStream(inputStream, DEFAULT_READ_LENGTH), charsetName);
    }


    //配列→コレクション
    public static List<ScheduleJJsonObject> convertArrToCollection(ScheduleJJsonObject[] array){
        return new ArrayList<ScheduleJJsonObject>(Arrays.asList(array));
    }

    public static float getDisplayScale(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.scaledDensity;
    }


}
