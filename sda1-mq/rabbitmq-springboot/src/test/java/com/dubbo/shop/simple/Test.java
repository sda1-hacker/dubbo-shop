package com.dubbo.shop.simple;

import com.dubbo.shop.publish.PublishSender;
import com.dubbo.shop.topic.TopicSender;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @Resource
    private SimpleSender simpleSender;

    @Resource
    private PublishSender publishSender;

    @Resource
    private TopicSender topicSender;

    @org.junit.Test
    public void simpleSenderTest(){
        simpleSender.send("springboot整个 rabbitmq ok ！！");
    }

    @org.junit.Test
    public void publishSenderTest(){
        publishSender.send("publish Test Ok !!");
    }

    @org.junit.Test
    public void topicSenderTest(){
        topicSender.send("nba 我喜欢看~~~");
    }

}
