package com.dubbo.shop.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv2 {

    private static final String QUEUE_NAME = "topic_exchange2";
    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory cf = new ConnectionFactory();
        cf.setVirtualHost("sda1_mq");
        cf.setUsername("sda1");
        cf.setPassword("123456");
        cf.setHost("192.168.157.128");
        cf.setPort(5672);

        Connection connection = cf.newConnection();

        // 通过channel进行消息的传递
        final Channel channel = connection.createChannel();

        // 限流
        // 表示每次接受一个消息，在没有处理完之前不会再接受新的消息了
        channel.basicQos(1);

        // 声明一个队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 将队列和交换机进行绑定
        // 队列名， 交换机名， 路由规则
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "#.second.#");

        // 声明一个消费者
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 在这里处理接受到的信息
                String msg = new String(body, "utf-8");
                System.out.println("消费者2  接受到的消息为：" + msg);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 手工回复， 在这里进行回复
                // 消息标识，false(回复一个)
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        // 让消费者去监听队列
        // 队列名称，手工回复（false），消费者
        // 使用了限流必须使用手工回复
        channel.basicConsume(QUEUE_NAME, false, consumer);

    }
}
