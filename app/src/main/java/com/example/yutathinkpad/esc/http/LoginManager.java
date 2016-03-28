package com.example.yutathinkpad.esc.http;

import android.content.Context;
import android.view.View;

/**
 * Created by Yuta on 2016/03/28.
 */
public class LoginManager {
    public void loginManager(final Context context, View view, String userId, String password){
        //アプデートマネージャの使用
        UpdateTimeTableManager utt = new UpdateTimeTableManager();
        utt.upDateTimeTableForLogin(context,userId,password);

        //GetAttendanceRateManager attendanceRateManager = new GetAttendanceRateManager();
        //attendanceRateManager.getAttendanceRate(context,userId,password);
    }
}
