package com.dubbo.shop.publish;

// 消费者只需要关注对应的队列就可以了

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;



@Component
public class PublishRecv {

    @RabbitListener(queues = "springboot-queue111")
    public void queue1_HandleMsg(String msg){
        System.out.println("publish  queue 1 收到的消息为：" + msg);
    }

    @RabbitListener(queues = "springboot-queue222")
    public void queue2_HandleMsg(String msg){
        System.out.println("publish  queue 2 收到的消息为：" + msg);
    }

}
