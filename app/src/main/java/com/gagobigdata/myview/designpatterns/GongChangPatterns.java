package com.gagobigdata.myview.designpatterns;

/**
 * Created by llx on 2018/10/30.
 */
public class GongChangPatterns {
}

/**
 *  以下代码以生产电脑为例 通过工厂生产不同电脑的情景
 *  此类型为简单工厂模式 抽象工厂模式与工厂模式类似
 */
interface IComputer{
    void createComputer();
}

class WindowsComputer implements IComputer{

    @Override
    public void createComputer() {
        System.out.println("Windows电脑");
    }
}

class MacComputer implements IComputer{

    @Override
    public void createComputer() {
        System.out.println("Mac电脑");
    }
}

class ComputerFactory{

    public static final int TYPE_WINDOWS = 1;
    public static final int TYPE_MAC = 2;

    public static IComputer productComputer(int type){
        if (type == TYPE_WINDOWS) {
            return new WindowsComputer();
        } else if (type == TYPE_MAC) {
            return new MacComputer();
        } else {
            return null;
        }
    }
}

class FactoryPatternsClient {
    public static void main(String[] args) {
        IComputer computer = ComputerFactory.productComputer(ComputerFactory.TYPE_WINDOWS);
        computer.createComputer();
    }
}



