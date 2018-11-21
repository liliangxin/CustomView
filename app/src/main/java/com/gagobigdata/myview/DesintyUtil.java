package com.gagobigdata.myview;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by llx on 2018/10/25.
 */
public class DesintyUtil {
    public static float dp2px(int dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp, Resources.getSystem().getDisplayMetrics());
    }
}
