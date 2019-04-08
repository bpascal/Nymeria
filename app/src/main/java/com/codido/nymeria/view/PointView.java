package com.codido.nymeria.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.codido.nymeria.R;


/**
 * 位置指示组件
 *
 * @author liu_kf
 */
public class PointView extends View {
    private int count;
    private int index;
    private int pointRadius = 10;
    private int pointSpacing = 30;
    Paint paint = new Paint();
    private int selcetColor = 0xFFFFFFFF;
    private int unSelectColor = 0xFFFFFFFF;

    public PointView(Context context) {
        super(context);
        init(context, null);
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return super.getSuggestedMinimumHeight();
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (pointRadius * 2 + pointSpacing) * count;
    }

    public void init(Context context, AttributeSet attrs) {

        if (attrs == null) {
            return;
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PointView);
        pointRadius = (int) a.getDimension(R.styleable.PointView_pointRadius, 10F);
        pointSpacing = (int) a.getDimension(R.styleable.PointView_pointSpacing, 20F);
        selcetColor = (int) a.getColor(R.styleable.PointView_pointSelectedColor, getResources().getColor(R.color.common_titlebackground_red));
        unSelectColor = (int) a.getColor(R.styleable.PointView_pointUnSelectedColor, 0xFFFFFFFF);

    }

    public PointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PointView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (count <= 1) {
            return;
        }

        paint.setAntiAlias(true);

        startX = (getWidth() - (count - 1) * pointSpacing - count * pointRadius) / 2;
        for (int i = 0; i < count; i++) {
            if (i == index) {
                paint.setColor(selcetColor);
                canvas.drawCircle(startX + i * (pointSpacing + pointRadius),
                        (getHeight() - pointRadius) / 2, pointRadius, paint);
            } else {
                paint.setColor(unSelectColor);
                canvas.drawCircle(startX + i * (pointSpacing + pointRadius),
                        (getHeight() - pointRadius) / 2, (int) (pointRadius * 1), paint);
            }

        }
    }

    public int startX;

    public void c() {
        startX = (getWidth() - (count - 1) * pointSpacing - count * pointRadius) / 2;
    }

    public int getCount() {
        return count;
    }

    public int getIndex() {
        return index;
    }

    public void setCount(int count) {
        this.count = count;
        invalidate();
    }

    public void setIndex(int index) {
        this.index = index;
        invalidate();
    }

    public int getSelcetColor() {
        return selcetColor;
    }

    public int getUnSelectColor() {
        return unSelectColor;
    }

    public void setSelcetColor(int selcetColor) {
        this.selcetColor = selcetColor;
        invalidate();
    }

    public void setUnSelectColor(int unSelectColor) {
        this.unSelectColor = unSelectColor;
        invalidate();
    }
}
