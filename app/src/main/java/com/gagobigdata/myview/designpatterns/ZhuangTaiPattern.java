package com.gagobigdata.myview.designpatterns;

/**
 * Created by llx on 2018/10/30.
 */
public class ZhuangTaiPattern {
}


/**
 * 电视机的操作状态
 */
interface TvState{
    void nextChannel();
    void prevChannel();
    void turnUp();
    void turnDown();
}

/**
 * 关机状态操作不可用
 */
class PowerOffState implements TvState{

    @Override
    public void nextChannel() {

    }

    @Override
    public void prevChannel() {

    }

    @Override
    public void turnUp() {

    }

    @Override
    public void turnDown() {

    }
}

/**
 * 开机状态 操作可用
 */
class PowerOnState implements TvState{

    @Override
    public void nextChannel() {
        System.out.println("下一频道");
    }

    @Override
    public void prevChannel() {
        System.out.println("上一频道");
    }

    @Override
    public void turnUp() {
        System.out.println("加音量");
    }

    @Override
    public void turnDown() {
        System.out.println("减音量");
    }
}

/**
 * 控制开关机
 */
interface PowerController{
    /**
     * 开机
     */
    void powerOn();

    /**
     * 关机
     */
    void powerOff();
}

class TvController implements PowerController{

    TvState tvState;

    @Override
    public void powerOn() {
        setTvState(new PowerOnState());
    }

    @Override
    public void powerOff() {
        setTvState(new PowerOffState());
    }

    public void setTvState(TvState tvState) {
        this.tvState = tvState;
    }

    public void nextChannel() {
        tvState.nextChannel();
    }

    public void prevChannel() {
        tvState.prevChannel();
    }

    public void turnUp() {
        tvState.turnUp();
    }

    public void turnDown() {
        tvState.turnDown();
    }

}

class Clint{

    public static void main(String[] args){
        TvController controller = new TvController();
        controller.powerOn();
        controller.nextChannel();
        controller.turnDown();
        controller.powerOff();
    }

}