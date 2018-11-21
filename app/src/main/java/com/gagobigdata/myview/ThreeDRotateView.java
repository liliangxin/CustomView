package com.gagobigdata.myview;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by llx on 2018/10/29.
 */
public class ThreeDRotateView extends View {

    private float imageWidth = Utils.dpToPixel(150);
    private float padding = Utils.dpToPixel(100);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Camera camera = new Camera();

    private int topDegree = 0;
    private int bottomDegree = 0;
    private int canvasRotateDegree = 0;

    public ThreeDRotateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        camera.setLocation(0,0,Utils.getZForCamera());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(padding+imageWidth/2,padding+imageWidth/2);
        canvas.rotate(canvasRotateDegree);
        camera.save();
        camera.rotateX(topDegree);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.clipRect(-imageWidth,-imageWidth,imageWidth,0);
        canvas.rotate(-canvasRotateDegree);
        canvas.translate(-(padding+imageWidth/2),-(padding+imageWidth/2));
        canvas.drawBitmap(Utils.getAvatar(getResources(), (int) imageWidth),padding,padding,paint);
        canvas.restore();

        canvas.save();
        canvas.translate(padding+imageWidth/2,padding+imageWidth/2);
        canvas.rotate(canvasRotateDegree);
        camera.save();
        camera.rotateX(bottomDegree);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.clipRect(-imageWidth,0,imageWidth,imageWidth);
        canvas.rotate(-canvasRotateDegree);
        canvas.translate(-(padding+imageWidth/2),-(padding+imageWidth/2));
        canvas.drawBitmap(Utils.getAvatar(getResources(), (int) imageWidth),padding,padding,paint);
        canvas.restore();

    }

    public int getTopDegree() {
        return topDegree;
    }

    public void setTopDegree(int topDegree) {
        this.topDegree = topDegree;
        invalidate();
    }

    public int getBottomDegree() {
        return bottomDegree;
    }

    public void setBottomDegree(int bottomDegree) {
        this.bottomDegree = bottomDegree;
        invalidate();
    }

    public int getCanvasRotateDegree() {
        return canvasRotateDegree;
    }

    public void setCanvasRotateDegree(int canvasRotateDegree) {
        this.canvasRotateDegree = canvasRotateDegree;
        invalidate();
    }
}
