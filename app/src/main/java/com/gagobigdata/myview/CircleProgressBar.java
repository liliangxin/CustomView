package com.gagobigdata.myview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by llx on 2019/3/25
 */
public class CircleProgressBar extends View {

    private Paint mProgressPaint;
    private Paint mTextPaint;

    private int mSize;

    private int mProgress;// 0 - 100
    private float mThickness;
    private float mTextSize;
    private int mProgressColor;
    private int mTextColor;
    private boolean mIsShowText;

    private ObjectAnimator mProgressAnimator;

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mThickness = ta.getDimension(R.styleable.CircleProgressBar_circleProgressBarThickness, Utils.dpToPixel(5));
        mTextSize = ta.getDimension(R.styleable.CircleProgressBar_circleProgressBarTextSize, 15);
        mProgressColor = ta.getColor(R.styleable.CircleProgressBar_circleProgressBarProgressColor, Color.BLUE);
        mTextColor = ta.getColor(R.styleable.CircleProgressBar_circleProgressBarTextColor, Color.BLACK);
        mIsShowText = ta.getBoolean(R.styleable.CircleProgressBar_circleProgressBarShowText, false);
        ta.recycle();
        init();
    }

    private void init() {
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStrokeWidth(mThickness);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int width = getMeasuredWidth() - paddingLeft - paddingRight;
        int height = getMeasuredHeight() - paddingTop - paddingBottom;
        mSize = Math.min(width, height);
        setMeasuredDimension(mSize + paddingLeft + paddingRight,
                mSize + paddingTop + paddingBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        RectF rect = new RectF();
        rect.top = mThickness + paddingTop;
        rect.left = mThickness + paddingLeft;
        rect.bottom = getMeasuredHeight() - mThickness - paddingBottom;
        rect.right = getMeasuredWidth() - mThickness - paddingRight;
        canvas.drawArc(rect, -90, 360 * mProgress / 100, false, mProgressPaint);

        if (mIsShowText) {
            String text = mProgress + "%";
            Rect bounds = new Rect();
            mTextPaint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, 0, text.length(),
                    mSize / 2 - (bounds.right - bounds.left) / 2,
                    mSize / 2 + (bounds.bottom - bounds.top) / 2,
                    mTextPaint);
        }

    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            this.mProgress = 100;
        } else {
            this.mProgress = progress;
        }
        invalidate();
    }

    public void startAnim() {
        mProgressAnimator = getProgressAnimator();
        mProgressAnimator.start();
    }

    private ObjectAnimator getProgressAnimator() {
        mProgressAnimator = ObjectAnimator.ofInt(this, "progress", 0, mProgress);
        mProgressAnimator.setInterpolator(new LinearInterpolator());
        mProgressAnimator.setDuration(1500);
        return mProgressAnimator;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mProgressAnimator != null) {
            mProgressAnimator.cancel();
        }
    }

    public void setCenterTextColor(int textColor) {
        mTextPaint.setColor(textColor);
        invalidate();
    }

    public void setProgressColor(int progressColor) {
        mProgressPaint.setColor(progressColor);
        invalidate();
    }

    public void setProgressThickness(float thickness) {
        mProgressPaint.setStrokeWidth(thickness);
        invalidate();
    }

    public void setCenterTextSize(float textSize) {
        mTextPaint.setTextSize(textSize);
        invalidate();
    }

    public void setIsShowText(boolean isShowText) {
        this.mIsShowText = isShowText;
        invalidate();
    }
}
