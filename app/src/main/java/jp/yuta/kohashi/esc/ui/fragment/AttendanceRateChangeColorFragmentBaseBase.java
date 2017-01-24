package jp.yuta.kohashi.esc.ui.fragment;


import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thebluealliance.spectrum.SpectrumDialog;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.PrefItemModel;
import jp.yuta.kohashi.esc.model.enums.PrefViewType;
import jp.yuta.kohashi.esc.ui.adapter.PrefRecyclerAdapter;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceRateChangeColorFragmentBaseBase extends BasePrefBaseRecyclerViewFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void createItems() {
        addItem(new PrefItemModel(PrefViewType.EMPTY));
        addItem(new PrefItemModel(getResources().getString(R.string.pref_enable_change_color), R.drawable.ic_brush, PrefViewType.ITEM_SWITCH, PrefUtil.isChangeColor()));
//        addItem(new PrefItemModel(PrefViewType.EMPTY));
//        addItem(new PrefItemModel(getResources().getString(R.string.pref_set_color), PrefViewType.ITEM_GROUP_TITLE));
        addItem(new PrefItemModel(getResources().getString(R.string.pref_rate_u90), R.drawable.ic_color_lens, PrefViewType.ITEM_COLOR_PICK, PrefUtil.loadColorU90()));
        addItem(new PrefItemModel(getResources().getString(R.string.pref_rate_u81), R.drawable.ic_color_lens, PrefViewType.ITEM_COLOR_PICK, PrefUtil.loadColorU81()));
        addItem(new PrefItemModel(getResources().getString(R.string.pref_rate_u75), R.drawable.ic_color_lens, PrefViewType.ITEM_COLOR_PICK, PrefUtil.loadColorU75()));
        addItem(new PrefItemModel(PrefViewType.EMPTY));
        addItem(new PrefItemModel(getResources().getString(R.string.pref_group_title_time_other), PrefViewType.ITEM_GROUP_TITLE));
        addItem(new PrefItemModel(getResources().getString(R.string.pref_blackout), R.drawable.ic_visibility_off, PrefViewType.ITEM_SWITCH, PrefUtil.isBlackout()));
    }

    @Override
    public void initView(View v) {
        getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
        createAdapter(new PrefRecyclerAdapter(getItems(), getContext()) {
            // clickイベント
            @Override
            protected void onItemClicked(final CircleImageView view, @NonNull final PrefItemModel model) {
                super.onItemClicked(view, model);
                showColorPicker(R.color.fbutton_default_shadow_color, new Callback() {
                    @Override
                    public void selected(int color) {
                        view.setColorFilter(color);
                        if(model.getItemName().equals(getResources().getString(R.string.pref_rate_u90))){
                            PrefUtil.saveColorU90(color);
                        } else if(model.getItemName().equals(getResources().getString(R.string.pref_rate_u81))){
                            PrefUtil.saveColorU81(color);
                        } else if(model.getItemName().equals(getResources().getString(R.string.pref_rate_u75))){
                            PrefUtil.saveColorU75(color);
                        }
                    }
                });
            }

            // スイッチのcheckedChanged
            @Override
            protected void onItemCheckedChange(@NonNull boolean bool, @NonNull PrefItemModel model) {
                super.onItemCheckedChange(bool, model);
                if(model.getItemName().equals(getResources().getString(R.string.pref_enable_change_color))){
                    PrefUtil.saveEnableColorChange(bool);
                } else if (model.getItemName().equals(getResources().getString(R.string.pref_blackout))){
                    PrefUtil.saveBlackout(bool);
                }

            }
        });
        getRecyclerView().setAdapter(getAdapter());
    }


    private void showColorPicker(int nowColor, final Callback callback) {
        new SpectrumDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.pref_select_color))
                .setColors(R.array.diag_colors)
                .setSelectedColorRes(nowColor)
                .setDismissOnColorSelected(true)
                .setOutlineWidth(2)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if (positiveResult) {
                            callback.selected(color);
                        }
                    }
                }).build().show(getFragmentManager(), "");
    }

    private interface Callback {
        void selected(int color);
    }
}
