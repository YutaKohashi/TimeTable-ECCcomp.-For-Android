package jp.yuta.kohashi.esc.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import jp.yuta.kohashi.esc.App;
import jp.yuta.kohashi.esc.R;

/**
 * Created by yutakohashi on 2017/01/16.
 */

public class NotifyUtil {
    

    public static void successUpdate(){
        showToast("更新しました",R.drawable.ic_done);
    }

    public static void failureUpdate(){
        showToast("更新に失敗しました",R.drawable.ic_error);
    }

    public static void failureLogin(){
        showToast("ログインに失敗しました",R.drawable.ic_error);
    }

    public static void failureAttendanceRate(){showToast("出席情報の取得に\n失敗しました",R.drawable.ic_error);}

    public static void failureNetworkConnection(){showToast("インターネットに\n接続されていません",R.drawable.ic_portable_wifi_off);}

    public static void failureFileLoad(){
        showToast("ファイルのロードに失敗しました",R.drawable.ic_error);
    }

    public static void saveData(){
        showToast("保存されました。",R.drawable.ic_error);
    }

    public static void notChangeData(){
        showToast("データが変更されていません",R.drawable.ic_error);
    }

    public static void showToast(String string,int icon){
        LayoutInflater _inflater = (LayoutInflater) App.getAppContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );;
        View toastLayout = _inflater.inflate(R.layout.toast_custom_layout,null);
        ((TextView)toastLayout.findViewById(R.id.toast_text_View)).setText(string);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((ImageView)toastLayout.findViewById(R.id.toast_imdge_view)).setImageDrawable(App.getAppContext().getResources().getDrawable(icon,null));
        } else {
            ((ImageView)toastLayout.findViewById(R.id.toast_imdge_view)).setImageDrawable(App.getAppContext().getResources().getDrawable(icon));
        }
        Toast toast = new Toast(App.getAppContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }

    private static ProgressDialog mDialog;
    public static void showLoadingDiag(Context context){
        mDialog = createProgressDialog(context,"読み込み中...");
        mDialog.show();
    }

    public static void showLogoutingDiag(Context context){
        mDialog = createProgressDialog(context, "ログアウトしています...");
        mDialog.show();
    }

    public static void showUpdatingDiag(Context context){
        mDialog = createProgressDialog(context ,"更新中...");
        mDialog.show();
    }

    public static void dismiss(){
        mDialog.dismiss();
    }

    private static ProgressDialog createProgressDialog(Context context, String string) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show(); //　必須
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.dialog_loading,null,false);
        ((TextView)view.findViewById(R.id.dialog_text_view)).setText(string);
        dialog.setContentView(view);
        return dialog;
    }
}
