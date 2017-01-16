package jp.yuta.kohashi.esc.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.adapter.CustomTimeTableListAdapter;
import jp.yuta.kohashi.esc.adapter.TimeTableListAdapter;
import jp.yuta.kohashi.esc.fragment.CustomFragmentFragment;
import jp.yuta.kohashi.esc.object.TimeBlock;
import jp.yuta.kohashi.esc.preference.LoadManager;
import me.drakeet.materialdialog.MaterialDialog;

public class CustomTimeTableActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    MaterialDialog mMaterialDialog;
    FragmentManager manager;
    FrameLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_time_table);

        //フラグメントと追加
        Fragment fragment = new CustomFragmentFragment();

        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.custom_table_fragment_container, fragment, "dd")
                .addToBackStack(null)
                .commit();


        //ツールバーの戻るボタンをタップした時finishメソッドを呼び出す
        ImageButton imgButton = (ImageButton) findViewById(R.id.custom_time_table_back);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        ImageButton cleaBtn = (ImageButton) findViewById(R.id.reset_button);
        root = (FrameLayout) findViewById(R.id.custom_time_table_container);
        cleaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mMaterialDialog = new MaterialDialog(CustomTimeTableActivity.this)
                        .setTitle("注意")
                        .setMessage("編集した時間割を元に戻します。\n" +
                                "（全ての項目が編集前の状態に戻ります）")
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sharedPreferences = getSharedPreferences("customCell", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();

                                Fragment fragment = new CustomFragmentFragment();

                                manager = getSupportFragmentManager();
                                manager.beginTransaction()
                                        .replace(R.id.custom_table_fragment_container, fragment, "dd")
                                        .addToBackStack(null)
                                        .commit();

                                mMaterialDialog.dismiss();


                            }
                        })
                        .setNegativeButton("CANCEL", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();

                            }
                        });
                mMaterialDialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            return false;
        }
        return false;
    }


}
