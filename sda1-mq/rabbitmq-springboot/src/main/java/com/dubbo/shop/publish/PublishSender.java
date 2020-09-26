package com.dubbo.shop.publish;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 发送者只需要关注交换机就可以了。

@Component
public class PublishSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static final String FANOUT_EXCHANGE = "FanoutExchange";

    public void send(String msg){
        // 交换机名称， 路由key， 消息
        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE, "", msg);
    }
}
