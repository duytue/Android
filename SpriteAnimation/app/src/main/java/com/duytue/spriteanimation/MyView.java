package com.duytue.spriteanimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.jar.Attributes;

/**
 * Created by duytu on 12-Jun-17.
 */

public class MyView extends View {
    public static final int MAGIC_NUMBER_LEFT = 70;
    public static final int MAGIC_NUMBER_TOP = 35;
    private TimerTask timerTask;
    private ArrayList<MySprite> sprites;

    private Timer timer;

    // being dragged?
    private boolean bDrag = false;

    private int selectedSpriteIndex = -1;

    private float oldX, oldY;

    // floating?
    private boolean bFloat = false;

    //private int myScreenWidth, myScreenHeight;

    // used in beginFloat function
    private float vx = 0;
    private float vy = 0;
    private float ax = 0;
    private float ay = 0;
    private float epsilonV = 1f;
    private float alpha = 2;
    private float beta = 0.1f;



    public MyView(Context context, AttributeSet attrs) {
        super(context);

        sprites = new ArrayList<>();
        CreateBackGround(0, 0, R.drawable.bestmap);
        CreateBuilding(1185, 300, R.drawable.barn);
        CreateBuilding(900, 250, R.drawable.silo);
        for (int i=0; i<10; i++) {
            CreateField(170 + i * MAGIC_NUMBER_LEFT, 65 + i * MAGIC_NUMBER_TOP);
            CreateField(100 + i * MAGIC_NUMBER_LEFT, 100 + i * MAGIC_NUMBER_TOP);
            CreateField(30 + i * MAGIC_NUMBER_LEFT, 135+ i * MAGIC_NUMBER_TOP);
        }

        InitTimerTask();


        timer = new Timer();
        timer.schedule(timerTask, 1000, 16);  //16 = 60fps
    }

    private void InitTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                for (int i=0; i < sprites.size(); i++)
                    sprites.get(i).Update();
                postInvalidate();
            }
        };
    }

    private void CreateField(int left, int top) {
        Bitmap[] BMPs;
        BMPs = new Bitmap[5];

        //this may cause out of memory
        for (int i=0; i<5; i++)
            BMPs[i] = BitmapFactory.decodeResource(getResources(), R.drawable.field);
        //create ContentManager

        sprites.add(new MySprite(BMPs, left, top, 0, 0));
    }

    private void CreateBackGround(int left, int top, int BMPResource) {
        Bitmap[] BMPs = new Bitmap[1];
        BMPs[0] = BitmapFactory.decodeResource(getResources(), BMPResource);
        sprites.add(new MySprite(BMPs, left, top, 0, 0));
    }

    private void CreateBuilding(int left, int top, int BMPResource) {
        Bitmap[] BMPs = new Bitmap[1];
        BMPs[0] = BitmapFactory.decodeResource(getResources(), BMPResource);
        sprites.add(new MySprite(BMPs, left, top, 0, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRGB(59, 112, 168);
/*        Paint p = new Paint();
        p.setARGB(255, 255, 0, 0);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawCircle(100, 100, 50, p);*/


        Bitmap fakeBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);

        Canvas fakeCanvas = new Canvas(fakeBitmap);


        for (int i = 0; i < sprites.size(); i++)
            //    sprites.get(i).Draw(canvas);
            sprites.get(i).Draw(fakeCanvas);

        canvas.drawBitmap(fakeBitmap, 0, 0, null);


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
// get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // TODO use data
                bFloat = false;
                int tempIndex = getSelectedSpiteIndex(x, y);
                if (tempIndex !=-1) {
                    highlightSelected(tempIndex);
                    if (sprites.get(selectedSpriteIndex).State == 0)
                        beginDrag(tempIndex, x, y);
                    Log.i("touched", "touched");
                }

                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                // TODO use data
                if (bDrag==true)
                    processDrag(selectedSpriteIndex, x, y);
                    break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                if (bDrag)
                    endDrag(selectedSpriteIndex, x, y);
                    break;
            }
            case MotionEvent.ACTION_CANCEL: {
                // TODO use data
                break;
            }
        }

        return true;
    }

    private void highlightSelected(int newIndex) {
        if (newIndex != selectedSpriteIndex) {
            if (selectedSpriteIndex != -1)
                sprites.get(selectedSpriteIndex).State = 0;
            selectedSpriteIndex = newIndex;
            Log.i("heightliehgt", "dahiehglejgt");
            sprites.get(selectedSpriteIndex).State = 1;
            invalidate();
        } else if (sprites.get(selectedSpriteIndex).State == 1){
            sprites.get(selectedSpriteIndex).State = 0;
        }
        else {
            sprites.get(selectedSpriteIndex).State = 1;
        }
    }

    private int getSelectedSpiteIndex(float x, float y) {
        int id = -1;

        for (int i = sprites.size() - 1; i >= 0; --i) {
            if (sprites.get(i).isSelected(x, y)){
                id = i;
                break;
            }
        }

        return id;
    }


    private void beginDrag(int selectedSpriteIndex, float x, float y) {
        bDrag = true;
        oldX = x;
        oldY = y;
    }

    private void processDrag(int selIdx, float x, float y) {
        sprites.get(selIdx).left += x-oldX;
        sprites.get(selIdx).top += y-oldY;
        oldX = x;
        oldY = y;
        invalidate();
    }

    private void endDrag(int selectedSpriteIndex, float x, float y) {
//        processDrag(selectedSpriteIndex, x, y);  if not using floating -> use 1 last time
        bDrag = false;
        beginFloat(selectedSpriteIndex, x, y);

    }

    private void beginFloat(int selectedSpriteIndex, float x, float y) {
        bFloat = true;
        vx = alpha * ( x - oldX);
        vy = alpha * ( y - oldY);
        ax = -beta*vx;
        ay = -beta*vy;
    }

}
