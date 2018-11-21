package com.gagobigdata.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by llx on 2018/1/16.
 */

public class HoursWeatherView extends View {

    /**
     * 重要参数，两点之间分为几段描画，数字愈大分段越多，描画的曲线就越精细.
     */
    private static final int STEPS = 12;
    List<Integer> points_x;
    List<Integer> points_y;

    private int mWidth;
    private int mHeight;
    private Context context;
    private String[] timeString;
    private Paint linePaint;
    private Paint tempLinePaint;
    private Paint tempPointPaint;
    private Paint timeTextPaint;
    private Paint tempTextPaint;
    private Paint waterPaint;
    private Paint colorPaint;
    /**
     * 一天温度集合
     */
    private int mTempDay[] = new int[24];

    /**
     * 一天降水集合
     */
    private int mWater[] = new int[24];

    public HoursWeatherView(Context context) {
        this(context, null);
    }

    public HoursWeatherView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HoursWeatherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        timeString = new String[]{"01:00", "04:00", "07:00", "10:00", "13:00", "16:00", "19:00", "22:00"};
        points_x = new LinkedList<>();
        points_y = new LinkedList<>();
        //网格竖线
        linePaint = new Paint();
        linePaint.setStrokeWidth(dp2px(context, 0.5f));
        linePaint.setColor(Color.parseColor("#eaeced"));
        linePaint.setAntiAlias(true);

        //温度线
        tempLinePaint = new Paint();
        tempLinePaint.setStrokeWidth(1);
        tempLinePaint.setStyle(Paint.Style.STROKE);
        tempLinePaint.setColor(Color.parseColor("#f71b23"));
        tempLinePaint.setAntiAlias(true);

        //温度点
        tempPointPaint = new Paint();
        tempPointPaint.setColor(Color.parseColor("#f71b23"));
        tempPointPaint.setAntiAlias(true);

        //时间
        timeTextPaint = new Paint();
        timeTextPaint.setColor(Color.parseColor("#212121"));
        timeTextPaint.setAntiAlias(true);
        timeTextPaint.setTextSize(dp2px(context, 12));

        //温度
        tempTextPaint = new Paint();
        tempTextPaint.setStrokeWidth(1);
        tempTextPaint.setAntiAlias(true);
        tempTextPaint.setColor(Color.parseColor("#a5212121"));
        tempTextPaint.setTextSize(dp2px(context, 12));

