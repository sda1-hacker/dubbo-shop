package com.dubbo.shop.work;

// rabbitmq生产者消费者模式

// 一个生产者 多个消费者
// 多个消费者之间是竞争关系。
// 消息会依次发送给每一个消费者。每个消费者得到的消息数目（基本上）是相等的。
// 如果有的消费者处理的速度比较慢，那么这样就不是很合理

// 针对上述的描述，就需要进行限流。  -- 就是消费者在没有处理完消息之前不会接受到新的消息
// 使用在消费者那边添加如下代码：   表示每次接受一个消息，在没有处理完之前不会再接受新的消息了
// channel.basicQos(1);

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

// 生产者
public class Sender {

    private static final String QUEUE_NAME = "work";

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
        // 声明队列，如果不存在则创建，如果有了则不做处理
        // 队列名，是否持久化true，是否只给当前链接使用这个队列false，是否自动删除false
        channel.queueDeclare(QUEUE_NAME, true , false, false, null);

        // 3. 发送消息
        for(int i=1; i<=10; i++){

            String msg = "使用rabbitmq发送消息:" + i + "..";
            // 交换机，队列，null， 字节数组
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        }
        System.out.println("发送成功");
        System.exit(0);

    }

}
