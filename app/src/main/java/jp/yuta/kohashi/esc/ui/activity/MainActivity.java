package jp.yuta.kohashi.esc.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jp.yuta.kohashi.esc.R;


/***
 * このActivityに時間割や出席照会のFragmentがのる
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
