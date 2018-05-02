package com.codido.nymeria.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.codido.nymeria.util.Global;

/**
 * banner的viewpager
 */
public class BannerViewPager extends ViewPager {

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {

        return super.onTouchEvent(arg0);
    }

    private SwipeRefreshLayout refreshLayout;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret = super.dispatchTouchEvent(ev);

        if (ret) {
            requestDisallowInterceptTouchEvent(true);
        }

        long now = System.currentTimeMillis();
        if (x == 0 && y == 0) {
            x = ev.getX();
            y = ev.getY();
            time = now;

            return ret;
        }

        if (Math.abs(y - ev.getY()) > 10 || Math.abs(x - ev.getX()) > 10) {
            isMoved = true;
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
             if (!isMoved && now - time < 500L) {
                if (onItemClickListner != null && getChildCount() > 0) {
                    onItemClickListner.onItemClick(getCurrentItem());
                }
                Global.debug("被点击了哦...." + getChildCount() + "," + getCurrentItem());
            }
            isMoved = false;
            x = y = 0;
            time = 0;
        } else if (ev.getAction() == MotionEvent.ACTION_CANCEL) {
            Global.debug("MotionEvent.ACTION_CANCEL");
            isMoved = false;

            x = y = 0;
            time = 0;
        }

        return ret;
    }

    public static interface OnItemClickListner {
        public void onItemClick(int index);
    }

    OnItemClickListner onItemClickListner;

    private long time;
    private float x;
    private float y;
    private boolean isMoved;

    public OnItemClickListner getOnItemClickListner() {
        return onItemClickListner;
    }

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }
}
