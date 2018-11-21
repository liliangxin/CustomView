package com.gagobigdata.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by llx on 2018/1/15.
 */

public class WeekWeatherView extends View {
    /**
     * 重要参数，两点之间分为几段描画，数字愈大分段越多，描画的曲线就越精细.
     */
    private static final int STEPS = 12;
    List<Integer> points_x;
    List<Integer> points_y;
    /**
     * x轴集合
     */
    private float mXAxis[] = new float[7];
    /**
     * 白天y轴集合
     */
    private float mYAxisDay[] = new float[7];
    /**
     * 夜间y轴集合
     */
    private float mYAxisNight[] = new float[7];
    /**
     * x,y轴集合数
     */
    private static final int LENGTH = 7;
    /**
     * 白天温度集合
     */
    private int mTempDay[] = new int[7];
    /**
     * 夜间温度集合
     */
    private int mTempNight[] = new int[7];
    private int width;
    private int height;
    private Context context;
    private Paint textPaint;
    private Paint linePaint;
    private Paint tempPaint;
    private Paint pointPaint;
    private Paint colorPaint;
    private Paint tempLinePaint;


    public WeekWeatherView(Context context) {
        this(context, null);
    }

    public WeekWeatherView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekWeatherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {

        points_x = new LinkedList<>();
        points_y = new LinkedList<>();

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(dp2px(context, 12));
        textPaint.setColor(Color.parseColor("#212121"));

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor("#dcdfe6"));

        tempPaint = new Paint();
        tempPaint.setAntiAlias(true);
        tempPaint.setTextSize(dp2px(context, 12));
        tempPaint.setColor(Color.parseColor("#a5212121"));

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(Color.RED);


