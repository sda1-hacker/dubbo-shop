package com.dubbo.shop.publish;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv1 {

    private static final String QUEUE_NAME = "exchange1";

    private static final String EXCHANGE_NAME = "fanout";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory cf = new ConnectionFactory();
        cf.setVirtualHost("sda1_mq");
        cf.setUsername("sda1");
        cf.setPassword("123456");
        cf.setHost("192.168.157.128");
        cf.setPort(5672);

        Connection connection = cf.newConnection();

        // 通过channel进行消息的传递
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 将队列和交换机进行绑定
        // 队列名， 交换机名， 路由规则
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        // 声明一个消费者
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 在这里处理接受到的信息
                String msg = new String(body, "utf-8");
                System.out.println("消费者1  接受到的消息为：" + msg);
            }
        };

        // 让消费者去监听队列
        // 队列名称，自动回复（true），消费者
        channel.basicConsume(QUEUE_NAME, true, consumer);

    }
}
