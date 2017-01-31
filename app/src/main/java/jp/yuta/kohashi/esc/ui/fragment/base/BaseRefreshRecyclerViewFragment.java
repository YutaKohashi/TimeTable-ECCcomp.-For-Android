package jp.yuta.kohashi.esc.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.ui.fragment.ScrollController;

/**
 * Created by yutakohashi on 2017/01/31.
 */

//public class BaseRefreshRecyclerViewFragment extends BaseRecyclerViewFragment implements PullRefreshLayout.OnRefreshListener{
    public class BaseRefreshRecyclerViewFragment extends BaseRecyclerViewFragment implements SwipeRefreshLayout.OnRefreshListener{

    protected SwipeRefreshLayout mPullRefreshLayout;
    protected ScrollController mScrollController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_recycler_view_with_pull_to_refresh, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
//        mPullRefreshLayout = (PullRefreshLayout)mView.findViewById(R.id.refresh);
        mPullRefreshLayout = (SwipeRefreshLayout)mView.findViewById(R.id.refresh);
        mPullRefreshLayout.setOnRefreshListener(this);
        mScrollController = new ScrollController();
        createItems();
        initView(mView);
        return mView;
    }

    /**
     *
     */
    @Override
    public void createItems() {
    }

    @Override
    public void initView(View v) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addOnItemTouchListener(mScrollController);
    }

    @Override
    public void onRefresh() {
    }

    /**
     * リフレッシュを終了するメソッド
     */
    protected void endRefresh() {
        mPullRefreshLayout.setRefreshing(false);
    }


    protected void disableScroll(){
        mScrollController.disableScroll();
    }

    protected void enableScroll(){
        mScrollController.enableScroll();
    }
}
