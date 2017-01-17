package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.NewsModel;
import jp.yuta.kohashi.esc.model.PrefItemModel;
import jp.yuta.kohashi.esc.model.TimeBlockModel;

/**
 * Created by yutakohashi on 2017/01/17.
 */

public class PrefRecyclerAdapter extends RecyclerView.Adapter<PrefRecyclerAdapter.PrefViewHolder> {

    private List<PrefItemModel> items;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    // タップされたときに呼び出されるメソッド
    protected void onItemClicked(@NonNull PrefItemModel model) {
    }

    public PrefRecyclerAdapter(List<PrefItemModel> items, Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.mContext = context;
    }

    @Override
    public PrefRecyclerAdapter.PrefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch(viewType){
            case 0: //empty
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_empty, parent, false);
                break;
            case 1: // 項目
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_item, parent, false);
                break;
            case 2: //ログアウト
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_item_center_txt, parent, false);
                break;
            case 3: // グループタイトル
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_group_title, parent, false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_empty, parent, false);
        }

        final PrefRecyclerAdapter.PrefViewHolder holder = new PrefRecyclerAdapter.PrefViewHolder(v, viewType);
        if(viewType != 0){
            // onCreateViewHolder でリスナーをセット
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    onItemClicked(items.get(position));
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(PrefViewHolder holder, int position) {
        switch(holder.getItemViewType()){
            case 0:
                break;
            case 1:
                holder.itemNameView.setText(items.get(position).getItemName());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(items.get(position).getResourceId(),null));
                }else{
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(items.get(position).getResourceId()));
                }
                break;
            case 2:
                holder.itemNameView.setText(items.get(position).getItemName());
                break;
            case 3:
                holder.itemNameView.setText(items.get(position).getItemName());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //複数レイアウト
    @Override
    public int getItemViewType(int position) {
        PrefItemModel model = items.get(position);

        return model.getViewType().ordinal();
    }

    //ViewHolder
    public static class PrefViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        // Campos respectivos de un item
        public TextView itemNameView;
        public ImageView imageView;


        public PrefViewHolder(View v , int viewType) {
            super(v);
            container = (RelativeLayout)v.findViewById(R.id.container_view);
            switch (viewType){
                case 0: //empty
                    break;
                case 1: // 項目
                    itemNameView = (TextView)v.findViewById(R.id.title_text_view);
                    imageView = (ImageView)v.findViewById(R.id.image_view);
                    break;
                case 2: // ログアウト
                    itemNameView = (TextView)v.findViewById(R.id.text_view);
                    break;
                case 3: // グループタイトル
                    itemNameView = (TextView)v.findViewById(R.id.group_title);
                    break;
                default:
            }
        }
    }
}
