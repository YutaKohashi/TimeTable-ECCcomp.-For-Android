package jp.yuta.kohashi.esc.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yutakohashi on 2017/01/05.
 */

public class RegexManager {
    static final String TAG = RegexManager.class.getSimpleName();

    /***
     *
     * @param regex
     * @param target
     * @return
     */
    public static String getValues(String regex,String target){
        String result = "";
        try{
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(target);
            if(matcher.find()){
                result = matcher.group(1);
            }

        }catch(Exception e){
            Log.d(TAG,e.toString());
            result = "";
        }
        return result;
    }

    /***
     *
     * @param regex
     * @param target
     * @return
     */
    public static Matcher getGroupValues(String regex,String target){
        Pattern exp = Pattern.compile(regex);
        Matcher matcher = exp.matcher(target);
        return matcher;
    }

    /***
     *
     * @param start
     * @param end
     * @param target
     * @return
     */
    public static String narrowingValues(String start,String end,String target){
        String keyStr = "";
        String key = start + "(.+?)" + end;
        Pattern exp = Pattern.compile(key);
        Matcher matcher = exp.matcher(target);
        if(matcher.find()){
            keyStr = matcher.group();
        }
        return  keyStr;
    }

    /***
     *
     * @param target
     * @param tab
     * @return
     */
    public static String replaceCRLF(String target,boolean tab){
        String after;
        try{
            after = target.replaceAll("[\r\n]", "");
            after = after.replaceAll("[\n\r]", "");
            after= after.replaceAll("[\n]", "");

            if(tab){
                //タブを消去
                after = after.replaceAll("\t", "");
            }
//            //スペースを消去
//            after = after.replaceAll(" ","");
        }catch(Exception e){
            Log.d(TAG,e.toString());
            after = "";
        }

        return after;
    }

    public static String deletePercent(String str){
        str = str.replaceAll("%","");
        return str;
    }

    /***
     *
     * @param str
     * @return
     */
    public static String deleteNBSPTo0(String str){
        str = str.replaceAll("&nbsp;","0");
        return str;
    }
}
