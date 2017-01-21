package jp.yuta.kohashi.esc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.model.PrefItemModel;
import jp.yuta.kohashi.esc.ui.adapter.PrefRecyclerAdapter;

/**
 * Created by yutakohashi on 2017/01/21.
 */

public abstract class PrefRecyclerViewFragment extends RecyclerViewFragment {

    private PrefRecyclerAdapter mRecyclerAdapter;
    private List<PrefItemModel> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        items = new ArrayList<>();
        View v = super.onCreateView(inflater,container,savedInstanceState);
        return v;
    }

    /**
     * 表示する項目を作成
     */
    public abstract void createItems();

    /**
     * recyclerViewを作成
     * @param v
     */
    public abstract void initView(View v);


    protected PrefRecyclerAdapter getAdapter(){
        return this.mRecyclerAdapter;
    }

    protected void createAdapter(PrefRecyclerAdapter adapter){
        mRecyclerAdapter = adapter;
    }

    protected List<PrefItemModel> getItems(){
        return this.items;
    }

    protected void addItem(PrefItemModel item){
        items.add(item);
    }

    protected void clearItem(){
        items.clear();
    }

}
