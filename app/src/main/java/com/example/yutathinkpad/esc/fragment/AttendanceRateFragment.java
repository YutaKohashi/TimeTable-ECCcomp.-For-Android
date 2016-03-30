package com.example.yutathinkpad.esc.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yutathinkpad.esc.R;
import com.example.yutathinkpad.esc.adapter.RecyclerViewAdapter;
import com.example.yutathinkpad.esc.object.AttendanceRateObject;
import com.example.yutathinkpad.esc.preference.LoadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceRateFragment extends Fragment {
    static final String PREF_NAME ="sample";
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    TextView textView;


    public AttendanceRateFragment() {
        // Required empty public constructor
    }

    public AttendanceRateFragment newInstance() {
        AttendanceRateFragment frag = new AttendanceRateFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attendance_rate, container, false);

        List<AttendanceRateObject> rateObjectList = new ArrayList<>();
        LoadManager loadManager = new LoadManager();
        rateObjectList = loadManager.loadManagerWithPreferenceForAttendance(getActivity(),PREF_NAME,"attendanceList");

        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(rateObjectList);
        recyclerView.setAdapter(adapter);

        //タイトルの設定
        textView = (TextView)getActivity().findViewById(R.id.title_name_text);
        textView.setText("出席照会");

        return v;
    }

}
