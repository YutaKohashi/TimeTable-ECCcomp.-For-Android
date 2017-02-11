package jp.yuta.kohashi.esc.ui.fragment;


import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.thebluealliance.spectrum.SpectrumDialog;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.PrefItem;
import jp.yuta.kohashi.esc.model.enums.PrefViewType;
import jp.yuta.kohashi.esc.ui.adapter.PrefRecyclerAdapter;
import jp.yuta.kohashi.esc.ui.fragment.base.BasePrefBaseRecyclerViewFragment;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceRateChangeColorFragment extends BasePrefBaseRecyclerViewFragment {
    private static final String TAG = AttendanceRateChangeColorFragment.class.getSimpleName();
    private static final Integer U90 = 0;
    private static final Integer U81 = 1;
    private static final Integer U75 = 2;

    private static int u90_rate;
    private static int u81_rate;
    private static int u75_rate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void createItems() {
        u90_rate = PrefUtil.loadRateU90();
        u81_rate = PrefUtil.loadRateU81();
        u75_rate = PrefUtil.loadRateU75();
        addItem(new PrefItem(PrefViewType.EMPTY));
        addItem(new PrefItem(getResources().getString(R.string.pref_enable_change_color), R.drawable.ic_brush, PrefViewType.ITEM_SWITCH, PrefUtil.isChangeColor()));
        addItem(new PrefItem(getResources().getString(R.string.pref_rate_u, PrefUtil.loadRateU90()), R.drawable.ic_color_lens, PrefViewType.ITEM_COLOR_PICK_EXPAND, PrefUtil.loadColorU90(), U90, PrefUtil.loadRateU90(), 0));
        addItem(new PrefItem(getResources().getString(R.string.pref_rate_u, PrefUtil.loadRateU81()), R.drawable.ic_color_lens, PrefViewType.ITEM_COLOR_PICK_EXPAND, PrefUtil.loadColorU81(), U81, PrefUtil.loadRateU81(), 0));
        addItem(new PrefItem(getResources().getString(R.string.pref_rate_u, PrefUtil.loadRateU75()), R.drawable.ic_color_lens, PrefViewType.ITEM_COLOR_PICK_EXPAND, PrefUtil.loadColorU75(), U75, PrefUtil.loadRateU75(), 0));
        addItem(new PrefItem(getResources().getString(R.string.pref_group_title_time_other), PrefViewType.ITEM_GROUP_TITLE));
        addItem(new PrefItem(getResources().getString(R.string.pref_blackout), R.drawable.ic_visibility_off, PrefViewType.ITEM_SWITCH, PrefUtil.isBlackout()));
        addItem(new PrefItem(PrefViewType.EMPTY));
    }

    @Override
    public void initView(View v) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        createAdapter(new PrefRecyclerAdapter(getItems(), getContext()) {
            // clickイベント

            @Override
            protected void onItemClicked(View view) {
                super.onItemClicked(view);
                if (view.getVisibility() == View.GONE) view.setVisibility(View.VISIBLE);
                else view.setVisibility(View.GONE);
            }

            @Override
            protected void onItemProgressChanged(int progress, TextView targetTextView, @NonNull PrefItem mode) {
                super.onItemProgressChanged(progress, targetTextView, mode);
                targetTextView.setText(getResources().getString(R.string.pref_rate_u, progress));
            }

            @Override
            protected void onItemStopTrackingTouch(Integer tag, SeekBar seekbar, @NonNull PrefItem model) {
                int progress = seekbar.getProgress();
                super.onItemStopTrackingTouch(tag, seekbar, model);
                Log.d(TAG, String.valueOf(tag));
                if (tag.equals(U90)) {
                    if (progress < u81_rate) {
                        progress = u81_rate;
                        seekbar.setProgress(progress);
                    }
                    u90_rate = progress;
                    PrefUtil.saveRateU90(progress);
                } else if (tag.equals(U81)) {
                    if (progress > u90_rate) {
                        progress = u90_rate;
                        seekbar.setProgress(progress);
                    } else if (progress < u75_rate) {
                        progress = u75_rate;
                        seekbar.setProgress(progress);
                    }
                    u81_rate = progress;
                    PrefUtil.saveRateU81(progress);
                } else if (tag.equals(U75)) {
                    if (progress > u81_rate) {
                        progress = u81_rate;
                        seekbar.setProgress(progress);
                    }
                    u75_rate = progress;
                    PrefUtil.saveRateU75(progress);
                }
            }

            @Override
            protected void onItemClicked(Integer tag, final CircleImageView view, @NonNull final PrefItem model) {
                super.onItemClicked(tag, view, model);
                Log.d(TAG, String.valueOf(tag));
                int defaultColor = R.color.bg_title;
                if (tag.equals(U90)) defaultColor = PrefUtil.loadColorU90();
                else if (tag.equals(U81)) defaultColor = PrefUtil.loadColorU81();
                else if (tag.equals(U75)) defaultColor = PrefUtil.loadColorU75();
                Log.d(TAG, String.valueOf(defaultColor));
                Log.d(TAG, String.valueOf(R.color.bg_title));
                showColorPicker(defaultColor, color -> {
                    view.setColorFilter(color);
                    Log.d(TAG, String.valueOf(tag));
                    if (tag.equals(U90)) PrefUtil.saveColorU90(color);
                    else if (tag.equals(U81)) PrefUtil.saveColorU81(color);
                    else if (tag.equals(U75)) PrefUtil.saveColorU75(color);
                });
            }

            // スイッチのcheckedChanged
            @Override
            protected void onItemCheckedChange(@NonNull boolean bool, @NonNull PrefItem model) {
                super.onItemCheckedChange(bool, model);
                if (model.getItemName().equals(getResources().getString(R.string.pref_enable_change_color)))
                    PrefUtil.saveEnableColorChange(bool);
                else if (model.getItemName().equals(getResources().getString(R.string.pref_blackout)))
                    PrefUtil.saveBlackout(bool);
            }
        });
        mRootView.setBackgroundColor(Util.getColor(R.color.colorPrimaryDark));
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    protected void swap() {}

    @Override
    protected void getSavedItems() {}

    private void showColorPicker(int color, ColorCallBack callBack) {
        new SpectrumDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.pref_select_color))
                .setColors(R.array.diag_colors)
                .setSelectedColor(color)
                .setDismissOnColorSelected(true)
                .setOutlineWidth(2)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if (positiveResult) {
                            callBack.callback(color);
                        }
                    }
                }).build().show(getFragmentManager(), "");
    }

    private interface ColorCallBack {
        void callback(int color);
    }
}
