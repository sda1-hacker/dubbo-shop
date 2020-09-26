package com.dubbo.shop.topic;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String msg){
        // 交换机， 路由key， 消息
        rabbitTemplate.convertAndSend("TopicExchange", "nba", msg);
        System.out.println("TopicExchange 已发送消息");
    }
}
