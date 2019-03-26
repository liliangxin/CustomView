package com.gagobigdata.myview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by llx on 2019/3/25
 */
public class HorizontalLoadingView extends View {

    private Paint mBackgroundPaint;
    private Paint mSliderPaint;

    private float mStart;
    private float mStop;

    private float mLineWidth;
    private float mSliderWidth;
    private int mBackgroundColor;
    private int mSliderColor;

    public HorizontalLoadingView(Context context) {
        super(context);
    }

    public HorizontalLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HorizontalLoadingView);
        mLineWidth = ta.getDimension(R.styleable.HorizontalLoadingView_horizontalLoadingViewLineWidth, Utils.dpToPixel(5));
        mSliderWidth = ta.getDimension(R.styleable.HorizontalLoadingView_horizontalLoadingViewSliderWidth, Utils.dpToPixel(200));
        mStart = -mSliderWidth;
        mStop = 0;
        mBackgroundColor = ta.getColor(R.styleable.HorizontalLoadingView_horizontalLoadingViewBackgroundColor, Color.GRAY);
        mSliderColor = ta.getColor(R.styleable.HorizontalLoadingView_horizontalLoadingViewSliderColor, Color.BLACK);
        ta.recycle();
        init();
    }

    private void init() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStrokeWidth(mLineWidth);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);

        mSliderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSliderPaint.setStrokeWidth(mLineWidth);
        mSliderPaint.setColor(mSliderColor);
        mSliderPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        canvas.drawLine(mLineWidth / 2, height / 2, width - mLineWidth / 2 , height / 2, mBackgroundPaint);
        canvas.drawLine(mStart + mLineWidth / 2, height / 2, mStop - mLineWidth / 2 , height / 2, mSliderPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(new Runnable() {
            @Override
            public void run() {
                startAnimator();
            }
        });

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    private void startAnimator() {
        int measuredWidth = getMeasuredWidth();
        ObjectAnimator startAnimator = ObjectAnimator.ofFloat(this, "start", mStart, measuredWidth + mSliderWidth);
        startAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });

        ObjectAnimator stopAnimator = ObjectAnimator.ofFloat(this, "stop", mStop, measuredWidth + mSliderWidth * 2);
        stopAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(startAnimator, stopAnimator);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mStart = -mSliderWidth;
                mStop = 0;
                startAnimator();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.start();

    }

    public float getStart() {
        return mStart;
    }

    public void setStart(float start) {
        this.mStart = start;
    }

    public float getStop() {
        return mStop;
    }

    public void setStop(float stop) {
        this.mStop = stop;
    }
}
