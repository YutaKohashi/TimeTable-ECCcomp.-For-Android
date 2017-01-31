package jp.yuta.kohashi.esc.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.yuta.kohashi.esc.R;

/**
 * Created by yutakohashi on 2017/01/21.
 */

public abstract class BaseRecyclerViewFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        createItems();
        initView(mView);
        return mView;
    }

    /**
     * 表示する項目を作成
     */
    public abstract void createItems();

    /**
     * recyclerViewを作成
     *
     * @param v
     */
    public abstract void initView(View v);


}
