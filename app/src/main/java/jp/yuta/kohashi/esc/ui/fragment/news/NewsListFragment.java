package jp.yuta.kohashi.esc.ui.fragment.news;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.yuta.kohashi.esc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends Fragment {


    public NewsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_news_list, container, false);

//        FrameLayout frameLayout = (FrameLayout)v.findViewById(R.id.fragment_news_list);
//        CalendarView calendarView = new CalendarView(getContext(),null);
//        calendarView.setMonth(9);
//        frameLayout.addView(calendarView);
        return v;
    }

}
