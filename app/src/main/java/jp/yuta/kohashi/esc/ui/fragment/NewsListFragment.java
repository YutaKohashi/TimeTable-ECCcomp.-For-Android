package jp.yuta.kohashi.esc.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.NewsModel;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.network.service.HttpHelper;
import jp.yuta.kohashi.esc.ui.activity.NewsDetailActivity;
import jp.yuta.kohashi.esc.ui.adapter.NewsRecyclerAdapter;
import jp.yuta.kohashi.esc.util.NotifyUtil;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends Fragment implements PullRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private List<NewsModel> items;

    private String userId;
    private String password;
    private PullRefreshLayout mPullrefreshLayout;
    private NewsRecyclerAdapter mRecyclerAdapter;
    private int contains;  //0:school,  1:teacher
    private ScrollController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        controller = new ScrollController();
        userId = PrefUtil.getId();
        password = PrefUtil.getPss();

        initView(view);
        return view;
    }

    private void initView(View view) {
        mPullrefreshLayout = (PullRefreshLayout) view.findViewById(R.id.refresh);
        mPullrefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addOnItemTouchListener(controller);

        mRecyclerAdapter = new NewsRecyclerAdapter(items, getContext()) {
            @Override
            protected void onItemClicked(@NonNull final NewsModel model) {
                super.onItemClicked(model);
                if(!Util.netWorkCheck()){
                    NotifyUtil.failureNetworkConnection(); return;}

                NotifyUtil.showLoadingDiag(getActivity());

                new HttpConnector().requestNewsDetail(userId, password, model.getUri(), new HttpHelper.AccessCallbacks() {
                    @Override
                    public void callback(String html, boolean bool) {
                        if (bool) {
                            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                            intent.putExtra(NewsDetailActivity.NEWS_MODEL, model);
                            intent.putExtra(NewsDetailActivity.NEWS_HTML, html);

                            startActivity(intent);
                        }
                        NotifyUtil.dismiss();
                    }
                });
            }

        };

        mRecyclerView.setAdapter(mRecyclerAdapter);
    }


    public void setItems(List<NewsModel> items) {
        this.items = items;
    }

    /**
     * コンテンツ番号を設定するメソッド
     *
     * @param num
     */
    public void setContains(int num) {
        contains = num;
    }

    @Override
    public void onRefresh() {
        if(!Util.netWorkCheck()){
            NotifyUtil.failureNetworkConnection();
            endRefresh();
            return;
        }
        controller.disableScroll();
        switch (contains) {
            case 0: // 学校からのお知らせ
                new HttpConnector().request(HttpConnector.Type.NEWS_SCHOOL, userId, password, new HttpConnector.Callback() {
                    @Override
                    public void callback(boolean bool) {
                        if (bool) {
                            mRecyclerAdapter.swap(PrefUtil.loadSchoolNewsList());
                            NotifyUtil.successUpdate();
                        } else {
                            NotifyUtil.failureUpdate();
                        }
                        endRefresh();
                        controller.enableScroll();
                    }
                });
                break;
            case 1: // 担任からのお知らせ
                new HttpConnector().request(HttpConnector.Type.NEWS_TEACHER, userId, password, new HttpConnector.Callback() {
                    @Override
                    public void callback(boolean bool) {
                        if (bool) {
                            mRecyclerAdapter.swap(PrefUtil.loadTanninNewsList());
                            NotifyUtil.successUpdate();
                        } else {
                            NotifyUtil.failureUpdate();
                        }
                        endRefresh();
                        controller.enableScroll();
                    }
                });
        }
    }

    /**
     * リフレッシュを終了するメソッド
     */
    private void endRefresh(){
        mPullrefreshLayout.setRefreshing(false);
    }

}
