package jp.yuta.kohashi.esc.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.object.SyllabusItem;

/**
 * Created by Yuta on 2016/04/06.
 */
public class SyllabusRecyclerAdapter extends RecyclerView.Adapter<SyllabusRecyclerAdapter.SyllabusViewHolder> {
    private List<SyllabusItem> items;
    private Context context;


    public SyllabusRecyclerAdapter(List<SyllabusItem> items, Context context){
        this.items = items;
        this.context = context;
    }


    @Override
    public SyllabusViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from((viewGroup.getContext()))
                .inflate(R.layout.shyllabus_item,viewGroup,false);
        return new SyllabusViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SyllabusViewHolder holder, int position) {
        holder.dateText.setText(items.get(position).getDateText());
        holder.containtsText.setText(items.get(position).getContaintsText());

        setAnimation(holder.container,position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class SyllabusViewHolder extends RecyclerView.ViewHolder {
        CardView container;

        // Campos respectivos de un item
        public TextView dateText;
        public TextView containtsText;

        public SyllabusViewHolder(View v) {
            super(v);

            container = (CardView) itemView.findViewById(R.id.tab_syllabus_cardView);
            dateText = (TextView)v.findViewById(R.id.date_text);
            containtsText = (TextView)v.findViewById(R.id.contents_text);
        }
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

}
