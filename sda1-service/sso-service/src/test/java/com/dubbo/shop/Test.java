package com.dubbo.shop;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // BCrypt 加密    --   相同的字符串 得到的密文不同
    @org.junit.Test
    public void BCryptTest(){
        String passwd = "123456";
        String resEncode = bCryptPasswordEncoder.encode(passwd); // 加密
        boolean isTrue = bCryptPasswordEncoder.matches(passwd, resEncode); // 验证

        System.out.println(resEncode);
        System.out.println(isTrue);
        System.out.println("-----------------");

        String resEncode1 = bCryptPasswordEncoder.encode(passwd);
        boolean isTrue1 = bCryptPasswordEncoder.matches(passwd, resEncode);
        boolean isTrue2 = bCryptPasswordEncoder.matches(passwd, resEncode1);
        System.out.println(isTrue1);
        System.out.println(isTrue2);

    }
}
