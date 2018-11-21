package com.gagobigdata.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by llx on 2018/10/25.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DashBoardView extends View {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    float radius = DesintyUtil.dp2px(150);
    int angle = 120;
    PathDashPathEffect effect;
    Path dash = new Path();//刻度

    public DashBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DesintyUtil.dp2px(2));
        dash.addRect(0, 0, DesintyUtil.dp2px(2), DesintyUtil.dp2px(10), Path.Direction.CW);

        Path path = new Path();
        path.addArc(getWidth() / 2 - radius,
                getHeight() / 2 - radius,
                getWidth() / 2 + radius,
                getHeight() / 2 + radius,
                angle / 2 + 90,
                360 - angle);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float length = pathMeasure.getLength();

        effect = new PathDashPathEffect(dash, (length - DesintyUtil.dp2px(2)) / 30, 0, PathDashPathEffect.Style.ROTATE);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画线
        canvas.drawArc(getWidth() / 2 - radius,
                getHeight() / 2 - radius,
                getWidth() / 2 + radius,
                getHeight() / 2 + radius,
                angle / 2 + 90,
                360 - angle,
                false, paint);
        //画刻度
        paint.setPathEffect(effect);
        canvas.drawArc(getWidth() / 2 - radius,
                getHeight() / 2 - radius,
                getWidth() / 2 + radius,
                getHeight() / 2 + radius,
                angle / 2 + 90,
                360 - angle,
                false, paint);
        paint.setPathEffect(null);

        canvas.drawLine(getWidth() / 2,
                getHeight() / 2,
                (float) (Math.cos(Math.toRadians(getCurrentAngle(8))) * radius * 0.7 + getWidth() / 2),
                (float) (Math.sin(Math.toRadians(getCurrentAngle(8))) * radius  * 0.7+ getHeight() / 2)
                , paint);
    }

    int getCurrentAngle(int markNum) {
        return (int) (90 + angle / 2 + (360 - angle) / 30f * markNum);
    }
}
