package com.example.yutathinkpad.esc.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yutathinkpad.esc.R;
import com.example.yutathinkpad.esc.object.TimeBlock;
import com.example.yutathinkpad.esc.preference.LoadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeTableFragment extends Fragment {

    static final String prefName ="sample";

    //月曜日から金曜日の曜日ごとのリスト
    List<TimeBlock> MondayList;
    List<TimeBlock> TuesdayList;
    List<TimeBlock> WednesdayList;
    List<TimeBlock> ThursdayList;
    List<TimeBlock> FridayList;

    LinearLayout monLayout;
    LinearLayout tueLayout;
    LinearLayout wedLayout;
    LinearLayout thurLayout;
    LinearLayout friLayout;

    LoadManager loadManager;

    TextView textView;
 //   LayoutInflater inflater;

    public TimeTableFragment() {
        // Required empty public constructor
    }

    public TimeTableFragment newInstance() {
        TimeTableFragment frag = new TimeTableFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_time_table, container, false);




        MondayList = new ArrayList<>();
        TuesdayList = new ArrayList<>();
        WednesdayList = new ArrayList<>();
        ThursdayList = new ArrayList<>();
        FridayList = new ArrayList<>();


        monLayout = (LinearLayout)v.findViewById(R.id.mon_col);
        tueLayout =(LinearLayout)v.findViewById(R.id.tue_col);
        wedLayout = (LinearLayout)v.findViewById(R.id.wed_col);
        thurLayout = (LinearLayout)v.findViewById(R.id.thur_col);
        friLayout = (LinearLayout)v.findViewById(R.id.fri_col);

        loadManager = new LoadManager();

        MondayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"monList");
        TuesdayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"tueList");
        WednesdayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"wedList");
        ThursdayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"thurList");
        FridayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"friList");


        monLayout.removeAllViews();
        for(int i = 0;i < MondayList.size();i++){

            TimeBlock timeBlock = MondayList.get(i);
            String subStr =timeBlock.getSubject();
            String roomStr = timeBlock.getClassRoom();
            CardView cardView = (CardView)inflater.inflate(R.layout.time_table_block, monLayout, false);
            cardView.setMinimumHeight(300);
            TextView mSubjectTextView = (TextView)cardView.findViewById(R.id.text_subject);
            TextView mClassRoomTextView = (TextView)cardView.findViewById(R.id.text_classRoom);
            mSubjectTextView.setText(subStr);
            mClassRoomTextView.setText(roomStr);

            monLayout.addView(cardView);
        }

        tueLayout.removeAllViews();
        for(int i = 0;i < TuesdayList.size();i++){

            TimeBlock timeBlock = TuesdayList.get(i);
            String subStr =timeBlock.getSubject();
            String roomStr = timeBlock.getClassRoom();
            CardView cardView = (CardView)inflater.inflate(R.layout.time_table_block, tueLayout, false);
            cardView.setMinimumHeight(300);
            TextView mSubjectTextView = (TextView)cardView.findViewById(R.id.text_subject);
            TextView mClassRoomTextView = (TextView)cardView.findViewById(R.id.text_classRoom);
            mSubjectTextView.setText(subStr);
            mClassRoomTextView.setText(roomStr);

            tueLayout.addView(cardView);
        }

        wedLayout.removeAllViews();
        for(int i = 0;i < WednesdayList.size();i++){

            TimeBlock timeBlock = WednesdayList.get(i);
            String subStr =timeBlock.getSubject();
            String roomStr = timeBlock.getClassRoom();
            CardView cardView = (CardView)inflater.inflate(R.layout.time_table_block, wedLayout, false);
            cardView.setMinimumHeight(300);
            TextView mSubjectTextView = (TextView)cardView.findViewById(R.id.text_subject);
            TextView mClassRoomTextView = (TextView)cardView.findViewById(R.id.text_classRoom);
            mSubjectTextView.setText(subStr);
            mClassRoomTextView.setText(roomStr);

            wedLayout.addView(cardView);
        }

        thurLayout.removeAllViews();
        for(int i = 0;i < TuesdayList.size();i++){

            TimeBlock timeBlock = ThursdayList.get(i);
            String subStr =timeBlock.getSubject();
            String roomStr = timeBlock.getClassRoom();
            CardView cardView = (CardView)inflater.inflate(R.layout.time_table_block, thurLayout, false);
            cardView.setMinimumHeight(300);
            TextView mSubjectTextView = (TextView)cardView.findViewById(R.id.text_subject);
            TextView mClassRoomTextView = (TextView)cardView.findViewById(R.id.text_classRoom);
            mSubjectTextView.setText(subStr);
            mClassRoomTextView.setText(roomStr);

            thurLayout.addView(cardView);
        }

        friLayout.removeAllViews();
        for(int i = 0;i < FridayList.size();i++){

            TimeBlock timeBlock = FridayList.get(i);
            String subStr =timeBlock.getSubject();
            String roomStr = timeBlock.getClassRoom();
            CardView cardView = (CardView)inflater.inflate(R.layout.time_table_block, friLayout, false);
            cardView.setMinimumHeight(300);
            TextView mSubjectTextView = (TextView)cardView.findViewById(R.id.text_subject);
            TextView mClassRoomTextView = (TextView)cardView.findViewById(R.id.text_classRoom);
            mSubjectTextView.setText(subStr);
            mClassRoomTextView.setText(roomStr);

            friLayout.addView(cardView);
        }

        //タイトルの設定
        textView = (TextView)getActivity().findViewById(R.id.title_name_text);
        textView.setText("時間割");

        return v;
    }

}
