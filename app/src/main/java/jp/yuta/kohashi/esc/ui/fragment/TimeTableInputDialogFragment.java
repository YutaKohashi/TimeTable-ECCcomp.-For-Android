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
import android.widget.Button;
import android.widget.TextView;

import java.util.EventListener;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.TimeBlockModel;

/**
 * Created by yutakohashi on 2017/01/19.
 */

public class TimeTableInputDialogFragment extends DialogFragment implements View.OnClickListener {
    private Callback callback = null;

    private Dialog mDialog;
    private TimeBlockModel beforeModel;

    private TextView mSubjectTextView;
    private TextView mRoomTextView;
    private TextView mTeacherTextView;
    private Button mOkBtn;
    private Button mCancelBtn;

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
//                save();
                TimeBlockModel afterModel = createAfterModel();
                callback.positive(beforeModel,afterModel);
                dismiss();
                break;
            case R.id.cancel_button:
                dismiss();
                callback.negative();
                break;
        }
    }

    private void initView(){
        mSubjectTextView = (TextView)mDialog.findViewById(R.id.edit_subject);
        mTeacherTextView = (TextView)mDialog.findViewById(R.id.edit_teacher);
        mRoomTextView = (TextView)mDialog.findViewById(R.id.edit_room);
        mSubjectTextView.setText(beforeModel.getSubject());
        mTeacherTextView.setText(beforeModel.getTeacherName());
        mRoomTextView.setText(beforeModel.getClassRoom());

        mOkBtn = (Button)mDialog.findViewById(R.id.ok_button);
        mCancelBtn = (Button)mDialog.findViewById(R.id.cancel_button);
        mOkBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
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

//
//    private void save(){
//        // 保存処理
//        String subject = mSubjectTextView.getText().toString();
//        String teacher = mTeacherTextView.getText().toString();
//        String room = mRoomTextView.getText().toString();
//
//        // データが変更されている場合のみ
//        if(!beforeModel.getSubject().equals(subject) ||
//                !beforeModel.getTeacherName().equals(teacher) ||
//                !beforeModel.getClassRoom().equals(room)){
//
//            List<TimeBlockModel> saveModels = createSaveList(subject,teacher, room);
//
//            // 曜日別
//            switch(beforeModel.getColNum()){
//                case 1:
//                    PrefUtil.saveTimeTableMon(saveModels);
//                    break;
//                case 2:
//                    PrefUtil.saveTimeTableTue(saveModels);
//                    break;
//                case 3:
//                    PrefUtil.saveTimeTableWed(saveModels);
//                    break;
//                case 4:
//                    PrefUtil.saveTimeTableThur(saveModels);
//                    break;
//                case 5:
//                    PrefUtil.saveTimeTableFri(saveModels);
//                    break;
//            }
//            NotifyUtil.saveData();
//        } else {
//            NotifyUtil.notChangeData();
//        }
//    }
//
//    /**
//     * 保存するリストを作成
//     * @param subject
//     * @param teacher
//     * @param room
//     * @return
//     */
//    private List<TimeBlockModel> createSaveList(String subject, String teacher, String room){
//        List<TimeBlockModel> saveModels = new ArrayList<>();
//        for(TimeBlockModel m:items){
//            if(beforeModel.getRowNum() == m.getRowNum()){
//                TimeBlockModel saveModel = new TimeBlockModel();
//                saveModel.setSubject(subject);
//                saveModel.setTeacherName(teacher);
//                saveModel.setClassRoom(room);
//                saveModel.setRowNum(m.getRowNum());
//                saveModel.setColNum(m.getColNum());
//                saveModels.add(saveModel);
//            } else{
//                saveModels.add(m);
//            }
//        }
//        return saveModels;
//    }
//
}
