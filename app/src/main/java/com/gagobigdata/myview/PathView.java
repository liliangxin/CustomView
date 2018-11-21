package com.gagobigdata.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by llx on 2017/12/14.
 */

public class PathView extends View {


    public PathView(Context context) {
        super(context);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.reset();
        path.moveTo(50,50);
        path.lineTo(400,400);
        path.lineTo(400,50);
        path.close();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setPathEffect(new DashPathEffect(new float[] {15, 5}, 0));
        canvas.drawPath(path,paint);
        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(4);
        paint1.setAlpha(122);
        paint1.setPathEffect(null);
        paint1.setColor(Color.YELLOW);
        paint1.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path,paint1);
    }
}
