package com.dubbo.shop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dubbo.shop.entity.TUser;
import com.dubbo.shop.mapper.TUserMapper;
import com.dubbo.shop.service.ITUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sda1
 * @since 2020-11-18
 */
@Component
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {

    @Resource
    private TUserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate; // spring redis

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // spring security 加密

    public TUser checkLogin(String userName, String password) {

        TUser currUser = userMapper.findUserByUserName(userName);

        if(currUser != null){
            if(passwordEncoder.matches(password, currUser.getPassword())){
                return currUser;
            }
        }
        return null;
    }

    // 根据token获取对应的用户  --  判断用户是否登录
    public TUser checkIsLogin(String userToken) { // userToken 通过 @CookieValue(name = "user_token", required = false) String uuid 获取

        if("".equals(userToken) || userToken == null){
            return null;
        }

        StringBuilder redisKey = new StringBuilder("user:token:").append(userToken);

        TUser currUser = (TUser) redisTemplate.opsForValue().get(redisKey.toString());

        if(currUser != null){
            redisTemplate.expire(redisKey.toString(), 30, TimeUnit.MINUTES);
            return currUser;
        }

        return null;
    }
}
