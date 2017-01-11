package jp.yuta.kohashi.esc.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.TextView;

import jp.yuta.kohashi.esc.R;

/**
 * Created by yutakohashi on 2017/01/10.
 */

public class Util {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * ネットワーク接続確認
     *
     * @return
     */
    public static boolean netWorkCheck() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
    public static boolean checkTextField(TextView view) {
        String text = view.getText().toString();
        if (TextUtils.isEmpty(text)) {
            view.setError(mContext.getString(R.string.string_text_view_error));
            return false;
        } else {
            return true;
        }
    }

}
