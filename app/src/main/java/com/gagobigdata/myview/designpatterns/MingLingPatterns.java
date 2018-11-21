package com.gagobigdata.myview.designpatterns;

/**
 * Created by llx on 2018/10/30.
 */
public class MingLingPatterns {
}

/**
 * 以下代码以俄罗斯方块游戏作为蓝本写成的代码
 */

/**
 * 具体命令的接收者
 * 整个命令模式中唯一的处理逻辑代码
 */
class TetrisMachine {

    public void toLeft() {
        System.out.println("向左");
    }

    public void toRight() {
        System.out.println("向右");
    }

    public void fastToBottom() {
        System.out.println("快速向下");
    }

    public void transfrom() {
        System.out.println("变换形状");
    }

}

/**
 * 抽象命令者
 */
interface Command {
    /**
     * 具体执行方法
     */
    void execute();
}

/**
 * 具体命令类 向左 向右 快速落下 变形
 * 需持有接受者的饮用
 */
class LeftCommand implements Command {

    private TetrisMachine machine;

    public LeftCommand(TetrisMachine machine) {
        this.machine = machine;
    }

    @Override
    public void execute() {
        machine.toLeft();
    }
}

class RightCommand implements Command {

    private TetrisMachine machine;

    public RightCommand(TetrisMachine machine) {
        this.machine = machine;
    }

    @Override
    public void execute() {
        machine.toRight();
    }
}

class FastToBottomCommand implements Command {

    private TetrisMachine machine;

    public FastToBottomCommand(TetrisMachine machine) {
        this.machine = machine;
    }

    @Override
    public void execute() {
        machine.fastToBottom();
    }
}

class TransformCommand implements Command {

    private TetrisMachine machine;

    public TransformCommand(TetrisMachine machine) {
        this.machine = machine;
    }

    @Override
    public void execute() {
        machine.transfrom();
    }
}

/**
 * 命令管理类
 * 可以记录需要的信息等等
 */
class Manager {

    private LeftCommand leftCommand;
    private RightCommand rightCommand;
    private FastToBottomCommand fastToBottomCommand;
    private TransformCommand transformCommand;

    public void setLeftCommand(LeftCommand leftCommand) {
        this.leftCommand = leftCommand;
    }

    public void setRightCommand(RightCommand rightCommand) {
        this.rightCommand = rightCommand;
    }

    public void setFastToBottomCommand(FastToBottomCommand fastToBottomCommand) {
        this.fastToBottomCommand = fastToBottomCommand;
    }

    public void setTransformCommand(TransformCommand transformCommand) {
        this.transformCommand = transformCommand;
    }

    public void toLeft() {
        leftCommand.execute();
    }

    public void toRight() {
        rightCommand.execute();
    }

    public void fastToBottom() {
        fastToBottomCommand.execute();
    }

    public void transform() {
        transformCommand.execute();
    }
}

class MingLingClint {

    public static void main(String[] args) {
        //命令接收者
        TetrisMachine machine = new TetrisMachine();

        //实际命令类
        LeftCommand leftCommand = new LeftCommand(machine);
        RightCommand rightCommand = new RightCommand(machine);
        FastToBottomCommand fastToBottomCommand = new FastToBottomCommand(machine);
        TransformCommand transformCommand = new TransformCommand(machine);

        //管理者
        Manager manager = new Manager();
        manager.setLeftCommand(leftCommand);
        manager.setRightCommand(rightCommand);
        manager.setFastToBottomCommand(fastToBottomCommand);
        manager.setTransformCommand(transformCommand);

        //调用
        manager.toLeft();

    }

}


