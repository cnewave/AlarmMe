
package com.woodduck.alarmme.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class DailyView extends View {
    // Paint paint = new Paint();
    private String TAG = "DailyView";
    private int mWidth = 480;
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
        mGradient.setBounds(mPadding+10, mPadding+10, mWidth - mPadding-10, 1800-10);
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
        mDrawable.setBounds(mPadding, mPadding, mWidth - mPadding, 1800);
    }

    /*
     * private void initShapeDrawable() { mDrawable = new ShapeDrawable(new RectShape());
     * mDrawable.getPaint().setColor(Color.GREEN); mDrawable.getPaint().setStyle(Style.FILL_AND_STROKE);
     * mDrawable.getPaint().setStrokeWidth(2); // setStroke(int width, int color, float dashWidth, float dashGap)
     * mDrawable.setPadding(10, 10, 10, 10); mDrawable.setBounds(mPadding, mPadding, mWidth - mPadding, 1800); }
     */

    @Override
    public void onDraw(Canvas canvas) {
        mDrawable.draw(canvas);

        mGradient.draw(canvas);
    }
}
