package jp.yuta.kohashi.esc.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.activity.CustomTimeTableActivity;
import jp.yuta.kohashi.esc.adapter.CustomTimeTableListAdapter;
import jp.yuta.kohashi.esc.object.CustomTimeTableCell;
import jp.yuta.kohashi.esc.object.TimeBlock;
import jp.yuta.kohashi.esc.preference.LoadManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomFragmentFragment extends Fragment {
    static final String prefName ="sample";

    /******        カスタム定数      *******/
    static final String CUSTOM_CELL_PREF_NAME ="customCell";
    private static final String KEY = "CUSTOM_TIME_TABLE";
    /**********************************/

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

    List<CustomTimeTableCell> customTimeList;


    public CustomFragmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_custom, container, false);

        MondayList = new ArrayList<>();
        TuesdayList = new ArrayList<>();
        WednesdayList = new ArrayList<>();
        ThursdayList = new ArrayList<>();
        FridayList = new ArrayList<>();

        loadManager = new LoadManager();

        MondayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity() ,prefName,"monList");
        TuesdayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"tueList");
        WednesdayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"wedList");
        ThursdayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"thurList");
        FridayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"friList");

        /**************************************************************************/
        /*カスタム処理
        /**************************************************************************/
        customTimeList = new ArrayList<>();
        customTimeList = loadManager.loadManagerWithPreferenceForCustomTimeTable(getActivity(),CUSTOM_CELL_PREF_NAME,KEY);
        try{
            customTimeList.size();
        }catch(Exception e){
            customTimeList = new ArrayList<>();
        }

        for(CustomTimeTableCell preCell:customTimeList){
            int x =  preCell.getX();
            int y = preCell.getY();

            switch (x){
                case 0:
                    List<TimeBlock> list = new ArrayList<>(MondayList);
                    MondayList.clear();
                    for(int i = 0;i < list.size();i++){
                        if(i == y){
                            TimeBlock timeBlock = new TimeBlock();
                            timeBlock.setSubject(preCell.getSubject());
                            timeBlock.setClassRoom(preCell.getRoom());
                            MondayList.add(timeBlock);
                        }else{
                            MondayList.add(list.get(i));
                        }
                    }
                    break;
                case 1:
                    List<TimeBlock> list1 = new ArrayList<>(TuesdayList);
                    TuesdayList.clear();
                    for(int i = 0;i < list1.size();i++){
                        if(i == y){
                            TimeBlock timeBlock = new TimeBlock();
                            timeBlock.setSubject(preCell.getSubject());
                            timeBlock.setClassRoom(preCell.getRoom());
                            TuesdayList.add(timeBlock);
                        }else{
                            TuesdayList.add(list1.get(i));
                        }
                    }
                    break;
                case 2:
                    List<TimeBlock> list2 = new ArrayList<>(WednesdayList);
                    WednesdayList.clear();
                    for(int i = 0;i < list2.size();i++){
                        if(i == y){
                            TimeBlock timeBlock = new TimeBlock();
                            timeBlock.setSubject(preCell.getSubject());
                            timeBlock.setClassRoom(preCell.getRoom());
                            WednesdayList.add(timeBlock);
                        }else{
                            WednesdayList.add(list2.get(i));
                        }
                    }
                    break;
                case 3:
                    List<TimeBlock> list3 = new ArrayList<>(ThursdayList);
                    ThursdayList.clear();
                    for(int i = 0;i < list3.size();i++){
                        if(i == y){
                            TimeBlock timeBlock = new TimeBlock();
                            timeBlock.setSubject(preCell.getSubject());
                            timeBlock.setClassRoom(preCell.getRoom());
                            ThursdayList.add(timeBlock);
                        }else{
                            ThursdayList.add(list3.get(i));
                        }
                    }
                    break;
                case 4:
                    List<TimeBlock> list4 = new ArrayList<>(FridayList);
                    FridayList.clear();
                    for(int i = 0;i < list4.size();i++){
                        if(i == y){
                            TimeBlock timeBlock = new TimeBlock();
                            timeBlock.setSubject(preCell.getSubject());
                            timeBlock.setClassRoom(preCell.getRoom());
                            FridayList.add(timeBlock);
                        }else{
                            FridayList.add(list4.get(i));
                        }
                    }
                    break;

            }
        }

        /**************************************************************************/
        /*
        /**************************************************************************/

        //曜日ごとにRecyclerViewを配置
        monList = (RecyclerView)v.findViewById(R.id.mon_col);
        tueList = (RecyclerView)v.findViewById(R.id.tue_col);
        wedList = (RecyclerView)v.findViewById(R.id.wed_col);
        thurList = (RecyclerView)v.findViewById(R.id.thur_col);
        friList = (RecyclerView)v.findViewById(R.id.fri_col);

        //月曜日
        CustomTimeTableListAdapter monAdapter = new CustomTimeTableListAdapter(MondayList,getActivity(),0);
        monList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        monList.setLayoutManager(layoutManager);
        monList.setAdapter(monAdapter);
        //火曜日
        CustomTimeTableListAdapter tueAdapter = new CustomTimeTableListAdapter(TuesdayList,getActivity(),1);
        tueList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        tueList.setLayoutManager(layoutManager);
        tueList.setAdapter(tueAdapter);
        //水曜日
        CustomTimeTableListAdapter wedAdapter = new CustomTimeTableListAdapter(WednesdayList,getActivity(),2);
        wedList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        wedList.setLayoutManager(layoutManager);
        wedList.setAdapter(wedAdapter);
        //木曜日
        CustomTimeTableListAdapter thurAdapter = new CustomTimeTableListAdapter(ThursdayList,getActivity(),3);
        thurList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        thurList.setLayoutManager(layoutManager);
        thurList.setAdapter(thurAdapter);
        //金曜日
        CustomTimeTableListAdapter friAdapter = new CustomTimeTableListAdapter(FridayList,getActivity(),4);
        friList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        friList.setLayoutManager(layoutManager);
        friList.setAdapter(friAdapter);



        return v;
    }


}
