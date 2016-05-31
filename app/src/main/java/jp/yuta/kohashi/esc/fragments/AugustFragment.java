package jp.yuta.kohashi.esc.fragments;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.adapter.SyllabusRecyclerAdapter;
import jp.yuta.kohashi.esc.object.ScheduleJJsonObject;
import jp.yuta.kohashi.esc.tools.CreateCalenderManager;
import jp.yuta.kohashi.esc.tools.GetValuesBase;

/**
 * A simple {@link Fragment} subclass.
 */
public class AugustFragment extends Fragment {

    static final int MONTH = 8;

    public AugustFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SyllabusRecyclerAdapter adapter;
    List<ScheduleJJsonObject> schedulelist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_august, container, false);

        GetValuesBase getValuesBase = new GetValuesBase();

        String fileName = "2016Schedule.json";  // "assets/res/data/sample.txt" となる
        String text = "空";  //this は起動した Activity が良い(Context)
        try {
            text = getValuesBase.loadTextAsset(fileName, getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.d("json:::", text);
        //該当の月を取り出し
        text = text.replace("\n","");
        text = text.replace("\t","");
        String jsonText = getValuesBase.NarrowingValuesforJson("\"August\":[","],\"September\":",text,false);
//        Log.d("json:::",jsonText);

        //＊＊＊＊＊＊＊＊＊この時点でAprilのJsonデータがjsonTExtに入っている＊＊＊＊＊＊＊＊＊
        //カスタムオブジェクトScheduleJsonObjectを作成
        Gson gson = new Gson();
        ScheduleJJsonObject[] objects = gson.fromJson(jsonText,ScheduleJJsonObject[].class);

        //Log.d("test;;;;",objects.toString());

        schedulelist = new ArrayList<>();
        schedulelist = getValuesBase.convertArrToCollection(objects);

        recyclerView = (RecyclerView)v.findViewById(R.id.august_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SyllabusRecyclerAdapter(schedulelist,getActivity());
        recyclerView.setAdapter(adapter);

        return v;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Calendar cal = Calendar.getInstance();
        if (isVisibleToUser) {

            Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.callender_toolbar);
            toolbar.setTitle(getResources().getString(R.string.schedule));

            GetValuesBase getValuesBase = new GetValuesBase();

            String fileName = "2016Schedule.json";  // "assets/res/data/sample.txt" となる
            String text = "空";  //this は起動した Activity が良い(Context)
            try {
                text = getValuesBase.loadTextAsset(fileName, getActivity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.d("json:::", text);
            //該当の月を取り出し
            text = text.replace("\n","");
            text = text.replace("\t","");
            String jsonText = getValuesBase.NarrowingValuesforJson("\"August\":[","],\"September\":",text,false);

            Gson gson = new Gson();
            ScheduleJJsonObject[] objects = gson.fromJson(jsonText,ScheduleJJsonObject[].class);

            //Log.d("test;;;;",objects.toString());

            schedulelist = new ArrayList<>();
            schedulelist = getValuesBase.convertArrToCollection(objects);

            List<String> dateList = new ArrayList<>();
            for(ScheduleJJsonObject sobj:schedulelist){
                dateList.add(sobj.getDate());
            }
            CreateCalenderManager createCalenderManager = new CreateCalenderManager();
            createCalenderManager.createCalender(cal.get(Calendar.YEAR), MONTH, getActivity(),dateList);

        }
    }
}
