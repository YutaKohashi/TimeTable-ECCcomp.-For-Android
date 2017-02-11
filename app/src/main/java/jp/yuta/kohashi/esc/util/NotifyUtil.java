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
        showToast(App.getAppContext().getResources().getString(R.string.success_update),R.drawable.ic_done);
    }

    public static void failureUpdate(){
        showToast(App.getAppContext().getResources().getString(R.string.failure_update),R.drawable.ic_error);
    }

    public static void failureLogin(){
        showToast(App.getAppContext().getString(R.string.failure_login),R.drawable.ic_error);
    }

    public static void failureAttendanceRate(){showToast(App.getAppContext().getResources().getString(R.string.failure_get_attendance_rate),R.drawable.ic_error);}

    public static void failureNetworkConnection(){showToast(App.getAppContext().getResources().getString(R.string.failure_connect_internet),R.drawable.ic_portable_wifi_off);}

    public static void failureFileLoad(){
        showToast(App.getAppContext().getResources().getString(R.string.failure_load_file),R.drawable.ic_error);
    }

    public static void saveData(){
        showToast(App.getAppContext().getResources().getString(R.string.success_save),R.drawable.ic_save);
    }

    public static void allReset(){
        showToast(App.getAppContext().getResources().getString(R.string.all_reset),R.drawable.ic_settings_backup_restore);
    }

    public static void notChangeData(){
        showToast(App.getAppContext().getResources().getString(R.string.not_change_data),R.drawable.ic_error);
    }

    public static void  cancel(){
        showToast(App.getAppContext().getResources().getString(R.string.cancel),R.drawable.ic_cancel);
    }

    private static ProgressDialog mDialog;
    public static void showLoadingDiag(Context context){
        mDialog = createProgressDialog(context,context.getResources().getString(R.string.loading));
        mDialog.show();
    }

    public static void showLogoutingDiag(Context context){
        mDialog = createProgressDialog(context, context.getResources().getString(R.string.logouting));
        mDialog.show();
    }

    public static void showUpdatingDiag(Context context){
        mDialog = createProgressDialog(context ,context.getResources().getString(R.string.updating));
        mDialog.show();
    }

    public static void dismiss(){
        mDialog.dismiss();
    }

    public static void showToast(String string,int icon){
        LayoutInflater _inflater = (LayoutInflater) App.getAppContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );;
        View toastLayout = _inflater.inflate(R.layout.toast_custom_layout,null);
        ((TextView)toastLayout.findViewById(R.id.toast_text_View)).setText(string);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((ImageView)toastLayout.findViewById(R.id.toast_image_view)).setImageDrawable(App.getAppContext().getResources().getDrawable(icon,null));
        } else {
            ((ImageView)toastLayout.findViewById(R.id.toast_image_view)).setImageDrawable(App.getAppContext().getResources().getDrawable(icon));
        }
        Toast toast = new Toast(App.getAppContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
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
