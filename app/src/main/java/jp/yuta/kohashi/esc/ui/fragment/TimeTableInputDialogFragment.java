package jp.yuta.kohashi.esc.ui.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.EventListener;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.TimeBlockModel;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * Created by yutakohashi on 2017/01/19.
 */

public class TimeTableInputDialogFragment extends DialogFragment implements View.OnClickListener {
    private Callback callback = null;

    private Dialog mDialog;
    private TimeBlockModel beforeModel;

    private EditText mSubjectTextView;
    private EditText mRoomTextView;
    private EditText mTeacherTextView;
    private LinearLayout mOkBtn;
    private LinearLayout mCancelBtn;
    private ImageButton mUndoBtn;

    private TextView mTitleTextView;

    public interface Callback extends EventListener {
        void positive(TimeBlockModel before,TimeBlockModel after);
        void negative();
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public static TimeTableInputDialogFragment newInstance(Fragment fragment){
        TimeTableInputDialogFragment diagFragment = new TimeTableInputDialogFragment();
        diagFragment.setTargetFragment(fragment,0);
        return diagFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity());
        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE); // タイトル非表示
        mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN); // フルスクリーン
        mDialog.setContentView(R.layout.dialog_input_time_table );
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 背景を透明にする


        initView();

        return mDialog;
    }

    public void setInfo(TimeBlockModel item){
        this.beforeModel = item;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ok_button:
                TimeBlockModel afterModel = createAfterModel();
                callback.positive(beforeModel,afterModel);
                dismiss();
                break;
            case R.id.cancel_button:
                dismiss();
                callback.negative();
                break;
            case R.id.undo_button:
                try{
                    undoItem();
                } catch (IndexOutOfBoundsException e){
//                    Log.d()
                }
                break;
        }
    }

    private void initView(){
        mTitleTextView = (TextView)mDialog.findViewById(R.id.title_text_view);
        mSubjectTextView = (EditText)mDialog.findViewById(R.id.edit_subject);
        mTeacherTextView = (EditText)mDialog.findViewById(R.id.edit_teacher);
        mRoomTextView = (EditText)mDialog.findViewById(R.id.edit_room);
        mSubjectTextView.setText(beforeModel.getSubject());
        mTeacherTextView.setText(beforeModel.getTeacherName());
        mRoomTextView.setText(beforeModel.getClassRoom());
        mTitleTextView.setText(createTitle(beforeModel));
        mUndoBtn = (ImageButton)mDialog.findViewById(R.id.undo_button);

        mOkBtn = (LinearLayout)mDialog.findViewById(R.id.ok_button);
        mCancelBtn = (LinearLayout)mDialog.findViewById(R.id.cancel_button);
        mOkBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mUndoBtn.setOnClickListener(this);
    }

    private TimeBlockModel createAfterModel(){
        String subject = mSubjectTextView.getText().toString();
        String teacher = mTeacherTextView.getText().toString();
        String room = mRoomTextView.getText().toString();

        TimeBlockModel afterModel = new TimeBlockModel();
        afterModel.setSubject(subject);
        afterModel.setTeacherName(teacher);
        afterModel.setClassRoom(room);
        afterModel.setRowNum(beforeModel.getRowNum());
        afterModel.setColNum(beforeModel.getColNum());
        return afterModel;
    }

    /**
     * 変更前に戻す
     */
    private void undoItem() throws IndexOutOfBoundsException{
        List<List<TimeBlockModel>> lists =  PrefUtil.loadOriginalTimeBlockList();
        List<TimeBlockModel> list = lists.get(beforeModel.getColNum() - 1);
        TimeBlockModel original = list.get(beforeModel.getRowNum()-1);
        mSubjectTextView.setText(original.getSubject());
        mTeacherTextView.setText(original.getTeacherName());
        mRoomTextView.setText(original.getClassRoom());
        mSubjectTextView.setSelection(mSubjectTextView.getText().length());
        mTeacherTextView.setSelection(mTeacherTextView.getText().length());
        mRoomTextView.setSelection(mRoomTextView.getText().length());
    }

    private String createTitle(TimeBlockModel model){
        int row = model.getRowNum();
        int col = model.getColNum();

        String week;
        switch (col){
            case 1:
                week = "月曜日";
                break;
            case 2:
                week = "火曜日";
                break;
            case 3:
                week = "水曜日";
                break;
            case 4:
                week = "木曜日";
                break;
            case 5:
                week = "金曜日";
                break;
            default:
                week = "";
        }

        return week + " " + String.valueOf(row) + "限";
    }
}
