package jp.yuta.kohashi.esc.ui.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jp.yuta.kohashi.esc.R;

/**
 * Created by yutakohashi on 2017/02/17.
 */

public class TimeTableBottomSheet extends RelativeLayout implements Animation.AnimationListener, View.OnClickListener {
    private View view;
    private View mCloseView;

    private RelativeLayout mBottomSheetView;
    private TextView mSubjectTextView;
    private TextView mTeacherTextView;
    private TextView mTimeTextView;

    private Animation inAnim;
    private Animation outAnim;
    private Animation fadeInAnim;
    private Animation fadeOutAnim;

    private Builder builder;

    public final Builder getBuilder() {
        return builder;
    }

    public TimeTableBottomSheet(Builder builder) {
        this(builder.context);
        this.builder = builder;
        initView();
    }

    public TimeTableBottomSheet(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.view_time_table_bottom_sheet, this);
    }

    /**
     * show BottomSheet
     */
    public void show() {
        mBottomSheetView.startAnimation(inAnim);
        mBottomSheetView.setVisibility(View.VISIBLE);
        mBottomSheetView.startAnimation(inAnim);
        mCloseView.setVisibility(View.VISIBLE);
        mCloseView.startAnimation(fadeInAnim);
    }

    /**
     * close BottomSheet
     */
    public void Dismiss() {
        fadeOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_bottom_sheet_fade_out);
        mBottomSheetView.startAnimation(outAnim);
        mBottomSheetView.setVisibility(View.INVISIBLE);
        mCloseView.setVisibility(View.INVISIBLE);
        mCloseView.setAnimation(fadeOutAnim);
        fadeOutAnim.setAnimationListener(this);
    }

    private void initView() {
        mSubjectTextView = (TextView) view.findViewById(R.id.txt_subject);
        mTeacherTextView = (TextView) view.findViewById(R.id.txt_teacher);
        mTimeTextView = (TextView) view.findViewById(R.id.txt_time);

        mSubjectTextView.setText(builder.subject);
        mTeacherTextView.setText(builder.teacher);
        mTimeTextView.setText(builder.time);

        mBottomSheetView = (RelativeLayout) view.findViewById(R.id.bottom_sheet_view);
        mBottomSheetView.setVisibility(INVISIBLE);
        mCloseView = view.findViewById(R.id.close_view);
        mCloseView.setVisibility(INVISIBLE);
        mCloseView.setOnClickListener(this);

        inAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_bottom_sheet_in);
        outAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_bottom_sheet_out);
        fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_bottom_sheet_fade_in);
        ((ViewGroup)builder.containerViewGroup).addView(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        this.removeAllViews();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onClick(View view) {
        Dismiss();
    }


    public static class Builder {
        Context context;
        View containerViewGroup;
        String subject;
        String teacher;
        String time;

        public Builder(Fragment fragment) {
            this.context = fragment.getContext();
            containerViewGroup = fragment.getView();
        }

        public Builder(Activity activity) {
            this.context = activity.getApplicationContext();
            containerViewGroup = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder teacher(String teacher) {
            this.teacher = teacher;
            return this;
        }

        public Builder time(String time) {
            this.time = time;
            return this;
        }

        public TimeTableBottomSheet build() {
            TimeTableBottomSheet bottomSheet = new TimeTableBottomSheet(this);
            return bottomSheet;
        }
    }

}
