package jp.yuta.kohashi.esc.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.TimeBlockItem;
import jp.yuta.kohashi.esc.ui.fragment.base.BaseDialogFragment;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * Created by yutakohashi on 2017/01/19.
 */

public class TimeTableInputDialogFragment extends BaseDialogFragment implements View.OnClickListener {
    // "static" is require for object null  when  display rotation
    private static TimeBlockItem beforeModel;

    private EditText mSubjectTextView;
    private EditText mRoomTextView;
    private EditText mTeacherTextView;
    private ImageButton mUndoBtn;

    private TextView mTitleTextView;

    public static TimeTableInputDialogFragment newInstance(Fragment fragment){
        TimeTableInputDialogFragment diagFragment = new TimeTableInputDialogFragment();
        diagFragment.setTargetFragment(fragment,0);
        return diagFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    public void setInfo(TimeBlockItem item){
        this.beforeModel = item;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.undo_button){
            try{
                undoItem();
            } catch (IndexOutOfBoundsException e){
            }
        }
    }

    @Override
    protected int setContentViewId() {
        return R.layout.dialog_input_time_table;
    }

    @Override
    protected void pressOkButton() {
        TimeBlockItem afterModel = createAfterModel();
        callback.positive(beforeModel,afterModel);
    }

    @Override
    protected void pressNegativeButton() {
        callback.negative();
    }

    @Override
    protected void initView(){
        super.initView();
        mTitleTextView = (TextView)mDialog.findViewById(R.id.title_text_view);
        mSubjectTextView = (EditText)mDialog.findViewById(R.id.edit_subject);
        mTeacherTextView = (EditText)mDialog.findViewById(R.id.edit_teacher);
        mRoomTextView = (EditText)mDialog.findViewById(R.id.edit_room);
        mSubjectTextView.setText(beforeModel.getSubject());
        mTeacherTextView.setText(beforeModel.getTeacherName());
        mRoomTextView.setText(beforeModel.getClassRoom());
        mTitleTextView.setText(createTitle(beforeModel));
        mUndoBtn = (ImageButton)mDialog.findViewById(R.id.undo_button);
        mUndoBtn.setOnClickListener(this);
    }

    private TimeBlockItem createAfterModel(){
        String subject = mSubjectTextView.getText().toString();
        String teacher = mTeacherTextView.getText().toString();
        String room = mRoomTextView.getText().toString();

        TimeBlockItem afterModel = new TimeBlockItem();
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
        List<List<TimeBlockItem>> lists =  PrefUtil.loadOriginalTimeBlockList();
        List<TimeBlockItem> list = lists.get(beforeModel.getColNum() - 1);
        TimeBlockItem original = list.get(beforeModel.getRowNum()-1);
        mSubjectTextView.setText(original.getSubject());
        mTeacherTextView.setText(original.getTeacherName());
        mRoomTextView.setText(original.getClassRoom());
        mSubjectTextView.setSelection(mSubjectTextView.getText().length());
        mTeacherTextView.setSelection(mTeacherTextView.getText().length());
        mRoomTextView.setSelection(mRoomTextView.getText().length());
    }

    private String createTitle(TimeBlockItem model){
        int row = model.getRowNum();
        int col = model.getColNum();

        String week;
        switch (col){
            case 1:
                week = getResources().getString(R.string.monday_string);
                break;
            case 2:
                week = getResources().getString(R.string.tuesday_string);
                break;
            case 3:
                week = getResources().getString(R.string.wednesday_string);
                break;
            case 4:
                week = getResources().getString(R.string.thursday_string);
                break;
            case 5:
                week = getResources().getString(R.string.friday_string);
                break;
            default:
                week = "";
        }

        return week + " " + String.valueOf(row) + "限";
    }
}
