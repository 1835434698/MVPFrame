package com.tangzy.mvpframe.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/10/31.
 */

public class CanForbidViewPager extends ViewPager {

    private boolean isSlide = true;
    private EventListener eventListener;

    public CanForbidViewPager(Context context) {
        super(context);
    }

    public CanForbidViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(eventListener != null){
                    eventListener.onDown(this, ev);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(eventListener != null){
                    eventListener.onUp(this, ev);
                }
                break;
        }

        if(isSlide){
            return super.dispatchTouchEvent(ev);
        }
        return isSlide;
    }


    public void setSlide(boolean isSlide) {
        this.isSlide = isSlide;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }



    public interface EventListener{
        void onDown(ViewPager viewPager, MotionEvent ev);
        void onUp(ViewPager viewPager, MotionEvent ev);
    }
}
