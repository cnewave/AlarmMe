
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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

    public DailyView(Context context) {
        super(context);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mWidth = metrics.widthPixels / 2;
        mHeight = metrics.heightPixels;
        Log.d(TAG, "W " + mWidth + " H " + mHeight);
        initRectangle();
        initGradient();
    }

    private List<EventLayout> mlyaoutlist = new ArrayList<EventLayout>();

    public void setlist(List<EventItem> list) {
        mList = list;
        for (int i = 0; i < mList.size(); i++)
        {
            String path = mList.get(i).getPicturePath();
            EventLayout event = new EventLayout(mEvent_X, mEvent_Y + SHIFT * i, mEvent_X + mEvent_Width, mEvent_Y
                    + mEvent_HEIGHT);
            event.setPicPath(path);
            mlyaoutlist.add(event);
        }
    }

    CallBackInterface mCallback;
    public void setCallbackHandler(CallBackInterface inteface){
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

    private int mEvent_Width = 200;
    private int mEvent_HEIGHT = 300;
    private int mEvent_X = 120;
    private int mEvent_Y = 100;
    private int SHIFT = 500;

    @Override
    public void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw " + getWidth() + "onDraw " + getHeight());

        mDrawable.draw(canvas);
        mGradient.draw(canvas);

        for (int i = 0; i < mlyaoutlist.size(); i++){
            initEventLayout(canvas, mlyaoutlist.get(i));
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
        drawable.setBounds(event.getX(), event.getY(), event.getAbsoluteRight(), event.getAbsolutebottom());

        // RotateDrawable rotate = new RotateDrawable();
        // rotate.setDrawable(drawable);
        // rotate.draw(canvas);
        drawable.draw(canvas);
        // draw
        drawImage(canvas, event);
    }

    private void drawImage(Canvas canvas, EventLayout event) {
        String pic = event.getPicPath();
        if (pic == null) {
            return;
        }
        Log.d(TAG, "onDraw pic ," + pic);
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

        Bitmap bitmap = BitmapFactory.decodeFile(pic, bmOptions);
        if (bitmap != null) {
            Log.d(TAG, "draw... bitmap");
            Rect dst = new Rect(event.getX() + event.getInnerbound(), event.getY() + event.getInnerbound(),
                    event.getAbsoluteRight() - event.getInnerbound(), event.getAbsolutebottom() - event.getInnerbound());
            canvas.drawBitmap(bitmap, null, dst, null);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent... X:" + event.getX() + " Y:" + event.getY());
        if (event.getAction() == MotionEvent.ACTION_UP) {
            checkIfLayoutClick(event);
        }
        return true;
    }

    private void checkIfLayoutClick(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int index = -1;
        for (int i = 0; i < mlyaoutlist.size(); i++) {
            if(mlyaoutlist.get(i).contains(x, y)){            
                index = i;
                break;
            }
        }
        if (index >= 0) {
            Log.d(TAG, "onTouchEvent...  found");
        } else {
            Log.d(TAG, "onTouchEvent... not found");
        }
        if(mCallback != null){
            mCallback.onViewItemClick(index);
        }
    }
}
