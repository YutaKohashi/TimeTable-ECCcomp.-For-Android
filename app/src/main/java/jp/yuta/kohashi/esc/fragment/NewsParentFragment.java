package jp.yuta.kohashi.esc.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.adapter.DesignPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsParentFragment extends Fragment {

    ViewPager viewPager;

    public NewsParentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_news_parent, container, false);


        DesignPagerAdapter adapter = new DesignPagerAdapter(getChildFragmentManager());
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(Color.GRAY,Color.WHITE);



        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DesignPagerAdapter adapter = new DesignPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
    }

    public void setNum(){

    }


}
