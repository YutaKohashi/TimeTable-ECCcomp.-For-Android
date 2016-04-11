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
public class JanuaryFragment extends Fragment {

    static final int MONTH = 1;

    public JanuaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_january, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Calendar cal = Calendar.getInstance();

        if (isVisibleToUser) {
            CreateCalenderManager createCalenderManager = new CreateCalenderManager();
            createCalenderManager.createCalender(cal.get(Calendar.YEAR) + 1, MONTH, getActivity());

        }
    }

}
