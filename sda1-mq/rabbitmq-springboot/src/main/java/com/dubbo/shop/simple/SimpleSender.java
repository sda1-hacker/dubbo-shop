package com.dubbo.shop.simple;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleSender {

    public static final String QUEUE_NAME = "springboot-queue";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String msg){
        // 交换机名称， 队列名称， 消息
        rabbitTemplate.convertAndSend("", QUEUE_NAME, msg);
        System.out.println("消息成功发出");
    }

}
