package com;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo // 开启Dubbo注解
public class GConsumerApplication {

    public static void main(String[] args) {

        System.out.println("消费者服务端启动");
        SpringApplication.run(GConsumerApplication.class);
    }
}
