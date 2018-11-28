/*
 * Copyright (C) 2018 Beijing GAGO Technology Ltd.
 */

package com.gagobigdata.myview.segment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.gagobigdata.myview.R;
import com.gagobigdata.myview.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by llx 2018/8/22.
 *
 */
public class SegmentedControlView extends View implements ISegmentedControl {

    /**
     * 圆角模式时 默认的圆角弧度
     */
    private static final int DEFAULT_RADIUS = 10;
    /**
     * 整个view背景色 默认灰色
     */
    private static final int DEFAULT_OUTER_COLOR = Color.GRAY;
    /**
     * 默认未被选中时的文字颜色
     */
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    /**
     * 默认选中时的文字颜色
     */
    private static final int DEFAULT_SELECTED_TEXT_COLOR = Color.WHITE;
    /**
     * 渐变色开始及结束颜色
     */
    private static final int DEFAULT_SHADER_START_COLOR = Color.RED;
    private static final int DEFAULT_SHADER_END_COLOR = Color.GREEN;
    /**
     * 滑动动画的次序事件
     */
    private static final int ANIMATION_DURATION = 300;

    /**
     * mode is ROUND
     */
    private static final int ROUND = 0;

    /**
     * mode is CIRCLE;
     */
    private static final int CIRCLE = 1;

    @IntDef({ROUND, CIRCLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    /**
     * The radius
     */
    private float mRadius;

    /**
     * The whole background's color
     */
    private int mOuterColor;
    /**
     * Item outer left or right margin
     */
    private int mItemMarginLeft;
    /**
     * Item outer top or bottom margin
     */
    private int mItemMarginTop;
    /**
     * The item background's shader start color
     */
    private int mItemShaderStartColor;
    /**
     * The item background's shader end color
     */
    private int mItemShaderEndColor;
    /**
     * Item's text size
     */
    private float mTextSize;
    /**
     * The unselected font color
     */
    private int mTextColor;
    /**
     * The selected font color
     */
    private int mSelectedTextColor;
    /**
     * Selected item position
     */
    private int mSelectedItem;
    /**
     * The mode(CIRCLE or ROUND)
     */
    private int mMode = ROUND;

    private boolean mScrollEnable = true;

    private int mStart;
    private int mEnd;
    private int mHeight;
    private int mItemWidth;
    private int mMaximumFlingVelocity;
    private RectF mRectF;
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mItemPaint;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private List<SegmentedControlItem> mSegmentedControlItems = new ArrayList<>();
    private OnSegItemClickListener mListener;

    /**
     * item 点击事件
     */
    public interface OnSegItemClickListener {
        /**
         * 点击事件
         *
         * @param item     item
         * @param position position
         */
        void onItemClick(SegmentedControlItem item, int position);
    }

    public SegmentedControlView(Context context) {
        this(context, null);
    }

    public SegmentedControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (isInEditMode()) {
            return;
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SegmentedControlView);
        if (ta == null) {
            return;
        }
        mRadius = ta.getDimension(R.styleable.SegmentedControlView_segRadius, Utils.dpToPixel(DEFAULT_RADIUS));
        mOuterColor = ta.getColor(R.styleable.SegmentedControlView_segOuterColor,DEFAULT_OUTER_COLOR);
        mItemShaderStartColor = ta.getColor(R.styleable.SegmentedControlView_segShaderStartColor,DEFAULT_SHADER_START_COLOR);
        mItemShaderEndColor = ta.getColor(R.styleable.SegmentedControlView_segShaderEndColor,DEFAULT_SHADER_END_COLOR);
        mSelectedTextColor = ta.getColor(R.styleable.SegmentedControlView_segSelectedTextColor,DEFAULT_SELECTED_TEXT_COLOR);
        mTextColor = ta.getColor(R.styleable.SegmentedControlView_segTextColor,DEFAULT_TEXT_COLOR);

        mItemMarginLeft = ta.getDimensionPixelSize(R.styleable.SegmentedControlView_segMarginLeft,
                0);
        mItemMarginTop = ta.getDimensionPixelSize(R.styleable.SegmentedControlView_segMarginTop, 0);
        mSelectedItem = ta.getInteger(R.styleable.SegmentedControlView_segSelectedItem, 0);
        mTextSize = ta.getDimensionPixelSize(R.styleable.SegmentedControlView_segTextSize,
                (int) getResources().getDimension(R.dimen.seg_textSize));
        mMode = ta.getInt(R.styleable.SegmentedControlView_segMode, ROUND);
        mScrollEnable = ta.getBoolean(R.styleable.SegmentedControlView_segScrollEnable, true);
        ta.recycle();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(null);
        } else {
            setBackgroundDrawable(null);
        }

        mScroller = new Scroller(context, new LinearInterpolator());
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();

        mRectF = new RectF();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mOuterColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

    }

