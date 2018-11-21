package com.gagobigdata.myview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.DisplayMetrics;

/**
 * Created by llx on 2017/12/4.
 */

public class CircleChartUtil {

    public static float dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }

    public static void startAnimotor(CircleChartView chartView, int duration, float outProgress,
                                     float centerProgress, float innerProgress) {
        if (centerProgress > 360 || outProgress > 360 || innerProgress > 360) {
            throw new IllegalArgumentException("the progress must be less than 360。");
        }
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator outAnimator = ObjectAnimator.ofFloat(chartView, "outProgress", 0, outProgress);
        outAnimator.setDuration(5000);
        outAnimator.setInterpolator(new FastOutSlowInInterpolator());

        ObjectAnimator centerAnimator = ObjectAnimator.ofFloat(chartView, "centerProgress", 0, centerProgress);
        centerAnimator.setDuration(duration);
        centerAnimator.setInterpolator(new FastOutSlowInInterpolator());

        ObjectAnimator innerAnimator = ObjectAnimator.ofFloat(chartView, "innerProgress", 0, innerProgress);
        innerAnimator.setDuration(duration);
        innerAnimator.setInterpolator(new FastOutSlowInInterpolator());

        animatorSet.playTogether(outAnimator, centerAnimator, innerAnimator);
        animatorSet.start();
    }
}
