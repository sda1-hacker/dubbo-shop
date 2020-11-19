package com.dubbo.shop;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class SsoControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoControllerApplication.class);
    }
}
