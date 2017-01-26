package jp.yuta.kohashi.esc.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.TimeBlockModel;
import jp.yuta.kohashi.esc.ui.adapter.TimeTableRecyclerAdapter;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeTableFragment extends Fragment implements View.OnClickListener{

    private List<TimeBlockModel> monList;
    private List<TimeBlockModel> tueList;
    private List<TimeBlockModel> wedList;
    private List<TimeBlockModel> thurList;
    private List<TimeBlockModel> friList;

    private RecyclerView mMonRecyclerView;
    private RecyclerView mTueRecyclerView;
    private RecyclerView mWedRecyclerView;
    private RecyclerView mThurRecyclerView;
    private RecyclerView mFriRecyclerView;

    //BottomSheet
    private RelativeLayout mBottomSheet;
    private TextView mSubjectTextView;
    private TextView mTeacherTextView;
    private TextView mTimeTextView;
    private Animation inAnim;
    private Animation outAnim;
    private Animation fadeInAnim;
    private Animation fadeOutAnim;
    private FrameLayout mCloseView;

    private List<TimeTableRecyclerAdapter> mAdapters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);

        mAdapters = new ArrayList<>();
        initView(view);

        return view;
    }

    private void initView(View view) {
        loadLists();

        mMonRecyclerView = (RecyclerView) view.findViewById(R.id.mon_col);
        mTueRecyclerView = (RecyclerView) view.findViewById(R.id.tue_col);
        mWedRecyclerView = (RecyclerView) view.findViewById(R.id.wed_col);
        mThurRecyclerView = (RecyclerView) view.findViewById(R.id.thur_col);
        mFriRecyclerView = (RecyclerView) view.findViewById(R.id.fri_col);

        mMonRecyclerView.setHasFixedSize(true);
        mTueRecyclerView.setHasFixedSize(true);
        mWedRecyclerView.setHasFixedSize(true);
        mThurRecyclerView.setHasFixedSize(true);
        mFriRecyclerView.setHasFixedSize(true);

        createRecyclerView(mMonRecyclerView, monList);
        createRecyclerView(mTueRecyclerView, tueList);
        createRecyclerView(mWedRecyclerView, wedList);
        createRecyclerView(mThurRecyclerView, thurList);
        createRecyclerView(mFriRecyclerView, friList);

        //BottomSheetView
        mBottomSheet = (RelativeLayout)view.findViewById(R.id.bottom_sheet_view);
        mSubjectTextView = (TextView)view.findViewById(R.id.txt_subject);
        mTeacherTextView = (TextView)view.findViewById(R.id.txt_teacher);
        mTimeTextView = (TextView)view.findViewById(R.id.txt_time);
        mCloseView = (FrameLayout)view.findViewById(R.id.close_view);
        mCloseView.setOnClickListener(this);
        inAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_bottom_sheet_in);
        outAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_bottom_sheet_out);
        fadeInAnim = AnimationUtils.loadAnimation(getContext(),R.anim.anim_bottom_sheet_fade_in);
    }

    private void createRecyclerView(RecyclerView recyclerView, List<TimeBlockModel> list) {
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        TimeTableRecyclerAdapter adapter = new TimeTableRecyclerAdapter(list, R.color.bg_classroom_green ,getActivity()) {
            @Override
            protected void onItemClicked(@NonNull List<TimeBlockModel> items, TimeBlockModel model) {
                super.onItemClicked(items, model);
                if(TextUtils.isEmpty(model.getSubject())) return;
                mSubjectTextView.setText(model.getSubject());
                mTeacherTextView.setText(model.getTeacherName());
                mTimeTextView.setText(getRoom(model.getRowNum()));

                mBottomSheet.startAnimation(inAnim);
                mBottomSheet.setVisibility(View.VISIBLE);
                mBottomSheet.startAnimation(inAnim);
                mCloseView.setVisibility(View.VISIBLE);
                mCloseView.startAnimation(fadeInAnim);
            }

        };
        mAdapters.add(adapter);
        recyclerView.setAdapter(adapter);
    }

    private void swapAll() {
        List<List<TimeBlockModel>> lists = PrefUtil.loadTimeBlockList();
        for (int i = 0; i < 5; i++) {
            mAdapters.get(i).swap(lists.get(i));
        }
    }

    @NonNull
    private String getRoom(int i){
        switch(i){
            case 0:
                return getResources().getString(R.string.time_zero_class);
            case 1:
                return getResources().getString(R.string.time_one_class);
            case 2:
                return getResources().getString(R.string.time_two_class);
            case 3:
                return getResources().getString(R.string.time_three_class);
            case 4:
                return getResources().getString(R.string.time_four_class);
            case 5:
                return getResources().getString(R.string.time_five_class);
            default:
                return "";
        }
    }

    private void loadLists() {
        List<List<TimeBlockModel>> lists = PrefUtil.loadTimeBlockList();
        monList = lists.get(0);
        tueList = lists.get(1);
        wedList = lists.get(2);
        thurList = lists.get(3);
        friList = lists.get(4);
    }

    @Override
    public void onClick(View view) {
        // Button For Close BottomSheet
        if(view.getId() == R.id.close_view){
            // require every times
            fadeOutAnim = AnimationUtils.loadAnimation(getContext(),R.anim.anim_bottom_sheet_fade_out);

            mBottomSheet.startAnimation(outAnim);
            mBottomSheet.setVisibility(View.INVISIBLE);
            mCloseView.setVisibility(View.INVISIBLE);
            mCloseView.setAnimation(fadeOutAnim);
        }
    }

    /**
     * RecyclerViewのレイアウトマネージャにScroll無効化処理を追加
     */
    public class CustomLinearLayoutManager extends LinearLayoutManager {
        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }
        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        swapAll();
    }
}
