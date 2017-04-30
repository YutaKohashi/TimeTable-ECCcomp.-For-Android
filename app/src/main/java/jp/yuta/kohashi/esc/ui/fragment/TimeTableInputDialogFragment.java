package jp.yuta.kohashi.esc.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.api.model.timeTable.TeacherTimeTable;
import jp.yuta.kohashi.esc.network.api.model.timeTable.TimeTable;
import jp.yuta.kohashi.esc.ui.fragment.base.BaseDialogFragment;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * Created by yutakohashi on 2017/01/19.
 */

public class TimeTableInputDialogFragment extends BaseDialogFragment implements View.OnClickListener {
    // "static" is require for object null  when  display rotation
    private static TimeTable beforeModel;

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

    public void setInfo(TimeTable item){
        beforeModel = item;
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
        TimeTable afterModel = createAfterModel();
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
        mSubjectTextView.setText(beforeModel.getLessonName());
        String teacher = beforeModel.getTeacherNames();
        mTeacherTextView.setText(teacher);
        mRoomTextView.setText(beforeModel.getRoom());
        mTitleTextView.setText(createTitle(beforeModel));
        mUndoBtn = (ImageButton)mDialog.findViewById(R.id.undo_button);
        mUndoBtn.setOnClickListener(this);
    }

    private TimeTable createAfterModel(){
        String subject = mSubjectTextView.getText().toString();
        String teacher = mTeacherTextView.getText().toString();
        String room = mRoomTextView.getText().toString();

        TimeTable afterModel = new TimeTable();
        afterModel.setLessonName(subject);
        List<TeacherTimeTable> teacherTimeTables = new ArrayList<>();
        teacherTimeTables.add(new TeacherTimeTable(teacher));

        afterModel.setTeachers(teacherTimeTables);
        afterModel.setRoom(room);
        afterModel.setTerm(beforeModel.getTerm());
        afterModel.setWeek(beforeModel.getWeek());
        return afterModel;
    }

    /**
     * 変更前に戻す
     */
    private void undoItem() throws IndexOutOfBoundsException{
        List<List<TimeTable>> lists =  PrefUtil.loadOriginalTimeBlockList();
        List<TimeTable> list = lists.get(beforeModel.getWeek() - 1);
        TimeTable original = list.get(beforeModel.getTerm()-1);
        mSubjectTextView.setText(original.getLessonName());
        mTeacherTextView.setText(original.getTeacherNames());
        mRoomTextView.setText(original.getRoom());
        mSubjectTextView.setSelection(mSubjectTextView.getText().length());
        mTeacherTextView.setSelection(mTeacherTextView.getText().length());
        mRoomTextView.setSelection(mRoomTextView.getText().length());
    }

    private String createTitle(TimeTable model){
        int row = model.getTerm();
        int col = model.getWeek();

        String week;
        switch (col){
            case 0:
                week = getResources().getString(R.string.sunday_string);
                break;
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
            case 6:
                week = getResources().getString(R.string.saturday_string);
                break;
            default:
                week = "";
        }

        return week + " " + String.valueOf(row) + "限";
    }
}
