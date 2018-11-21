package com.gagobigdata.myview.designpatterns;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by llx on 2018/10/30.
 */
public class GuanChaZhePatterns {
}

/**
 * 以下代码大致以某个程序员订阅某网站进行书写
 */
class Coder implements Observer {

    private String name;

    public Coder(String name) {
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("hi," + name + "网站更新内容了，内容:" + arg.toString());
    }

    @Override
    public String toString() {
        return "程序员：" + name;
    }
}

class DevWebNet extends Observable {

    public void postNewPublication(String content){
        // 标识状态或者内容发生改变
        setChanged();
        //通知所有观察者
        notifyObservers(content);
    }

}

class GuanChaZheClient {

    public static void main(String[] args){
        // 被观察者
        DevWebNet devWebNet = new DevWebNet();
        //观察者
        Coder coder1 = new Coder("HeKun");
        Coder coder2 = new Coder("YaLong");
        Coder coder3 = new Coder("DongXu");
        Coder coder4 = new Coder("LiangXin");

        devWebNet.addObserver(coder1);
        devWebNet.addObserver(coder2);
        devWebNet.addObserver(coder3);
        devWebNet.addObserver(coder4);

        devWebNet.postNewPublication("课程更新了");
    }

}


