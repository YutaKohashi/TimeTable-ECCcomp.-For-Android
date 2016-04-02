package com.example.yutathinkpad.esc.fragment;


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

import com.example.yutathinkpad.esc.R;
import com.example.yutathinkpad.esc.adapter.RecyclerViewAdapter;
import com.example.yutathinkpad.esc.http.GetAttendanceRateManager;
import com.example.yutathinkpad.esc.object.AttendanceRateObject;
import com.example.yutathinkpad.esc.preference.LoadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceRateFragment extends Fragment {
    static final String PREF_NAME ="sample";
    static final String PREF_NAME_ID_PASS = "ip";
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    TextView textView;


    public AttendanceRateFragment() {
        // Required empty public constructor
    }

    public AttendanceRateFragment newInstance() {
        AttendanceRateFragment frag = new AttendanceRateFragment();
        return frag;
    }

    List<AttendanceRateObject> rateObjectList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_attendance_rate, container, false);
//
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

        // SwipeRefreshLayoutの設定
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
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
        mSwipeRefreshLayout.setColorScheme(R.color.colorPrimary1, R.color.colorPrimary2, R.color.colorPrimary6, R.color.colorPrimary7);

        new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                mSwipeRefreshLayout.setEnabled(i == 0);
            }
        };



        return v;
    }

//    @Override
//    public void onStart(){
//        super.onStart();
//        DrawerLayout drawerLayout= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
//        drawerLayout.closeDrawer(GravityCompat.START);
//
//    }

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
