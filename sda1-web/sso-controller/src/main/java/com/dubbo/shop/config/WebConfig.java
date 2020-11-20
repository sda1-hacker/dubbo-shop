package com.dubbo.shop.config;

import com.dubbo.shop.intercepter.AuthIntercepter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 生成一个bean 交给 spring管理， 否则  在 AuthIntercepter  使用  @Reference 注入失败
    @Bean
    public AuthIntercepter getAuthIntercepter(){
        return new AuthIntercepter();
    }

    // 添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthIntercepter())
        .addPathPatterns("/**"); // 拦截的路径
    }
}
