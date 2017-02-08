package jp.yuta.kohashi.esc.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yutakohashi on 2017/02/08.
 */

public class SynchronizedViewPager extends ViewPager {
    SynchronizedViewPager mCustomPager;
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
            mCustomPager.setTouched(true);
            mCustomPager.onInterceptTouchEvent(ev);
            mCustomPager.setTouched(false);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!touched) {
            mCustomPager.setTouched(true);
            mCustomPager.onTouchEvent(ev);
            mCustomPager.setTouched(false);
        }

        return super.onTouchEvent(ev);
    }

    public void setTargetViewPager(SynchronizedViewPager customPager) {
        mCustomPager = customPager;
        this.addOnPageChangeListener(new FixSynchronizedViewPager(this,mCustomPager));
    }

    /**
     * 同期ターゲットをセットする
     * @param touched
     */
    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (!touched) {
            mCustomPager.setTouched(true);
            mCustomPager.setCurrentItem(item, smoothScroll);
            mCustomPager.setTouched(false);
        }
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (!touched) {
            mCustomPager.setTouched(true);
            mCustomPager.setCurrentItem(item);
            mCustomPager.setTouched(false);
        }
        super.setCurrentItem(item);
    }

    private class FixSynchronizedViewPager implements ViewPager.OnPageChangeListener{

        ViewPager own;
        ViewPager target;

        FixSynchronizedViewPager(ViewPager own, ViewPager target){
            this.own = own;
            this.target = target;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state == ViewPager.SCROLL_STATE_IDLE){
                if(own.getCurrentItem() != target.getCurrentItem()){
                    target.setCurrentItem(own.getCurrentItem());
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageSelected(int position) {}
    }
}
