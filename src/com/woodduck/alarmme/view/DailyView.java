
package com.woodduck.alarmme.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.os.Handler;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import com.woodduck.alarmme.EventItem;
import com.woodduck.alarmme.view.DayScheduleActivity.CallBackInterface;

public class DailyView extends View {
    // Paint paint = new Paint();
    private String TAG = "DailyView";
    private int mWidth = 320;
    private int mHeight = 640;
    private int mPadding = 50;
    GradientDrawable mDrawable;
    GradientDrawable mGradient;
    List<EventItem> mList;
    // Use the ViewSwitcher to 1. do scroll up/down event. onFling or onScroll. 2. redraw the images.

    GestureDetector mGestureDetector;
    ScaleGestureDetector mScaleGestureDetector;

    public DailyView(Context context) {
        super(context);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mWidth = metrics.widthPixels / 2;
        mHeight = metrics.heightPixels;
        Log.d(TAG, "W " + mWidth + " H " + mHeight);
        initRectangle();
        initGradient();
        mGestureDetector = new GestureDetector(context, new CalendarGestureListener());
        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleDect);
        mScroller = new OverScroller(context);
    }

    private List<EventLayout> mlyaoutlist = new ArrayList<EventLayout>();

    public void setlist(List<EventItem> list) {
        mList = list;
        recomputeLayout();
    }

    private int mX_Scale = 1;
    private int mY_Scale = 1;
    private int mEvent_Width = 300;
    private int mEvent_HEIGHT = 400;
    private int mEvent_X = 120;
    private int mEvent_Y = 100;
    private int SHIFT = 500;

    private void recomputeLayout() {
        mlyaoutlist.clear();
        for (int i = 0; i < mList.size(); i++) {
            String path = mList.get(i).getPicturePath();
            EventLayout event = new EventLayout(mEvent_X, mEvent_Y + SHIFT * i, mEvent_X + mEvent_Width, mEvent_Y
                    + mEvent_HEIGHT + SHIFT * i);
            event.setPicPath(path);
            event.bitmap = decodeImage(event);
            mlyaoutlist.add(event);
            // Log.d(TAG, "initEventLayout " + event);
        }
    }

    CallBackInterface mCallback;

    public void setCallbackHandler(CallBackInterface inteface) {
        mCallback = inteface;
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
        mGradient.setBounds(mPadding + 10, mPadding + 10, mWidth - mPadding - 10, mHeight - 10);
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
        mDrawable.setBounds(mPadding, mPadding, mWidth - mPadding, mHeight);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw " + getWidth() + "onDraw " + getHeight());

        mDrawable.draw(canvas);
        mGradient.draw(canvas);

        for (int i = 0; i < mlyaoutlist.size(); i++) {
            initEventLayout(canvas, mlyaoutlist.get(i));
        }
    }

    private void initEventLayout(final Canvas canvas, EventLayout event) {
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
        // drawable.setBounds(event.getX(), event.getY(), event.getAbsoluteRight(), event.getAbsolutebottom());
        drawable.setBounds(event.getX(), event.getY(), event.getRight(), event.getbottom());

        // RotateDrawable rotate = new RotateDrawable();
        // rotate.setDrawable(drawable);
        // rotate.draw(canvas);
        drawable.draw(canvas);
        // draw
        drawImage(canvas, event);
    }

    private Handler mHandler;

    @Override
    protected void onAttachedToWindow() {
        if (mHandler == null) {
            mHandler = getHandler();

        }
    }

    class updateDrawable implements Runnable {
        Canvas canvas;
        EventLayout event;

        public updateDrawable(Canvas canvas, EventLayout event) {
            this.canvas = canvas;
            this.event = event;
        }

        public void run() {
            drawImage(canvas, event);
        }
    }

    private Bitmap decodeImage(EventLayout event) {
        String pic = event.getPicPath();
        if (pic == null) {
            return null;
        }
        // Log.d(TAG, "onDraw pic ," + pic);
        int targetW = event.getRight();
        int targetH = event.getbottom();
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(event.getPicPath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inMutable = true;
        return BitmapFactory.decodeFile(pic, bmOptions);
    }
    private void drawImage(Canvas canvas, EventLayout event) {
        Bitmap bitmap = event.bitmap;
        if (bitmap != null) {
            // Log.d(TAG, "draw... bitmap");
            Rect dst = new Rect(event.getX() + event.getInnerbound(), event.getY() + event.getInnerbound(),
                    event.getRight() - event.getInnerbound(), event.getbottom() - event.getInnerbound());
            
            canvas.drawBitmap(bitmap, null, dst, null);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        // Log.d(TAG, "onTouchEvent... X:" + event.getX() + " Y:" + event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                mGestureDetector.onTouchEvent(event);
                break;
        }

        // mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private void checkIfLayoutClick(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int index = -1;
        for (int i = 0; i < mlyaoutlist.size(); i++) {
            if (mlyaoutlist.get(i).contains(x, y)) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            Log.d(TAG, "onTouchEvent...  found");
            if (mCallback != null) {
                mCallback.onViewItemClick(index);
            }
        } else {
            Log.d(TAG, "onTouchEvent... not found");
        }

    }

    private boolean DEBUG = true;

    class CalendarGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent ev) {
            if (DEBUG)
                Log.e(TAG, "GestureDetector.onSingleTapUp");
            checkIfLayoutClick(ev);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent ev) {
            if (DEBUG)
                Log.e(TAG, "GestureDetector.onLongPress");
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (DEBUG)
                Log.e(TAG, "GestureDetector.onScroll");
            float deltaY = e2.getY() - e1.getY();
            smoothScrollBy(0, (int) deltaY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (DEBUG)
                Log.e(TAG, "GestureDetector.onFling");
            doFling(e1, e2, velocityX, velocityY);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent ev) {
            if (DEBUG)
                Log.e(TAG, "GestureDetector.onDown");
            return true;
        }
    }

    // the flip action is not smooth.
    private void doFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "doFling: velocityX " + velocityX + " velocityY:" + velocityY);
        float deltaX = e2.getX() - e1.getX();
        float deltaY = e2.getY() - e1.getY();

        Log.d(TAG, "doFling: delta " + deltaY);

        mEvent_Y = mEvent_Y - (int) (deltaY / 50);
        // recomputeLayout();
        // invalidate();
        smoothScrollBy(0, (int) deltaY);

    }

    public class ScaleGestureDect implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.d(TAG, "onScale " + detector.getScaleFactor());
            // need to check the scale up/down
            Log.d(TAG, "onScaleBegin mX_Scale " + mX_Scale + " mY_Scale " + mY_Scale);
            mEvent_Width += 30;
            mEvent_HEIGHT += 40;
            recomputeLayout();
            invalidate();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.d(TAG, "onScaleBegin ");
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

            float factor = detector.getScaleFactor();
            Log.d(TAG, "onScaleEnd " + factor + " " + detector.getCurrentSpanX());
        }

    }

    private ScaleGestureDect mScaleDect = new ScaleGestureDect();
    private OverScroller mScroller;

    public void smoothScrollBy(int dx, int dy) {

        // 设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        invalidate();// 这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    @Override
    public void computeScroll() {
        Log.d(TAG, "computeScroll...");
        // 先判斷mScroller滾動是否完成
        if (mScroller.computeScrollOffset()) {

            // 這裡調用View的scrollTo()完成實際的滾動
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            // 必須調用該方法，否則不一定能看到滾動效果
            postInvalidate();
        }
        super.computeScroll();
    }
}
