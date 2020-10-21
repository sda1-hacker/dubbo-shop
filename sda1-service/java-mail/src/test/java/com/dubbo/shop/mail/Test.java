package com.dubbo.shop.mail;

import org.junit.platform.engine.support.descriptor.FileSystemSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${mail.fromAddress}")
    String fromAddress;

    String toAddress = "xxxx@qq.com"

    // 发送简单的邮件
    @org.junit.Test
    public void sendToQMail(){

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(fromAddress);
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject("java - mail - test");
        simpleMailMessage.setText("java mail 测试 ....  <a href='https://www.baidu.com'> 点击这里开始验证 </a> ");

        mailSender.send(simpleMailMessage);

    }


    // 发送html格式的文件   --  html标签
    @org.junit.Test
    public void sendHtmlToQMail(){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom(fromAddress);
            mimeMessageHelper.setTo(toAddress);
            mimeMessageHelper.setSubject("发送了一条带有html格式的邮件");
            // true表示开启html格式
            mimeMessageHelper.setText("点击链接进行验证。<a href='https://www.baidu.com'> 点击这里开始验证 </a>", true);
            javaMailSender.send(message);
        }catch (Exception e){
            System.out.println("发送过程中出现异常");
        }

    }


    // 发送带附件的邮件
    @org.junit.Test
    public void sendFileToQMail(){
        MimeMessage message = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom(fromAddress);
            mimeMessageHelper.setTo(toAddress);
            mimeMessageHelper.setSubject("发送一个带附件的邮件");
            mimeMessageHelper.setText("这个邮件里面带了附件 <h2> h2 哈哈哈 </h2>", true);

            // 附件
            FileSystemResource resource = new FileSystemResource("C:\\Users\\86150\\Desktop\\乐观锁.txt");
            mimeMessageHelper.addAttachment("乐观锁.txt", resource);

            javaMailSender.send(message);

        }catch (Exception e){
            System.out.println("发送过程中出现异常");
        }
    }


    // 邮件模板  -- 发送一个htlm页面
    @org.junit.Test
    public void sendPageToQMail() throws Exception{
        // 获取到要发送的模板
        Context context = new Context();
        String content = templateEngine.process("mailTemplate.html", context); // 发送的内容

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromAddress);
        helper.setTo(toAddress);
        helper.setSubject("应该是发送了一个网页");
        helper.setText(content, true);

        javaMailSender.send(message);


    }
}
