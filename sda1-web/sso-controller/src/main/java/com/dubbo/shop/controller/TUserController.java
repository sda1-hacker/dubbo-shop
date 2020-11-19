package com.dubbo.shop.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.dubbo.shop.entity.TUser;
import com.dubbo.shop.pojo.ResBean;
import com.dubbo.shop.service.ITUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sda1
 * @since 2020-11-18
 */
@Controller
@RequestMapping("/shop/t-user")
public class TUserController {

    @Reference
    private ITUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    // 展示登录页面
    @GetMapping("showSsoIndexPage")
    public String shoSsoIndexPage(){

        return "ssoIndexPage";
    }

    // 权限验证 -- 登录  -- 使用redis代替session来存储用户信息  -- 到登录系统中去登录
    @ResponseBody
    @PostMapping("checkLogin")
    public String checkLogin(String userName, String password, HttpServletResponse response){

        TUser currUser = userService.checkLogin(userName, password);

        if(currUser != null){
            return "login Success!!";
        }

        // 生成token
        String uuid = UUID.randomUUID().toString();

        // 构造cookie
        Cookie cookie = new Cookie("user_token", uuid);
        cookie.setPath("/");
        cookie.setHttpOnly(true);   // 基于安全的控制, 只允许通过后端来获取的cookie的信息, 不能通过前端脚本获取到cookie信息

        // cookie.setDomain("");    // 域名设置 -- 解决单点登录的跨域问题，
        // 系统A:  aaa.qq.com        系统B: bbb.qq.com      系统C: ccc.qq.com
        // cookie默认会返回当前系统的域名, 如果是多个系统那么就无法访问其他的系统了。因此需要设置域名, 设置成父域名
        // cookie.setDomain(".qq.com");  // -- 不能设置成某个特定的域名，需要设置成父域名

        // redis中保存凭证信息
        StringBuilder redisKey = new StringBuilder("user:token:").append(uuid); // 直接用uuid也可以 -- 加上前缀方便区分
        redisTemplate.opsForValue().set(redisKey.toString(), currUser);
        redisTemplate.expire(redisKey.toString(), 30, TimeUnit.MINUTES); // 设置有效时间

        // 将cookie返回给前端
        response.addCookie(cookie);

        return  "login failed";
    }


    // 判断是否已经登录  --  从redis中读取用户信息
    /*
    @ResponseBody
    @PostMapping("checkIsLogin")
    public ResBean checkIsLogin(HttpServletRequest request) {

        // 获取cookie中的uuid
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return ResBean.error("未登录!!");
        }

        for(Cookie cookie : cookies){
            if("user_token".equals(cookie.getName())){
                String uuid = cookie.getValue();
                StringBuilder redisKey = new StringBuilder("user:token:").append(uuid);
                TUser currUser = (TUser) redisTemplate.opsForValue().get(redisKey.toString());
                if(currUser !=null){
                    redisTemplate.expire(redisKey.toString(), 30, TimeUnit.MINUTES); // 刷新有效时间
                    return ResBean.success(currUser.getUsername());
                }
            }
        }

        return ResBean.error("未登录!!");
    }
     */

    // 判断是否已经登录， 使用@CookieValue注解的方式  --  自动获取指定名字的cookie 的 value
    @PostMapping("checkIsLoginNew")
    @ResponseBody
    public ResBean checkIsLoginNew(@CookieValue(name = "user_token", required = false) String uuid){

        // 如果其他使用想要使用这个登录验证，有两种方式：
        // 1. 使用 apache http client 调用controller
        // 2. 将这个controller中的方法 抽取出来到 service中

        if(uuid == null){
            return ResBean.error("用户未登录!!");
        }

        StringBuilder redisKey = new StringBuilder("user:token:").append(uuid);

        TUser currUser = (TUser) redisTemplate.opsForValue().get(redisKey.toString());

        if(currUser != null){
            redisTemplate.expire(redisKey.toString(), 30, TimeUnit.MINUTES);
            return ResBean.success(currUser.getUsername());
        }

        return ResBean.error("用户未登录!!");
    }

    // 注销  --  到登录系统中注销
    @GetMapping("logout")
    @ResponseBody
    public ResBean logout(@CookieValue(name = "user_token", required = false) String uuid, HttpServletResponse response){

        if(uuid == null){
            return ResBean.error("注销失败!!");
        }

        // 删除cookie  -- 重新生成一个失效的cookie返回给用户
        Cookie cookie = new Cookie("user_token", ""); // name一定要一样，为了覆盖原来的cookie, value无所谓
        cookie.setPath("/"); // path要和之前一样
        cookie.setHttpOnly(true);
        // cookie.setDomain("qq.com");  // 解决单点登录跨域问题
        cookie.setMaxAge(0); // 让cookie失效
        response.addCookie(cookie);

        // 删除redis中存储的信息
        StringBuilder redisKey = new StringBuilder("user:token:").append(uuid);
        TUser currUser = (TUser) redisTemplate.opsForValue().get(redisKey.toString());

        if(currUser != null){
            redisTemplate.delete(redisKey.toString());
            return ResBean.success(currUser.getUsername() + ", 注销成功~");
        }

        return ResBean.error("注销失败!!");
    }
}
