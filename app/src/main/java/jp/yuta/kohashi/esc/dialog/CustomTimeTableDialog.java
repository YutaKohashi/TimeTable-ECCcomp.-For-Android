package jp.yuta.kohashi.esc.dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.fragment.CustomFragmentFragment;
import jp.yuta.kohashi.esc.object.CustomTimeTableCell;
import jp.yuta.kohashi.esc.object.TeacherNameObject;
import jp.yuta.kohashi.esc.preference.LoadManager;
import jp.yuta.kohashi.esc.preference.SaveManager;

/**
 * Created by Yuta on 2016/06/21.
 */
public class CustomTimeTableDialog extends DialogFragment {
    static final String PREF_TEACHERS_KEY = "teachers";
    static final String PREF_NAME ="sample";
    static final String CUSTOM_CELL_PREF_NAME ="customCell";
//    private static final String PREF_NAME_CUSTOM = "";
    private static final String KEY = "CUSTOM_TIME_TABLE";

    private EditText subjectText;
    private EditText teacherText;
    private EditText roomText;

    private String subject;
    private String teacher;
    private String room;

    private String youbi;
    private String jikan;

    private int x;
    private int y;

    Dialog dialog;

    List teacherNameList;
    LoadManager loadManager;
    CustomFragmentFragment fragment;
    FragmentManager manager;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreateDialog(savedInstanceState);

        dialog = new Dialog(getActivity());

        // タイトル非表示
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // フルスクリーン
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_custom_time_table);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));

        //タイトルをセット
        TextView title = (TextView)dialog.getWindow().findViewById(R.id.custom_cell_title);
        title.setText(youbi + " " + jikan);

        teacherNameList = new ArrayList<>();
        loadManager = new LoadManager();
        teacherNameList = loadManager.loadManagerWithPreferenceForTeacherName(getActivity(),PREF_NAME,PREF_TEACHERS_KEY);


        subjectText = (EditText)dialog.getWindow().findViewById(R.id.edit_subject);
        subjectText.setInputType(InputType.TYPE_CLASS_TEXT);
        subjectText.setText(this.subject.trim());
        subjectText.setSelection(subjectText.getText().length());

        teacherText = (EditText)dialog.getWindow().findViewById(R.id.edit_teacher);
        teacherText.setInputType(InputType.TYPE_CLASS_TEXT);
        teacherText.setText(this.teacher.trim());

        roomText = (EditText)dialog.getWindow().findViewById(R.id.edit_room);
        roomText.setInputType(InputType.TYPE_CLASS_TEXT);
        roomText.setText(this.room.trim());



        //保存
        dialog.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //保存
                CustomTimeTableCell cell = new CustomTimeTableCell();
                //TODO ダイアログから値を取得
                String subject = subjectText.getText().toString().trim();
                String teacher = teacherText.getText().toString().trim();
                String room = roomText.getText().toString().trim();


                //判定（全て入力されているか）
//                if(subject.equals("")  || teacher.equals("") || room.equals("") ){
//                    if(subject == ""){
//                        subjectText.setError("値を入力してください");
//                    }
//                    if(teacher == ""){
//                        teacherText.setError("値を入力してください");
//                    }
//                    if(room == ""){
//                        roomText.setError("値を入力してください");
//                    }
//
//                    Toast.makeText(getActivity(),"入力されていない部分があります",Toast.LENGTH_SHORT).show();
//
//                    //ボタン押下完了処理を回避
//                    //値をデータベースに格納しない
//                    return;
//                }

                cell.setSubject(subject);
                cell.setRoom(room);
                cell.setTeacherName(teacher);
                //座標も格納
                cell.setX(getX());
                cell.setY(getY());

                //読み出し
                LoadManager loadManager = new LoadManager();
                List<CustomTimeTableCell> list = new ArrayList();
                list = loadManager.loadManagerWithPreferenceForCustomTimeTable(getActivity(),CUSTOM_CELL_PREF_NAME,KEY);

                try{
                    list.size();
                }catch(Exception ex){
                    list = new ArrayList<CustomTimeTableCell>();
                }

                //すでに登録されているかを確認
                for(CustomTimeTableCell preCell:list){
                    int x = preCell.getX();
                    int y = preCell.getY();

                    if(x == getX() && y == getY()){
                        list.remove(preCell);
                        break;
                    }
                }

                //リストに追加
                list.add(cell);

                //保存
                SaveManager saveManager = new SaveManager(getContext());
                saveManager.saveMangerWithPreference(getActivity(),CUSTOM_CELL_PREF_NAME,list,KEY);

                List<TeacherNameObject> teacherNames = new ArrayList();
                teacherNames = loadManager.loadManagerWithPreferenceForTeacherName(getActivity(),PREF_NAME,PREF_TEACHERS_KEY);

                TeacherNameObject teacherNameObject = new TeacherNameObject();
                teacherNameObject.setSubject(subject);
                teacherNameObject.setTeacher(teacher);
                teacherNames.add(teacherNameObject);

                //先生と教科の対比を保存する
                saveManager.saveMangerWithPreference(getActivity(),PREF_NAME,teacherNames,PREF_TEACHERS_KEY);

                fragment = new CustomFragmentFragment();
//               manager = getActivity().getSupportFragmentManager();
//                manager.beginTransaction()
//                        .replace(R.id.custom_table_fragment_container, fragment, "dd")
//                        .addToBackStack(null)
//                        .commit();

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                // addを呼んでいるので、重なる
                transaction.add(R.id.custom_table_fragment_container,fragment);
                transaction.addToBackStack(null);
                transaction.commit();

                dialog.dismiss();
            }
        });

        //破棄
        dialog.findViewById(R.id.remove_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }
    public void setTeacher(String teacher){
        this.teacher = teacher;
    }
    public void setRoom(String room){
        this.room = room;
    }

    public void setTitle(String youbi,String jikan){
        this.youbi = youbi;
        this.jikan = jikan;

    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }


    /**
     *
     * @param subjectName
     * @param list
     * @return
     */
    private static String searchTeacherName(String subjectName, List<TeacherNameObject> list){
        String teacherName = "null";
        for(int i = 0; i < list.size();i++){
            TeacherNameObject teacherNameObject = new TeacherNameObject();
            teacherNameObject=list.get(i);

            String target = teacherNameObject.getSubject();
            if(subjectName.equals(target)){
                teacherName = teacherNameObject.getTeacher();
                break;
            }
        }

        return teacherName;
    }
}



