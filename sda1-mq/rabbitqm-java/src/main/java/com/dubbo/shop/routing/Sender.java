package com.dubbo.shop.routing;

// rabbitmq 发布订阅模式

// 发布者 -- 交换机  -- 队列 -- 消费者
// 发布者和交换机相连，因此在这里不需要声明队列    消费者和队列相连，
// 交换机的消息会传递到每一个队列里面。
// 交换机并不存储消息，只是做消息的转发，如果没有队列则清空对应的消息。

// direct类型的交换机  -- 可以指定规则将消息传递到不通的队列中

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

// 生产者
public class Sender {

    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1. 连接到rabbitmq服务器
        ConnectionFactory cf = new ConnectionFactory();
        // virtual host
        cf.setVirtualHost("sda1_mq");   // 这个路径要和rabbitmq控制台中的路径保持一直，/不能省略
        // 用户名
        cf.setUsername("sda1");
        // 密码
        cf.setPassword("123456");
        // 服务器ip
        cf.setHost("192.168.157.128");
        // 服务端口
        cf.setPort(5672);

        // 2. 创建队列
        Connection connection = cf.newConnection();
        // 通过channel进行通信
        Channel channel = connection.createChannel();

        // 创建交换机 -- 类型是  direct  根据 routing key进行路由转发
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        // 3. 发送消息

        String msg1 = "NBA...";
        String msg2 = "CBA...";
        // 交换机名字， 路由key，默认规则，消息
        for(int i = 1; i < 10; i++){
            channel.basicPublish(EXCHANGE_NAME, "nba", null, (msg1+i).getBytes());
            channel.basicPublish(EXCHANGE_NAME, "cba", null, (msg2+i).getBytes());

        }

        System.out.println("发送成功");
        System.exit(0);

    }

}
