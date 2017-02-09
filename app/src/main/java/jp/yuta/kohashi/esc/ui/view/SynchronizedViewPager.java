package jp.yuta.kohashi.esc.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yutakohashi on 2017/02/08.
 */

public class SynchronizedViewPager extends ViewPager {
    SynchronizedViewPager target;
    private boolean touched;

    public SynchronizedViewPager(Context context) {
        super(context);
    }

    public SynchronizedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!touched) {
            target.setTouched(true);
            target.onInterceptTouchEvent(ev);
            target.setTouched(false);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!touched) {
            target.setTouched(true);
            target.onTouchEvent(ev);
            target.setTouched(false);
        }

        return super.onTouchEvent(ev);
    }

    public void setTargetViewPager(SynchronizedViewPager customPager) {
        target = customPager;
        this.addOnPageChangeListener(new FixSynchronizedViewPager(this, target));
    }

    /**
     * 同期ターゲットをセットする
     * @param touched
     */
    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public boolean isTouched(){
        return this.touched;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (!touched) {
            target.setTouched(true);
            target.setCurrentItem(item, smoothScroll);
            target.setTouched(false);
        }
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (!touched) {
            target.setTouched(true);
            target.setCurrentItem(item);
            target.setTouched(false);
        }
        super.setCurrentItem(item);
    }

    private class FixSynchronizedViewPager implements ViewPager.OnPageChangeListener{

        ViewPager thisView;
        ViewPager target;

        FixSynchronizedViewPager(ViewPager thisView, ViewPager target){
            this.thisView = thisView;
            this.target = target;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state == ViewPager.SCROLL_STATE_IDLE){
                if(thisView.getCurrentItem() != target.getCurrentItem()){
                    target.setCurrentItem(thisView.getCurrentItem());
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageSelected(int position) {}
    }
}
