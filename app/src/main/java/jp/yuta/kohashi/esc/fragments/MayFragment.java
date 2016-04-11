package jp.yuta.kohashi.esc.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.tools.CreateCalenderManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MayFragment extends Fragment {

    static final int MONTH = 5;

    public MayFragment() {
        // Required empty public constructor
    }

    LinearLayout callenderArea;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_may, container, false);

        return v;
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
