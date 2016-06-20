package jp.yuta.kohashi.esc.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.adapter.CustomTimeTableListAdapter;
import jp.yuta.kohashi.esc.adapter.TimeTableListAdapter;
import jp.yuta.kohashi.esc.object.TimeBlock;
import jp.yuta.kohashi.esc.preference.LoadManager;

public class CustomTimeTableActivity extends AppCompatActivity {

    static final String prefName ="sample";

    List<TimeBlock> MondayList;
    List<TimeBlock> TuesdayList;
    List<TimeBlock> WednesdayList;
    List<TimeBlock> ThursdayList;
    List<TimeBlock> FridayList;

    RecyclerView monList;
    RecyclerView tueList;
    RecyclerView wedList;
    RecyclerView thurList;
    RecyclerView friList;
    RecyclerView.LayoutManager layoutManager;

    LoadManager loadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_time_table);


        MondayList = new ArrayList<>();
        TuesdayList = new ArrayList<>();
        WednesdayList = new ArrayList<>();
        ThursdayList = new ArrayList<>();
        FridayList = new ArrayList<>();

        loadManager = new LoadManager();

        MondayList = loadManager.loadManagerWithPreferenceForTimeBlock(CustomTimeTableActivity.this ,prefName,"monList");
        TuesdayList = loadManager.loadManagerWithPreferenceForTimeBlock(CustomTimeTableActivity.this,prefName,"tueList");
        WednesdayList = loadManager.loadManagerWithPreferenceForTimeBlock(CustomTimeTableActivity.this,prefName,"wedList");
        ThursdayList = loadManager.loadManagerWithPreferenceForTimeBlock(CustomTimeTableActivity.this,prefName,"thurList");
        FridayList = loadManager.loadManagerWithPreferenceForTimeBlock(CustomTimeTableActivity.this,prefName,"friList");

        //曜日ごとにRecyclerViewを配置
        monList = (RecyclerView)findViewById(R.id.mon_col);
        tueList = (RecyclerView)findViewById(R.id.tue_col);
        wedList = (RecyclerView)findViewById(R.id.wed_col);
        thurList = (RecyclerView)findViewById(R.id.thur_col);
        friList = (RecyclerView)findViewById(R.id.fri_col);

        //月曜日
        CustomTimeTableListAdapter monAdapter = new CustomTimeTableListAdapter(MondayList,CustomTimeTableActivity.this);
        monList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(CustomTimeTableActivity.this);
        monList.setLayoutManager(layoutManager);
        monList.setAdapter(monAdapter);
        //火曜日
        CustomTimeTableListAdapter tueAdapter = new CustomTimeTableListAdapter(TuesdayList,CustomTimeTableActivity.this);
        tueList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(CustomTimeTableActivity.this);
        tueList.setLayoutManager(layoutManager);
        tueList.setAdapter(tueAdapter);
        //水曜日
        CustomTimeTableListAdapter wedAdapter = new CustomTimeTableListAdapter(WednesdayList,CustomTimeTableActivity.this);
        wedList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(CustomTimeTableActivity.this);
        wedList.setLayoutManager(layoutManager);
        wedList.setAdapter(wedAdapter);
        //木曜日
        CustomTimeTableListAdapter thurAdapter = new CustomTimeTableListAdapter(ThursdayList,CustomTimeTableActivity.this);
        thurList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(CustomTimeTableActivity.this);
        thurList.setLayoutManager(layoutManager);
        thurList.setAdapter(thurAdapter);
        //金曜日
        CustomTimeTableListAdapter friAdapter = new CustomTimeTableListAdapter(FridayList,CustomTimeTableActivity.this);
        friList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(CustomTimeTableActivity.this);
        friList.setLayoutManager(layoutManager);
        friList.setAdapter(friAdapter);


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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        this.finish();
//        Intent intent = new Intent(CustomTimeTableActivity.this, PreferenceRelationActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.none_anim, R.anim.push_out_up);
//    }

}
