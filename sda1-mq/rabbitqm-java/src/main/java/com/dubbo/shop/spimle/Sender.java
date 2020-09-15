package com.dubbo.shop.spimle;

// rabbitmq生产者消费者模式

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

// 生产者
public class Sender {

    private static final String QUEUE_NAME = "simple";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1. 连接到rabbitmq服务器
        ConnectionFactory cf = new ConnectionFactory();
        // virtual host
        cf.setVirtualHost("/sda1_mq");
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
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 3. 发送消息
        String msg = "使用rabbitmq发送消息";
        // 交换机，队列，null， 字节数组
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        System.out.println("发送成功");
    }

}
