package jp.yuta.kohashi.esc.tools;

import android.util.Log;

import jp.yuta.kohashi.esc.object.TimeBlock;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yuta on 2016/03/21.
 */
public class CreateTimeTableLists {

    public TimeBlock CreateTimeTableList(String result) {
        TimeBlock timeBlock = new TimeBlock();

        //曜日ごとに切り出し
        Pattern pattern1 = Pattern.compile("<td>(.+?)</td>");
        Matcher matcher1 = pattern1.matcher(result);
        while (matcher1.find()) {
            //Log.d("regex::::", matcher1.group());

            int count = 0;
            Pattern pattern = Pattern.compile("<li>(.+?)</li>");
            Matcher matcher = pattern.matcher(matcher1.group());


            while (matcher.find()) {
                switch (count) {
                    case 0:
                        timeBlock.setSubject(matcher.group().replaceAll("<.+?>", " ").trim());
                        //Log.d("regex::::", matcher.group().replaceAll("<.+?>", " "));
                        break;
                    case 1:
                        timeBlock.setClassRoom(matcher.group().replaceAll("<.+?>", " ").trim());
                        //Log.d("regex::::", matcher.group().replaceAll("<.+?>", " "));
                        break;
                }
                count++;
            }
        }
        return timeBlock;
    }

}
