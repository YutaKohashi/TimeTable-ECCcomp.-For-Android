package jp.yuta.kohashi.esc.ui.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.App;
import jp.yuta.kohashi.esc.Const;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.manager.NotifyManager;
import jp.yuta.kohashi.esc.manager.preference.PrefManager;
import jp.yuta.kohashi.esc.model.PrefItemModel;
import jp.yuta.kohashi.esc.model.enums.PrefViewType;
import jp.yuta.kohashi.esc.ui.activity.LoginCheckActivity;
import jp.yuta.kohashi.esc.ui.adapter.PrefRecyclerAdapter;

/**
 * 設定画面
 * PrefrenceFragmentを継承しない
 */
public class PreferenceFragment extends Fragment {

    RecyclerView mRecyclerView;
    PrefRecyclerAdapter mRecyclerAdapter;
    List<PrefItemModel> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preference, container, false);
        createItems();
        initView(view);
        return view;
    }

    /**
     * 表示する項目を作成
     */
    private void createItems() {
        items = new ArrayList<>();
        items.add(new PrefItemModel("時間割", PrefViewType.ITEM_GROUP_TITLE));
        items.add(new PrefItemModel("時間割を更新", R.drawable.ic_refresh, PrefViewType.ITEM));
        items.add(new PrefItemModel("時間割データを変更", R.drawable.ic_create, PrefViewType.ITEM_RIGHT_ARROW));
        items.add(new PrefItemModel(PrefViewType.EMPTY));
        items.add(new PrefItemModel("出席照会", PrefViewType.ITEM_GROUP_TITLE));
        items.add(new PrefItemModel("出席照会を色分けする", R.drawable.ic_brush, PrefViewType.ITEM_SWITCH));
        items.add(new PrefItemModel("ログを確認する", R.drawable.ic_folder_open, PrefViewType.ITEM_RIGHT_ARROW));
        items.add(new PrefItemModel(PrefViewType.EMPTY));
        items.add(new PrefItemModel("その他", PrefViewType.ITEM_GROUP_TITLE));
        items.add(new PrefItemModel("著作権情報", R.drawable.ic_business, PrefViewType.ITEM_RIGHT_ARROW));
        items.add(new PrefItemModel("このアプリについて", R.drawable.ic_error, PrefViewType.ITEM_RIGHT_ARROW));
        items.add(new PrefItemModel("アプリバージョン", Const.APP_VERSION, R.drawable.ic_android, PrefViewType.ITEM_RIGHT_TXT));
        items.add(new PrefItemModel(PrefViewType.EMPTY));
        items.add(new PrefItemModel("ログアウト", PrefViewType.ITEM_CENTER_TXT));
        items.add(new PrefItemModel(PrefViewType.EMPTY));
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.pref_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerAdapter = new PrefRecyclerAdapter(items, getContext()) {
            @Override
            protected void onItemClicked(@NonNull PrefItemModel model) {
                super.onItemClicked(model);
                switch(model.getItemName()){
                    case "ログアウト":
                        logout();
                        break;

                }
            }
        };
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    private void logout() {
        NotifyManager.showLogoutingDiag(getActivity());
        Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                PrefManager.deleteAll();
                NotifyManager.dismiss();
                Intent intent = new Intent(getContext().getApplicationContext(), LoginCheckActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(getActivity());
            }
        };
        mHandler.postDelayed(runnable, 2000);
    }
}
