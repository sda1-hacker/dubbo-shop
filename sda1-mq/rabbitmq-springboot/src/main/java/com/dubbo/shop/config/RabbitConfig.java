package com.dubbo.shop.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// rabbitmq 相关配置
// 主要是声明交换机以及队列， 还有将队列绑定到对应的交换机上， 不对消息做处理
@Configuration
public class RabbitConfig {

    public static final String QUEUE_NAME1 = "springboot-queue111";

    public static final String QUEUE_NAME2 = "springboot-queue222";

    public static final String FANOUT_EXCHANGE = "FanoutExchange";

    // 声明队列
    @Bean
    public Queue getQueue1(){
        // 队列名， 持久化， 排他性（是否只允许当前链接访问）， 自动删除
        return new Queue(QUEUE_NAME1, true, false, false, null);
    }

    // 声明队列
    @Bean
    public Queue getQueue2(){
        // 队列名， 持久化， 排他性（是否只允许当前链接访问）， 自动删除
        return new Queue(QUEUE_NAME2, true, false, false, null);
    }

    // 声明交换机
    @Bean
    public FanoutExchange getExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    // 带有标识的交换机   TopicExchange（模糊匹配）, 或者 DirectExchange（精确匹配）
    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange("TopicExchange");
    }



    // 交换机和队列进行绑定
    // 这里声明交换机和队列的名字， 和上面的函数名保持一直
    @Bean
    public Binding bindExchangeWithQueueOne(Queue getQueue1, FanoutExchange getExchange){

        return BindingBuilder.bind(getQueue1).to(getExchange);
    }

    // 交换机和队列进行绑定
    @Bean
    public Binding bindExchangeWithQueueTwo(Queue getQueue2, FanoutExchange getExchange){

        return BindingBuilder.bind(getQueue2).to(getExchange);
    }

    // Topic交换机和队列进行绑定
    @Bean
    public Binding bindTopicExchangeWithQueue(Queue getQueue1, TopicExchange getTopicExchange){
        // TopicExchange（模糊匹配）, 或者 DirectExchange（精确匹配）
        // 要写上路由关键字
        return BindingBuilder.bind(getQueue1).to(getTopicExchange).with("nba.#");
    }
}
