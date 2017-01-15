package jp.yuta.kohashi.esc.ui.fragment.news;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.NewsModel;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.network.service.HttpHelper;
import jp.yuta.kohashi.esc.ui.activity.NewsDetailActivity;
import jp.yuta.kohashi.esc.ui.adapter.NewsRecyclerAdapter;
import jp.yuta.kohashi.esc.util.preference.PrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<NewsModel> items;

    private String userId;
    private String password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        userId = PrefManager.getId();
        password  = PrefManager.getPss();
        userId = "2140257";
        password = "455478";

        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        NewsRecyclerAdapter adapter = new NewsRecyclerAdapter(items, getContext()) {
            @Override
            protected void onItemClicked(@NonNull final NewsModel model) {
                super.onItemClicked(model);

                new HttpConnector().requestNewsDetail(userId, password, model.getUri(), new HttpHelper.AccessCallbacks() {
                    @Override
                    public void callback(String html, boolean bool) {
                        if(bool){
                            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                            intent.putExtra(NewsDetailActivity.NEWS_MODEL, model);
                            intent.putExtra(NewsDetailActivity.NEWS_HTML,html);

                            startActivity(intent);
                        }
                    }
                });
            }
        };

        mRecyclerView.setAdapter(adapter);
        return view;
    }

    public void setItems(List<NewsModel> items) {
        this.items = items;
    }
}
