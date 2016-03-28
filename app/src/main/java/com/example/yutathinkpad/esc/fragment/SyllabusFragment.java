package com.example.yutathinkpad.esc.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yutathinkpad.esc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyllabusFragment extends Fragment {


    public SyllabusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_syllabus, container, false);
    }

}
