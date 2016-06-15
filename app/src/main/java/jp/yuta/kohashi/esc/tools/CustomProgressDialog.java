package jp.yuta.kohashi.esc.tools;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import jp.yuta.kohashi.esc.R;

/**
 * Created by Yuta on 2016/04/03.
 */
public class CustomProgressDialog {

    public static android.app.ProgressDialog createProgressDialogForTimeTableUpdate(Context mContext) {
        android.app.ProgressDialog dialog = new android.app.ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_dialog_layout);
        // dialog.setMessage(Message);
        return dialog;
    }

    public static android.app.ProgressDialog createProgressDialogForLogout(Context mContext) {
        android.app.ProgressDialog dialog = new android.app.ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_dialog_logout);
        // dialog.setMessage(Message);
        return dialog;
    }


    public static android.app.ProgressDialog createProgressDialogForFileRead(Context mContext) {
        android.app.ProgressDialog dialog = new android.app.ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_dialog_pdf);
        // dialog.setMessage(Message);
        return dialog;
    }

    public static android.app.ProgressDialog createProgressDialogForLoading(Context mContext) {
        android.app.ProgressDialog dialog = new android.app.ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_dialog_loading);
        // dialog.setMessage(Message);
        return dialog;
    }

}
