package com.dubbo.shop;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @org.junit.Test
    public void test() throws Exception{
        System.out.println(new Date());
        System.in.read(); // 阻塞，等待定时任务去执行。
    }

}
