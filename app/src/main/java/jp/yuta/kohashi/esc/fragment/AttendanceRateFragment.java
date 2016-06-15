package jp.yuta.kohashi.esc.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.adapter.RecyclerViewAdapter;
import jp.yuta.kohashi.esc.http.GetAttendanceRateManager;
import jp.yuta.kohashi.esc.object.AttendanceRateObject;
import jp.yuta.kohashi.esc.preference.LoadManager;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceRateFragment extends Fragment {
    static final String PREF_NAME ="sample";
    static final String PREF_NAME_ID_PASS = "ip";
    static final String PREF_KEY_LATAST_UP = "latestUp";
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    TextView textView;
    TextView latestUpText;
    SharedPreferences pref;

    private static final String SHOWCASE_ID = "sequence1";


    public AttendanceRateFragment() {
        // Required empty public constructor
    }

    public AttendanceRateFragment newInstance() {
        AttendanceRateFragment frag = new AttendanceRateFragment();
        return frag;
    }

    List<AttendanceRateObject> rateObjectList;

    @Override
    public void onStart() {
        super.onStart();

        latestUpText = (TextView)getActivity().findViewById(R.id.latest_update);
        textView = (TextView)getActivity().findViewById(R.id.title_name_text);

        //プリファレンスからチュートリアルを表示、非表示
        SharedPreferences data = getActivity().getSharedPreferences("material_showcaseview_prefs", Context.MODE_PRIVATE);
        boolean bool = data.getBoolean("enableTutorial",true);

        //設定画面からこのFragmentに来た時のみonStartメソッドでチュートリアルを表示する
        if(bool){
            ShowcaseConfig config = new ShowcaseConfig();
            config.setDelay(400);
            MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);
            sequence.setConfig(config);
            sequence.addSequenceItem(textView,
                    "下向きにスワイプすることでデータを更新できます。", "次へ");
            sequence.addSequenceItem(latestUpText,
                    "下部に最終更新日時が表示されます。", "開始する");
            sequence.start();


            SharedPreferences.Editor editor = data.edit();
            editor.putBoolean("enableTutorial", false);
            editor.commit();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_attendance_rate, container, false);
//
//        latestUpText = (TextView)getActivity().findViewById(R.id.latest_update);
//        latestUpText.setVisibility(View.VISIBLE);

        //最終更新日時をタイトル欄に表示する
        latestUpText = (TextView)v.findViewById(R.id.latest_update);
        pref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String latestDate = pref.getString(PREF_KEY_LATAST_UP,"");
        latestUpText.setText("最終更新日時:" + latestDate);

        rateObjectList = new ArrayList<>();
        LoadManager loadManager = new LoadManager();
        rateObjectList = loadManager.loadManagerWithPreferenceForAttendance(getActivity(),PREF_NAME,"attendanceList");

        if (rateObjectList == null){
            rateObjectList = new ArrayList<>();
            rateObjectList.add(
                    new AttendanceRateObject((getString(R.string.get_rate_error))));
        }


        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(rateObjectList,getActivity());
        recyclerView.setAdapter(adapter);

        //タイトルの設定
        textView = (TextView)getActivity().findViewById(R.id.title_name_text);
        textView.setText("出席照会");

        //通常はonCreateViewでチュートリアルを表示する
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(400);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);
        sequence.setConfig(config);
        sequence.addSequenceItem(textView,
                "下向きにスワイプすることでデータを更新できます。", "次へ");
        sequence.addSequenceItem(latestUpText,
                "下部に最終更新日時が表示されます。", "開始する");
        sequence.start();

        SharedPreferences preferences = getActivity().getSharedPreferences("material_showcaseview_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("enableTutorial", false);
        editor.commit();


        // SwipeRefreshLayoutの設定
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        //swipeTorefresh使用時のリスナー
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<String> list = new ArrayList<String>();
                LoadManager loadManager = new LoadManager();
                list = loadManager.loadManagerWithPreferenceForString(getActivity(),PREF_NAME_ID_PASS,"ip");

                String userId = list.get(0);
                String pass = list.get(1);
                GetAttendanceRateManager getAttendanceRateManager = new GetAttendanceRateManager();
                getAttendanceRateManager.getAttendanceRate(getActivity(),v,userId,pass);

            }
        });

        //setColorSchemeResources   推奨
        //setColorScheme            非推奨
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary1, R.color.colorPrimary2, R.color.colorPrimary6, R.color.colorPrimary7);

        new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                mSwipeRefreshLayout.setEnabled(i == 0);
            }
        };


        return v;
    }



    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
//            // 3秒待機
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mSwipeRefreshLayout.setRefreshing(false);
//                }
//            }, 3000);


        }
    };
}
