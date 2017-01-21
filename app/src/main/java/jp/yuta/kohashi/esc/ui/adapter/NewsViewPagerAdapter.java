package jp.yuta.kohashi.esc.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import jp.yuta.kohashi.esc.model.NewsModel;
import jp.yuta.kohashi.esc.ui.fragment.NewsListFragment;

/**
 * Created by Yuta on 2016/06/18.
 */
//タブに関するアダプタ
public class NewsViewPagerAdapter extends FragmentPagerAdapter {

    private static final int TAB_COUNT = 2;
    private List<NewsModel> schoolNews;
    private List<NewsModel> tanninNews;

    public NewsViewPagerAdapter(FragmentManager fm, List<NewsModel> schoolNews, List<NewsModel> tanninNews) {
        super(fm);
        this.schoolNews = schoolNews;
        this.tanninNews = tanninNews;
    }

    //フラグメントによって変更する
    @Override
    public Fragment getItem(int position) {
        NewsListFragment fragment = null;
        switch(position){
            case 0:
                fragment =   new NewsListFragment();
                fragment.setItems(schoolNews);
                fragment.setContains(0);
                break;
            case 1:
                fragment =  new NewsListFragment();
                fragment.setItems(tanninNews);
                fragment.setContains(1);
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";

        switch (position){
            case 0:
                title = "学校から";
                break;
            case 1:
                title = "担任から";
        }
        return title;
    }
}

