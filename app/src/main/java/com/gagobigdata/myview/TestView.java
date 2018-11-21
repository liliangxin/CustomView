package com.gagobigdata.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by llx on 2018/10/31.
 */
public class TestView extends View {
    private float imageWidth = Utils.dpToPixel(200);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    Bitmap bitmap;

    RectF savedArea = new RectF();
    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_HARDWARE,null);
        bitmap = Utils.getAvatar(getResources(), (int) imageWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(imageWidth/2,imageWidth/2,imageWidth/2,paint);

        int saveLayer = canvas.saveLayer(savedArea, paint);
        paint.setXfermode(xfermode);
        canvas.drawBitmap(bitmap,0,0,paint);
        paint.setXfermode(null);
        canvas.restoreToCount(saveLayer);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        savedArea.set(0, 0, imageWidth/2, imageWidth/2);
    }
}
