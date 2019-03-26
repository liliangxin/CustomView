package com.gagobigdata.myview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by llx on 2019/3/21
 */
public class CustomCircleLoadingView extends View {

    private static final float INDETERMINANT_MIN_SWEEP = 15f;

    private Paint mPaint;
    private int mSize = 0;
    private RectF mBounds;

    private float mIndeterminateSweep;
    private float mIndeterminateRotateOffset;
    private int mThickness;
    private int mColor;
    private int mAnimDuration = 4000;
    private int mAnimSteps = 3;

    private float mStartAngle = -90;
    private ValueAnimator mStartAngleRotate;
    private ValueAnimator mProgressAnimator;
    private AnimatorSet mIndeterminateAnimator;

    public CustomCircleLoadingView(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomCircleLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomCircleLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    protected void init(AttributeSet attrs, int defStyle) {

        initAttributes(attrs, defStyle);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        updatePaint();
        mBounds = new RectF();
    }

    private void initAttributes(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CustomCircleLoadingView, defStyle, 0);
        mThickness = a.getDimensionPixelSize(R.styleable.CustomCircleLoadingView_circleLoadingView_thickness, (int) Utils.dpToPixel(5));
        mColor = a.getColor(R.styleable.CustomCircleLoadingView_circleLoadingView_color, Color.parseColor("#2196F3"));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int xPad = getPaddingLeft() + getPaddingRight();
        int yPad = getPaddingTop() + getPaddingBottom();
        int width = getMeasuredWidth() - xPad;
        int height = getMeasuredHeight() - yPad;
        mSize = (width < height) ? width : height;
        setMeasuredDimension(mSize + xPad, mSize + yPad);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSize = (w < h) ? w : h;
        updateBounds();
    }

    private void updateBounds() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        mBounds.set(paddingLeft + mThickness, paddingTop + mThickness, mSize - paddingLeft - mThickness, mSize - paddingTop - mThickness);
    }

    private void updatePaint() {
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mThickness);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mBounds, mStartAngle + mIndeterminateRotateOffset, mIndeterminateSweep, false, mPaint);
    }

    public int getThickness() {
        return mThickness;
    }

    public void setThickness(int thickness) {
        this.mThickness = thickness;
        updatePaint();
        updateBounds();
        invalidate();
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
        updatePaint();
        invalidate();
    }

    public void startAnimation() {
        resetAnimation();
    }

    public void resetAnimation() {
        // Cancel all the old animators
        if (mStartAngleRotate != null && mStartAngleRotate.isRunning())
            mStartAngleRotate.cancel();
        if (mProgressAnimator != null && mProgressAnimator.isRunning())
            mProgressAnimator.cancel();
        if (mIndeterminateAnimator != null && mIndeterminateAnimator.isRunning())
            mIndeterminateAnimator.cancel();

        mIndeterminateSweep = INDETERMINANT_MIN_SWEEP;
        // Build the whole AnimatorSet
        mIndeterminateAnimator = new AnimatorSet();
        AnimatorSet prevSet = null, nextSet;
        for (int k = 0; k < mAnimSteps; k++) {
            nextSet = createIndeterminateAnimator(k);
            AnimatorSet.Builder builder = mIndeterminateAnimator.play(nextSet);
            if (prevSet != null)
                builder.after(prevSet);
            prevSet = nextSet;
        }

        // Listen to end of animation so we can infinitely loop
        mIndeterminateAnimator.addListener(new AnimatorListenerAdapter() {
            boolean wasCancelled = false;

            @Override
            public void onAnimationCancel(Animator animation) {
                wasCancelled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!wasCancelled)
                    resetAnimation();
            }
        });
        mIndeterminateAnimator.start();


    }

    /**
     * Stops the animation
     */

    public void stopAnimation() {
        if (mStartAngleRotate != null) {
            mStartAngleRotate.cancel();
            mStartAngleRotate = null;
        }
        if (mProgressAnimator != null) {
            mProgressAnimator.cancel();
            mProgressAnimator = null;
        }
        if (mIndeterminateAnimator != null) {
            mIndeterminateAnimator.cancel();
            mIndeterminateAnimator = null;
        }
    }

    // Creates the animators for one step of the animation
    private AnimatorSet createIndeterminateAnimator(float step) {
        final float maxSweep = 360f * (mAnimSteps - 1) / mAnimSteps + INDETERMINANT_MIN_SWEEP;
        final float start = -90f + step * (maxSweep - INDETERMINANT_MIN_SWEEP);

        // Extending the front of the arc
        ValueAnimator frontEndExtend = ValueAnimator.ofFloat(INDETERMINANT_MIN_SWEEP, maxSweep);
        frontEndExtend.setDuration(mAnimDuration / mAnimSteps / 2);
        frontEndExtend.setInterpolator(new DecelerateInterpolator(1));
        frontEndExtend.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIndeterminateSweep = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });

        // Overall rotation
        ValueAnimator rotateAnimator1 = ValueAnimator.ofFloat(step * 720f / mAnimSteps, (step + .5f) * 720f / mAnimSteps);
        rotateAnimator1.setDuration(mAnimDuration / mAnimSteps / 2);
        rotateAnimator1.setInterpolator(new LinearInterpolator());
        rotateAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIndeterminateRotateOffset = (Float) animation.getAnimatedValue();
            }
        });

        // Followed by...

        // Retracting the back end of the arc
        ValueAnimator backEndRetract = ValueAnimator.ofFloat(start, start + maxSweep - INDETERMINANT_MIN_SWEEP);
        backEndRetract.setDuration(mAnimDuration / mAnimSteps / 2);
        backEndRetract.setInterpolator(new DecelerateInterpolator(1));
        backEndRetract.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStartAngle = (Float) animation.getAnimatedValue();
                mIndeterminateSweep = maxSweep - mStartAngle + start;
                invalidate();
            }
        });

        // More overall rotation
        ValueAnimator rotateAnimator2 = ValueAnimator.ofFloat((step + .5f) * 720f / mAnimSteps, (step + 1) * 720f / mAnimSteps);
        rotateAnimator2.setDuration(mAnimDuration / mAnimSteps / 2);
        rotateAnimator2.setInterpolator(new LinearInterpolator());
        rotateAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIndeterminateRotateOffset = (Float) animation.getAnimatedValue();
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.play(frontEndExtend).with(rotateAnimator1);
        set.play(backEndRetract).with(rotateAnimator2).after(rotateAnimator1);
        return set;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    @Override
    public void setVisibility(int visibility) {
        int currentVisibility = getVisibility();
        super.setVisibility(visibility);
        if (visibility != currentVisibility) {
            if (visibility == View.VISIBLE) {
                resetAnimation();
            } else if (visibility == View.GONE || visibility == View.INVISIBLE) {
                stopAnimation();
            }
        }
    }
}
