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
public class CircularProgressView extends View {

    private static final float INDETERMINANT_MIN_SWEEP = 15f;

    private Paint mPaint;
    private int mSize = 0;
    private RectF mBounds;

    private float indeterminateSweep;
    private float indeterminateRotateOffset;
    private int thickness;
    private int color;
    private int animDuration = 4000;
    private int animSteps = 3;

    private float startAngle = -90;
    private ValueAnimator startAngleRotate;
    private ValueAnimator progressAnimator;
    private AnimatorSet indeterminateAnimator;

    public CircularProgressView(Context context) {
        super(context);
        init(null, 0);
    }

    public CircularProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircularProgressView(Context context, AttributeSet attrs, int defStyle) {
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
                attrs, R.styleable.CircularProgressView, defStyle, 0);
        thickness = a.getDimensionPixelSize(R.styleable.CircularProgressView_cpv_thickness, (int) Utils.dpToPixel(5));
        color = a.getColor(R.styleable.CircularProgressView_cpv_color, Color.parseColor("#2196F3"));
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
        mBounds.set(paddingLeft + thickness, paddingTop + thickness, mSize - paddingLeft - thickness, mSize - paddingTop - thickness);
    }

    private void updatePaint() {
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(thickness);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mBounds, startAngle + indeterminateRotateOffset, indeterminateSweep, false, mPaint);
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
        updatePaint();
        updateBounds();
        invalidate();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        updatePaint();
        invalidate();
    }

    public void startAnimation() {
        resetAnimation();
    }

    public void resetAnimation() {
        // Cancel all the old animators
        if (startAngleRotate != null && startAngleRotate.isRunning())
            startAngleRotate.cancel();
        if (progressAnimator != null && progressAnimator.isRunning())
            progressAnimator.cancel();
        if (indeterminateAnimator != null && indeterminateAnimator.isRunning())
            indeterminateAnimator.cancel();

        indeterminateSweep = INDETERMINANT_MIN_SWEEP;
        // Build the whole AnimatorSet
        indeterminateAnimator = new AnimatorSet();
        AnimatorSet prevSet = null, nextSet;
        for (int k = 0; k < animSteps; k++) {
            nextSet = createIndeterminateAnimator(k);
            AnimatorSet.Builder builder = indeterminateAnimator.play(nextSet);
            if (prevSet != null)
                builder.after(prevSet);
            prevSet = nextSet;
        }

        // Listen to end of animation so we can infinitely loop
        indeterminateAnimator.addListener(new AnimatorListenerAdapter() {
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
        indeterminateAnimator.start();


    }

    /**
     * Stops the animation
     */

    public void stopAnimation() {
        if (startAngleRotate != null) {
            startAngleRotate.cancel();
            startAngleRotate = null;
        }
        if (progressAnimator != null) {
            progressAnimator.cancel();
            progressAnimator = null;
        }
        if (indeterminateAnimator != null) {
            indeterminateAnimator.cancel();
            indeterminateAnimator = null;
        }
    }

    // Creates the animators for one step of the animation
    private AnimatorSet createIndeterminateAnimator(float step) {
        final float maxSweep = 360f * (animSteps - 1) / animSteps + INDETERMINANT_MIN_SWEEP;
        final float start = -90f + step * (maxSweep - INDETERMINANT_MIN_SWEEP);

        // Extending the front of the arc
        ValueAnimator frontEndExtend = ValueAnimator.ofFloat(INDETERMINANT_MIN_SWEEP, maxSweep);
        frontEndExtend.setDuration(animDuration / animSteps / 2);
        frontEndExtend.setInterpolator(new DecelerateInterpolator(1));
        frontEndExtend.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indeterminateSweep = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });

        // Overall rotation
        ValueAnimator rotateAnimator1 = ValueAnimator.ofFloat(step * 720f / animSteps, (step + .5f) * 720f / animSteps);
        rotateAnimator1.setDuration(animDuration / animSteps / 2);
        rotateAnimator1.setInterpolator(new LinearInterpolator());
        rotateAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indeterminateRotateOffset = (Float) animation.getAnimatedValue();
            }
        });

        // Followed by...

        // Retracting the back end of the arc
        ValueAnimator backEndRetract = ValueAnimator.ofFloat(start, start + maxSweep - INDETERMINANT_MIN_SWEEP);
        backEndRetract.setDuration(animDuration / animSteps / 2);
        backEndRetract.setInterpolator(new DecelerateInterpolator(1));
        backEndRetract.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startAngle = (Float) animation.getAnimatedValue();
                indeterminateSweep = maxSweep - startAngle + start;
                invalidate();
            }
        });

        // More overall rotation
        ValueAnimator rotateAnimator2 = ValueAnimator.ofFloat((step + .5f) * 720f / animSteps, (step + 1) * 720f / animSteps);
        rotateAnimator2.setDuration(animDuration / animSteps / 2);
        rotateAnimator2.setInterpolator(new LinearInterpolator());
        rotateAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indeterminateRotateOffset = (Float) animation.getAnimatedValue();
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
