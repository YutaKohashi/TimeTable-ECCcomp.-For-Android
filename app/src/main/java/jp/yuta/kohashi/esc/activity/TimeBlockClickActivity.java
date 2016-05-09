package jp.yuta.kohashi.esc.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import jp.yuta.kohashi.esc.R;

public class TimeBlockClickActivity extends AppCompatActivity {
    TextView subjectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_block_click);

        subjectText = (TextView)findViewById(R.id.subject_name_dialog);
        Intent intent = getIntent();
        subjectText.setText(intent.getStringExtra("subjectName"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.none_anim, R.anim.push_out_up);
    }
}
