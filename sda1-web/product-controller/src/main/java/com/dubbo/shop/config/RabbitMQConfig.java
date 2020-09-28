package com.dubbo.shop.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PRODUCT_EXCHANGE_NAME_ADD = "add_exchange";

    // 声明交换机
    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange(PRODUCT_EXCHANGE_NAME_ADD);
    }

}
