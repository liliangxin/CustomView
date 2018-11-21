package com.gagobigdata.myview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends Activity {

    ThreeDRotateView view;

    ImageView imageView;

    CustomEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        editText = findViewById(R.id.editText);
//        editText.setFloating(true);
//        editText.setFloatingTextColor(Color.RED);
//        属性动画 ====start====
//        view = findViewById(R.id.view);
//        ObjectAnimator topAnimator = ObjectAnimator.ofInt(view,"topDegree",-30);
//        topAnimator.setDuration(2000);
//
//        ObjectAnimator canvasRotateAnimator = ObjectAnimator.ofInt(view,"canvasRotateDegree",360);
//        canvasRotateAnimator.setDuration(2000);
//
//        ObjectAnimator bottomAnimator = ObjectAnimator.ofInt(view,"bottomDegree",30);
//        bottomAnimator.setDuration(2000);
//
//        AnimatorSet set = new AnimatorSet();
//        set.playSequentially(topAnimator,canvasRotateAnimator,bottomAnimator);
//        set.setStartDelay(1000);
//        set.start();

//        PropertyValuesHolder topHolder =  PropertyValuesHolder.ofInt("topDegree",-30);
//        PropertyValuesHolder canvasRotateHolder =  PropertyValuesHolder.ofInt("canvasRotateDegree",360);
//        PropertyValuesHolder bottomHolder =  PropertyValuesHolder.ofInt("bottomDegree",30);
//
//        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view,topHolder,canvasRotateHolder,bottomHolder);
//        animator.setStartDelay(1000);
//        animator.setDuration(2000);
//        animator.start();

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
//        ============属性动画end==============

//        DayWeatherView view = (DayWeatherView) findViewById(R.id.day);
//        int[] temp = {0, 3, 2, 5, 1, 4, -1, 0, 2, -4, -2, -1, 0, 1, 2, 4, 1, -1, 0, -2, 0, 2, 1, -1};
//        int[] water = {0, 6, 4, 12, 15, 4, 2, 1, 10, 10, 20, 5, 15, 6, 2, 4, 1, 8, 0, 9, 0, 2, 1, 12};
//        view.setData(temp, water);

//        WeekWeatherView view = (WeekWeatherView) findViewById(R.id.weekView);
//        view.setTempDay(new int[]{14, 15, 16, 17, 9, 9,11});
//        // set night
//        view.setTempNight(new int[]{7, 5, 9, 15, 3, 6,1});
//        view.invalidate();


//        chartView = (CircleChartView) findViewById(R.id.chart_view);
//        CircleChartUtil.startAnimotor(chartView,3000,250,290,150);

//        AnimatorSet animatorSet = new AnimatorSet();
//        // 创建 ObjectAnimator 对象
//        ObjectAnimator outAnimator = ObjectAnimator.ofFloat(chartView, "outProgress", 0, 360);
//        outAnimator.setDuration(5000);
//        outAnimator.setInterpolator(new FastOutSlowInInterpolator());
//
//        ObjectAnimator centerAnimator = ObjectAnimator.ofFloat(chartView, "centerProgress", 0, 165);
//        centerAnimator.setDuration(3000);
//        centerAnimator.setInterpolator(new FastOutSlowInInterpolator());
//
//        ObjectAnimator innerAnimator = ObjectAnimator.ofFloat(chartView, "innerProgress", 0, 275);
//        innerAnimator.setDuration(3000);
//        innerAnimator.setInterpolator(new FastOutSlowInInterpolator());
//        // 执行动画
//        animatorSet.playTogether(outAnimator,centerAnimator,innerAnimator);
//        animatorSet.start();


//        final TextView tv = (TextView) findViewById(R.id.tv);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                DatePickerDialog dp = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int iyear, int monthOfYear, int dayOfMonth) {
//               long maxDate = datePicker.getMaxDate();//日历最大能设置的时间的毫秒值
//               int year = datePicker.getYear();//年
//               int month = datePicker.getMonth();//月-1
//               int dayOfMonth1 = datePicker.getDayOfMonth();//日*
//               //iyear:年，monthOfYear:月-1，dayOfMonth:日
//               Toast.makeText(getApplicationContext(), iyear +":"+ (monthOfYear+1)+":"+dayOfMonth , Toast.LENGTH_LONG).show();
//           }
//       }, 2013, 2, 1);//2013:初始年份，2：初始月份-1 ，1：初始日期
//       dp.show();
//                Calendar startDate = Calendar.getInstance();
//                startDate.set(2013,0,1);
//                TimePickerView pvTime = new TimePickerView.Builder(MainActivity.this, new TimePickerView.OnTimeSelectListener() {
//                    @Override
//                    public void onTimeSelect(Date date, View v) {//选中事件回调
//                        tv.setText(date.toString());
//                    }
//                })
//                        .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
//                        .setCancelText("取消")//取消按钮文字
//                        .setSubmitText("确认")//确认按钮文字
//                        .setSubCalSize(14)//确定和取消文字大小
//                        .setContentSize(16)//滚轮文字大小
//                        .setTitleSize(16)//标题文字大小
//                        .setTitleText("选择时间")//标题文字
//                        .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
//                        .isCyclic(false)//是否循环滚动
//                        .setTitleColor(Color.parseColor("#212121"))//标题文字颜色
//                        .setSubmitColor(Color.parseColor("#212121"))//确定按钮文字颜色
//                        .setCancelColor(Color.parseColor("#212121"))//取消按钮文字颜色
//                        .setTitleBgColor(Color.parseColor("#e5e9f3"))//标题背景颜色 Night mode
//                        .setBgColor(Color.parseColor("#ffffff"))//滚轮背景颜色 Night mode
//                        .setDate(Calendar.getInstance())// 如果不设置的话，默认是系统时间*/
//                        .setRangDate(startDate,Calendar.getInstance())//起始终止年月日设定
//                        .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
//                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                        .isDialog(false)//是否显示为对话框样式
//                        .build();
////                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
//                pvTime.show();
//            }
//        });

//        List<String> list = new ArrayList<>();
//        list.add("1");
//        list.add("2");
//        list.add("3");
//        list.add("4");
//        list.add("5");
//
//        WheelView wheelView = (WheelView) findViewById(R.id.wheelview);
//        wheelView.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源
//        wheelView.setWheelData(list);// 数据集合
//        wheelView.setSkin(WheelView.Skin.Holo);
//        wheelView.setLoop(false);
//        wheelView.setWheelSize(list.size());
//        WheelView.WheelViewStyle  style= new WheelView.WheelViewStyle();
//        style.holoBorderColor = Color.BLACK;
//        style.backgroundColor = Color.parseColor("#e5e9f3");
//        wheelView.setStyle(style);

//        listView = (SwipeMenuListView) findViewById(R.id.listView);
//        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//            @Override
//            public void create(SwipeMenu menu) {
//                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
//                // set item width
//                openItem.setWidth(90);
//                // set item title
//                openItem.setTitle("删除");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);
//            }
//        };
//
//        listView.setMenuCreator(creator);
//
//        listView.setAdapter(new MyAdapter());
//
//        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                Toast.makeText(MainActivity.this, "删除", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, "条目"+position, Toast.LENGTH_SHORT).show();
//            }
//        });

//        myView = (MyView) findViewById(R.id.myView);
//        // 创建 ObjectAnimator 对象
//        ObjectAnimator animator = ObjectAnimator.ofFloat(myView, "progress", 0, 65);
//        animator.setDuration(3000);
//        animator.setInterpolator(new LinearInterpolator());
//        // 执行动画
//        animator.start();

//        readString().subscribe(new Consumer<List<String>>() {
//            @Override
//            public void accept(List<String> strings) throws Exception {
//                for (int i = 0; i < strings.size(); i++) {
//                    System.out.println(strings.get(i));
//                }
//            }
//        });

//        readString().map(new Function<List<String>, String>() {
//            @Override
//            public String apply(List<String> strings) throws Exception {
//                return strings.get(0);
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                System.out.println(s);
//            }
//        });

//        readString().flatMap(new Function<List<String>, ObservableSource<String>>() {
//            @Override
//            public ObservableSource<String> apply(final List<String> strings) throws Exception {
//
////                return new ObservableSource<String>() {
////                    @Override
////                    public void subscribe(Observer<? super String> observer) {
////                        for (int i = 0; i < strings.size(); i++) {
////                            observer.onNext(strings.get(i));
////                        }
////                    }
////                };
//                return Observable.fromIterable(strings);
//            }
//        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                System.out.println(s);
//            }
//        });

    }

//    public Observable<List<String>> readString() {
//        return Observable.create(new ObservableOnSubscribe<List<String>>() {
//            @Override
//            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
//                List<String> list = new ArrayList<String>();
//                list.add("2222");
//                list.add("3333");
//                list.add("1111");
//                list.add("4444");
//                e.onNext(list);
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
//    }
//
//
//    class MyAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return 10;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            TextView tv = new TextView(MainActivity.this);
//            tv.setText("tiaomu");
//
//            return tv;
//        }
//    }
}
