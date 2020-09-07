package com.dubbo.shop.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @Resource
    private Configuration configuration;

    // 根据模板生成静态页面。
    @org.junit.Test
    public void createHtml() throws Exception{

        // 模板 + 数据 ---> 数据静态页面

        // 获取模板对象
        Template template = configuration.getTemplate("index.ftl");

        // 数据
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "吼吼吼吼 freemarker");
        data.put("data", new Date());

        List<String> list = new ArrayList<String >();
        list.add("张三");
        list.add("李四");
        list.add("王五");
        data.put("personList", list);

        data.put("condition1", 60);


        // 将数据传递到模板 -- 生成静态页面
        FileWriter fw = new FileWriter("D:\\JavaCode\\dubbo-shop\\sda1-web\\freemarker-demo\\src\\main\\resources\\templates\\index.html");
        template.process(data,fw);
    }

}
