package jp.yuta.kohashi.esc.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.PrefItem;
import jp.yuta.kohashi.esc.model.enums.PrefViewType;
import jp.yuta.kohashi.esc.ui.adapter.PrefRecyclerAdapter;
import jp.yuta.kohashi.esc.ui.fragment.base.BasePrefBaseRecyclerViewFragment;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeTableEnableDisableFragment extends BasePrefBaseRecyclerViewFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 表示する項目を作成
     */
    @Override
    public void createItems() {
        addItem(new PrefItem(getResources().getString(R.string.pref_enable_time_table), PrefViewType.ITEM_GROUP_TITLE));
        addItem(new PrefItem(getResources().getString(R.string.pref_enable_zero_gen), R.drawable.ic_view_headline, PrefViewType.ITEM_SWITCH, PrefUtil.isEnableZeroGen()));
        addItem(new PrefItem(getResources().getString(R.string.pref_enable_go_gen), R.drawable.ic_view_headline, PrefViewType.ITEM_SWITCH, PrefUtil.isEnableGoGen()));
        addItem(new PrefItem(getResources().getString(R.string.pref_enable_sun), R.drawable.ic_view_week, PrefViewType.ITEM_SWITCH, PrefUtil.isEnableSunCol()));
        addItem(new PrefItem(getResources().getString(R.string.pref_enable_sat), R.drawable.ic_view_week, PrefViewType.ITEM_SWITCH, PrefUtil.isEnableSatCol()));
        addItem(new PrefItem(PrefViewType.EMPTY));
    }

    @Override
    public void initView(View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        createAdapter(new PrefRecyclerAdapter(getItems(), getContext()) {

            @Override
            protected void onItemCheckedChange(@NonNull boolean bool, @NonNull PrefItem model) {
                super.onItemCheckedChange(bool, model);
                if (model.getItemName().equals(getResources().getString(R.string.pref_enable_zero_gen))) {
                    PrefUtil.saveEnableZeroGen(bool);
                } else if(model.getItemName().equals(getResources().getString(R.string.pref_enable_go_gen))){
                    PrefUtil.saveEnableGoGen(bool);
                } else if(model.getItemName().equals(getResources().getString(R.string.pref_enable_sun))){
                    PrefUtil.saveEnableSunCol(bool);
                } else if(model.getItemName().equals(getResources().getString(R.string.pref_enable_sat))){
                    PrefUtil.saveEnableSatCol(bool);
                }
            }
        });
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    protected void swap() {
    }

    @Override
    protected void getSavedItems() {
    }

}
