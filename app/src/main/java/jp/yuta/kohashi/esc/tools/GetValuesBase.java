package jp.yuta.kohashi.esc.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

}
