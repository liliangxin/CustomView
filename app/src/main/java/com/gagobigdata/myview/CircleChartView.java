package com.gagobigdata.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by llx on 2017/12/4.
 */

public class CircleChartView extends View {

    private int radius; //圆环最大半径
    private Paint backPaint;
    private Paint linePaint; //线画笔
    private Paint circlePaint; //圆画笔
    private Paint arcPaint; // 中间弧画笔
    private Paint inArcPaint; //内部弧画笔
    private float outProgress = 0;
    private float centerProgress = 0; //中间弧初始角度
    private float innerProgress = 0;//内部弧初始角度
    private int height; //控件高度
    private int width; //控件宽度
    private float circleWidth;
    private int backColor;
    private int outCircleColor;
    private int centerCircleColor;
    private int innerCircleColor;

    public CircleChartView(Context context) {
        this(context, null);
    }

    public CircleChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray  = context.obtainStyledAttributes(attrs, R.styleable.CircleChartView);
        circleWidth = typedArray.getDimension(R.styleable.CircleChartView_circle_width, 30);
        backColor = typedArray.getColor(R.styleable.CircleChartView_back_circle_color, Color.parseColor("#d8dfe6"));
        outCircleColor = typedArray.getColor(R.styleable.CircleChartView_out_circle_color, Color.parseColor("#66ffef5c"));
        centerCircleColor = typedArray.getColor(R.styleable.CircleChartView_center_circle_color, Color.parseColor("#4ec288"));
        innerCircleColor = typedArray.getColor(R.styleable.CircleChartView_inner_circle_color, Color.parseColor("#638fff"));
        typedArray.recycle();
        init();
    }

    private void init() {

        float strokeWidth = CircleChartUtil.dpToPx(circleWidth);

        backPaint = new Paint();
        backPaint.setAntiAlias(true);
        backPaint.setColor(backColor);
        backPaint.setStrokeWidth(strokeWidth);
        backPaint.setStyle(Paint.Style.STROKE);
        backPaint.setStrokeCap(Paint.Cap.ROUND);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(backColor);
        linePaint.setStrokeWidth(4);
        linePaint.setStrokeCap(Paint.Cap.ROUND);

        circlePaint = new Paint();
        circlePaint.setColor(outCircleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(strokeWidth);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);

        arcPaint = new Paint();
        arcPaint.setColor(centerCircleColor);
        arcPaint.setAntiAlias(true);
        arcPaint.setStrokeWidth(strokeWidth);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);

        inArcPaint = new Paint();
        inArcPaint.setColor(innerCircleColor);
        inArcPaint.setAntiAlias(true);
        inArcPaint.setStrokeWidth(strokeWidth);
        inArcPaint.setStyle(Paint.Style.STROKE);
        inArcPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = w / 2;
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawGrayCircle(canvas);
        drawCircle(canvas);
        drawArc(canvas);
    }

    /**
     * 灰色背景圆的绘制
     * @param canvas
     */
    private void drawGrayCircle(Canvas canvas) {
        canvas.drawCircle(width / 2, height / 2,radius -20,backPaint);
        canvas.drawCircle(width / 2, height / 2,(radius -20)/3*2,backPaint);
        canvas.drawCircle(width / 2, height / 2,(radius -20)/3,backPaint);
    }

    /**
     * 圆弧的绘制
     * @param canvas
     */
    private void drawArc(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.set(radius - (radius-20)/3*2,radius - (radius-20)/3*2,radius +(radius-20)/3*2,radius +(radius-20)/3*2);
        canvas.drawArc(rectF,-90,centerProgress,false,arcPaint);

        RectF rectF1 = new RectF();
        rectF1.set(radius - (radius-20)/3,radius - (radius-20)/3,radius +(radius-20)/3,radius +(radius-20)/3);
        canvas.drawArc(rectF1,-90,innerProgress,false,inArcPaint);
    }

    private void drawCircle(Canvas canvas) {
        RectF rectF1 = new RectF();
        rectF1.set(radius-(radius-20),radius-(radius-20),radius +(radius-20),radius +(radius-20));
        canvas.drawArc(rectF1,-90,outProgress,false,circlePaint);
    }

    /**
     * 放射线绘制
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        canvas.drawLine(width / 2, height / 2, width / 2, 0, linePaint);
        for (int i = 1; i < 10; i++) {
            canvas.save();
            canvas.rotate(36*i,width / 2, height / 2);
            canvas.drawLine(width / 2, height / 2, width / 2, 0, linePaint);
            canvas.restore();
        }
    }

    public float getOutProgress() {
        return outProgress;
    }

    public void setOutProgress(float outProgress) {
        this.outProgress = outProgress;
        invalidate();
    }

    public float getCenterProgress() {
        return centerProgress;
    }

    public void setCenterProgress(float centerProgress) {
        this.centerProgress = centerProgress;
        invalidate();
    }

    public float getInnerProgress() {
        return innerProgress;
    }

    public void setInnerProgress(float innerProgress) {
        this.innerProgress = innerProgress;
        invalidate();
    }
}
