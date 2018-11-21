package com.gagobigdata.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by llx on 2018/11/12.
 */
public class ScanableImageView extends View implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private int imageWidth = 200;
    private Bitmap bitmap;
    private Paint paint;

    private float originalOffsetX;
    private float originalOffsetY;

    private float smallScale;
    private float bigScale;
    private float currentScale;

    private GestureDetectorCompat detector;


    public ScanableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmap = Utils.getAvatar(getResources(), (int) Utils.dpToPixel(imageWidth));
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        detector = new GestureDetectorCompat(context,this);
        detector.setOnDoubleTapListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        originalOffsetX = (getWidth() - bitmap.getWidth())/2f;
        originalOffsetY = (getHeight() - bitmap.getHeight())/2f;

        if ((float)bitmap.getWidth() / (float) bitmap.getHeight() > getWidth()/getHeight()){
            smallScale = (float) getWidth()/bitmap.getWidth();
            bigScale = (float) getHeight()/bitmap.getHeight();
        } else {
            smallScale = (float) getHeight()/bitmap.getHeight();
            bigScale = (float) getWidth()/bitmap.getWidth();
        }
        currentScale = smallScale;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap,originalOffsetX,originalOffsetY,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
