package jp.yuta.kohashi.esc.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.adapter.TimeTableListAdapter;
import jp.yuta.kohashi.esc.object.CustomTimeTableCell;
import jp.yuta.kohashi.esc.object.TimeBlock;
import jp.yuta.kohashi.esc.preference.LoadManager;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeTableFragment extends Fragment {

    static final String prefName ="sample";
    private static final String SHOWCASE_ID = "sequence4";
    static final String CUSTOM_CELL_PREF_NAME ="customCell";
    private static final String KEY = "CUSTOM_TIME_TABLE";

    private static int count = 0;

    //月曜日から金曜日の曜日ごとのリスト
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

    TextView textView;

    List<CustomTimeTableCell> customTimeList;
    TextView latestUpText;
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

        loadManager = new LoadManager();


        MondayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"monList");
        TuesdayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"tueList");
        WednesdayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"wedList");
        ThursdayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"thurList");
        FridayList = loadManager.loadManagerWithPreferenceForTimeBlock(getActivity(),prefName,"friList");


        textView = (TextView)v.findViewById(R.id.title_name_text);
        textView.setText("時間割");

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

        //曜日ごとにRecyclerViewを配置
        monList = (RecyclerView)v.findViewById(R.id.mon_col);
        tueList = (RecyclerView)v.findViewById(R.id.tue_col);
        wedList = (RecyclerView)v.findViewById(R.id.wed_col);
        thurList = (RecyclerView)v.findViewById(R.id.thur_col);
        friList = (RecyclerView)v.findViewById(R.id.fri_col);


        //月曜日
        TimeTableListAdapter monAdapter = new TimeTableListAdapter(MondayList,getActivity());
        monList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        monList.setLayoutManager(layoutManager);
        monList.setAdapter(monAdapter);
        //火曜日
        TimeTableListAdapter tueAdapter = new TimeTableListAdapter(TuesdayList,getActivity());
        tueList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        tueList.setLayoutManager(layoutManager);
        tueList.setAdapter(tueAdapter);
        //水曜日
        TimeTableListAdapter wedAdapter = new TimeTableListAdapter(WednesdayList,getActivity());
        wedList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        wedList.setLayoutManager(layoutManager);
        wedList.setAdapter(wedAdapter);
        //木曜日
        TimeTableListAdapter thurAdapter = new TimeTableListAdapter(ThursdayList,getActivity());
        thurList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        thurList.setLayoutManager(layoutManager);
        thurList.setAdapter(thurAdapter);
        //金曜日
        TimeTableListAdapter friAdapter = new TimeTableListAdapter(FridayList,getActivity());
        friList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        friList.setLayoutManager(layoutManager);
        friList.setAdapter(friAdapter);




//        SharedPreferences prefs = getActivity().getSharedPreferences("material_showcaseview_prefs", Context.MODE_PRIVATE);
//
        //チュートリアル
        //通常はonCreateviewでチュートリアルを表示する
//        TextView view = (TextView)getActivity().findViewById(R.id.dummy_edit);
//        ShowcaseConfig config = new ShowcaseConfig();
//        config.setDelay(500);
//        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);
//        sequence.setConfig(config);
//        sequence.addSequenceItem(view,
//                "各ブロックをタップすと詳細情報が表示されます。", "次へ");
//        sequence.addSequenceItem(getNavButtonView((Toolbar) getActivity().findViewById(R.id.toolbar)),
//                "トグルをタップ、または右にスワイプすることでコンテンツを選択できます。", "開始する");
//        sequence.start();
//
//        SharedPreferences preferences = getActivity().getSharedPreferences("material_showcaseview_prefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean("enableTutorial", false);
//        editor.commit();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        //設定画面から「チュートリアルをもう一度表示する」をタップぷした場合のみこのメソッドを通す
        if(true){

        }
        //プリファレンスからチュートリアルを表示、非表示
        SharedPreferences data = getActivity().getSharedPreferences("material_showcaseview_prefs", Context.MODE_PRIVATE);
        boolean bool = data.getBoolean("enableTutorial",true);

        //設定画面からこのFragmentに来た時のみonStartメソッドでチュートリアルを表示する
        if(bool){
            if(bool){
                TextView view = (TextView)getActivity().findViewById(R.id.dummy_edit);
                Log.d("TimeTableFragment::","onCreate" + String.valueOf(count++));
                ShowcaseConfig config = new ShowcaseConfig();
                config.setDelay(500);
                MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);
                sequence.setConfig(config);
                sequence.addSequenceItem(view,
                        "各ブロックをタップすと詳細情報が表示されます。", "次へ");
                sequence.addSequenceItem(getNavButtonView((Toolbar) getActivity().findViewById(R.id.toolbar)),
                        "トグルをタップ、または右にスワイプすることでコンテンツを選択できます。", "開始する");
                sequence.start();

                //プリファレンスの内容をfalseに変更
                SharedPreferences.Editor editor = data.edit();
                editor.putBoolean("enableTutorial", false);
                editor.commit();
            }
        }

    }


    //時間割の各ブロックのレイアウトを設定するクラス
    /**
     *
     * @param list
     * @param containerLayout
     * @param inflater
     */
    CardView cardView;
    private void setBlockToTimeTabe(List<TimeBlock> list, LinearLayout containerLayout,LayoutInflater inflater){
        containerLayout.removeAllViews();
        for(int i = 0;i < list.size();i++){

            TimeBlock timeBlock = list.get(i);
            String subStr =timeBlock.getSubject();
            String roomStr = timeBlock.getClassRoom();
            cardView = (CardView)inflater.inflate(R.layout.time_table_block, containerLayout, false);
            cardView.setMinimumHeight(300);
            TextView mSubjectTextView = (TextView)cardView.findViewById(R.id.text_subject);
            TextView mClassRoomTextView = (TextView)cardView.findViewById(R.id.text_classRoom);
            mSubjectTextView.setText(subStr);
            mClassRoomTextView.setText(roomStr);

            containerLayout.addView(cardView);

            //各ブロックのClickイベント
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewParent parentLayout =  cardView.getParent();
//                    parentLayout

                    TextView mSubjectTextView = (TextView)cardView.findViewById(R.id.text_subject);
                    String subjectName = mSubjectTextView.getText().toString();

                    Toast.makeText(getActivity(),subjectName,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private static Point getDisplaySize(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    private static ImageButton getNavButtonView(Toolbar toolbar)
    {
        for (int i = 0; i < toolbar.getChildCount(); i++)
            if(toolbar.getChildAt(i) instanceof ImageButton)
                return (ImageButton) toolbar.getChildAt(i);

        return null;
    }



}
