package jp.yuta.kohashi.esc.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.adapter.SyllabusRecyclerAdapter;
import jp.yuta.kohashi.esc.object.SyllabusItem;
import jp.yuta.kohashi.esc.tools.CreateCalenderManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class AprilFragment extends Fragment {

    static final int MONTH = 4;

    LinearLayout callenderArea;
    TextView textView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SyllabusRecyclerAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_april, container, false);
        List<SyllabusItem> list= new ArrayList<>();

        for(int i =0;i < 30;i++){
            SyllabusItem item = new SyllabusItem();
            item.setDateText(String.valueOf(i) + "日");
            item.setContaintsText("運動会");
            list.add(item);
        }

        recyclerView = (RecyclerView)v.findViewById(R.id.april_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SyllabusRecyclerAdapter(list,getActivity());
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Calendar cal = Calendar.getInstance();

        if (isVisibleToUser) {
            CreateCalenderManager createCalenderManager = new CreateCalenderManager();
            createCalenderManager.createCalender(cal.get(Calendar.YEAR),MONTH,getActivity());


        }
    }
}
