package jp.yuta.kohashi.esc.ui.fragment;


import android.content.Intent;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.Const;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.PrefItemModel;
import jp.yuta.kohashi.esc.model.enums.PrefViewType;
import jp.yuta.kohashi.esc.network.HttpConnector;
import jp.yuta.kohashi.esc.ui.activity.AboutActivity;
import jp.yuta.kohashi.esc.ui.activity.LicenceActivity;
import jp.yuta.kohashi.esc.ui.activity.LoginCheckActivity;
import jp.yuta.kohashi.esc.ui.activity.TimeTableChangeActivity;
import jp.yuta.kohashi.esc.ui.adapter.PrefRecyclerAdapter;
import jp.yuta.kohashi.esc.util.NotifyUtil;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

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
        items.add(new PrefItemModel(getResources().getString(R.string.pref_group_title_time_table), PrefViewType.ITEM_GROUP_TITLE));
        items.add(new PrefItemModel(getResources().getString(R.string.pref_update_time_table), R.drawable.ic_refresh, PrefViewType.ITEM));
        items.add(new PrefItemModel(getResources().getString(R.string.pref_change_time_table), R.drawable.ic_create, PrefViewType.ITEM_RIGHT_ARROW));
        items.add(new PrefItemModel(PrefViewType.EMPTY));
        items.add(new PrefItemModel(getResources().getString(R.string.pref_group_title_time_attendance), PrefViewType.ITEM_GROUP_TITLE));
        items.add(new PrefItemModel(getResources().getString(R.string.pref_attendance_color), R.drawable.ic_brush, PrefViewType.ITEM_SWITCH));
        items.add(new PrefItemModel(getResources().getString(R.string.pref_attendance_log), R.drawable.ic_folder_open, PrefViewType.ITEM_RIGHT_ARROW));
        items.add(new PrefItemModel(PrefViewType.EMPTY));
        items.add(new PrefItemModel(getResources().getString(R.string.pref_group_title_time_other), PrefViewType.ITEM_GROUP_TITLE));
        items.add(new PrefItemModel(getResources().getString(R.string.pref_lisence), R.drawable.ic_business, PrefViewType.ITEM_RIGHT_ARROW));
        items.add(new PrefItemModel(getResources().getString(R.string.pref_about), R.drawable.ic_error, PrefViewType.ITEM_RIGHT_ARROW));
        items.add(new PrefItemModel(getResources().getString(R.string.pref_app_version), Const.APP_VERSION, R.drawable.ic_android, PrefViewType.ITEM_RIGHT_TXT));
        items.add(new PrefItemModel(PrefViewType.EMPTY));
        items.add(new PrefItemModel(getResources().getString(R.string.pref_logout), PrefViewType.ITEM_CENTER_TXT));
        items.add(new PrefItemModel(PrefViewType.EMPTY));
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.pref_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerAdapter = new PrefRecyclerAdapter(items, getContext()) {
            @Override
            protected void onItemClicked(@NonNull PrefItemModel model) {
                super.onItemClicked(model);
                String name = model.getItemName();
                if (name.equals(getResources().getString(R.string.pref_update_time_table))) {
                    updateTimeTable();
                } else if (name.equals(getResources().getString(R.string.pref_change_time_table))) {
                    changeTimeTable();
                } else if (name.equals(getResources().getString(R.string.pref_lisence))) {
                    showLicence();
                } else if (name.equals(getResources().getString(R.string.pref_about))) {
                    showAbout();
                } else if (name.equals(getResources().getString(R.string.pref_logout))) {
                    logout();
                }
            }
        };
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    /**
     * 時間割を更新
     */
    private void updateTimeTable() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .content(R.string.dialog_time_table_update_comment)
                .positiveText(R.string.dialog_positive_ok)
                .negativeText(R.string.dialog_negative_cancel)
                .title(R.string.dialog_title_update_check)
                .positiveColor(getResources().getColor(R.color.diag_text_color_cancel))
                .negativeColor(getResources().getColor(R.color.colorPrimary))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        if (!Util.netWorkCheck()) {
                            NotifyUtil.failureNetworkConnection();
                        } else {
                            NotifyUtil.showUpdatingDiag(getActivity());
                            String userId = PrefUtil.getId();
                            String password = PrefUtil.getPss();
                            new HttpConnector().request(HttpConnector.Type.TIME_TABLE, userId, password, new HttpConnector.Callback() {
                                @Override
                                public void callback(boolean bool) {
                                    NotifyUtil.dismiss();
                                    if (bool) {
                                        NotifyUtil.successUpdate();
//                              int appWidgetId = getActivity().getIntent().getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
//                              TimeTableWidget.updateAppWidget(getContext(), AppWidgetManager.getInstance(getContext()),appWidgetId);
                                    } else {
                                        NotifyUtil.failureUpdate();
                                    }
                                }
                            });

                        }

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();


    }

    /**
     * 時間割を変更する
     */
    private void changeTimeTable() {
        startActivity(new Intent(getActivity(), TimeTableChangeActivity.class));
    }

    /**
     * 出席照会を色分け
     */
    private void changeColorAttendance() {

    }

    /**
     * 著作権情報
     */
    private void showLicence() {
        startActivity(new Intent(getActivity(), LicenceActivity.class));
    }

    /**
     * このアプリについて
     */
    private void showAbout() {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }

    /**
     * ログアウト
     */
    private void logout() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .content(R.string.dialog_comment_comment)
                .positiveText(R.string.dialog_positive_ok)
                .negativeText(R.string.dialog_negative_cancel)
                .positiveColor(getResources().getColor(R.color.diag_text_color_cancel))
                .negativeColor(getResources().getColor(R.color.colorPrimary))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        NotifyUtil.showLogoutingDiag(getActivity());
                        Handler mHandler = new Handler();
                        Runnable runnable = new Runnable() {
                            public void run() {
                                PrefUtil.deleteAll();
                                NotifyUtil.dismiss();
                                Intent intent = new Intent(getContext().getApplicationContext(), LoginCheckActivity.class);
                                startActivity(intent);
                                ActivityCompat.finishAffinity(getActivity());
                            }
                        };
                        mHandler.postDelayed(runnable, 2000);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();

    }
}
