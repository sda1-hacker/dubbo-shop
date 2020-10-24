package com.dubbo.shop.timer.springboottask;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SpringTask {

    // 默认也是单线程的模式  -- 需要添加配置才可以变成多线程

    // 这里添加cron表达式，表示定期执行任务  -- https://cron.qqe2.com/ 这里可以自动生成cron表达式
    // @Scheduled(cron = "0/2 * * * * ?")

    // 每3秒执行一次
    @Scheduled(fixedDelay = 3000)
    public void printXXX(){
        System.out.println(Thread.currentThread().getName() + ", 哈哈哈哈" + new Date());
    }

}
