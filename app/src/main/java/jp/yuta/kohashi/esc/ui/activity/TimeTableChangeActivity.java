package jp.yuta.kohashi.esc.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.ui.activity.base.BaseActivity;
import jp.yuta.kohashi.esc.ui.fragment.TimeTableChangeFragment;
import jp.yuta.kohashi.esc.util.NotifyUtil;
import jp.yuta.kohashi.esc.util.Util;

public class TimeTableChangeActivity extends BaseActivity {

    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_time_table);

        initToolbar();
        enableBackBtn();
        setTitle(getResources().getString(R.string.pref_change_time_table));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items_activity_time_table_change, menu);

         mMenu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.btn_all_reset){
            showResetDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showResetDialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.dialog_all_reset_title)
                .content(R.string.dialog_all_reset_comment)
                .positiveText(R.string.dialog_positive_ok)
                .negativeText(R.string.dialog_negative_cancel)
                .positiveColor(Util.getColor(R.color.diag_text_color_cancel))
                .negativeColor(Util.getColor(R.color.colorPrimary))
                .onPositive(((dialog, which) ->
                        allReset()))
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                    NotifyUtil.cancel();
                }));

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    private void allReset(){
        // get fragment
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.time_table);
        if (fragment != null && fragment instanceof TimeTableChangeFragment) {
            ((TimeTableChangeFragment) fragment).allReset();
        }
    }

}
