package org.pdftools.utils;

/**
 * XY array for general use to maintain X,Y coordinates. Used in Pdf objects painting
 * @author asingh7!
 *
 */
public class XYArray {
    
    protected float x;
    protected float y;

    private XYArray(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public static XYArray getInstance(float x, float y) {
        return new XYArray(x,y);
    }
}