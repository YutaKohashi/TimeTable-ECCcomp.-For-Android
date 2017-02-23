package jp.yuta.kohashi.esc.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

/**
 * Created by yutakohashi on 2017/01/17.
 */

public class ScrollController implements RecyclerView.OnItemTouchListener {

    private boolean isDisableScroll;

    public ScrollController() {
    }

    public void disableScroll() {
        isDisableScroll = true;
    }

    public void enableScroll() {
        isDisableScroll = false;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return isDisableScroll;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}