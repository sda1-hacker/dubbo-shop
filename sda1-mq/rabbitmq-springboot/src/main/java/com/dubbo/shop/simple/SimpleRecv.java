package com.dubbo.shop.simple;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "springboot-queue")    // 监听 -- 消息发出之后，这里会自动接受
public class SimpleRecv {

    public static final String QUEUE_NAME = "springboot-queue";

    @RabbitHandler  // 处理队列消息的注解
    public void process(String msg){
        System.out.println("收到的消息是：" + msg);
    }

//    @RabbitListener(queues = "springboot-queue") // 这个注解可以对函数生效，也可以对类生效
//    public void handle(String msg){
//        System.out.println("----" + msg);
//    }
}
