package com.gagobigdata.myview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by llx on 2018/11/5.
 */
public class TagLayout extends ViewGroup {

    private List<Rect> mChildBounds;

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        mChildBounds = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthUsed = 0;
        int heightUsed = 0;
        int lineWidthUsed = 0;
        int lineMaxHeight = 0;

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);

            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);

            if (lineWidthUsed + childView.getMeasuredWidth() > specSize) {
                lineWidthUsed = 0;
                heightUsed += lineMaxHeight;
                measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
            }

            Rect childBound;
            if (mChildBounds.size() <= i) {
                childBound = new Rect();
                mChildBounds.add(childBound);
            } else {
                childBound = mChildBounds.get(i);
            }

            childBound.set(lineWidthUsed,
                    heightUsed,
                    lineWidthUsed + childView.getMeasuredWidth(),
                    heightUsed + childView.getMeasuredHeight());

            lineWidthUsed += childView.getMeasuredWidth();
            widthUsed = Math.max(widthUsed,lineWidthUsed);
            lineMaxHeight = Math.max(lineMaxHeight,childView.getMeasuredHeight());
        }

        int selfWidth = widthUsed;
        int selfHeight = heightUsed;
        setMeasuredDimension(selfWidth, selfHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            Rect rect = mChildBounds.get(i);
            childAt.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