        //降水
        waterPaint = new Paint();
        waterPaint.setAlpha(125);
        waterPaint.setAntiAlias(true);
        waterPaint.setStyle(Paint.Style.FILL);
        Shader shader = new LinearGradient(0,mHeight/3*2+dp2px(context,10),0,mHeight,
                Color.parseColor("#63a7ff"),Color.parseColor("#87dff5"),Shader.TileMode.CLAMP);
        waterPaint.setShader(shader);
        waterPaint.setStrokeWidth(dp2px(context,9));
        waterPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
        drawLines(canvas);
        addColor(canvas);
        drawPointAndLine(canvas);
        drawWaterLine(canvas);
    }

    private void drawWaterLine(Canvas canvas) {
        int minTempDay = mWater[0];
        // 存放最高降水量
        int maxTempDay = mWater[0];
        for (int item : mWater) {
            if (item > maxTempDay) {
                maxTempDay = item;
            }
        }

        // 份数
        float parts = maxTempDay - minTempDay;
        float lengthToTop = mHeight/3*2+dp2px(context, 40);
        //每一份的高度
        float partValue = (mHeight-lengthToTop) / parts;
        for (int i = 1; i < mWater.length; i = i + 3) {
            canvas.drawLine(mWidth / 24 * i,mHeight-dp2px(context,5), mWidth / 24 * i,mHeight-dp2px(context,5)-mWater[i] * partValue,  waterPaint);
            // 显示降水量文字
            Rect rect = new Rect();
            String str = mWater[i] + "mm";
            tempTextPaint.getTextBounds(str, 0, str.length(), rect);
            float strWidth = rect.width();//字符串的宽度
            canvas.drawText(mWater[i] +"mm", mWidth / 24 * i - strWidth / 2, mHeight-mWater[i] * partValue - dp2px(context, 12), tempTextPaint);
        }
    }

    private void addColor(Canvas canvas) {
        List<Point> points = new LinkedList<>();
        colorPaint = new Paint();
        colorPaint.setAlpha(125);
        colorPaint.setAntiAlias(true);
        colorPaint.setStyle(Paint.Style.FILL);
        Shader shader = new LinearGradient(0,0,0,mHeight/3*2+dp2px(context,10),
                Color.parseColor("#ffe666"),Color.parseColor("#ff6647"),Shader.TileMode.CLAMP);
        colorPaint.setShader(shader);

        // 存放最低温度
        int minTempDay = mTempDay[0];
        // 存放最高温度
        int maxTempDay = mTempDay[0];
        for (int item : mTempDay) {
            if (item < minTempDay) {
                minTempDay = item;
            }
            if (item > maxTempDay) {
                maxTempDay = item;
            }
        }
        // 份数
        float parts = maxTempDay - minTempDay;
        float lengthToTop = dp2px(context, 30);
        //每一份的高度
        float partValue = mHeight / 3 / parts;
        for (int i = 0; i < mTempDay.length; i++) {
            points.add(new Point((int)mWidth / 24 * i, (int)((maxTempDay - mTempDay[i]) * partValue + lengthToTop + mHeight / 6)));
        }
        points.add(new Point(mWidth, (int)((maxTempDay - mTempDay[0]) * partValue + lengthToTop + mHeight / 6)));
        Path path = new Path();
        points_x.clear();
        points_y.clear();
        for (int i = 0; i < points.size(); i++) {
            points_x.add(points.get(i).x);
            points_y.add(points.get(i).y);
        }
        List<Cubic> calculate_x = calculate(points_x);
        List<Cubic> calculate_y = calculate(points_y);
        path.moveTo(calculate_x.get(0).eval(0), calculate_y.get(0).eval(0));
        for (int i = 0; i < calculate_x.size(); i++) {

            for (int j = 1; j <= STEPS; j++) {
                float u = j / (float) STEPS;
                path.lineTo(calculate_x.get(i).eval(u), calculate_y.get(i)
                        .eval(u));
            }

        }
        path.lineTo(mWidth,mHeight/3*2+dp2px(context,10));
        path.lineTo(0,mHeight/3*2+dp2px(context,10));
        canvas.drawPath(path,colorPaint);
    }

    private void drawPointAndLine(Canvas canvas) {
        List<Point> points = new ArrayList<>();
        // 存放最低温度
        int minTempDay = mTempDay[0];
        // 存放最高温度
        int maxTempDay = mTempDay[0];
        for (int item : mTempDay) {
            if (item < minTempDay) {
                minTempDay = item;
            }
            if (item > maxTempDay) {
                maxTempDay = item;
            }
        }
        // 份数
        float parts = maxTempDay - minTempDay;
        float lengthToTop = dp2px(context, 30);
        //每一份的高度
        float partValue = mHeight / 3 / parts;

        for (int i = 0; i < mTempDay.length; i++) {
            points.add(new Point((int)mWidth / 24 * i, (int)((maxTempDay - mTempDay[i]) * partValue + lengthToTop + mHeight / 6)));
        }
        points.add(new Point(mWidth, (int)((maxTempDay - mTempDay[0]) * partValue + lengthToTop + mHeight / 6)));
        drawCurve(canvas,points);

        for (int i = 1; i < mTempDay.length; i = i + 3) {
            canvas.drawCircle(mWidth / 24 * i, (maxTempDay - mTempDay[i]) * partValue + lengthToTop + mHeight / 6, dp2px(context, 4), tempPointPaint);
            // 显示白天气温
            Rect rect = new Rect();
            String str = mTempDay[i] + "";
            tempTextPaint.getTextBounds(str, 0, str.length(), rect);
            float strWidth = rect.width();//字符串的宽度
            canvas.drawText(mTempDay[i] + "°", mWidth / 24 * i - strWidth / 2, (maxTempDay - mTempDay[i]) * partValue + lengthToTop + mHeight / 6 - dp2px(context, 8), tempTextPaint);
        }

    }

    private void drawText(Canvas canvas) {

        for (int i = 0; i < timeString.length; i++) {
            Rect rect = new Rect();
            String str = timeString[i];
            timeTextPaint.getTextBounds(str, 0, str.length(), rect);
            float strWidth = rect.width();//字符串的宽度
            float strHeight = rect.height();//字符串的高度
            canvas.drawText(timeString[i], mWidth / 24 - strWidth / 2 + mWidth / 24 * 3 * i, mHeight / 6 / 2 + strHeight / 2, timeTextPaint);
        }
    }


    /**
     * 画曲线.
     *
     * @param canvas
     */
    private void drawCurve(Canvas canvas, List<Point> points) {
        Path curvePath = new Path();
        points_x.clear();
        points_y.clear();
        for (int i = 0; i < points.size() ; i++) {
            points_x.add(points.get(i).x);
            points_y.add(points.get(i).y);
        }
        List<Cubic> calculate_x = calculate(points_x);
        List<Cubic> calculate_y = calculate(points_y);
        curvePath.moveTo(calculate_x.get(0).eval(0), calculate_y.get(0).eval(0));

        for (int i = 0; i < calculate_x.size(); i++) {

            for (int j = 1; j <= STEPS; j++) {
                float u = j / (float) STEPS;
                curvePath.lineTo(calculate_x.get(i).eval(u), calculate_y.get(i)
                        .eval(u));
            }

        }
        canvas.drawPath(curvePath, tempLinePaint);
    }

    private List<Cubic> calculate(List<Integer> x) {
        int n = x.size() - 1;
        float[] gamma = new float[n + 1];
        float[] delta = new float[n + 1];
        float[] D = new float[n + 1];
        int i;
        /*
		 * We solve the equation [2 1 ] [D[0]] [3(x[1] - x[0]) ] |1 4 1 | |D[1]|
		 * |3(x[2] - x[0]) | | 1 4 1 | | . | = | . | | ..... | | . | | . | | 1 4
		 * 1| | . | |3(x[n] - x[n-2])| [ 1 2] [D[n]] [3(x[n] - x[n-1])]
		 *
		 * by using row operations to convert the matrix to upper triangular and
		 * then back sustitution. The D[i] are the derivatives at the knots.
		 */

        gamma[0] = 1.0f / 2.0f;
        for (i = 1; i < n; i++) {
            gamma[i] = 1 / (4 - gamma[i - 1]);
        }
        gamma[n] = 1 / (2 - gamma[n - 1]);

        delta[0] = 3 * (x.get(1) - x.get(0)) * gamma[0];
        for (i = 1; i < n; i++) {
            delta[i] = (3 * (x.get(i + 1) - x.get(i - 1)) - delta[i - 1])
                    * gamma[i];
        }
        delta[n] = (3 * (x.get(n) - x.get(n - 1)) - delta[n - 1]) * gamma[n];

        D[n] = delta[n];
        for (i = n - 1; i >= 0; i--) {
            D[i] = delta[i] - gamma[i] * D[i + 1];
        }

		/* now compute the coefficients of the cubics */
        List<Cubic> cubics = new LinkedList<Cubic>();
        for (i = 0; i < n; i++) {
            Cubic c = new Cubic(x.get(i), D[i], 3 * (x.get(i + 1) - x.get(i))
                    - 2 * D[i] - D[i + 1], 2 * (x.get(i) - x.get(i + 1)) + D[i]
                    + D[i + 1]);
            cubics.add(c);
        }
        return cubics;
    }

    private void drawLines(Canvas canvas) {
        int currentWidth = mWidth / 24;
        for (int i = 0; i < 24; i++) {
            canvas.drawLine(currentWidth * (i + 1), mHeight / 6, currentWidth * (i + 1), mHeight, linePaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = 24 * dp2px(context, 24);
        mHeight = h;
    }

    /**
     * dp转px
     *
     * @param context
     * @return
     */
    public int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 请传入长度为24整型数组的
     *
     * @param mTempDay
     */
    public void setTemperature(int[] mTempDay) {
        this.mTempDay = mTempDay;
    }

    /**
     * 请传入长度为24整型数组的
     *
     * @param mWater
     */
    public void setWater(int[] mWater) {
        this.mWater = mWater;
    }
}
