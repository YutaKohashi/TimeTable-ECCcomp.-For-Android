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
    private boolean forSuper;

    public SynchronizedViewPager(Context context) {
        super(context);
    }

    public SynchronizedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!forSuper) {
            mCustomPager.forSuper(true);
            mCustomPager.onInterceptTouchEvent(ev);
            mCustomPager.forSuper(false);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!forSuper) {
            mCustomPager.forSuper(true);
            mCustomPager.onTouchEvent(ev);
            mCustomPager.forSuper(false);
        }

        return super.onTouchEvent(ev);
    }

    public void setTargetViewPager(SynchronizedViewPager customPager) {
        mCustomPager = customPager;
    }

    public void forSuper(boolean forSuper) {
        this.forSuper = forSuper;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (!forSuper) {
            mCustomPager.forSuper(true);
            mCustomPager.setCurrentItem(item, smoothScroll);
            mCustomPager.forSuper(false);
        }
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (!forSuper) {
            mCustomPager.forSuper(true);
            mCustomPager.setCurrentItem(item);
            mCustomPager.forSuper(false);
        }
        super.setCurrentItem(item);
    }
}
