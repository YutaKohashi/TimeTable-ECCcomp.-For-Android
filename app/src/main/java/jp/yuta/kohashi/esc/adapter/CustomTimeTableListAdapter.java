package jp.yuta.kohashi.esc.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.dialog.CustomTimeTableDialog;
import jp.yuta.kohashi.esc.fragment.TimeBlockTouchDialogFragment;
import jp.yuta.kohashi.esc.object.TeacherNameObject;
import jp.yuta.kohashi.esc.object.TimeBlock;
import jp.yuta.kohashi.esc.preference.LoadManager;

/**
 * Created by Yuta on 2016/06/20.
 */
public class CustomTimeTableListAdapter extends RecyclerView.Adapter<CustomTimeTableListAdapter.TimeBlockViewHolder> {

    static final String PREF_TEACHERS_KEY = "teachers";
    static final String PREF_NAME ="sample";
    private static final String PREF_NAME_CUSTOM = "";
    private static final String KEY = "";

    String youbi;
    String zikan;

    private static final String CUSTM_TABLE_PREF = "customtimetable";

    private int colNum;
    private List<TimeBlock> items;
    private int lastPosition = -1;
    private Context context;
    LoadManager loadManager;
    List <TeacherNameObject> teacherNameList;

    public CustomTimeTableListAdapter (List<TimeBlock> items, Context context, int colNum) {
        this.items = items;
        this.context = context;
        this.colNum = colNum;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public TimeBlockViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_time_table_block, viewGroup, false);
        return new TimeBlockViewHolder(v);
    }


    //ここでCARDVIEWのアイテムについて設定
    int x;
    int y;
    @Override
    public void onBindViewHolder(final TimeBlockViewHolder viewHolder, final int position) {
        viewHolder.subjectName.setText(items.get(position).getSubject());
        viewHolder.roomName.setText(items.get(position).getClassRoom());

        //タップしたセルの位置を取得

        //セルをタップした時のイベント
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, OrizinalBottomSheetActivity.class);
//                context.startActivity(intent);
                x = colNum;
                y = position;

                //x軸の値を曜日に変換
                switch(x){
                    case 0:
                        youbi = "月曜日";
                        break;
                    case 1:
                        youbi = "火曜日";
                        break;
                    case 2:
                        youbi = "水曜日";
                        break;
                    case 3:
                        youbi = "木曜日";
                        break;
                    case 4:
                        youbi = "土曜日";
                        break;

                }

                //y軸の値を時限に変kなん
                switch(y){
                    case 0:
                        zikan = "1限目";
                        break;
                    case 1:
                        zikan = "2限目";
                        break;
                    case 2:
                        zikan = "3限目";
                        break;
                    case 3:
                        zikan = "4限目";
                        break;

                }

                String subject = items.get(position).getSubject().trim();
                teacherNameList = new ArrayList<>();
                loadManager = new LoadManager();
                teacherNameList = loadManager.loadManagerWithPreferenceForTeacherName(context,PREF_NAME,PREF_TEACHERS_KEY);

                //先生を探索
                String teacherName = searchTeacherName(subject,teacherNameList);
                teacherName = teacherName.replace("     "," ");
                teacherName = teacherName.replace("    "," ");
                teacherName = teacherName.replace("   "," ");
                teacherName = teacherName.replace("  "," ");

                CustomTimeTableDialog dialog = new CustomTimeTableDialog();

                dialog.setSubject(subject);
                dialog.setTeacher(teacherName);
                dialog.setRoom(items.get(position).getClassRoom());

                //タイトルをセット
                dialog.setTitle(youbi,zikan);
                dialog.setX(x);
                dialog.setY(y);

                dialog.setCancelable(false);
                //ダイアログを表示
                dialog.show(((FragmentActivity)context).getSupportFragmentManager(), "custom");

            }
        });
    }

    //ViewHolder
    public static class TimeBlockViewHolder extends RecyclerView.ViewHolder {
        CardView container;

        // Campos respectivos de un item
        public TextView subjectName;
        public TextView roomName;

        public TimeBlockViewHolder(View v) {
            super(v);
            container = (CardView) itemView.findViewById(R.id.card_view_time_block);
            subjectName = (TextView)v.findViewById(R.id.text_subject);
            roomName = (TextView)v.findViewById(R.id.text_classRoom);
        }
    }

    private void openFragmentDialog(TimeBlock item) {
        Toast.makeText(context,item.getSubject(),Toast.LENGTH_SHORT).show();
        DialogFragment dialog = new TimeBlockTouchDialogFragment();
//        dialog.show(( );
    }

    //アニメーション
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition)
//        if(position < 5)
//        {
//            //アニメーション
//            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
//            viewToAnimate.startAnimation(animation);
////            lastPosition = position;
//        }
    }

    public void showMyDialog() {

    }

    /**
     *
     * @param subjectName
     * @param list
     * @return
     */
    private static String searchTeacherName(String subjectName, List<TeacherNameObject> list){
        String teacherName = "";
        for(int i = 0; i < list.size();i++){
            TeacherNameObject teacherNameObject = new TeacherNameObject();
            teacherNameObject=list.get(i);

            String target = teacherNameObject.getSubject().trim();
            if(subjectName.equals(target)){
                teacherName = teacherNameObject.getTeacher();
                break;
            }
        }

        return teacherName;
    }


}
