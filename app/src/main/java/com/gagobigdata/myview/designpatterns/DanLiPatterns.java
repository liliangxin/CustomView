package com.gagobigdata.myview.designpatterns;

/**
 * Created by llx on 2018/10/30.
 */
public class DanLiPatterns {
}

/**
 * 饿汉式
 */
class Single {
    private static final Single sSingle = new Single();
    //构造私有
    private Single(){}
    public static Single getInstance(){
        return sSingle;
    }
}

/**
 * 懒汉式
 */
class Single1 {
    private static Single1 sSingle = null;
    //构造私有
    private Single1(){}
    public static Single1 getInstance(){
        if (sSingle == null) {
            synchronized (Single.class){
                if (sSingle == null){
                    sSingle = new Single1();
                }
            }
        }
        return sSingle;
    }
}

/**
 * 静态内部类方式
 */
class Single3 {
    //构造私有
    private Single3(){}
    public static Single3 getInstance(){
        return SingleHolder.sInstance;
    }
    private static class SingleHolder{
        private static final Single3 sInstance = new Single3();
    }
}
