package com.dubbo.shop.timer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


// spring定时任务，默认是单线程的
// 想要开启的多线程需要进行配置
@Configuration
public class SpringTakManyThreadConfig implements SchedulingConfigurer {

    // 线程执行器 -- 100个线程
    @Bean
    public Executor getTaskExecutor(){
        return Executors.newScheduledThreadPool(100);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(getTaskExecutor()); // 设置线程池
    }
}
