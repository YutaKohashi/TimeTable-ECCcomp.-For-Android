package jp.yuta.kohashi.esc.ui.fragment.news;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.NewsModel;
import jp.yuta.kohashi.esc.ui.adapter.NewsViewPagerAdapter;
import jp.yuta.kohashi.esc.manager.preference.PrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsParentFragment extends Fragment {

    ViewPager mViewPager;
    List<NewsModel> schoolNews;
    List<NewsModel> tanninNews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_news_parent, container, false);

        schoolNews = PrefManager.loadSchoolNewsList();
        tanninNews = PrefManager.loadTanninNewsList();
        NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getChildFragmentManager(),schoolNews,tanninNews);
        mViewPager = (ViewPager)v.findViewById(R.id.news_viewpager);
        mViewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)v.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.setTabTextColors(Color.GRAY,Color.WHITE);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getChildFragmentManager(),schoolNews,tanninNews);
        mViewPager.setAdapter(adapter);
    }
}
