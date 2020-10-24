package com.dubbo.shop.timer.jdk;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

// djk提供的定时任务
// 自定义任务
public class MyTimerTask extends TimerTask {

    // 这里是执行具体任务的函数
    @Override
    public void run() {

        System.out.println("----timer 定时任务" + new Date());
    }

    public static void main(String[] args) {

        Timer timer = new Timer(); // 开启一个线程执行任务 -- 单线程执行

        MyTimerTask task = new MyTimerTask();

        System.out.println(Thread.currentThread().getName() + ", 任务还没有开始执行，" + new Date());

        // 执行任务， --  3秒后执行 -- 只执行一次
        // timer.schedule(task, 3000);

        // 周期性的执行，3秒后开始执行， 每1秒执行一次
        timer.schedule(task, 3000, 1000);
    }

    // 局限性
    // 1.单线程
    // 2.这种方式只能是固定的时间间隔，如果时间间隔不同则无法去处理，比如：每个月的15号去执行某个任务（这个时间间隔不太确定）
}
