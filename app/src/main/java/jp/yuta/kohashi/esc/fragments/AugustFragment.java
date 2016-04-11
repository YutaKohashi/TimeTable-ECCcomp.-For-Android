package jp.yuta.kohashi.esc.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.tools.CreateCalenderManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class AugustFragment extends Fragment {

    static final int MONTH = 8;

    public AugustFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_august, container, false);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            Calendar cal = Calendar.getInstance();

            CreateCalenderManager createCalenderManager = new CreateCalenderManager();
            createCalenderManager.createCalender(cal.get(Calendar.YEAR), MONTH, getActivity());

        }
    }
}
