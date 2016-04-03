package jp.yuta.kohashi.esc;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Yuta on 2016/03/29.
 */
public class stab {
    public String FireLoad(Context context) {
        String returnStr = "";

        try {
            //ファイルを読み込む
            Resources res = context.getResources();
            InputStream inputStream = res.openRawResource(R.raw.shusekirate);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));


            //読み込んだファイルを１行ずつ画面出力する
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                returnStr += line;
            }

            //終了処理
            br.close();
            //fr.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnStr;


    }
}
