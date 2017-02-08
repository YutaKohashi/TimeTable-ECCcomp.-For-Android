package jp.yuta.kohashi.esc.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import jp.yuta.kohashi.esc.App;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.ui.service.EccNewsManageService;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by yutakohashi on 2017/01/10.
 */

public class Util {

    /**
     * ネットワーク接続確認
     *
     * @return
     */
    public static boolean netWorkCheck() {
        ConnectivityManager cm = (ConnectivityManager) App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            return info.isConnected();
        } else {
            return false;
        }
    }

    /**
     * テキストフィールドチェック
     *
     * @param view
     * @return
     */
    public static boolean checkTextField(AppCompatEditText view) {
        String text = view.getText().toString();
        if (TextUtils.isEmpty(text)) {
            view.setError(App.getAppContext().getString(R.string.string_text_view_error));
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkTextField(AppCompatEditText view, TextInputLayout wrapper) {
        String text = view.getText().toString();
        if (TextUtils.isEmpty(text)) {
            wrapper.setError(App.getAppContext().getString(R.string.string_text_view_error));
            return false;
        } else {
            return true;
        }
    }


    /**
     * @return
     */
    public static float getDisplayScale() {
        WindowManager wm = (WindowManager) App.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.scaledDensity;
    }

    /**
     * タブのポジションから月を求める
     * @param position
     * @return
     */
    public static int getTabPositionToMonth(int position){
        int month;
        if(position < 9){
            month = position + 4;
        } else {
            month = position - 8;
        }
        return month;
    }

    public static int getMonthToTabPosition(int month){
        int position;
        if(month <= 3) {
            position = month + 8;
        } else {
            position = month - 4;
        }
        return position;
    }


    public static boolean isStartService(){
        boolean found = false;
        ActivityManager am = (ActivityManager) App.getAppContext().getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> listServiceInfo = am.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo curr : listServiceInfo) {
            // クラス名を比較
            if (curr.service.getClassName().equals(EccNewsManageService.class.getName())) {
                // 実行中のサービスと一致
                found = true;
            }
        }
        return found;
    }

    public static Drawable getDrawable(int id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return App.getAppContext().getResources().getDrawable(id, null);
        else
            return App.getAppContext().getResources().getDrawable(id);
    }

    public static int getColor(int id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return App.getAppContext().getResources().getColor(id, null);
        else
            return App.getAppContext().getResources().getColor(id);
    }

    public static long getCurrentTimeMillis(){
        return new Date(System.currentTimeMillis()).getTime();
    }

    //**
    //region assets フォルダから、テキストファイルを読み込む(Android 用)
    //**

    public static String loadTextAsset(String fileName) throws IOException {
        final AssetManager assetManager = App.getAppContext().getAssets();
        InputStream is = assetManager.open(fileName);
        return loadText(is, "UTF-8");
    }

    //ストリームから読み込み、バイト配列で返す
    private static byte[] readStream(InputStream inputStream, int readLength) throws IOException {
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
    private static String loadText(InputStream inputStream, String charsetName)
            throws IOException, UnsupportedEncodingException {
        return new String(readStream(inputStream, DEFAULT_READ_LENGTH), charsetName);
    }


    //**
    //endregion
    //**

}
