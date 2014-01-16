package com.sakisds.frozenled.views;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by stratisg on 10/9/2013.
 */
public class ColorSwatch extends View {
    int mColor = getResources().getColor(R.color.holo_blue_dark);
    Paint mStrokePaint, mFillPaint;
    float mDiameter, mXCenter, mYCenter;

    public ColorSwatch(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mFillPaint = new Paint();
        mFillPaint.setAntiAlias(true);
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setColor(mColor);

        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(getResources().getColor(R.color.darker_gray));
        mStrokePaint.setStrokeWidth(2.5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        // Centers
        mXCenter = ww / 2;
        mYCenter = hh / 2;

        // Figure out how big we can make the pie.
        mDiameter = Math.min(ww, hh) / 1.5f;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mXCenter, mYCenter, (mDiameter / 2) - 1, mFillPaint);
        canvas.drawCircle(mXCenter, mYCenter, mDiameter / 2, mStrokePaint);
    }

    public void setColor(int color) {
        mColor = color;

        // Refresh view
        init();
        invalidate();
        requestLayout();
    }

    public int getColor() {
        return mColor;
    }
}