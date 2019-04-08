package com.codido.nymeria.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.codido.nymeria.R;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.YUtils;

import java.util.ArrayList;

public class TabHeaderTextView extends View {
    private int index;
    private float positionOffset;
    private int position;
    private ArrayList<String> tabs;
    private int colorText;
    OnTabSelecetedListener onTabSelecetedListener;

    public static interface OnTabSelecetedListener {
        public void onTabSelected(int index);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOff) {
        this.positionOffset = positionOffset;
        this.position = position;
        invalidate();

    }

    private float downX;
    private float downY;

    public boolean onTouchEvent(MotionEvent event) {
        if (tabs == null || tabs.size() == 0) {
            return false;
        }

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (downX == 0 && downY == 0) {
                    downX = x;
                    downY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(x - downX) < 50 && Math.abs(y - downY) < 50) {
                    int index = (int) (x / (getWidth() / tabs.size()));
                    setIndex(index);

                    Global.debug("onTabSelecetedListener.onTabSelected(index)," + index);
                }
                break;

        }
        return true;
    }

    public void setIndex(int index) {
        this.index = index;
        if (onTabSelecetedListener != null) {
            onTabSelecetedListener.onTabSelected(index);
        }
        invalidate();
    }

    public void setTabs(ArrayList<String> tabs) {
        this.tabs = tabs;
        invalidate();
    }

    public TabHeaderTextView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        colorText = getResources().getColor(R.color.app_main_color);

        setClickable(true);
    }

    public TabHeaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabHeaderTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    Paint paint = new Paint();
    Rect rect = new Rect();

    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (tabs == null || tabs.size() == 0) {
            return;
        }

        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        paint.setColor(0xFFCCCCCC);
        canvas.drawRect(0, getHeight() - 2, getWidth(), getHeight(), paint);

        paint.setColor(colorText);
        int itemWidth = getWidth() / tabs.size();
        int left = 0;
//        left = position * itemWidth + (int) (positionOffset * itemWidth);

        if (positionOffset != 0) {
            left = position * itemWidth + (int) (positionOffset * itemWidth);
        } else {
            left = index * itemWidth + (int) (positionOffset * itemWidth);
        }
        canvas.drawRect(left, getHeight() - YUtils.dip2px(getContext(), 2F), left + itemWidth,
                getHeight(), paint);

        paint.setTextSize(YUtils.dip2px(getContext(), 12F));

        for (int i = 0; i < tabs.size(); i++) {
            if (i == index) {
                paint.setColor(colorText);
            } else {
                paint.setColor(0xFF333333);
            }
            paint.getTextBounds(tabs.get(i), 0, tabs.get(i).length(), rect);

            canvas.drawText(tabs.get(i), itemWidth * i + (itemWidth - rect.width()) / 2,
                    getHeight() / 8 * 7 / 2 + rect.height() / 2, paint);

        }
    }

    public int startX;

    public OnTabSelecetedListener getOnTabSelecetedListener() {
        return onTabSelecetedListener;
    }

    public void setOnTabSelecetedListener(OnTabSelecetedListener onTabSelecetedListener) {
        this.onTabSelecetedListener = onTabSelecetedListener;
    }
}
