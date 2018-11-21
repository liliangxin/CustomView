package com.gagobigdata.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by llx on 2018/3/27.
 */

public class PathFillView extends View {

    private Paint bgPaint;
    private Paint pathPaint;
    public PathFillView(Context context) {
        super(context);
    }

    public PathFillView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PathFillView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.moveTo(50,50);
        path.arcTo(new RectF(50,50,350,350),270,360);
        path.lineTo(50,100);
        path.close();
        canvas.drawColor(Color.RED);
        drawPath(path,canvas);
        drawFill(path,canvas);
    }

    private void drawFill(Path path, Canvas canvas) {
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.YELLOW);
        bgPaint.setStrokeWidth(2);
        bgPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path,pathPaint);
    }

    private void drawPath(Path path, Canvas canvas) {
        pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setColor(Color.BLUE);
        pathPaint.setStrokeWidth(2);
        canvas.drawPath(path,pathPaint);
    }
}
