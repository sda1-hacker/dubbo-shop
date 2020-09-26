package com.dubbo.shop.topic;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TopicRecv {

    @RabbitListener(queues = "springboot-queue111")
    public void handle(String msg){
        System.out.println("使用topic 模式收到的消息为：" + msg);
    }
}
