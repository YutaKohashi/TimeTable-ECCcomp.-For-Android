package jp.yuta.kohashi.esc.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.adapter.NewsRecyclerViewAdapter;
import jp.yuta.kohashi.esc.http.GetNewsManager;
import jp.yuta.kohashi.esc.object.NewsChildListItem;
import jp.yuta.kohashi.esc.object.NewsParentListItem;
import jp.yuta.kohashi.esc.preference.LoadManager;

public class NewsFragment extends Fragment{

    static final String PREF_NAME_ID_PASS = "ip";
    static final String PREF_NAME ="sample";

    TextView textView;
//    private ExpadableListAdapter expAdapter;
    private List<NewsChildListItem> expListItems;
//    private AnimatedExpandableListView mExpandList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    GetNewsManager getNewsManager;

    RecyclerView recyclerView;
    NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;
    LoadManager loadmanager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_news_part2, container, false);

        //タイトルの設定
        textView = (TextView)getActivity().findViewById(R.id.title_name_text);
        textView.setText("お知らせ");

        loadmanager = new LoadManager();
        expListItems = new ArrayList<>();
        expListItems = loadmanager.loadManagerWithPreferenceForNews(getActivity(),PREF_NAME,"newsList");
        //リサイクラーViewに設定する処理

        try {
            expListItems.size();
        }catch(NullPointerException ex){
            NewsChildListItem item = new NewsChildListItem();
            item.setTitle("コンテンツがありません。更新してください。");
            expListItems = new ArrayList<>();
            expListItems.add(item);
        }
        recyclerView = (RecyclerView) v.findViewById(R.id.news_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);



        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(expListItems, getActivity());
        recyclerView.setAdapter(newsRecyclerViewAdapter);


        getNewsManager = new GetNewsManager();
        // SwipeRefreshLayoutの設定
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_news);
        //swipeTorefresh使用時のリスナー
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<String> list = new ArrayList<String>();
                LoadManager loadManager = new LoadManager();
                list = loadManager.loadManagerWithPreferenceForString(getActivity(),PREF_NAME_ID_PASS,"ip");

                String userId = list.get(0);
                String pass = list.get(1);
                getNewsManager.getNews(getActivity(),v,userId,pass);
            }
        });

        //setColorSchemeResources   推奨
        //setColorScheme            非推奨
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary1, R.color.colorPrimary2, R.color.colorPrimary6, R.color.colorPrimary7);

        new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                mSwipeRefreshLayout.setEnabled(i == 0);
            }
        };

//        mExpandList = (AnimatedExpandableListView)v.findViewById(R.id.expandable_list_view);
//        expListItems = SetStandardGroups();
//        expAdapter = new ExpadableListAdapter(getActivity(), expListItems);
//        mExpandList.setAdapter(expAdapter);

//        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//
//        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(rateObjectList,getActivity());
//        recyclerView.setAdapter(newsRecyclerViewAdapter);

//        //アニメーション
//        mExpandList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                if (mExpandList.isGroupExpanded(groupPosition)) {
//                    mExpandList.collapseGroupWithAnimation(groupPosition);
//                } else {
//                    mExpandList.expandGroupWithAnimation(groupPosition);
//                }
//                return true;
//            }
//        });




        return v;
    }


    //スタブ
    private ArrayList<NewsParentListItem> SetStandardGroups() {
        ArrayList<NewsParentListItem> group_list = new ArrayList<NewsParentListItem>();
        ArrayList<NewsChildListItem> child_list;

        // Setting Group 1
        child_list = new ArrayList<NewsChildListItem>();
        NewsParentListItem gru1 = new NewsParentListItem();
        gru1.setTitle("Apple");

        NewsChildListItem ch1_1 = new NewsChildListItem();
        ch1_1.setTitle("Iphone");
        child_list.add(ch1_1);

        NewsChildListItem ch1_2 = new NewsChildListItem();
        ch1_2.setTitle("ipad");
        child_list.add(ch1_2);

        NewsChildListItem ch1_3 = new NewsChildListItem();
        ch1_3.setTitle("ipod");
        child_list.add(ch1_3);

        gru1.setChildItems(child_list);

        // Setting Group 2
        child_list = new ArrayList<NewsChildListItem>();
        NewsParentListItem gru2 = new NewsParentListItem();
        gru2.setTitle("SAMSUNG");

        NewsChildListItem ch2_1 = new NewsChildListItem();
        ch2_1.setTitle("Galaxy Grand");
        child_list.add(ch2_1);

        NewsChildListItem ch2_2 = new NewsChildListItem();
        ch2_2.setTitle("Galaxy Note");
        child_list.add(ch2_2);

        NewsChildListItem ch2_3 = new NewsChildListItem();
        ch2_3.setTitle("Galaxy Mega");
        child_list.add(ch2_3);

        NewsChildListItem ch2_4 = new NewsChildListItem();
        ch2_4.setTitle("Galaxy Neo");
        child_list.add(ch2_4);

        gru2.setChildItems(child_list);

        //listing all groups
        group_list.add(gru1);
        group_list.add(gru2);

        return group_list;
    }

    public void showToastMsg(String Msg) {
        Toast.makeText(getActivity(), Msg, Toast.LENGTH_SHORT).show();
    }
}