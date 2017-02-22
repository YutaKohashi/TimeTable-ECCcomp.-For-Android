package jp.yuta.kohashi.esc.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by yutakohashi and Shion T. Fujie on 2017/02/08.
 */

public class SynchronizedViewPager extends ViewPager {
    private boolean touched;
    private Action1<Action1<SynchronizedViewPager>> eventInvoker;

    public SynchronizedViewPager(Context context) {
        super(context);
    }

    public void setTargetViewPager(SynchronizedViewPager customPager) {
        eventInvoker = invoker -> { if(!touched) invokeEvent(customPager, invoker); };
        this.addOnPageChangeListener(new FixSynchronizedViewPager( customPager));
        ((ViewGroup)this.getRootView()).setMotionEventSplittingEnabled(false); // disable multitouch on RootView
    }

    private  void invokeEvent(SynchronizedViewPager target, Action1<SynchronizedViewPager> invoker){
        target.setTouched(true);
        invoker.apply(target);
        target.setTouched(false);
    }

    public SynchronizedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        eventInvoker.apply(pager -> pager.onInterceptTouchEvent(ev));
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        eventInvoker.apply(pager -> pager.onTouchEvent(ev));
        return super.onTouchEvent(ev);
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
        eventInvoker.apply(pager -> pager.setCurrentItem(item,smoothScroll));
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        eventInvoker.apply(pager -> pager.setCurrentItem(item));
        super.setCurrentItem(item);
    }

    private class FixSynchronizedViewPager implements ViewPager.OnPageChangeListener{

        ViewPager target;

        FixSynchronizedViewPager(ViewPager target){
            this.target = target;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state == ViewPager.SCROLL_STATE_IDLE){
                if(SynchronizedViewPager.this.getCurrentItem() != target.getCurrentItem()){
                    target.setCurrentItem(SynchronizedViewPager.this.getCurrentItem());
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageSelected(int position) {}
    }

    /**
     *  an interface to use lambda expression.
     */
    interface Action0{
        void apply();
    }

    interface Action1<T> {
        void apply(T pager);
    }
}
