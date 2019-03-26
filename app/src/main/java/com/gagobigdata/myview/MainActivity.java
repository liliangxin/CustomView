package com.gagobigdata.myview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.gagobigdata.myview.segment.SegmentedControlItem;
import com.gagobigdata.myview.segment.SegmentedControlView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    SegmentedControlView segmentedControlView;
    CircleProgressBar progressBar;
    @SuppressLint("HandlerLeak")
    Handler  handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressBar.setProgress(50);
            progressBar.startAnim();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.circleProgressBar);
        progressBar.setProgress(27);
        progressBar.startAnim();



        handler.sendEmptyMessageDelayed(1,4000);


//        segmentedControlView = findViewById(R.id.segmentedControlView);
//        List<SegmentedControlItem> items = new ArrayList<>();
//        items.add(new SegmentedControlItem("Yesterday"));
//        items.add(new SegmentedControlItem("Today"));
//        items.add(new SegmentedControlItem("Tomorrow"));
//
//        segmentedControlView.addItems(items);
//
//        segmentedControlView.setOnSegItemClickListener(new SegmentedControlView.OnSegItemClickListener() {
//            @Override
//            public void onItemClick(SegmentedControlItem item, int position) {
//                String msg = String.format(Locale.getDefault(), "selected:%d", position);
//
//            }
//        });

//        float shakeLength = Utils.dpToPixel(3);
//        imageView = findViewById(R.id.imageView);
//        Keyframe keyframe1 = Keyframe.ofFloat(0f, 0f);
//        Keyframe keyframe2 = Keyframe.ofFloat(.1f, -3f * shakeLength);
//        Keyframe keyframe3 = Keyframe.ofFloat(.2f, -3f * shakeLength);
//        Keyframe keyframe4 = Keyframe.ofFloat(.3f, 3f * shakeLength);
//        Keyframe keyframe5 = Keyframe.ofFloat(.4f, -3f * shakeLength);
//        Keyframe keyframe6 = Keyframe.ofFloat(.5f, 3f * shakeLength);
//        Keyframe keyframe7 = Keyframe.ofFloat(.6f, -3f * shakeLength);
//        Keyframe keyframe8 = Keyframe.ofFloat(.7f, 3f * shakeLength);
//        Keyframe keyframe9 = Keyframe.ofFloat(.8f, -3f * shakeLength);
//        Keyframe keyframe10 = Keyframe.ofFloat(.9f, 3f * shakeLength);
//        Keyframe keyframe11 = Keyframe.ofFloat(1f, 0);
//        PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
//                keyframe1,
//                keyframe2,
//                keyframe3,
//                keyframe4,
//                keyframe5,
//                keyframe6,
//                keyframe7,
//                keyframe8,
//                keyframe9,
//                keyframe10,
//                keyframe11);
//        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(imageView,holder);
//        objectAnimator.setDuration(1000);
//        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        objectAnimator.start();

//        DayWeatherView view = findViewById(R.id.day);
//        int[] temp = {0, 3, 2, 5, 1, 4, -1, 0, 2, -4, -2, -1, 0, 1, 2, 4, 1, -1, 0, -2, 0, 2, 1, -1};
//        int[] water = {0, 6, 4, 12, 15, 4, 2, 1, 10, 10, 20, 5, 15, 6, 2, 4, 1, 8, 0, 9, 0, 2, 1, 12};
//        view.setData(temp, water);

//        WeekWeatherView view = findViewById(R.id.weekView);
//        view.setTempDay(new int[]{14, 15, 16, 17, 9, 9,11});
//        // set night
//        view.setTempNight(new int[]{7, 5, 9, 15, 3, 6,1});
//        view.invalidate();


//        CircleChartView chartView = findViewById(R.id.chart_view);
//        CircleChartUtil.startAnimotor(chartView, 3000, 250, 290, 150);
    }
}
