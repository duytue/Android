package com.duytue.spriteanimation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by duytu on 12-Jun-17.
 */

public class MySprite {
    public int nBMPs;       // number of bitmaps
    public int iBMP;        // current bitmap
    public Bitmap[] BMPs;   // array of bitmaps
    public int left;        // left coordinate/ position
    public int top;         // top
    public int width;       // width
    public int height;      // height

    // is selected?
    public int State = 0;
    // deviation
    public int d = 0;
    // deviation direction?
    public int d2 = 1;

    public MySprite(Bitmap[] bmps, int left, int top, int width, int height)
    {
        BMPs = bmps;
        nBMPs = bmps.length;
        iBMP = 0;
        this.left = left;
        this.top = top;
        if (width == 0 && height==0)
        {
            width = bmps[0].getWidth();
            height = bmps[0].getHeight();
        }
        this.width = width;
        this.height = height;
    }

    public void Update() {
        // cycle through all images
        //iBMP = (iBMP + 1) % 10;

        //zooming in n out
        if (State==1)
        {
            if (d>=10 || d<=-10)
                d2*=-1;
            d+=d2;
        }
    }

    public void Draw(Canvas canvas) {
        if (State == 0) {
            canvas.drawBitmap(BMPs[iBMP], left, top, null); // (bitmap, left, top, Paint)
        } else  {
            Rect sourceRect = new Rect(0, 0, width-1, height-1);
            Rect destRect = new Rect(left-d, top-d, left+width+2*d-1, top+height+2*d-1);
            canvas.drawBitmap(BMPs[iBMP],sourceRect, destRect, null);
        }
    }

    public boolean isSelected(float x, float y) {
        // touched position
        if (x >= left && x <= (left + width) && y >= top && y <= (top + height)) {
            return true;
        }
        return false;
    }
}
