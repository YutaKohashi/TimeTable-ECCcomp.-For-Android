package jp.yuta.kohashi.esc.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();

        ((findViewById(R.id.close_btn))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        ((TextView) findViewById(R.id.school_number)).setText(PrefUtil.getId());
        ((TextView) findViewById(R.id.name)).setText(PrefUtil.getUserName());
        ((TextView) findViewById(R.id.course)).setText(PrefUtil.getCourse());
        ((TextView) findViewById(R.id.gakka)).setText(PrefUtil.getGakka());
        ((TextView) findViewById(R.id.class_txt)).setText(PrefUtil.getClassTxt());
        ((TextView) findViewById(R.id.shuseki_num)).setText(PrefUtil.getShusekiNum());
    }
}
