package com.example.yutathinkpad.esc.tools;

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
        if(token.equals("")){
            token = "ERROR";
            Log.d(TAG,"トークンの取得に失敗");
        }
        return token;
    }


}
