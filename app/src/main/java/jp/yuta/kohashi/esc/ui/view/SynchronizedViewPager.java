package jp.yuta.kohashi.esc.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by yutakohashi and Shion T. Fujie on 2017/02/08.
 */

/**
 *
 */
public class SynchronizedViewPager extends ViewPager {
    /**
     * receiving an action of synchronized view pager, it applies the action to the
     * sychronized view pager initialized in constructors or the method setTargetViewPager.
     */
    private Action1<Action1<SynchronizedViewPager>> eventInvoker;

    public SynchronizedViewPager(Context context, SynchronizedViewPager target){
        this(context);
        eventInvoker = invoker -> invoker.apply(target);
        ((ViewGroup)this.getRootView()).setMotionEventSplittingEnabled(false); // disable multitouch on RootView
    }

    public SynchronizedViewPager(Context context) {
        super(context);
    }

    public SynchronizedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SynchronizedViewPager setTargetViewPager(SynchronizedViewPager customPager) {
        eventInvoker = invoker -> invoker.apply(customPager);
        ((ViewGroup)this.getRootView()).setMotionEventSplittingEnabled(false); // disable multitouch on RootView
        return  this;
    }

    /**
     * Note: To prevent the recursive call, it calls a helper method onInterceptTouchEventOnly internally.
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        eventInvoker.apply(pager -> pager.onInterceptTouchEventOnly(ev));
        return super.onInterceptTouchEvent(ev);
    }

    /** This method is merely for invoking the non-overriden onInterceptTouchEvent
     * ,namely the super class's method,instead of calling the overriden one.
     *
     * @param ev
     * @return
     */
    public boolean onInterceptTouchEventOnly(MotionEvent ev){
        return super.onInterceptTouchEvent(ev);
    }


    /**
     * Note: To prevent the recursive call, it calls a helper method onTouchEventOnly internally.
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        eventInvoker.apply(pager -> pager.onTouchEventOnly(ev));
        return super.onTouchEvent(ev);
    }

    /** This method is merely for invoking the non-overriden onTouchEvent
     * ,namely the super class's method,instead of calling the overriden one.
     *
     * @param ev
     * @return
     */
    public boolean onTouchEventOnly(MotionEvent ev){
        return super.onTouchEvent(ev);
    }

    /**
     * 
     * @param item
     */
    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    /**
     * Note: To prevent the recursive call, it calls a helper method setCurrentItemOnly internally.
     * @param item
     */
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        eventInvoker.apply(pager -> pager.setCurrentItemOnly(item, smoothScroll));
        super.setCurrentItem(item, smoothScroll);
    }

    /** This method is merely for invoking the non-overriden onTouchEvent
     * ,namely the super class's method,instead of calling the overriden one.
     * @param item
     */
    public void setCurrentItemOnly(int item, boolean smoothScroll){
        super.setCurrentItem(item, smoothScroll);
    }

    /**
     * an interface to use lambda expression.
     */

    interface Action1<T> {
        void apply(T pager);
    }
}
