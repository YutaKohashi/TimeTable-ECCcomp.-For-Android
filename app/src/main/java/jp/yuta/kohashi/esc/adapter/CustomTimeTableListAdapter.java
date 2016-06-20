package jp.yuta.kohashi.esc.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import jp.yuta.kohashi.esc.R;
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

    private List<TimeBlock> items;
    private int lastPosition = -1;
    private Context context;
    LoadManager loadManager;
    List <TeacherNameObject> teacherNameList;

    public CustomTimeTableListAdapter (List<TimeBlock> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public TimeBlockViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.time_table_block, viewGroup, false);
        return new TimeBlockViewHolder(v);
    }


    //ここでCARDVIEWのアイテムについて設定
    @Override
    public void onBindViewHolder(TimeBlockViewHolder viewHolder, final int position) {
        viewHolder.subjectName.setText(items.get(position).getSubject());
        viewHolder.roomName.setText(items.get(position).getClassRoom());

        //色の指定
//        TODO --- 出席率に応じて各セルの背景を変更

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context,"テスト",Toast.LENGTH_SHORT).show();
                return false;
            }
            //ロングタップしたセル情報を取得

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
