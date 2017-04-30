package jp.yuta.kohashi.esc.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.network.api.model.news.NewsItem;

/**
 * Created by yutakohashi on 2017/01/15.
 */

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsRecyclerViewHolder> {

    private List<NewsItem> items;
    private Context mContext;

    // タップされたときに呼び出されるメソッド
    protected void onItemClicked(@NonNull NewsItem model) {}

    public NewsRecyclerAdapter(List<NewsItem> items, Context context) {
//        if(this.items == null) this.items = new ArrayList<>();
//        else  this.items.clear();
//
//        this.items.addAll(items);
        this.items = items;
        mContext = context;
    }

    @Override
    public NewsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        // 記事レイアウト
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_news_item, parent, false);

        final NewsRecyclerViewHolder holder = new NewsRecyclerViewHolder(v);
        holder.itemView.setOnClickListener(view -> {
            int position = holder.getAdapterPosition();
            onItemClicked(items.get(position));
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsRecyclerViewHolder holder, int position) {
        try {
            holder.title.setText(items.get(position).getTitle());
            holder.from.setText(items.get(position).getCategory());
            holder.time.setText(items.get(position).getUpdated_date());
//                holder.time.setText(items.get(position).getDate());
//                holder.uri.setText(items.get(position).getUri());
        } catch (NullPointerException ex) {
            holder.title.setText("");
            holder.time.setText("");
            holder.from.setText("");
        }
        // view type に応じて処理を分ける。
//        if (holder.getItemViewType() == -1) {
//            //タイトル
//            try {
////                holder.parentTitle.setText(items.get(position).getGroupTitle());
//            } catch (NullPointerException ex) {
//                holder.parentTitle.setText("");
//            }
//        } else {
//            //記事
//            try {
//                holder.title.setText(items.get(position).getTitle());
////                holder.time.setText(items.get(position).getDate());
////                holder.uri.setText(items.get(position).getUri());
//            } catch (NullPointerException ex) {
//                holder.title.setText("");
//                holder.time.setText("");
//                holder.uri.setText("");
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void swap(List<NewsItem> items){
        this.items = items;
//        this.items.clear();
//        this.items.addAll(items);
        notifyDataSetChanged();
    }

//    //複数レイアウト
//    @Override
//    public int getItemViewType(int position) {
//        NewsItem childListItem = items.get(position);
////        if (childListItem.getGroupTitle() != null) {
////            return -1;
////        }
//        return position;
//    }

    static class NewsRecyclerViewHolder extends RecyclerView.ViewHolder {

        //記事
        TextView title;
        TextView time;
//        TextView uri;
        TextView from;
        CardView cardView;

//        //タイトル
//        TextView parentTitle;

        NewsRecyclerViewHolder(View v) {
            super(v);

            //記事
            title = (TextView) v.findViewById(R.id.news_child_title);
            time = (TextView) v.findViewById(R.id.news_child_time);
//            uri = (TextView) v.findViewById(R.id.url_hidden_text_view);
            from = (TextView)v.findViewById(R.id.from_text);
            cardView = (CardView) itemView.findViewById(R.id.news_child_item);

//            //viewTypeによってレイアウトを分ける
//            if (viewType == -1) {
//                //タイトル
//                parentTitle = (TextView) v.findViewById(R.id.news_parent_title);
//            } else {
//                //記事
//                title = (TextView) v.findViewById(R.id.news_child_title);
//                time = (TextView) v.findViewById(R.id.news_child_time);
//                uri = (TextView) v.findViewById(R.id.url_hidden_text_view);
//                cardView = (CardView) itemView.findViewById(R.id.news_child_item);
//            }
        }
    }
}
