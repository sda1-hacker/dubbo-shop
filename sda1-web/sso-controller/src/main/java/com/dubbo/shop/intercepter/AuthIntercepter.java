package com.dubbo.shop.intercepter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dubbo.shop.entity.TUser;
import com.dubbo.shop.service.ITUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 登录拦截
@Component
public class AuthIntercepter implements HandlerInterceptor {

    @Reference
    private ITUserService userService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取用户的登录状态
        // 调用controller  --  使用 http client

        // 调用service直接调用
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return true;
        }

        TUser currUser = null;
        for (Cookie cookie : cookies){
            if("user_token".equals(cookie.getName())) {
                currUser = userService.checkIsLogin(cookie.getValue()); // userService注入失败
                if(currUser != null){
                    request.setAttribute("user", currUser);
                    return true;
                }
            }
        }
        // 在这里可以实现重定向，可以将没有登录的用户重定向到登录页面
        // response.sendRedirect("xxxx");
        return true;
    }
}
