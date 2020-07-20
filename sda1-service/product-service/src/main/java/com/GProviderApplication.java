package com;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo // 开启Dubbo注解
@MapperScan("com.dubbo.shop.mapper")   // mapper的扫描要配制，否则会出错
public class GProviderApplication {

    public static void main(String[] args) {

        System.out.println("provider 已经启动");
        SpringApplication.run(GProviderApplication.class);
    }
}
