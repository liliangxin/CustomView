package com.gagobigdata.myview.designpatterns;

/**
 * Created by llx on 2018/10/30.
 */
public class CeLvePattrens {
}

/**
 * 以下代码以不同的乘车方式使用不同的计价算法为场景模拟多种策略
 */

// 策略接口
interface Calculator {
    double calculator(int km);
}

//公交计价策略实现类
class BusCalculator implements Calculator {
    @Override
    public double calculator(int km) {
        double price = 0;
        if (km <= 10) {
            price = 2;
        } else if (km > 10) {
            price = 2 + 0.5 * (km - 10);
        }
        return price;
    }
}

//出租计价策略实现类
class CarCalculator implements Calculator {
    @Override
    public double calculator(int km) {
        return 2 * km;
    }
}

//地铁计价策略实现类
class TrainCalculator implements Calculator {
    @Override
    public double calculator(int km) {
        if (km <= 6) {
            return 3;
        } else if (km > 6 && km <= 10) {
            return 4;
        } else if (km > 10 && km <= 15) {
            return 5;
        } else if (km > 15 && km <= 22) {
            return 6;
        }
        return 7;
    }
}

//策略管理类
class CalculatorManager {

    private Calculator calculator;

    public void setCalculator(Calculator calculator) {
        this.calculator = calculator;
    }

    public double getPrice(int km) {
        if (calculator == null) {
            return 0;
        } else {
            return calculator.calculator(km);
        }
    }
}

class CeLveClient {
    public static void main(String[] args) {
        //使用
        CalculatorManager manager = new CalculatorManager();
        manager.setCalculator(new TrainCalculator());
        double price = manager.getPrice(10);
        System.out.println("价格：" + price);
    }
}
