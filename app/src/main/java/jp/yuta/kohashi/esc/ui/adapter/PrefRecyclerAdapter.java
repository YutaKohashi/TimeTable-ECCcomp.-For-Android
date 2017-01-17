package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.PrefItemModel;
import jp.yuta.kohashi.esc.model.enums.PrefViewType;

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
        PrefViewType type = PrefRecyclerAdapter.getEnum(viewType);
        View v;
        switch (type) {
            case EMPTY:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_empty, parent, false);
                break;
            case ITEM:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_item, parent, false);
                break;
            case ITEM_RIGHT_ARROW:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_item_right_arrow, parent, false);
                break;
            case ITEM_SWITCH:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_item_switch, parent, false);
                break;
            case ITEM_CENTER_TXT:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_item_center_txt, parent, false);
                break;
            case ITEM_GROUP_TITLE:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_group_title, parent, false);
                break;
            case ITEM_RIGHT_TXT:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_item_right_txt, parent, false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_empty, parent, false);
        }

        final PrefRecyclerAdapter.PrefViewHolder holder = new PrefRecyclerAdapter.PrefViewHolder(v, viewType);
        if (viewType != 0) {
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
        PrefViewType type = PrefRecyclerAdapter.getEnum(holder.getItemViewType());
        switch (type) {
            case EMPTY:
                break;
            case ITEM:
                holder.leftTextView.setText(items.get(position).getItemName());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(items.get(position).getResourceId(), null));
                } else {
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(items.get(position).getResourceId()));
                }
                break;
            case ITEM_RIGHT_ARROW:
                holder.leftTextView.setText(items.get(position).getItemName());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(items.get(position).getResourceId(), null));
                } else {
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(items.get(position).getResourceId()));
                }
                break;
            case ITEM_SWITCH:
                holder.leftTextView.setText(items.get(position).getItemName());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(items.get(position).getResourceId(), null));
                } else {
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(items.get(position).getResourceId()));
                }
                break;
            case ITEM_CENTER_TXT:
                holder.leftTextView.setText(items.get(position).getItemName());
                break;
            case ITEM_GROUP_TITLE:
                holder.leftTextView.setText(items.get(position).getItemName());
                break;
            case ITEM_RIGHT_TXT:
                holder.leftTextView.setText(items.get(position).getItemName());
                holder.rightTextView.setText(items.get(position).getRightText());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(items.get(position).getResourceId(), null));
                } else {
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(items.get(position).getResourceId()));
                }
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
        public TextView leftTextView;
        public TextView rightTextView;
        public ImageView imageView;
        public SwitchCompat switchCompat;


        public PrefViewHolder(View v, int viewType) {
            super(v);
            container = (RelativeLayout) v.findViewById(R.id.container_view);
            PrefViewType type = PrefRecyclerAdapter.getEnum(viewType);
            switch (type) {
                 case EMPTY:
                    break;
                case ITEM:
                    leftTextView = (TextView) v.findViewById(R.id.title_text_view);
                    imageView = (ImageView) v.findViewById(R.id.image_view);
                    break;
                case ITEM_RIGHT_ARROW:
                    leftTextView = (TextView) v.findViewById(R.id.title_text_view);
                    imageView = (ImageView) v.findViewById(R.id.image_view);
                    break;
                case ITEM_SWITCH:
                    leftTextView = (TextView) v.findViewById(R.id.title_text_view);
                    imageView = (ImageView) v.findViewById(R.id.image_view);
                    switchCompat = (SwitchCompat) v.findViewById(R.id.switch_view);
                    break;
                case ITEM_CENTER_TXT:
                    leftTextView = (TextView) v.findViewById(R.id.text_view);
                    break;
                case ITEM_GROUP_TITLE:
                    leftTextView = (TextView) v.findViewById(R.id.group_title);
                    break;
                case ITEM_RIGHT_TXT:
                    imageView = (ImageView) v.findViewById(R.id.image_view);
                    leftTextView = (TextView) v.findViewById(R.id.title_text_view);
                    rightTextView = (TextView) v.findViewById(R.id.right_text_view);
                default:
            }
        }
    }

    protected static PrefViewType getEnum(int viewType) {
        PrefViewType[] values = PrefViewType.values();
        PrefViewType type = values[viewType];
        return type;
    }
}
