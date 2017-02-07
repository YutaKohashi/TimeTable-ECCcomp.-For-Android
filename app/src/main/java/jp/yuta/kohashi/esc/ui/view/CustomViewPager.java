package jp.yuta.kohashi.esc.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yutakohashi on 2017/01/31.
 */

public class CustomViewPager extends ViewPager {

    boolean isSwipe = false;
    public CustomViewPager(Context context) {
        super(context);
    }
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * スワイプによるページ切り替え有効/無効設定
     */
    public void setSwipeEnable(boolean enable) {
        isSwipe = enable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ( !isSwipe ) return false;
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)  {
        if ( !isSwipe ) return false;
        return super.onInterceptTouchEvent(event);
    }
}
