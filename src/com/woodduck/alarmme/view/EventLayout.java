
package com.woodduck.alarmme.view;

public class EventLayout {
    /** Layout left */
    public int left;
    /** Layout top */
    public int top;
    /** Layout right */
    public int right;
    /** Layout bottom */
    public int buttom;
    /** inner bound for display image */
    public int innerbound = 10;

    public EventLayout(int left, int top, int right, int buttom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.buttom = buttom;
    }

    public String toString() {
        return " LayoutView:{" + left + "," + top + "," + right + "," + buttom + "}" +
                " Inbound:" + innerbound;
    }

    public int getX() {
        return this.left;
    }

    public int getY() {
        return this.top;
    }
    
    public int getAbsoluteRight(){
        return this.left + this.right;
    }
    
    public int getAbsoluteButtom(){
        return this.top + this.buttom;
    }

}
