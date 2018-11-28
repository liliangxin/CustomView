/*
 * Copyright (C) 2018 Beijing GAGO Technology Ltd.
 */

package com.gagobigdata.myview.segment;

/**
 * Created by llx 2018/8/22.
 * @author llx
 */
public interface ISegmentedControl {

    /**
     * item 数量
     * @return *
     */
    int getCount();

    /**
     * 选中item
     * @param position *
     * @return *
     */
    SegmentedControlItem getItem(int position);

    /**
     * 选中item的名字
     * @param position *
     * @return *
     */
    String getName(int position);

}
