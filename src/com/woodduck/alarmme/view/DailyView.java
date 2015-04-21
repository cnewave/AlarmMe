
package com.woodduck.alarmme.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class DailyView extends View {
    // Paint paint = new Paint();
    private String TAG = "DailyView";
    private int mWidth = 320;
    private int mHeight = 640;
    private int mPadding = 50;
    GradientDrawable mDrawable;
    GradientDrawable mGradient;

    public DailyView(Context context) {
        super(context);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels;
        Log.d(TAG, "W " + mWidth + " H " + mHeight);
        initRectangle();
        initGradient();
    }

    private void initGradient() {
        mGradient = new GradientDrawable();
        mGradient.setStroke(10, Color.WHITE, 20, 30);
        float topRadius = 0;
        float bottomRadius = 0;
        mGradient.setCornerRadii(new float[] {
                100, 100,
                topRadius, topRadius,
                100, 100,
                bottomRadius, bottomRadius
        });
        // mGradient.setCornerRadius(50);
        mGradient.setBounds(mPadding + 10, mPadding + 10, mWidth - mPadding - 10, 1500 - 10);
    }

    private void initRectangle() {
        mDrawable = new GradientDrawable();
        mDrawable.setColor(Color.GREEN);
        // mGradient.setStroke(10, Color.WHITE, 20,30);
        float topRadius = 0;
        float bottomRadius = 0;

        mDrawable.setCornerRadii(new float[] {
                100, 100,
                topRadius, topRadius,
                100, 100,
                bottomRadius, bottomRadius
        });
        // mGradient.setCornerRadius(50);
        mDrawable.setBounds(mPadding, mPadding, mWidth - mPadding, 1500);
    }

    private int mEvent_Width = 200;
    private int mEvent_HEIGHT = 200;
    private int mEvent_X = 140;
    private int mEvent_Y = 100;
    private int SHIFT = 300;

    // EventLayout event = new EventLayout(mEvent_X, mEvent_Y, mEvent_X+mEvent_Width,mEvent_Y+ mEvent_HEIGHT);
    // EventLayout event2 = new EventLayout(mEvent_X+mShift, mEvent_Y+mShift, mEvent_X+mEvent_Width+mShift,mEvent_Y+
    // mEvent_HEIGHT+mShift);
    @Override
    public void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw " + getWidth() + "onDraw " + getHeight());

        mDrawable.draw(canvas);
        mGradient.draw(canvas);

        for (int i = 0; i < 3; i++) {
            EventLayout event = new EventLayout(mEvent_X, mEvent_Y + SHIFT * i, mEvent_X + mEvent_Width, mEvent_Y
                    + mEvent_HEIGHT + SHIFT * i);
            initEventLayout(canvas, event);
        }
    }

    private void initEventLayout(Canvas canvas, EventLayout event) {
        Log.d(TAG, "initEventLayout " + event);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.BLUE);
        float topRadius = 0;
        float bottomRadius = 0;
        float corner = 50;

        drawable.setCornerRadii(new float[] {
                corner, corner,
                topRadius, topRadius,
                corner, corner,
                bottomRadius, bottomRadius
        });
        // mGradient.setCornerRadius(50);
        drawable.setBounds(event.left, event.top, event.right, event.buttom);

        // RotateDrawable rotate = new RotateDrawable();
        // rotate.setDrawable(drawable);
        // rotate.draw(canvas);
        drawable.draw(canvas);
        // draw

    }

}