    /**
     * 设置view的显示模式
     *
     * @param mode 显示模式
     */
    public void setMode(@Mode int mode) {
        mMode = mode;
        invalidate();
    }

    /**
     * 设置未选中状态下的文字颜色
     *
     * @param color *
     */
    public void setTextColor(int color) {
        this.mTextColor = color;
        invalidate();
    }

    /**
     * 设置选中状态下的文字颜色
     *
     * @param color *
     */
    public void setSelectedTextColor(int color) {
        this.mSelectedTextColor = color;
        invalidate();
    }

    /**
     * 设置选中某一项
     *
     * @param position *
     */
    public void setSelectedItem(int position) {
        this.mSelectedItem = position;
        mSelectedItem = position < getCount() ? position : getCount() - 1;
        invalidate();
    }

    public void setOnSegItemClickListener(OnSegItemClickListener listener) {
        this.mListener = listener;
    }

    /**
     * item 点击事件
     *
     * @param item     *
     * @param position *
     */
    public void onItemClick(SegmentedControlItem item, int position) {
        if (null != mListener) {
            mListener.onItemClick(item, position);
        }
    }

    /**
     * 批量添加item
     *
     * @param list *
     */
    public void addItems(List<SegmentedControlItem> list) {
        if (list == null) {
            throw new IllegalArgumentException("list is null");
        }
        mSegmentedControlItems.addAll(list);
        requestLayout();
        invalidate();
    }

    /**
     * 添加item
     *
     * @param item *
     */
    public void addItem(SegmentedControlItem item) {
        if (item == null) {
            throw new IllegalArgumentException("item is null");
        }
        mSegmentedControlItems.add(item);
        requestLayout();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (checkCount()) {
            return;
        }

        drawBackground(canvas);

        drawText(canvas);

        drawItem(canvas);

        drawOuterText(canvas);
    }

    private void drawOuterText(Canvas canvas) {
        canvas.saveLayer(mStart, 0, mStart + mItemWidth, getHeight(), null, Canvas.ALL_SAVE_FLAG);
        mTextPaint.setColor(mSelectedTextColor);
        mTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        int begin = mStart / mItemWidth;
        int end = (begin + 2) < getCount() ? begin + 2 : getCount();

        for (int i = begin; i < end; i++) {
            int start = mItemMarginLeft + i * mItemWidth;
            float xpoint = start + mItemWidth / 2 - mTextPaint.measureText(getName(i)) / 2;
            float ypoint = getHeight() / 2 - (mTextPaint.ascent() + mTextPaint.descent()) / 2;
            canvas.drawText(getName(i), xpoint, ypoint, mTextPaint);
        }
        canvas.restore();
    }

