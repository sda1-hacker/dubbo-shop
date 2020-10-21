package com.dubbo.shop.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

// 通过注解的方式配置pagehelper
@Configuration
public class PageHelperConfig {

    @Bean
    public PageHelper getPageHelper() {
        PageHelper pageHelper = new PageHelper();

        Properties properties = new Properties();
        properties.setProperty("dialect", "mysql");
        properties.setProperty("reasonable", "true");
        pageHelper.setProperties(properties);

        return pageHelper;
    }
}
