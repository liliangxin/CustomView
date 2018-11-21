package com.gagobigdata.myview.designpatterns;

/**
 * Created by llx on 2018/10/30.
 */
public class ZeRenLianPattren {
}

/**
 * 下面代码 以一个实例来说明
 * 某人出差花费50000元，需报销，组长，经理，总经理权限不够，只能由老板报销
 */
abstract class Leader {

    protected Leader nextHandler;//上一级处理者，需动态指定

    /**
     * 处理报销请求
     *
     * @param money 报销的金额
     */
    public final void handleRequest(int money) {
        if (money <= limit()) {
            handle(money);
        } else {
            nextHandler.handleRequest(money);
        }
    }

    /**
     * 自身能处理的报销限额
     *
     * @return 报销限额
     */
    public abstract int limit();

    /**
     * 处理报销行为
     *
     * @param money 具体金额
     */
    public abstract void handle(int money);
}

/**
 * 组长只有5000的限额
 */
class GroupLeader extends Leader{

    @Override
    public int limit() {
        return 5000;
    }

    @Override
    public void handle(int money) {
        System.out.println("组长处理了报销金额：" + money + "元");
    }
}

/**
 * 经理只有10000的限额
 */
class DirectorLeader extends Leader{

    @Override
    public int limit() {
        return 10000;
    }

    @Override
    public void handle(int money) {
        System.out.println("经理处理了报销金额：" + money + "元");
    }
}

/**
 * 总经理有30000的限额
 */
class ManagerLeader extends Leader{

    @Override
    public int limit() {
        return 30000;
    }

    @Override
    public void handle(int money) {
        System.out.println("总经理处理了报销金额：" + money + "元");
    }
}

/**
 * 老板不限额
 */
class Boss extends Leader{

    @Override
    public int limit() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void handle(int money) {
        System.out.println("老板处理了报销金额：" + money + "元");
    }
}

class ZeRenLianClint{

    public static void main(String[] args){
        // 构造处理对象
        GroupLeader groupLeader = new GroupLeader();
        DirectorLeader directorLeader = new DirectorLeader();
        ManagerLeader managerLeader = new ManagerLeader();
        Boss boss = new Boss();

        //动态指定上级处理对象
        groupLeader.nextHandler = directorLeader;
        directorLeader.nextHandler = managerLeader;
        managerLeader.nextHandler = boss;

        //从组长处开始报销请求
        groupLeader.handleRequest(50000);
    }

}