    float mXi = 0;
    int mMovePosition = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isEnabled() || !isInTouchMode() || getCount() == 0) {
            return false;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            mXi = event.getX();
            mMovePosition = -1;
            final float y = event.getY();
            if (isItemInside(mXi, y)) {
                if (!mScrollEnable) {
                    return false;
                }
                return true;
            } else if (isItemOutside(mXi, y)) {
                mMovePosition = (int) ((mXi - mItemMarginLeft) / mItemWidth);
                startScroll(positionStart(mXi));
                if (!mScrollEnable) {
                    onStateChange(mMovePosition);
                    return false;
                }
                return true;
            }
            return false;
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (!mScroller.isFinished()) {
                return true;
            }
            float dx = event.getX() - mXi;
            if (Math.abs(dx) > 5f) {
                mStart = (int) (mStart + dx);
                mStart = Math.min(Math.max(mStart, mItemMarginLeft), mEnd);
                postInvalidate();
                mXi = event.getX();
            }
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            int newSelectedItem;
            float offset = (mStart - mItemMarginLeft) % mItemWidth;
            int pos = (mStart - mItemMarginLeft) / mItemWidth;
            if (!mScroller.isFinished() && mMovePosition != -1) {
                newSelectedItem = mMovePosition;
            } else {
                if (offset == 0f) {
                    newSelectedItem = pos;
                } else {
                    VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);

                    int initialVelocity = (int) velocityTracker.getXVelocity();
                    if (Math.abs(initialVelocity) > 1500) {
                        newSelectedItem = initialVelocity > 0 ? pos + 1 : pos - 1;
                    } else {
                        newSelectedItem = Math.round(offset / mItemWidth) + pos;
                    }
                    newSelectedItem = Math.max(Math.min(newSelectedItem, getCount() - 1), 0);
                    startScroll(getXByPosition(newSelectedItem));
                }
            }
            onStateChange(newSelectedItem);
            mVelocityTracker = null;
            mMovePosition = -1;
            return true;
        }
        return super.onTouchEvent(event);
    }

    private int getXByPosition(int item) {
        return item * mItemWidth + mItemMarginLeft;
    }

    private void onStateChange(int selectedItem) {
        if (selectedItem != mSelectedItem) {
            mSelectedItem = selectedItem;
            onItemClick(getItem(mSelectedItem), mSelectedItem);
        }
    }

    private void startScroll(int dx) {
        mScroller.startScroll(mStart, 0, dx - mStart, 0, ANIMATION_DURATION);
        postInvalidate();
    }

    private int positionStart(float x1) {
        return mItemMarginLeft + (int) ((x1 - mItemMarginLeft) / mItemWidth) * mItemWidth;
    }

    private boolean isItemInside(float x1, float y1) {
        return x1 >= mStart
                && x1 <= mStart + mItemWidth
                && y1 > mItemMarginTop
                && y1 < mHeight - mItemMarginTop;
    }

    private boolean isItemOutside(float x1, float y1) {

        return !isItemInside(x1, y1)
                && y1 > mItemMarginTop
                && y1 < mHeight - mItemMarginTop
                && x1 < mEnd + mItemWidth;
    }

    private void drawItem(Canvas canvas) {
        final float radius = mMode == ROUND ? mRadius : mHeight / 2 - mItemMarginTop;
        mItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mItemPaint.setAntiAlias(true);
        mItemPaint.setTextSize(mTextSize);
        mItemPaint.setShader(new LinearGradient(mStart,
                mItemMarginTop,
                mStart + mItemWidth,
                getHeight() - mItemMarginTop,
                mItemShaderStartColor,
                mItemShaderEndColor,
                Shader.TileMode.REPEAT));
        mRectF.set(mStart, mItemMarginTop, mStart + mItemWidth, getHeight() - mItemMarginTop);
        canvas.drawRoundRect(mRectF, radius, radius, mItemPaint);
    }

    private void drawBackground(Canvas canvas) {
        final float radius = mMode == ROUND ? mRadius : mHeight / 2;
        mPaint.setXfermode(null);
        mPaint.setColor(mOuterColor);
        mRectF.set(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(mRectF, radius, radius, mPaint);
    }

    private void drawText(Canvas canvas) {
        mTextPaint.setColor(mTextColor);
        mTextPaint.setXfermode(null);
        for (int i = 0; i < getCount(); i++) {
            int start = mItemMarginLeft + i * mItemWidth;
            float xpoint = start + mItemWidth / 2 - mTextPaint.measureText(getName(i)) / 2;
            float ypoint = getHeight() / 2 - (mTextPaint.ascent() + mTextPaint.descent()) / 2;
            canvas.drawText(getName(i), xpoint, ypoint, mTextPaint);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            mStart = mScroller.getCurrX();
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (checkCount() || getMeasuredWidth() == 0) {
            return;
        }
        mHeight = getMeasuredHeight();
        int width = getMeasuredWidth();
        mItemWidth = (width - 2 * mItemMarginLeft) / getCount();
        mStart = mItemMarginLeft + mItemWidth * mSelectedItem;
        mEnd = width - mItemMarginLeft - mItemWidth;

    }

    private boolean checkCount() {
        return getCount() == 0;
    }

    @Override
    public int getCount() {
        return mSegmentedControlItems.size();
    }

    @Override
    public SegmentedControlItem getItem(int position) {
        return mSegmentedControlItems.get(position);
    }

    @Override
    public String getName(int position) {
        return getItem(position).getName();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        PullToLoadState pullToLoadState = new PullToLoadState(parcelable);
        pullToLoadState.mSelectedItem = mSelectedItem;
        return pullToLoadState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof PullToLoadState)) {
            return;
        }
        PullToLoadState pullToLoadState = ((PullToLoadState) state);
        super.onRestoreInstanceState(pullToLoadState.getSuperState());
        mSelectedItem = pullToLoadState.mSelectedItem;
        invalidate();
    }

    private static class PullToLoadState extends View.BaseSavedState {

        private int mSelectedItem;

        public static final Creator CREATOR = new Creator<PullToLoadState>() {

            @Override
            public PullToLoadState createFromParcel(Parcel source) {
                return new PullToLoadState(source);
            }

            @Override
            public PullToLoadState[] newArray(int size) {
                return new PullToLoadState[size];
            }
        };

        PullToLoadState(Parcel superState) {
            super(superState);
            mSelectedItem = superState.readInt();
        }

        PullToLoadState(Parcelable source) {
            super(source);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mSelectedItem);
        }
    }

    /**
     * 从新绘制view
     */
    public void invalidateView() {

        invalidate();
    }
}
