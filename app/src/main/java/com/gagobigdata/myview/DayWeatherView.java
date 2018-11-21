package com.gagobigdata.myview;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by llx on 2018/1/16.
 */

public class DayWeatherView extends LinearLayout {

    private Context context;

    private TextView maxTemp;
    private TextView minTemp;
    private HoursWeatherView hourView;
    private HorizontalScrollView scrollView;

    public DayWeatherView(Context context) {
        this(context,null);
    }

    public DayWeatherView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DayWeatherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = View.inflate(context,R.layout.day_weather_view,this);
        hourView = view.findViewById(R.id.hour_view);
        maxTemp = view.findViewById(R.id.tv_max_temperature);
        minTemp = view.findViewById(R.id.tv_min_temperature);
        scrollView = view.findViewById(R.id.scrollView_day_water_view);
    }

    /**
     * 数组长度为24
     * @param temp 24小时气温，从凌晨 0：00 至 23：00
     * @param water 24小时降水，从凌晨 0：00 至 23：00
     */
    public void setData(int[] temp,int[] water){
        // 存放最低温度
        int minTempDay = temp[0];
        // 存放最高温度
        int maxTempDay = temp[0];
        for (int item : temp) {
            if (item < minTempDay) {
                minTempDay = item;
            }
            if (item > maxTempDay) {
                maxTempDay = item;
            }
        }
        maxTemp.setText(maxTempDay+"°");
        minTemp.setText(minTempDay+"°");
        hourView.setTemperature(temp);
        hourView.setWater(water);
        hourView.invalidate();
        final int hour = getHour();
        hourView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int hourViewWidth = hourView.getWidth();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(hourViewWidth*hour/24,0);
                    }
                },1000);

            }
        });

    }

    private int getHour(){
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR_OF_DAY);
    }
}
