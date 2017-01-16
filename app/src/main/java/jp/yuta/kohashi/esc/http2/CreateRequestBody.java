package jp.yuta.kohashi.esc.http2;

import java.util.HashMap;
import java.util.Map;

import jp.yuta.kohashi.esc.tools.GetValuesBase;

/**
 * Created by yutakohashi on 2016/11/15.
 */

public class CreateRequestBody {

    GetValuesBase getValuesBase;

    CreateRequestBody(){
        getValuesBase = new GetValuesBase();
    }

    // 山口学園学生サービスログイン時のリクエストボディ
    public Map<String,String> createPostDataForEscLogin(String userId, String password, String html){
        Map<String,String> body = new HashMap<>();
        String token = getValuesBase.GetToken(html);

        body.put("_token",token);
        body.put("userid",userId);
        body.put("password",password);
        return body;
    }

    // 山口学園学生サービスログイン時のリクエストボディ
    public  Map<String,String> createPostDataForYSLogin(String userId,String password,String html){
        String __LASTFOCUS = getValuesBase.GetValues("input type=\"hidden\" name=\"__LASTFOCUS\" id=\"__LASTFOCUS\" value=\"(.+?)\"",html);
        String __VIEWSTATE =  getValuesBase.GetValues("input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"(.+?)\"",html);
        String __SCROLLPOSITIONX = getValuesBase.GetValues("input type=\"hidden\" name=\"__SCROLLPOSITIONX\" id=\"__SCROLLPOSITIONX\" value=\"(.+?)\"",html);
        String __SCROLLPOSITIONY = getValuesBase.GetValues("input type=\"hidden\" name=\"__SCROLLPOSITIONY\" id=\"__SCROLLPOSITIONY\" value=\"(.+?)\"",html);
        String __EVENTTARGET = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"(.+?)\"",html);
        String __EVENTARGUMENT = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"(.+?)\"",html);
        String __EVENTVALIDATION = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"(.+?)\"",html);
        String ctl00$ContentPlaceHolder1$txtUserId = userId;
        String ctl00$ContentPlaceHolder1$txtPassword = password;
        String ctl00$ContentPlaceHolder1$btnLogin = "ログイン";

        Map<String,String> body = new HashMap<>();
        body.put("__LASTFOCUS",__LASTFOCUS);
        body.put("__VIEWSTATE",__VIEWSTATE);
        body.put("__SCROLLPOSITIONX",__SCROLLPOSITIONX);
        body.put("__SCROLLPOSITIONY",__SCROLLPOSITIONY);
        body.put("__EVENTTARGET",__EVENTTARGET);
        body.put("__EVENTARGUMENT",__EVENTARGUMENT);
        body.put("__EVENTVALIDATION",__EVENTVALIDATION);
        body.put("ctl00$ContentPlaceHolder1$txtUserId",ctl00$ContentPlaceHolder1$txtUserId);
        body.put("ctl00$ContentPlaceHolder1$txtPassword",ctl00$ContentPlaceHolder1$txtPassword);
        body.put("ctl00$ContentPlaceHolder1$btnLogin",ctl00$ContentPlaceHolder1$btnLogin);

        return body;
    }

    // 出席率画面遷移時のリクエストボディ
    public Map<String,String>  createPostDataForRatePage(String userId,String password,String html){
        Map<String,String> body = new HashMap<>();

        String __EVENTTARGET2 = "ctl00$btnSyuseki";
        String __EVENTARGUMENT2 = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"(.+?)\"",html);
        String __VIEWSTATE2 = getValuesBase.GetValues("input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"(.+?)\"",html);
        String __SCROLLPOSITIONX2 = getValuesBase.GetValues("input type=\"hidden\" name=\"__SCROLLPOSITIONX\" id=\"__SCROLLPOSITIONX\" value=\"(.+?)\"",html);
        String __SCROLLPOSITIONY2 = getValuesBase.GetValues("input type=\"hidden\" name=\"__SCROLLPOSITIONY\" id=\"__SCROLLPOSITIONY\" value=\"(.+?)\"",html);
        String __EVENTVALIDATION2 = getValuesBase.GetValues("input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"(.+?)\"",html);
        String ctl00$txtWindowOpenFlg = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtWindowOpenFlg\" id=\"ctl00_txtWindowOpenFlg\" value=\"(.+?)\"",html);
        String ctl00$txtWindowOpenUrl = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtWindowOpenUrl\" id=\"ctl00_txtWindowOpenUrl\" value=\"(.+?)\"",html);
        String ctl00$txtWindowOpenName = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtWindowOpenName\" id=\"ctl00_txtWindowOpenName\" value=\"(.+?)\"",html);
        String ctl00$txtWindowOpenStyle = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtWindowOpenStyle\" id=\"ctl00_txtWindowOpenStyle\" value=\"(.+?)\"",html);
        String ctl00$txtSearchKey = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtSearchKey\" id=\"ctl00_txtSearchKey\" value=\"(.+?)\"",html);
        String ctl00$txtParamKey = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtParamKey\" id=\"ctl00_txtParamKey\" value=\"(.+?)\"",html);
        String ctl00$txtCssFileName = getValuesBase.GetValues("input type=\"hidden\" name=\"ctl00\\$txtCssFileName\" id=\"ctl00_txtCssFileName\" value=\"(.+?)\"",html);
        String ctl00$txtHeadTitle = "";
//
        body.put("__EVENTTARGET",__EVENTTARGET2);
        body.put("__EVENTARGUMENT",__EVENTARGUMENT2);
        body.put("__VIEWSTATE",__VIEWSTATE2);
        body.put("__SCROLLPOSITIONX",__SCROLLPOSITIONX2);
        body.put("__SCROLLPOSITIONY",__SCROLLPOSITIONY2);
        body.put("__EVENTVALIDATION",__EVENTVALIDATION2);
        body.put("ctl00$txtWindowOpenFlg",ctl00$txtWindowOpenFlg);
        body.put("ctl00$txtWindowOpenUrl",ctl00$txtWindowOpenUrl);
        body.put("ctl00$txtWindowOpenName",ctl00$txtWindowOpenName);
        body.put("ctl00$txtWindowOpenStyle",ctl00$txtWindowOpenStyle);
        body.put("ctl00$txtSearchKey",ctl00$txtSearchKey);
        body.put("ctl00$txtParamKey",ctl00$txtParamKey);
        body.put("ctl00$txtCssFileName",ctl00$txtParamKey);
        body.put("ctl00$txtCssFileName",ctl00$txtCssFileName);
        body.put("ctl00$txtHeadTitle",ctl00$txtHeadTitle);

        return body;
    }
}
