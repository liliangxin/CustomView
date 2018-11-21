package com.gagobigdata.myview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * Created by llx on 2018/10/30.
 */
public class CustomEditText extends android.support.v7.widget.AppCompatEditText {

    private boolean mFloating;
    private float mFloatingTextSize; // 悬浮文字大小
    private float mFloatingTextOffset = Utils.dpToPixel(2); // 悬浮文字距离空间顶部的距离
    private float mSpacing = Utils.dpToPixel(10);// 悬浮文字与正常输入框的距离
    private float mFloatingHorizontalOffset;
    private int mFloatingTextColor;
    private Rect mRect = new Rect();
    private Paint mPaint;
    private boolean mFloatingShown;
    private float mFloatingFraction;
    private ObjectAnimator mAnimator;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mFloatingHorizontalOffset = getPaddingLeft();
        mFloatingTextSize = Utils.dpToPixel(15);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mFloatingTextColor);
        mPaint.setTextSize(mFloatingTextSize);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mFloating) {
                    if (TextUtils.isEmpty(s) && mFloatingShown) {
                        // 消失动画
                        getAnimator().reverse();
                        mFloatingShown = !mFloatingShown;
                    } else if (!TextUtils.isEmpty(s) && !mFloatingShown) {
                        //显示动画
                        getAnimator().start();
                        mFloatingShown = !mFloatingShown;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setAlpha((int) (255 * mFloatingFraction));
        float extraOffset = (mSpacing +Utils.dpToPixel(15)) * (1 - mFloatingFraction);
        canvas.drawText(getHint().toString(),
                mFloatingHorizontalOffset,
                mFloatingTextOffset + mFloatingTextSize + extraOffset,
                mPaint);

    }

    public void setFloating(boolean floating){
        this.mFloating = floating;
        getBackground().getPadding(mRect);
        if (mFloating) {
            setPadding(getPaddingLeft(),
                    (int) (mRect.top + mFloatingTextSize + mFloatingTextOffset + mSpacing),
                    getPaddingRight(),
                    getPaddingBottom());
        } else {
            setPadding(getPaddingLeft(),
                    mRect.top,
                    getPaddingRight(),
                    getPaddingBottom());
        }
    }

    public void setFloatingTextColor(int floatingTextColor){
        this.mFloatingTextColor = floatingTextColor;
        mPaint.setColor(mFloatingTextColor);
//        invalidate();
    }

    private ObjectAnimator getAnimator(){
        if (mAnimator == null) {
            mAnimator = ObjectAnimator.ofFloat(this,"mFloatingFraction",0,1);
        }
        return mAnimator;
    }

    public float getMFloatingFraction() {
        return mFloatingFraction;
    }

    public void setMFloatingFraction(float mFloatingFraction) {
        this.mFloatingFraction = mFloatingFraction;
        invalidate();
    }
}
