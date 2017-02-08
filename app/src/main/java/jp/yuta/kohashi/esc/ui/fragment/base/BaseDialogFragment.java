package jp.yuta.kohashi.esc.ui.fragment.base;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.EventListener;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.TimeBlockItem;

/**
 * Created by yutakohashi on 2017/02/08.
 */

public abstract class BaseDialogFragment extends DialogFragment implements View.OnClickListener  {
    protected static Callback callback = null;
    protected Dialog mDialog;
    protected LinearLayout mOkBtn;
    protected LinearLayout mCancelBtn;

    public interface Callback extends EventListener {
        void positive(TimeBlockItem before, TimeBlockItem after);
        void negative();
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity());
        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE); // タイトル非表示
        mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN); // フルスクリーン
        mDialog.setContentView(setContentViewId());
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 背景を透明にする

        initView();

        return mDialog;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ok_button:
                pressOkButton();
                dismiss();
                break;
            case R.id.cancel_button:
                pressNegativeButton();
                dismiss();
                break;
        }
    }

    abstract protected int setContentViewId();

    protected void initView(){
        mOkBtn = (LinearLayout)mDialog.findViewById(R.id.ok_button);
        mCancelBtn = (LinearLayout)mDialog.findViewById(R.id.cancel_button);
        mOkBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    };


    abstract protected void pressOkButton();

    abstract protected void pressNegativeButton();


}
