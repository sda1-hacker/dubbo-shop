package com.dubbo.shop.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class RabbitMQConfig {

    public static final String PRODUCT_EXCHANGE_NAME_ADD = "add_exchange";

    public static final String PRODUCT_QUEUE_NAME_ADD = "add_queue";

    // 声明队列
    @Bean
    public Queue getQueue(){
        return new Queue(PRODUCT_QUEUE_NAME_ADD, true, false, false, null);
    }

    // 声明交换机
    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange(PRODUCT_EXCHANGE_NAME_ADD);
    }

    // 将队列绑定到交换机 -- 一个交换机可以绑定多个队列，  一个队列也可以绑定到多个交换机上
    @Bean
    public Binding bindProductAddQueueToExchange(TopicExchange getTopicExchange, Queue getQueue){
        return BindingBuilder.bind(getQueue).to(getTopicExchange).with("add_product.#");
    }

}
