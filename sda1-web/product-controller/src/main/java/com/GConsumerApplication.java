package com;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(FdfsClientConfig.class) // 引入fastdfs client的配置类
@SpringBootApplication
@EnableDubbo // 开启Dubbo注解
public class GConsumerApplication {

    public static void main(String[] args) {

        System.out.println("消费者服务端启动");
        SpringApplication.run(GConsumerApplication.class);
    }
}
