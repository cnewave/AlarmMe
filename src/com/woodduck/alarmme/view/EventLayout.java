
package com.woodduck.alarmme.view;

import android.graphics.Bitmap;
import android.util.Log;

public class EventLayout {
    /** Layout left */
    public int left;
    /** Layout top */
    public int top;
    /** Layout right */
    public int right;
    /** Layout bottom */
    public int bottom;
    /** inner bound for display image */
    public int innerbound = 30;

    public String picPath;
    public Bitmap bitmap;

    public EventLayout(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getPicPath() {
        return picPath;
    }
    
    public int getInnerbound(){
        return innerbound;
    }

    public String toString() {
        return " LayoutView:{" + left + "," + top + "," + right + "," + bottom + "}" +
                " Inbound:" + innerbound;
    }

    public int getX() {
        return this.left;
    }

    public int getY() {
        return this.top;
    }

    public int getRight() {
        return this.right;
    }
/*
    public int getAbsoluteRight() {
        return this.left + this.right;
    }
*/
    public int getbottom() {
        return this.bottom;
    }
/*
    public int getAbsolutebottom() {
        return this.top + this.bottom;
    }*/
    
    public boolean contains(float x, float y) {
//        Log.d("EventLayout","this ::"+ (left < right)+ ":"+(top < bottom));
//        Log.d("EventLayout","this :"+toString() + " :"+ (x>= left)+ ":"+(x< left+right));
//        Log.d("EventLayout","this ::"+ (y >= top)+ ":"+(y < top+bottom));
        return x >= left && x < left+right && y >= top && y < top+bottom;
    }
    

}
