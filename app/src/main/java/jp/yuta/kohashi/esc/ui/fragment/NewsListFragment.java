package jp.yuta.kohashi.esc.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.model.NewsItem;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.network.HttpHelper;
import jp.yuta.kohashi.esc.ui.activity.NewsDetailActivity;
import jp.yuta.kohashi.esc.ui.adapter.NewsRecyclerAdapter;
import jp.yuta.kohashi.esc.ui.fragment.base.BaseRefreshRecyclerViewFragment;
import jp.yuta.kohashi.esc.util.NotifyUtil;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends BaseRefreshRecyclerViewFragment {

    private List<NewsItem> items;

    private String userId;
    private String password;
    private NewsRecyclerAdapter mRecyclerAdapter;
    private int contains;  //0:school,  1:Teacher

    public static NewsListFragment newInstance(int contains){
        NewsListFragment fragment = new NewsListFragment();
        fragment.contains = contains;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);
        userId = PrefUtil.getId();
        password = PrefUtil.getPss();

        return view;
    }

    @Override
    public void createItems() {
        super.createItems();
    }

    @Override
    public void initView(View v) {
        super.initView(v);
        if(items == null){
            switch(contains){
                case 0:
                    items = PrefUtil.loadSchoolNewsList();
                    break;
                case 1:
                    items = PrefUtil.loadTanninNewsList();
                    break;
            }
        }
        mRecyclerAdapter = new NewsRecyclerAdapter(items, getContext()) {
            @Override
            protected void onItemClicked(@NonNull final NewsItem model) {
                super.onItemClicked(model);
                if (!Util.netWorkCheck()) {
                    NotifyUtil.failureNetworkConnection();
                    return;
                }

                NotifyUtil.showLoadingDiag(getActivity());

                HttpConnector.requestNewsDetail(userId, password, model.getUri(), new HttpHelper.AccessCallbacks() {
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

    @Override
    protected void swap() {

    }

    @Override
    protected void getSavedItems() {
        if(items == null) items = new ArrayList<>();
        else items.clear();
        if(contains == 0) items.addAll(PrefUtil.loadSchoolNewsList());
        else items.addAll(PrefUtil.loadTanninNewsList());
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!Util.netWorkCheck()) {
            NotifyUtil.failureNetworkConnection();
            endRefresh();
            return;
        }
       disableScroll();
        switch (contains) {
            case 0: // 学校からのお知らせ
                HttpConnector.request(HttpConnector.Type.NEWS_SCHOOL, userId, password, new HttpConnector.Callback() {
                    @Override
                    public void callback(boolean bool) {
                        if (bool) {
                            getSavedItems();
                            mRecyclerAdapter.swap(items);
                            NotifyUtil.successUpdate();
                        } else {
                            NotifyUtil.failureUpdate();
                        }
                        endRefresh();
                        enableScroll();
                    }
                });
                break;
            case 1: // 担任からのお知らせ
                HttpConnector.request(HttpConnector.Type.NEWS_TEACHER, userId, password, new HttpConnector.Callback() {
                    @Override
                    public void callback(boolean bool) {
                        if (bool) {
                            getSavedItems();
                            mRecyclerAdapter.swap(items);
                            NotifyUtil.successUpdate();
                        } else {
                            NotifyUtil.failureUpdate();
                        }
                        endRefresh();
                        enableScroll();
                    }
                });
        }
    }
}
