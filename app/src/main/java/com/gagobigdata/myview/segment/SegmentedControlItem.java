/*
 * Copyright (C) 2018 Beijing GAGO Technology Ltd.
 */

package com.gagobigdata.myview.segment;

/**
 * item实体类
 */
public class SegmentedControlItem {

    private String mName;

    public SegmentedControlItem(String name) {
        this.mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
