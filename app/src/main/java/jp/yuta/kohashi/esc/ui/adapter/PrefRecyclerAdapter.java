package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.PrefItem;
import jp.yuta.kohashi.esc.model.enums.PrefViewType;
import jp.yuta.kohashi.esc.util.Util;

/**
 * Created by yutakohashi on 2017/01/17.
 */

public class PrefRecyclerAdapter extends RecyclerView.Adapter<PrefRecyclerAdapter.PrefViewHolder> {
    private static final String TAG = PrefRecyclerAdapter.class.getCanonicalName();

    private List<PrefItem> items;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    // タップされたときに呼び出されるメソッド
    protected void onItemClicked(Integer tag, CircleImageView view, @NonNull PrefItem model) {
    }

    protected void onItemClicked(View view) { //expand
    }

    protected void onItemClicked(@NonNull PrefItem model) {
    }

    protected void onItemCheckedChange(@NonNull boolean bool, @NonNull PrefItem model) {
    }

    protected void onItemStopTrackingTouch(Integer tag, SeekBar seekBar, @NonNull PrefItem mode) {
    }

    protected void onItemProgressChanged(int progress, TextView targetTextView, @NonNull PrefItem model) {
    }

    public PrefRecyclerAdapter(List<PrefItem> items, Context context) {
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
            case ITEM_COLOR_PICK:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_item_color_picker, parent, false);
                break;
            case ITEM_COLOR_PICK_EXPAND:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_color_picker_expand, parent, false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_pref_empty, parent, false);
        }

        final PrefRecyclerAdapter.PrefViewHolder holder = new PrefRecyclerAdapter.PrefViewHolder(v, viewType);

        if (viewType == PrefViewType.ITEM.getId() ||
                viewType == PrefViewType.ITEM_RIGHT_ARROW.getId() ||
                viewType == PrefViewType.ITEM_CENTER_TXT.getId()) {
            // onCreateViewHolder でリスナーをセット
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    onItemClicked(items.get(position));
                }
            });

        } else if (viewType == PrefViewType.ITEM_COLOR_PICK.getId()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    onItemClicked((Integer) holder.itemView.getTag(), (CircleImageView) v.findViewById(R.id.circle_image_view), items.get(position));
                }
            });
        } else if (viewType == PrefViewType.ITEM_COLOR_PICK_EXPAND.getId()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClicked(v.findViewById(R.id.expand_view));
                }
            });
            holder.rightTextViewExpanded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    onItemClicked((Integer) holder.itemView.getTag(), holder.circleView, items.get(position));
                }
            });
            holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int position = holder.getAdapterPosition();
                    onItemProgressChanged(progress, holder.leftTextView, items.get(position));
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int position = holder.getAdapterPosition();
                    onItemStopTrackingTouch((Integer) holder.itemView.getTag(), seekBar, items.get(position));
                }
            });
        } else if (viewType == PrefViewType.ITEM_SWITCH.getId()) {
            holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int position = holder.getAdapterPosition();
                    onItemCheckedChange(b, items.get(position));
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
                holder.imageView.setImageDrawable(Util.getDrawable(items.get(position).getResourceId()));
                break;
            case ITEM_RIGHT_ARROW:
                holder.leftTextView.setText(items.get(position).getItemName());
                holder.imageView.setImageDrawable(Util.getDrawable(items.get(position).getResourceId()));
                break;
            case ITEM_SWITCH:
                holder.leftTextView.setText(items.get(position).getItemName());
                holder.imageView.setImageDrawable(Util.getDrawable(items.get(position).getResourceId()));
                holder.switchCompat.setChecked(items.get(position).isBool());
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
                holder.imageView.setImageDrawable(Util.getDrawable(items.get(position).getResourceId()));
                break;
            case ITEM_COLOR_PICK:
                holder.leftTextView.setText(items.get(position).getItemName());
                holder.imageView.setImageDrawable(Util.getDrawable(items.get(position).getResourceId()));
                holder.circleView.setColorFilter(items.get(position).getColor());
                break;
            case ITEM_COLOR_PICK_EXPAND:
                holder.leftTextView.setText(items.get(position).getItemName());
                holder.imageView.setImageDrawable(Util.getDrawable(items.get(position).getResourceId()));
                holder.circleView.setColorFilter(items.get(position).getColor());
                holder.seekBar.setProgress(items.get(position).getStart());
                holder.itemView.setTag(items.get(position).getTag());
//                holder.seekBar.setProgress(items.get(position).);

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //複数レイアウト
    @Override
    public int getItemViewType(int position) {
        PrefItem model = items.get(position);

        return model.getViewType().ordinal();
    }

    //ViewHolder
    public static class PrefViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        public TextView leftTextView;
        public TextView rightTextView;
        public ImageView imageView;
        public SwitchCompat switchCompat;
        public CircleImageView circleView;
        public AppCompatSeekBar seekBar;
        public TextView rightTextViewExpanded;
        public View include;

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
                    break;
                case ITEM_COLOR_PICK:
                    leftTextView = (TextView) v.findViewById(R.id.title_text_view);
                    circleView = (CircleImageView) v.findViewById(R.id.circle_image_view);
                    imageView = (ImageView) v.findViewById(R.id.image_view);
                    break;
                case ITEM_COLOR_PICK_EXPAND:
                    include = v.findViewById(R.id.include);
                    leftTextView = (TextView) v.findViewById(R.id.title_text_view);
                    circleView = (CircleImageView) v.findViewById(R.id.circle_image_view);
                    imageView = (ImageView) v.findViewById(R.id.image_view);
                    seekBar = (AppCompatSeekBar) v.findViewById(R.id.seekbar);
                    rightTextViewExpanded = (TextView) v.findViewById(R.id.expanded_right_text_view);
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
