package jp.yuta.kohashi.esc.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.model.TimeBlockItem;
import jp.yuta.kohashi.esc.ui.view.TimeTableView;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeTableFragment extends Fragment implements View.OnClickListener , TimeTableView.OnCellClickListener{

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

    private TimeTableView mTimeTableView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimeTableView.setData(PrefUtil.loadTimeBlockList());
    }

    private void initView(View view) {
        mTimeTableView = new TimeTableView(getActivity());
        mTimeTableView.setClassRoomColor(R.color.bg_classroom_green);
        mTimeTableView.setOnCellClickListener(this);
        ((FrameLayout) view.findViewById(R.id.container)).addView(mTimeTableView);

        //BottomSheetView
        mBottomSheet = (RelativeLayout)view.findViewById(R.id.bottom_sheet_view);
        mBottomSheet.setVisibility(View.INVISIBLE);
        mSubjectTextView = (TextView)view.findViewById(R.id.txt_subject);
        mTeacherTextView = (TextView)view.findViewById(R.id.txt_teacher);
        mTimeTextView = (TextView)view.findViewById(R.id.txt_time);
        mCloseView = (FrameLayout)view.findViewById(R.id.close_view);
        mCloseView.setOnClickListener(this);
        mCloseView.setVisibility(View.INVISIBLE);
        inAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_bottom_sheet_in);
        outAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_bottom_sheet_out);
        fadeInAnim = AnimationUtils.loadAnimation(getContext(),R.anim.anim_bottom_sheet_fade_in);
    }

    /**
     * セルクリック時
     * @param items
     * @param model
     */
    @Override
    public void onCellClick(List<TimeBlockItem> items, TimeBlockItem model) {
        if (TextUtils.isEmpty(model.getSubject())) return;
        mSubjectTextView.setText(model.getSubject());
        mTeacherTextView.setText(model.getTeacherName());
        mTimeTextView.setText(getRoom(model.getRowNum()));

        mBottomSheet.startAnimation(inAnim);
        mBottomSheet.setVisibility(View.VISIBLE);
        mBottomSheet.startAnimation(inAnim);
        mCloseView.setVisibility(View.VISIBLE);
        mCloseView.startAnimation(fadeInAnim);
    }

    /**
     * close bottomSheet
     * @param view
     */
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
}
