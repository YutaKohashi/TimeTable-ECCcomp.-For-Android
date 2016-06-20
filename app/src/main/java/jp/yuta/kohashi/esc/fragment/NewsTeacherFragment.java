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

public class NewsTeacherFragment extends Fragment{

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

        final View v = inflater.inflate(R.layout.fragment_news_teacher, container, false);

        //タイトルの設定
//        textView = (TextView)getActivity().findViewById(R.id.title_name_text);
//        textView.setText("お知らせ");

        loadmanager = new LoadManager();
        expListItems = new ArrayList<>();
        expListItems = loadmanager.loadManagerWithPreferenceForNews(getActivity(),PREF_NAME,"newsTeacherList");
        //リサイクラーViewに設定する処理

        try {
            int size = expListItems.size();

            if(size == 0){
                throw new NullPointerException("");
            }
        }catch(NullPointerException ex){
            NewsChildListItem item = new NewsChildListItem();
            item.setTitle("コンテンツがありません。更新してください。");
            expListItems = new ArrayList<>();
            expListItems.add(item);
        }
        recyclerView = (RecyclerView) v.findViewById(R.id.news_recycler_view_teacher);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);



        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(expListItems, getActivity());
        recyclerView.setAdapter(newsRecyclerViewAdapter);


        getNewsManager = new GetNewsManager();
        // SwipeRefreshLayoutの設定
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_news_teacher);
        //swipeTorefresh使用時のリスナー
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<String> list = new ArrayList<String>();
                LoadManager loadManager = new LoadManager();
                list = loadManager.loadManagerWithPreferenceForString(getActivity(),PREF_NAME_ID_PASS,"ip");

                String userId = list.get(0);
                String pass = list.get(1);
                getNewsManager.getNewsTeacher(getActivity(),v,userId,pass);
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

        return v;
    }

}