        tempLinePaint = new Paint();
        tempLinePaint.setAntiAlias(true);
        tempLinePaint.setColor(Color.RED);
        tempLinePaint.setStrokeWidth(dp2px(context, 1));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        float wh = width / 14;
        mXAxis[0] = wh;
        mXAxis[1] = wh * 3;
        mXAxis[2] = wh * 5;
        mXAxis[3] = wh * 7;
        mXAxis[4] = wh * 9;
        mXAxis[5] = wh * 11;
        mXAxis[6] = wh * 13;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLines(canvas);
        drawText(canvas);
        // 计算y轴集合数值
        computeYAxisValues();
        //填充颜色
        addColor(canvas);
        // 画白天折线图
        drawChart(canvas, Color.RED, mTempDay, mYAxisDay, 0);
        // 画夜间折线图
        drawChart(canvas, Color.BLUE, mTempNight, mYAxisNight, 1);

    }

    private void addColor(Canvas canvas) {
        List<Point> points = new LinkedList<>();
        colorPaint = new Paint();
        colorPaint.setAlpha(125);
        colorPaint.setAntiAlias(true);
        colorPaint.setStyle(Paint.Style.FILL);
        Shader shader = new LinearGradient(0,0,0,height,Color.parseColor("#f71b23"),Color.parseColor("#4785ff"),Shader.TileMode.CLAMP);
        colorPaint.setShader(shader);
        Path path = new Path();
        for (int i = 0; i < mXAxis.length; i++) {
            points.add(new Point((int)mXAxis[i],(int)mYAxisDay[i]));
        }
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

        path.lineTo((int)mXAxis[6],(int)mYAxisNight[6]);

        points.clear();
        for (int i = mYAxisNight.length - 1; i >=0 ; i--) {
            points.add(new Point((int)mXAxis[i],(int)mYAxisNight[i]));
        }

        points_x.clear();
        points_y.clear();
        for (int i = 0; i < points.size(); i++) {
            points_x.add(points.get(i).x);
            points_y.add(points.get(i).y);
        }
        List<Cubic> calculate_x1 = calculate(points_x);
        List<Cubic> calculate_y1 = calculate(points_y);

        for (int i = 0; i < calculate_x1.size(); i++) {

                for (int j = 1; j <= STEPS; j++) {
                    float u = j / (float) STEPS;
                    path.lineTo(calculate_x1.get(i).eval(u), calculate_y1.get(i)
                            .eval(u));
                }

        }
        path.close();
        canvas.drawPath(path, colorPaint);
    }

    /**
     * 计算y轴集合数值
     */
    private void computeYAxisValues() {
        // 存放白天最低温度
        int minTempDay = mTempDay[0];
        // 存放白天最高温度
        int maxTempDay = mTempDay[0];
        for (int item : mTempDay) {
            if (item < minTempDay) {
                minTempDay = item;
            }
            if (item > maxTempDay) {
                maxTempDay = item;
            }
        }

        // 存放夜间最低温度
        int minTempNight = mTempNight[0];
        // 存放夜间最高温度
        int maxTempNight = mTempNight[0];
        for (int item : mTempNight) {
            if (item < minTempNight) {
                minTempNight = item;
            }
            if (item > maxTempNight) {
                maxTempNight = item;
            }
        }
        // 白天，夜间中的最低温度
        int minTemp = minTempNight < minTempDay ? minTempNight : minTempDay;
        // 白天，夜间中的最高温度
        int maxTemp = maxTempDay > maxTempNight ? maxTempDay : maxTempNight;
        // 份数（白天，夜间综合温差）
        float parts = maxTemp - minTemp;
        // y轴一端到控件一端的距离
        float length = dp2px(context,20);
        // y轴高度
        float yAxisHeight = height / 6 * 5 - length * 2;
        // 当温度都相同时（被除数不能为0）
        if (parts == 0) {
            for (int i = 0; i < LENGTH; i++) {
                mYAxisDay[i] = height / 6 * 5 / 2;
                mYAxisNight[i] = height / 6 * 5 / 2;
            }
        } else {
            float partValue = yAxisHeight / parts;
            for (int i = 0; i < LENGTH; i++) {
                mYAxisDay[i] = height - length - partValue * (mTempDay[i] - minTemp);
                mYAxisNight[i] = height - length - partValue * (mTempNight[i] - minTemp);
            }
        }
    }

    /**
     * 画折线图
     *
     * @param canvas 画布
     * @param color  画图颜色
     * @param temp   温度集合
     * @param yAxis  y轴集合
     * @param type   折线种类：0，白天；1，夜间
     */
    private void drawChart(Canvas canvas, int color, int temp[], float[] yAxis, int type) {

        List<Point> point = new ArrayList<>();

        pointPaint.setColor(color);
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{10f, 20f}, 0);
        tempLinePaint.setColor(color);
        tempLinePaint.setStrokeWidth(dp2px(context, 1));
        tempLinePaint.setStyle(Paint.Style.STROKE);
        tempLinePaint.setStrokeCap(Paint.Cap.ROUND);
        tempLinePaint.setAntiAlias(true);
        tempLinePaint.setDither(true);
        tempLinePaint.setPathEffect(dashPathEffect);

        for (int i = 0; i < LENGTH; i++) {
            point.add(new Point((int)mXAxis[i],(int)yAxis[i]));
            canvas.drawCircle(mXAxis[i], yAxis[i], dp2px(context, 4), pointPaint);
            drawText(canvas, tempPaint, i, temp, yAxis, type);
        }
        point.add(new Point((int)mXAxis[mXAxis.length-1],(int)yAxis[yAxis.length - 1]));
        drawCurve(canvas,point);
    }

    /**
     * 绘制文字
     *
     * @param canvas    画布
     * @param textPaint 画笔
     * @param i         索引
     * @param temp      温度集合
     * @param yAxis     y轴集合
     * @param type      折线种类：0，白天；1，夜间
     */
    private void drawText(Canvas canvas, Paint textPaint, int i, int[] temp, float[] yAxis, int type) {
        // 显示白天气温
        Rect rect = new Rect();
        String str = temp[i]+"";
        textPaint.getTextBounds(str, 0, str.length(), rect);
        float strWidth = rect.width();//字符串的宽度
        switch (type) {
            case 0:
                canvas.drawText(temp[i] + "°", mXAxis[i] - strWidth/2, yAxis[i] - dp2px(context, 8), textPaint);
                break;
            case 1:
                canvas.drawText(temp[i] + "°", mXAxis[i]- strWidth/2, yAxis[i] + dp2px(context, 15), textPaint);
                break;
        }
    }


    private void drawText(Canvas canvas) {
        String[] date = new String[7];
        date[0] = "昨天";
        date[1] = "今天";
        date[2] = "明天";
        date[3] = getPastDate(2);
        date[4] = getPastDate(3);
        date[5] = getPastDate(4);
        date[6] = getPastDate(5);

        for (int i = 0; i < date.length; i++) {
            Rect rect = new Rect();
            String str = date[i];
            textPaint.getTextBounds(str, 0, str.length(), rect);
            float strWidth = rect.width();//字符串的宽度
            float strHeight = rect.height();//字符串的高度
            canvas.drawText(date[i], width / 14 - strWidth / 2 + width / 7 * i, height / 6 / 2 + strHeight / 2, textPaint);
        }
    }

    private void drawLines(Canvas canvas) {
        int currentWidth = width / 7;
        for (int i = 0; i < 6; i++) {
            canvas.drawLine(currentWidth * (i + 1), 0, currentWidth * (i + 1), height, linePaint);
        }
    }

    /**
     * 获取过去第几天的日期(- 操作) 或者 未来 第几天的日期( + 操作)
     *
     * @param past
     * @return
     */
    public String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        return format.format(today);
    }

    /**
     * dp转px
     *
     * @param context *
     * @return
     */
    public int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 画曲线.
     *
     * @param canvas
     */
    private void drawCurve(Canvas canvas,List<Point> points) {
        Path curvePath = new Path();
        points_x.clear();
        points_y.clear();
        for (int i = 0; i < points.size() - 1; i++) {
            points_x.add(points.get(i).x);
            points_y.add(points.get(i).y);
        }
        List<Cubic> calculate_x = calculate(points_x);
        List<Cubic> calculate_y = calculate(points_y);

        for (int i = 0; i < calculate_x.size(); i++) {
            if (i==0){
                curvePath.moveTo(calculate_x.get(0).eval(0), calculate_y.get(0).eval(0));
                for (int j = 1; j <= STEPS; j++) {
                    float u = j / (float) STEPS;
                    curvePath.lineTo(calculate_x.get(i).eval(u), calculate_y.get(i)
                            .eval(u));
                }
                canvas.drawPath(curvePath, tempLinePaint);
            } else {
                if (i==1){
                    curvePath.reset();
                    curvePath.moveTo(calculate_x.get(1).eval(0), calculate_y.get(1).eval(0));
                    tempLinePaint.setPathEffect(null);
                }

                for (int j = 1; j <= STEPS; j++) {
                    float u = j / (float) STEPS;
                    curvePath.lineTo(calculate_x.get(i).eval(u), calculate_y.get(i)
                            .eval(u));
                }
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

    /**
     * 设置白天温度
     *
     * @param tempDay 温度数组集合
     */
    public void setTempDay(int[] tempDay) {
        mTempDay = tempDay;
    }

    /**
     * 设置夜间温度
     *
     * @param tempNight 温度数组集合
     */
    public void setTempNight(int[] tempNight) {
        mTempNight = tempNight;
    }
}
